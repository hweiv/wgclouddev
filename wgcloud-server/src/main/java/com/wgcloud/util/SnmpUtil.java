package com.wgcloud.util;

import com.wgcloud.entity.SnmpInfo;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SnmpUtil {
   private static final Logger logger = LoggerFactory.getLogger(SnmpUtil.class);

   public static Target createDefault(String ip, String community, String port, int snmpVersion) {
      if (StringUtils.isBlank(ip)) {
         logger.error("ip is null.");
         return null;
      } else if (StringUtils.isBlank(community)) {
         logger.error("community is null.");
         return null;
      } else {
         Address address = GenericAddress.parse("udp:" + ip + "/" + port);
         Target target = null;
         if (snmpVersion == 3) {
            target = new UserTarget();
            ((Target)target).setSecurityLevel(3);
            ((Target)target).setSecurityName(new OctetString(community));
         } else {
            target = new CommunityTarget();
            ((CommunityTarget)target).setCommunity(new OctetString(community));
            if (snmpVersion == 1) {
               ((Target)target).setSecurityModel(2);
            }
         }

         ((Target)target).setVersion(snmpVersion);
         ((Target)target).setAddress(address);
         ((Target)target).setTimeout(3000L);
         ((Target)target).setRetries(2);
         return (Target)target;
      }
   }

   public static Map<String, String> getOnLineList(List<SnmpInfo> snmpInfoAllList) {
      Map<String, String> resultMap = new HashMap();
      Set<String> set = new HashSet();
      Iterator var3 = snmpInfoAllList.iterator();

      while(var3.hasNext()) {
         SnmpInfo snmpInfo = (SnmpInfo)var3.next();
         set.add(snmpInfo.getHostname());
      }

      var3 = set.iterator();

      while(var3.hasNext()) {
         String hostName = (String)var3.next();
         if (!isEthernetConnection(hostName)) {
            resultMap.put(hostName, "2");
         } else {
            resultMap.put(hostName, "1");
         }
      }

      logger.info("snmp设备在线集合：" + resultMap.toString());
      return resultMap;
   }

   public static boolean isEthernetConnection(String ip) {
      try {
         InetAddress ad = InetAddress.getByName(ip);
         boolean state = ad.isReachable(3000);
         return state;
      } catch (Exception var3) {
         logger.error("isEthernetConnection错误", var3);
         return false;
      }
   }

   public static SnmpInfo getAvgSnmpInfo(SnmpInfo snmpInfo) {
      SnmpInfo snmpInfo2 = new SnmpInfo();

      try {
         SnmpInfo snmpInfo1 = snmpGet(snmpInfo);
         long beginBytesRecv = Long.valueOf(snmpInfo1.getBytesRecv());
         long beginBytesSent = Long.valueOf(snmpInfo1.getBytesSent());
         logger.info(snmpInfo.getHostname() + " snmp beginBytesRecv：" + beginBytesRecv + "，beginBytesSent：" + beginBytesSent);
         long memSize;
         long memTotalSize;
         if (StringUtils.isEmpty(snmpInfo.getRecvAvgOid()) && StringUtils.isEmpty(snmpInfo.getSentAvgOid())) {
            Thread.sleep(3000L);
            snmpInfo2 = snmpGet(snmpInfo);
            memSize = Long.valueOf(snmpInfo2.getBytesRecv());
            memTotalSize = Long.valueOf(snmpInfo2.getBytesSent());
            logger.info(snmpInfo.getHostname() + " snmp endBytesRecv：" + memSize + "，endBytesSent：" + memTotalSize);
            double recvAvg = (double)(memSize - beginBytesRecv) / 3.0D;
            double sentAvg = (double)(memTotalSize - beginBytesSent) / 3.0D;
            snmpInfo2.setRecvAvg(FormatUtil.formatDouble((Double)recvAvg, 2) + "");
            snmpInfo2.setSentAvg(FormatUtil.formatDouble((Double)sentAvg, 2) + "");
         } else {
            snmpInfo2 = snmpInfo1;
         }

         double memPer;
         if (!StringUtils.isEmpty(snmpInfo.getCpuPerOID())) {
            if (!"block".equals(snmpInfo.getMemTotalSizeOID())) {
               memPer = 100.0D - Double.valueOf(snmpInfo2.getCpuPer());
               snmpInfo2.setCpuPer(FormatUtil.formatDouble((Double)memPer, 2) + "");
            } else {
               memPer = Double.valueOf(snmpInfo2.getCpuPer());
               snmpInfo2.setCpuPer(FormatUtil.formatDouble((Double)memPer, 2) + "");
            }
         } else {
            snmpInfo2.setCpuPer("0");
         }

         if (!StringUtils.isEmpty(snmpInfo.getMemSizeOID()) && !StringUtils.isEmpty(snmpInfo.getMemTotalSizeOID())) {
            if (!"block".equals(snmpInfo.getMemTotalSizeOID())) {
               memSize = Long.valueOf(snmpInfo2.getMemSize());
               memTotalSize = Long.valueOf(snmpInfo2.getMemTotalSize());
               snmpInfo2.setMemPer(FormatUtil.formatDouble((Double)((double)memSize / (double)memTotalSize * 100.0D), 2) + "");
            } else {
               memPer = Double.valueOf(snmpInfo2.getMemSize());
               snmpInfo2.setMemPer(FormatUtil.formatDouble((Double)memPer, 2) + "");
            }
         } else {
            snmpInfo2.setMemPer("0");
         }

         return snmpInfo2;
      } catch (Exception var15) {
         logger.error("getAvgSnmpInfo错误", var15);
         snmpInfo2.setRecvAvg("0");
         snmpInfo2.setSentAvg("0");
         snmpInfo2.setMemSize("0");
         snmpInfo2.setMemTotalSize("1");
         snmpInfo2.setCpuPer("0");
         snmpInfo2.setMemPer("0");
         return snmpInfo2;
      }
   }

   public static SnmpInfo snmpGet(SnmpInfo snmpInfo) {
      Snmp snmp = null;
      TransportMapping transport = null;
      SnmpInfo snmpInfoRes = new SnmpInfo();
      Vector vector = null;

      try {
         String ip = snmpInfo.getHostname();
         String community = snmpInfo.getSnmpCommunity();
         String port = snmpInfo.getSnmpPort();
         int snmpVersion = Integer.valueOf(snmpInfo.getSnmpVersion());
         String sendOID = snmpInfo.getSentOID();
         if (StringUtils.isEmpty(sendOID)) {
            sendOID = "";
         }

         String receiveOID = snmpInfo.getRecvOID();
         if (StringUtils.isEmpty(receiveOID)) {
            receiveOID = "";
         }

         String cpuPerOID = snmpInfo.getCpuPerOID();
         String memSizeOID = snmpInfo.getMemSizeOID();
         String memTotalSizeOID = snmpInfo.getMemTotalSizeOID();
         String diskPerOid = snmpInfo.getDiskPerOid();
         String recvAvgOid = snmpInfo.getRecvAvgOid();
         String sentAvgOid = snmpInfo.getSentAvgOid();
         if (StringUtils.isEmpty(sendOID) || StringUtils.isEmpty(receiveOID)) {
            snmpInfoRes.setBytesSent("0");
            snmpInfoRes.setBytesRecv("0");
         }

         if (StringUtils.isEmpty(cpuPerOID)) {
            snmpInfoRes.setCpuPer("100");
         }

         if (StringUtils.isEmpty(memSizeOID)) {
            snmpInfoRes.setMemSize("0");
         }

         if (StringUtils.isEmpty(memTotalSizeOID)) {
            snmpInfoRes.setMemTotalSize("1");
         }

         Target myTarget = createDefault(ip, community, port, snmpVersion);
         transport = new DefaultUdpTransportMapping();
         transport.listen();
         snmp = new Snmp(transport);
         PDU request = new PDU();
         String[] sendOIDs = sendOID.split("\\r\\n");
         String[] receiveOIDs = sendOIDs;
         int var21 = sendOIDs.length;

         int var22;
         for(var22 = 0; var22 < var21; ++var22) {
            String sendOIDTmp = receiveOIDs[var22];
            request.add(new VariableBinding(new OID(sendOIDTmp.trim())));
         }

         receiveOIDs = receiveOID.split("\\r\\n");
         String[] var36 = receiveOIDs;
         var22 = receiveOIDs.length;

         for(int var39 = 0; var39 < var22; ++var39) {
            String receiveOIDTmp = var36[var39];
            request.add(new VariableBinding(new OID(receiveOIDTmp.trim())));
         }

         if (!StringUtils.isEmpty(cpuPerOID)) {
            request.add(new VariableBinding(new OID(cpuPerOID.trim())));
         }

         if (!StringUtils.isEmpty(memSizeOID) && !StringUtils.isEmpty(memTotalSizeOID)) {
            request.add(new VariableBinding(new OID(memSizeOID.trim())));
            if (!"block".equals(snmpInfo.getMemTotalSizeOID())) {
               request.add(new VariableBinding(new OID(memTotalSizeOID.trim())));
            }
         }

         if (!StringUtils.isEmpty(diskPerOid)) {
            request.add(new VariableBinding(new OID(diskPerOid.trim())));
         }

         if (!StringUtils.isEmpty(recvAvgOid)) {
            request.add(new VariableBinding(new OID(recvAvgOid.trim())));
         }

         if (!StringUtils.isEmpty(sentAvgOid)) {
            request.add(new VariableBinding(new OID(sentAvgOid.trim())));
         }

         request.setType(-96);
         ResponseEvent responseEvent = snmp.send(request, myTarget);
         PDU response = responseEvent.getResponse();
         vector = (Vector) response.getVariableBindings();
         long bytesSentSum = 0L;
         long bytesRecvSum = 0L;

         for(int i = 0; i < vector.size(); ++i) {
            try {
               VariableBinding vb1 = (VariableBinding)vector.get(i);
               if (!StringUtils.isEmpty(sendOID) && sendOID.contains(String.valueOf(vb1.getOid()))) {
                  bytesSentSum += Long.valueOf(String.valueOf(vb1.getVariable()));
               } else if (!StringUtils.isEmpty(receiveOID) && receiveOID.contains(String.valueOf(vb1.getOid()))) {
                  bytesRecvSum += Long.valueOf(String.valueOf(vb1.getVariable()));
               } else if (!StringUtils.isEmpty(cpuPerOID) && cpuPerOID.contains(String.valueOf(vb1.getOid()))) {
                  snmpInfoRes.setCpuPer(String.valueOf(vb1.getVariable()));
               } else if (!StringUtils.isEmpty(memSizeOID) && memSizeOID.contains(String.valueOf(vb1.getOid()))) {
                  snmpInfoRes.setMemSize(String.valueOf(vb1.getVariable()));
               } else if (!StringUtils.isEmpty(memTotalSizeOID) && memTotalSizeOID.contains(String.valueOf(vb1.getOid()))) {
                  snmpInfoRes.setMemTotalSize(String.valueOf(vb1.getVariable()));
               } else if (!StringUtils.isEmpty(diskPerOid) && diskPerOid.contains(String.valueOf(vb1.getOid()))) {
                  snmpInfoRes.setDiskPer(String.valueOf(vb1.getVariable()));
               } else if (!StringUtils.isEmpty(recvAvgOid) && recvAvgOid.contains(String.valueOf(vb1.getOid()))) {
                  snmpInfoRes.setRecvAvg(FormatUtil.formatDouble((String)String.valueOf(vb1.getVariable()), 2) + "");
               } else if (!StringUtils.isEmpty(sentAvgOid) && sentAvgOid.contains(String.valueOf(vb1.getOid()))) {
                  snmpInfoRes.setSentAvg(FormatUtil.formatDouble((String)String.valueOf(vb1.getVariable()), 2) + "");
               }
            } catch (Exception var33) {
               logger.error("snmp应答pdu获得mib信息错误", var33);
            }
         }

         snmpInfoRes.setBytesSent(String.valueOf(bytesSentSum));
         snmpInfoRes.setBytesRecv(String.valueOf(bytesRecvSum));
         SnmpInfo var41 = snmpInfoRes;
         return var41;
      } catch (Exception var34) {
         logger.error("snmp检测错误", var34);
      } finally {
         closeTransport(transport);
         closeSnmp(snmp);
      }

      return snmpInfoRes;
   }

   private static void closeTransport(TransportMapping transport) {
      try {
         if (null != transport) {
            transport.close();
         }
      } catch (Exception var2) {
         logger.error("closeTransport错误", var2);
      }

   }

   private static void closeSnmp(Snmp snmp) {
      try {
         if (null != snmp) {
            snmp.close();
         }
      } catch (Exception var2) {
         logger.error("closeSnmp错误", var2);
      }

   }
}
