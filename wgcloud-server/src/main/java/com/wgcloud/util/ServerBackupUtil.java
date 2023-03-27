package com.wgcloud.util;

import com.wgcloud.entity.DbInfo;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.FtpInfo;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.entity.SnmpInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerBackupUtil {
   private static final Logger logger = LoggerFactory.getLogger(ServerBackupUtil.class);
   public static List<String> DB_INFO_ID_LIST = Collections.synchronizedList(new ArrayList());
   public static List<String> DCE_INFO_ID_LIST = Collections.synchronizedList(new ArrayList());
   public static List<String> SNMP_INFO_ID_LIST = Collections.synchronizedList(new ArrayList());
   public static List<String> HEATH_MONITOR_ID_LIST = Collections.synchronizedList(new ArrayList());
   public static List<String> FTP_ID_LIST = Collections.synchronizedList(new ArrayList());

   public static void clearCacheIdList() {
      logger.info("清空server-backup节点处理的缓存ID");
      DB_INFO_ID_LIST.clear();
      DCE_INFO_ID_LIST.clear();
      SNMP_INFO_ID_LIST.clear();
      HEATH_MONITOR_ID_LIST.clear();
      FTP_ID_LIST.clear();
   }

   public static void cacheSaveFtpInfoId(List<FtpInfo> ftpInfoList) {
      Iterator var1 = ftpInfoList.iterator();

      while(var1.hasNext()) {
         FtpInfo ftpInfo = (FtpInfo)var1.next();
         if (!FTP_ID_LIST.contains(ftpInfo.getId())) {
            FTP_ID_LIST.add(ftpInfo.getId());
         }
      }

   }

   public static void cacheSaveDbInfoId(List<DbInfo> dbInfoList) {
      Iterator var1 = dbInfoList.iterator();

      while(var1.hasNext()) {
         DbInfo dbInfo = (DbInfo)var1.next();
         if (!DB_INFO_ID_LIST.contains(dbInfo.getId())) {
            DB_INFO_ID_LIST.add(dbInfo.getId());
         }
      }

   }

   public static void cacheSaveDceInfoId(List<DceInfo> dceInfoList) {
      Iterator var1 = dceInfoList.iterator();

      while(var1.hasNext()) {
         DceInfo dceInfo = (DceInfo)var1.next();
         if (!DCE_INFO_ID_LIST.contains(dceInfo.getId())) {
            DCE_INFO_ID_LIST.add(dceInfo.getId());
         }
      }

   }

   public static void cacheSaveSnmpInfoId(List<SnmpInfo> snmpInfoList) {
      Iterator var1 = snmpInfoList.iterator();

      while(var1.hasNext()) {
         SnmpInfo snmpInfo = (SnmpInfo)var1.next();
         if (!SNMP_INFO_ID_LIST.contains(snmpInfo.getId())) {
            SNMP_INFO_ID_LIST.add(snmpInfo.getId());
         }
      }

   }

   public static void cacheSaveHeathMontiorId(List<HeathMonitor> heathMonitorList) {
      Iterator var1 = heathMonitorList.iterator();

      while(var1.hasNext()) {
         HeathMonitor heathMontior = (HeathMonitor)var1.next();
         if (!HEATH_MONITOR_ID_LIST.contains(heathMontior.getId())) {
            HEATH_MONITOR_ID_LIST.add(heathMontior.getId());
         }
      }

   }
}
