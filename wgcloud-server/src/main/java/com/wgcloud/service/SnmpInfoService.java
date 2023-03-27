package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.SnmpInfo;
import com.wgcloud.entity.SnmpState;
import com.wgcloud.mapper.SnmpInfoMapper;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.SnmpUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.staticvar.BatchData;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SnmpInfoService {
   @Autowired
   private SnmpInfoMapper snmpInfoMapper;
   @Autowired
   private LogInfoService logInfoService;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<SnmpInfo> list = this.snmpInfoMapper.selectByParams(params);
      PageInfo<SnmpInfo> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(SnmpInfo SnmpInfo) throws Exception {
      SnmpInfo.setId(UUIDUtil.getUUID());
      SnmpInfo.setCreateTime(new Date());
      SnmpInfo.setState("1");
      SnmpInfo.setSnmpUnit("byte");
      SnmpInfo.setBytesSent("0");
      SnmpInfo.setBytesRecv("0");
      SnmpInfo.setSentAvg("0");
      SnmpInfo.setRecvAvg("0");
      SnmpInfo.setCpuPer("0");
      SnmpInfo.setMemPer("0");
      if (StringUtils.isEmpty(SnmpInfo.getHostname())) {
         SnmpInfo.setHostname(SnmpInfo.getHostname().trim());
      }

      if (StringUtils.isEmpty(SnmpInfo.getRecvOID())) {
         SnmpInfo.setRecvOID(SnmpInfo.getRecvOID().trim());
      }

      if (StringUtils.isEmpty(SnmpInfo.getSentOID())) {
         SnmpInfo.setSentOID(SnmpInfo.getSentOID().trim());
      }

      if (StringUtils.isEmpty(SnmpInfo.getCpuPerOID())) {
         SnmpInfo.setCpuPerOID(SnmpInfo.getCpuPerOID().trim());
      }

      if (StringUtils.isEmpty(SnmpInfo.getMemSizeOID())) {
         SnmpInfo.setMemSize(SnmpInfo.getMemSizeOID().trim());
      }

      if (StringUtils.isEmpty(SnmpInfo.getMemTotalSizeOID())) {
         SnmpInfo.setMemTotalSizeOID(SnmpInfo.getMemTotalSizeOID().trim());
      }

      this.snmpInfoMapper.save(SnmpInfo);
   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.snmpInfoMapper.countByParams(params);
   }

   public int updateActive(Map<String, Object> params) throws Exception {
      return this.snmpInfoMapper.updateActive(params);
   }

   @Transactional
   public int deleteById(String[] id) throws Exception {
      return this.snmpInfoMapper.deleteById(id);
   }

   public void updateById(SnmpInfo snmpInfo) throws Exception {
      if (StringUtils.isEmpty(snmpInfo.getHostname())) {
         snmpInfo.setHostname(snmpInfo.getHostname().trim());
      }

      this.snmpInfoMapper.updateById(snmpInfo);
   }

   public SnmpInfo selectById(String id) throws Exception {
      return this.snmpInfoMapper.selectById(id);
   }

   @Transactional
   public void updateRecord(List<SnmpInfo> recordList) throws Exception {
      this.snmpInfoMapper.updateList(recordList);
   }

   public List<SnmpInfo> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.snmpInfoMapper.selectAllByParams(params);
   }

   public void taskThreadHandler(Map<String, String> snmpMap, SnmpInfo h, Date date) {
      SnmpInfo snmpInfo = null;
      if ("1".equals(snmpMap.get(h.getHostname()))) {
         snmpInfo = SnmpUtil.getAvgSnmpInfo(h);
      }

      h.setCreateTime(date);
      h.setState("1");
      if (null != snmpInfo) {
         h.setRecvAvg(snmpInfo.getRecvAvg());
         h.setSentAvg(snmpInfo.getSentAvg());
         h.setBytesSent(snmpInfo.getBytesSent());
         h.setBytesRecv(snmpInfo.getBytesRecv());
         h.setMemPer(snmpInfo.getMemPer());
         h.setCpuPer(snmpInfo.getCpuPer());
         h.setDiskPer(snmpInfo.getDiskPer());
      }

      if (null == snmpInfo) {
         h.setState("2");
         h.setCreateTime((Date)null);
         h.setRecvAvg((String)null);
         h.setSentAvg((String)null);
         h.setBytesSent((String)null);
         h.setBytesRecv((String)null);
         h.setMemPer((String)null);
         h.setCpuPer((String)null);
         h.setDiskPer((String)null);
      }

      try {
         this.updateById(h);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      if ("1".equals(h.getState())) {
         SnmpState snmpState = new SnmpState();
         snmpState.setRecvAvg(h.getRecvAvg());
         snmpState.setSentAvg(h.getSentAvg());
         snmpState.setCpuPer(h.getCpuPer());
         snmpState.setMemPer(h.getMemPer());
         snmpState.setSnmpInfoId(h.getId());
         snmpState.setCreateTime(date);
         BatchData.SNMP_STATE_LIST.add(snmpState);
      }

      if ("2".equals(h.getState())) {
         WarnOtherUtil.sendSnmpInfo(h, true);
      } else if (null != WarnPools.MEM_WARN_MAP.get(h.getId())) {
         WarnOtherUtil.sendSnmpInfo(h, false);
      }

   }

   public void saveLog(HttpServletRequest request, String action, SnmpInfo snmpInfo) {
      if (null != snmpInfo) {
         this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "snmp设备监测信息：" + snmpInfo.getHostname(), "snmp设备：" + snmpInfo.getRemark(), "2");
      }
   }

   public void updateToTargetAccount(Map<String, Object> params) throws Exception {
      this.snmpInfoMapper.updateToTargetAccount(params);
   }
}
