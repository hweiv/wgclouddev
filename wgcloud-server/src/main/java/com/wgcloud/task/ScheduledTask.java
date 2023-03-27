package com.wgcloud.task;

import cn.hutool.core.collection.CollectionUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.DockerInfo;
import com.wgcloud.entity.FileSafe;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.entity.MailSet;
import com.wgcloud.entity.PortInfo;
import com.wgcloud.entity.SnmpInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.CustomInfoService;
import com.wgcloud.service.DbTableCountService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.FileSafeService;
import com.wgcloud.service.FtpInfoService;
import com.wgcloud.service.HeathMonitorService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.MailSetService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.service.TaskUtilService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.SnmpUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.msg.WarnMailUtil;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.staticvar.BatchData;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {
   private Logger logger = LoggerFactory.getLogger(ScheduledTask.class);
   @Autowired
   private SystemInfoService systemInfoService;
   @Autowired
   private LogInfoService logInfoService;
   @Autowired
   private AppInfoService appInfoService;
   @Autowired
   private FileSafeService fileSafeService;
   @Autowired
   private DockerInfoService dockerInfoService;
   @Autowired
   private PortInfoService portInfoService;
   @Autowired
   private MailSetService mailSetService;
   @Autowired
   private AccountInfoService accountInfoService;
   @Autowired
   private TaskUtilService taskUtilService;
   @Autowired
   private CustomInfoService customInfoService;
   @Autowired
   private DbTableService dbTableService;
   @Autowired
   private FtpInfoService ftpInfoService;
   @Autowired
   private DbTableCountService dbTableCountService;
   @Autowired
   private HeathMonitorService heathMonitorService;
   @Autowired
   private DceInfoService dceInfoService;
   @Autowired
   private SnmpInfoService snmpInfoService;
   @Autowired
   private CommonConfig commonConfig;
   @Autowired
   private MailConfig mailConfig;
   @Autowired
   private ServletContext servletContext;

   @Scheduled(
      initialDelay = 5000L,
      fixedRate = 86400000L
   )
   public void validateLicense() {
      this.logger.info("validateLicense------------" + DateUtil.getDateTimeString(new Date()));

      try {
         this.servletContext.setAttribute("icoUrl", "/wgcloud/static/logincss/favicon.png");
         this.servletContext.setAttribute("logoUrl", "/wgcloud/static/logincss/logo.png");
         this.servletContext.setAttribute("wgName", "WGCLOUD");
         this.servletContext.setAttribute("wgShortName", "WGCLOUD");
         Map<String, Object> params = new HashMap();
         int listAgentNum = this.systemInfoService.countByParams(params);
         int listPingNum = this.dceInfoService.countByParams(params);
         StaticKeys.LICENSE_STATE = LicenseUtil.validateLicense(listAgentNum, this.commonConfig.getPageSize(), listPingNum);
         this.servletContext.setAttribute("LICENSE_STATE", StaticKeys.LICENSE_STATE);
         this.servletContext.setAttribute("LICENSE_DATE", StaticKeys.LICENSE_DATE);
         this.servletContext.setAttribute("LICENSE_NAME", StaticKeys.LICENSE_NAME);
         if (!StringUtils.isEmpty(StaticKeys.LICENSE_DATE) && StaticKeys.LICENSE_DATE.startsWith("2099")) {
            this.servletContext.setAttribute("LICENSE_DATE", "永久授权");
         }

         this.servletContext.setAttribute("LICENSE_NUM", StaticKeys.LICENSE_NUM);
         this.servletContext.setAttribute("copyRight", "true");
         if (StaticKeys.LICENSE_STATE.equals("1")) {
            if (!StringUtils.isEmpty(this.commonConfig.getIcoUrl())) {
               this.servletContext.setAttribute("icoUrl", "/wgcloud/resources/" + this.commonConfig.getIcoUrl());
            }

            if (!StringUtils.isEmpty(this.commonConfig.getLogoUrl())) {
               this.servletContext.setAttribute("logoUrl", "/wgcloud/resources/" + this.commonConfig.getLogoUrl());
            }

            if (!StringUtils.isEmpty(this.commonConfig.getWgName())) {
               this.servletContext.setAttribute("wgName", this.commonConfig.getWgName());
            }

            if (!StringUtils.isEmpty(this.commonConfig.getWgShortName())) {
               this.servletContext.setAttribute("wgShortName", this.commonConfig.getWgShortName());
            }

            this.servletContext.setAttribute("copyRight", this.commonConfig.getCopyRight());
         }
      } catch (Exception var4) {
         this.logger.error("检测license任务错误", var4);
      }

   }

   @Scheduled(
      initialDelay = 20000L,
      fixedRate = 360000L
   )
   public void initTask() {
      this.logger.info("initTask------------" + DateUtil.getDateTimeString(new Date()));

      try {
         Map<String, Object> params = new HashMap();
         List<MailSet> list = this.mailSetService.selectAllByParams(params);
         if (list.size() > 0) {
            StaticKeys.mailSet = (MailSet)list.get(0);
         } else {
            StaticKeys.mailSet = null;
         }

         StaticKeys.ACCOUNT_INFO_MAP.clear();
         if ("true".equals(this.commonConfig.getUserInfoManage()) && StaticKeys.LICENSE_STATE.equals("1")) {
            List<AccountInfo> accountInfoList = this.accountInfoService.selectAllByParams(new HashMap());
            Iterator var4 = accountInfoList.iterator();

            while(var4.hasNext()) {
               AccountInfo accountInfo = (AccountInfo)var4.next();
               StaticKeys.ACCOUNT_INFO_MAP.put(accountInfo.getAccount(), accountInfo);
            }
         }

         StaticKeys.WARN_CRON_TIME_SIGN = DateUtil.isWarnTime(this.mailConfig.getWarnCronTime());
      } catch (Exception var6) {
         this.logger.error("initTask错误", var6);
      }

   }

   @Scheduled(
      initialDelay = 15000L,
      fixedRate = 3600000L
   )
   public void sumDiskSizeCacheTask() {
      this.logger.info("sumDiskSizeCacheTask------------" + DateUtil.getDateTimeString(new Date()));

      try {
         this.servletContext.setAttribute("sumDiskSizeCache", this.taskUtilService.sumDiskSizeCache((HttpServletRequest)null));
      } catch (Exception var2) {
         this.logger.error("获取所有磁盘总容量之和任务错误", var2);
      }

   }

   @Scheduled(
      initialDelay = 150000L,
      fixedRateString = "${base.snmpTimes}000"
   )
   public void snmpInfoTask() {
      if (!"master".equals(this.commonConfig.getNodeType())) {
         this.logger.info("slave节点不执行snmp设备监测任务");
      } else if (DateUtil.isClearTime()) {
         this.logger.info("正在清空历史数据，不执行snmp设备监控----------" + DateUtil.getCurrentDateTime());
      } else {
         this.logger.info("snmpInfoTask------------" + DateUtil.getDateTimeString(new Date()));
         Map<String, Object> params = new HashMap();
         Date date = new Date();

         try {
            params.put("active", "1");
            List<SnmpInfo> snmpInfoAllList = this.snmpInfoService.selectAllByParams(params);
            if (snmpInfoAllList.size() > 0) {
               Map<String, String> snmpMap = SnmpUtil.getOnLineList(snmpInfoAllList);
               Iterator var5 = snmpInfoAllList.iterator();

               while(var5.hasNext()) {
                  SnmpInfo h = (SnmpInfo)var5.next();
                  if (ServerBackupUtil.SNMP_INFO_ID_LIST.contains(h.getId())) {
                     this.logger.info("此设备由wgcloud-server-backup监测:" + h.getHostname());
                  } else {
                     Runnable runnable = () -> {
                        this.snmpInfoService.taskThreadHandler(snmpMap, h, date);
                     };
                     ThreadPoolUtil.executor.execute(runnable);
                  }
               }
            }
         } catch (Exception var8) {
            this.logger.error("SNMP设备检测任务错误", var8);
            this.logInfoService.save("SNMP设备检测任务错误", var8.toString(), "2");
         }

      }
   }

   @Scheduled(
      initialDelay = 60000L,
      fixedRateString = "${base.heathTimes}000"
   )
   public void heathMonitorTask() {
      if (!"master".equals(this.commonConfig.getNodeType())) {
         this.logger.info("slave节点不执行检测服务接口任务");
      } else if (DateUtil.isClearTime()) {
         this.logger.info("正在清空历史数据，不执行服务接口监控----------" + DateUtil.getCurrentDateTime());
      } else {
         this.logger.info("heathMonitorTask------------" + DateUtil.getDateTimeString(new Date()));
         Map<String, Object> params = new HashMap();
         Date date = new Date();

         try {
            params.put("active", "1");
            List<HeathMonitor> heathMonitorAllList = this.heathMonitorService.selectAllByParams(params);
            if (heathMonitorAllList.size() > 0) {
               Iterator var4 = heathMonitorAllList.iterator();

               while(var4.hasNext()) {
                  HeathMonitor h = (HeathMonitor)var4.next();
                  if (ServerBackupUtil.HEATH_MONITOR_ID_LIST.contains(h.getId())) {
                     this.logger.info("此接口由wgcloud-server-backup监测:" + h.getAppName());
                  } else {
                     Runnable runnable = () -> {
                        this.heathMonitorService.taskThreadHandler(h, date);
                     };
                     ThreadPoolUtil.executor.execute(runnable);
                  }
               }
            }
         } catch (Exception var7) {
            this.logger.error("服务接口检测任务错误", var7);
            this.logInfoService.save("服务接口检测错误", var7.toString(), "2");
         }

      }
   }

   @Scheduled(
      initialDelay = 90000L,
      fixedRateString = "${base.dceTimes}000"
   )
   public void dceInfoTask() {
      if (!"master".equals(this.commonConfig.getNodeType())) {
         this.logger.info("slave节点不执行检测数通设备PING监控");
      } else if (DateUtil.isClearTime()) {
         this.logger.info("正在清空历史数据，不执行数通设备PING监控----------" + DateUtil.getCurrentDateTime());
      } else {
         this.logger.info("dceInfoTask------------" + DateUtil.getDateTimeString(new Date()));
         Map<String, Object> params = new HashMap();
         Date date = new Date();

         try {
            params.put("active", "1");
            List<DceInfo> dceInfoAllList = this.dceInfoService.selectAllByParams(params);
            if (dceInfoAllList.size() > 0) {
               Iterator var4 = dceInfoAllList.iterator();

               while(var4.hasNext()) {
                  DceInfo h = (DceInfo)var4.next();
                  if (ServerBackupUtil.DCE_INFO_ID_LIST.contains(h.getId())) {
                     this.logger.info("此设备由wgcloud-server-backup监测:" + h.getHostname());
                  } else {
                     Runnable runnable = () -> {
                        this.dceInfoService.taskThreadHandler(h, date);
                     };
                     ThreadPoolUtil.executor.execute(runnable);
                  }
               }
            }
         } catch (Exception var7) {
            this.logger.error("数通设备PING检测任务错误", var7);
            this.logInfoService.save("数通设备PING检测任务错误", var7.toString(), "2");
         }

      }
   }

   @Scheduled(
      initialDelay = 120000L,
      fixedRateString = "${base.dbTableTimes}000"
   )
   public void tableCountTask() {
      if (!"master".equals(this.commonConfig.getNodeType())) {
         this.logger.info("slave节点不执行数据表监控任务");
      } else if (DateUtil.isClearTime()) {
         this.logger.info("正在清空历史数据，不执行数据表监控----------" + DateUtil.getCurrentDateTime());
      } else {
         this.logger.info("tableCountTask------------" + DateUtil.getDateTimeString(new Date()));

         try {
            this.dbTableService.taskThreadHandler();
         } catch (Exception var2) {
            this.logger.error("数据表监控任务错误", var2);
            this.logInfoService.save("数据表监控任务错误", var2.toString(), "2");
         }

      }
   }

   @Scheduled(
      initialDelay = 140000L,
      fixedRateString = "${base.ftpTimes}000"
   )
   public void ftpInfoTask() {
      if (!"master".equals(this.commonConfig.getNodeType())) {
         this.logger.info("slave节点不执行数据表监控任务");
      } else if (DateUtil.isClearTime()) {
         this.logger.info("正在清空历史数据，不执行数据表监控----------" + DateUtil.getCurrentDateTime());
      } else {
         this.logger.info("ftpInfoTask------------" + DateUtil.getDateTimeString(new Date()));

         try {
            this.ftpInfoService.taskThreadHandler();
         } catch (Exception var2) {
            this.logger.error("FTP监控任务错误", var2);
            this.logInfoService.save("FTP监控任务错误", var2.toString(), "2");
         }

      }
   }

   @Scheduled(
      initialDelay = 300000L,
      fixedRate = 300000L
   )
   public void hostDownCheckTask() {
      if (!"master".equals(this.commonConfig.getNodeType())) {
         this.logger.info("slave节点不执行检测主机/进程/docker/端口是否恢复任务");
      } else if (DateUtil.isClearTimeForHost()) {
         this.logger.info("正在清空历史数据，不执行提交检测主机/进程/docker/端口数据----------" + DateUtil.getCurrentDateTime());
         BatchData.clearAll();
      } else {
         this.logger.info("hostDownCheckTask------------" + DateUtil.getDateTimeString(new Date()));
         this.checkHostDown();
         this.checkAppDown();
         this.checkDockerDown();
         this.checkPortDown();
         this.checkFileSafeDown();
      }
   }

   private void checkHostDown() {
      ArrayList downHostNameList = new ArrayList();

      try {
         Map<String, Object> params = new HashMap();
         List<SystemInfo> list = this.systemInfoService.selectAllByParams(params);
         if (!CollectionUtil.isEmpty(list)) {
            Iterator var4 = list.iterator();

            while(var4.hasNext()) {
               SystemInfo systemInfo = (SystemInfo)var4.next();
               this.agentTimeOutHandle(systemInfo, downHostNameList);
            }

            if (downHostNameList.size() > 0) {
               this.systemInfoService.downByHostName(downHostNameList);
               this.appInfoService.downByHostName(downHostNameList);
               this.portInfoService.downByHostName(downHostNameList);
               this.dockerInfoService.downByHostName(downHostNameList);
               this.fileSafeService.downByHostName(downHostNameList);
               this.customInfoService.downByHostName(downHostNameList);
            }

            HostUtil.initHostCacheMap(list);
         }
      } catch (Exception var6) {
         this.logger.error("检测主机是否下线错误", var6);
         this.logInfoService.save("检测主机是否下线错误", var6.toString(), "2");
      }

   }

   private void agentTimeOutHandle(SystemInfo systemInfo, List<String> downHostNameList) {
      Date createTime = systemInfo.getCreateTime();
      long diff = System.currentTimeMillis() - createTime.getTime();
      Integer submitSeconds = 180000;

      try {
         submitSeconds = (Integer.valueOf(systemInfo.getSubmitSeconds()) + 60) * 1000;
      } catch (Exception var8) {
         this.logger.error("Integer转换错误", var8);
      }

      Runnable runnable;
      if (diff >= (long)submitSeconds) {
         if (null == WarnPools.MEM_WARN_MAP.get(systemInfo.getId())) {
            downHostNameList.add(systemInfo.getHostname());
         }

         runnable = () -> {
            WarnMailUtil.sendHostDown(systemInfo, true);
         };
         ThreadPoolUtil.executor.execute(runnable);
      } else if (null != WarnPools.MEM_WARN_MAP.get(systemInfo.getId())) {
         runnable = () -> {
            WarnMailUtil.sendHostDown(systemInfo, false);
         };
         ThreadPoolUtil.executor.execute(runnable);
      }

   }

   private void checkAppDown() {
      try {
         Map<String, Object> params = new HashMap();
         params.put("state", "1");
         params.put("active", "1");
         List<AppInfo> list = this.appInfoService.selectAllByParams(params);
         if (!CollectionUtil.isEmpty(list)) {
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
               AppInfo appInfo = (AppInfo)var3.next();
               if (null != WarnPools.MEM_WARN_MAP.get(appInfo.getId())) {
                  Runnable runnable = () -> {
                     WarnMailUtil.sendAppDown(appInfo, false);
                  };
                  ThreadPoolUtil.executor.execute(runnable);
               }
            }
         }
      } catch (Exception var6) {
         this.logger.error("检测进程是否恢复错误", var6);
         this.logInfoService.save("检测进程是否恢复错误", var6.toString(), "2");
      }

   }

   private void checkDockerDown() {
      try {
         Map<String, Object> params = new HashMap();
         params.put("state", "1");
         params.put("active", "1");
         List<DockerInfo> list = this.dockerInfoService.selectAllByParams(params);
         if (!CollectionUtil.isEmpty(list)) {
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
               DockerInfo appInfo = (DockerInfo)var3.next();
               if (null != WarnPools.MEM_WARN_MAP.get(appInfo.getId())) {
                  Runnable runnable = () -> {
                     WarnMailUtil.sendDockerDown(appInfo, false);
                  };
                  ThreadPoolUtil.executor.execute(runnable);
               }
            }
         }
      } catch (Exception var6) {
         this.logger.error("检测docker是否恢复错误", var6);
         this.logInfoService.save("检测docker是否恢复错误", var6.toString(), "2");
      }

   }

   private void checkPortDown() {
      try {
         Map<String, Object> params = new HashMap();
         params.put("state", "1");
         params.put("active", "1");
         List<PortInfo> list = this.portInfoService.selectAllByParams(params);
         if (!CollectionUtil.isEmpty(list)) {
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
               PortInfo appInfo = (PortInfo)var3.next();
               if (null != WarnPools.MEM_WARN_MAP.get(appInfo.getId())) {
                  Runnable runnable = () -> {
                     WarnMailUtil.sendPortDown(appInfo, false);
                  };
                  ThreadPoolUtil.executor.execute(runnable);
               }
            }
         }
      } catch (Exception var6) {
         this.logger.error("检测端口是否恢复错误", var6);
         this.logInfoService.save("检测端口是否恢复错误", var6.toString(), "2");
      }

   }

   private void checkFileSafeDown() {
      try {
         Map<String, Object> params = new HashMap();
         params.put("state", "1");
         params.put("active", "1");
         List<FileSafe> list = this.fileSafeService.selectAllByParams(params);
         if (!CollectionUtil.isEmpty(list)) {
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
               FileSafe fileSafe = (FileSafe)var3.next();
               if (null != WarnPools.MEM_WARN_MAP.get(fileSafe.getId())) {
                  Runnable runnable = () -> {
                     WarnMailUtil.sendFileSafeDown(fileSafe, false);
                  };
                  ThreadPoolUtil.executor.execute(runnable);
               }
            }
         }
      } catch (Exception var6) {
         this.logger.error("检测文件防篡改监测是否恢复错误", var6);
         this.logInfoService.save("检测文件防篡改监测是否恢复错误", var6.toString(), "2");
      }

   }
}
