package com.wgcloud.task;

import cn.hutool.core.collection.CollectionUtil;
import com.wgcloud.common.NettytHandler;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.entity.CustomInfo;
import com.wgcloud.entity.DeskIo;
import com.wgcloud.entity.DiskSmart;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.DockerInfo;
import com.wgcloud.entity.FileSafe;
import com.wgcloud.entity.FileWarnInfo;
import com.wgcloud.entity.HostDiskPer;
import com.wgcloud.entity.PortInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.AppStateService;
import com.wgcloud.service.CpuStateService;
import com.wgcloud.service.CpuTemperaturesService;
import com.wgcloud.service.CustomInfoService;
import com.wgcloud.service.CustomStateService;
import com.wgcloud.service.DbTableCountService;
import com.wgcloud.service.DceStateService;
import com.wgcloud.service.DeskIoService;
import com.wgcloud.service.DiskSmartService;
import com.wgcloud.service.DiskStateService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.DockerStateService;
import com.wgcloud.service.FileSafeService;
import com.wgcloud.service.FileWarnInfoService;
import com.wgcloud.service.FileWarnStateService;
import com.wgcloud.service.HeathStateService;
import com.wgcloud.service.HostDiskPerService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.MemStateService;
import com.wgcloud.service.NetIoStateService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.ReportInfoService;
import com.wgcloud.service.ReportInstanceService;
import com.wgcloud.service.ShellStateService;
import com.wgcloud.service.SnmpStateService;
import com.wgcloud.service.SysLoadStateService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.service.TaskUtilService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.ThreadPoolUtil;
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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataScheduledTask {
   private Logger logger = LoggerFactory.getLogger(DataScheduledTask.class);
   @Autowired
   private SystemInfoService systemInfoService;
   @Autowired
   private DiskStateService diskStateService;
   @Autowired
   private DeskIoService deskIoService;
   @Autowired
   private DiskSmartService diskSmartService;
   @Autowired
   private HostDiskPerService hostDiskPerService;
   @Autowired
   private CpuTemperaturesService cpuTemperaturesService;
   @Autowired
   private LogInfoService logInfoService;
   @Autowired
   private AppInfoService appInfoService;
   @Autowired
   private CustomInfoService customInfoService;
   @Autowired
   private FileWarnInfoService fileWarnInfoService;
   @Autowired
   private DockerInfoService dockerInfoService;
   @Autowired
   private PortInfoService portInfoService;
   @Autowired
   private FileSafeService fileSafeService;
   @Autowired
   private CpuStateService cpuStateService;
   @Autowired
   private MemStateService memStateService;
   @Autowired
   private NetIoStateService netIoStateService;
   @Autowired
   private SysLoadStateService sysLoadStateService;
   @Autowired
   private AppStateService appStateService;
   @Autowired
   private CustomStateService customStateService;
   @Autowired
   private FileWarnStateService fileWarnStateService;
   @Autowired
   private ShellStateService shellStateService;
   @Autowired
   private HeathStateService heathStateService;
   @Autowired
   private DceStateService dceStateService;
   @Autowired
   private SnmpStateService snmpStateService;
   @Autowired
   private DockerStateService dockerStateService;
   @Autowired
   private DbTableCountService dbTableCountService;
   @Autowired
   private ReportInfoService reportInfoService;
   @Autowired
   private ReportInstanceService reportInstanceService;
   @Autowired
   private TaskUtilService taskUtilService;
   @Autowired
   private CommonConfig commonConfig;

   @Scheduled(
      initialDelay = 9000L,
      fixedRate = 9000L
   )
   public synchronized void commitTask() {
      if (DateUtil.isClearTime()) {
         this.logger.info("正在清空历史趋势图数据，不执行提交监控数据----------" + DateUtil.getCurrentDateTime());
         BatchData.clearAll();
      } else {
         this.logger.info("批量提交监控数据任务开始----------" + DateUtil.getCurrentDateTime());

         try {
            if (BatchData.SYSTEM_INFO_LIST.size() > 0) {
               this.batchCommitSystemInfo();
            }

            if (BatchData.APP_INFO_LIST.size() > 0) {
               this.batchCommitAppInfo();
            }

            if (BatchData.CUSTOM_INFO_LIST.size() > 0) {
               this.batchCommitCustomInfo();
            }

            if (BatchData.FILEWARN_INFO_LIST.size() > 0) {
               this.batchCommitFileWarnInfo();
            }

            if (BatchData.PORT_INFO_LIST.size() > 0) {
               this.batchCommitPortInfo();
            }

            if (BatchData.FILE_SAFE_LIST.size() > 0) {
               this.batchCommitFileSafes();
            }

            if (BatchData.DOCKER_INFO_LIST.size() > 0) {
               this.batchCommitDockerInfo();
            }

            ArrayList LOG_INFO_LIST;
            if (BatchData.FILEWARN_STATE_LIST.size() > 0) {
               LOG_INFO_LIST = new ArrayList();
               LOG_INFO_LIST.addAll(BatchData.FILEWARN_STATE_LIST);
               BatchData.FILEWARN_STATE_LIST.clear();
               this.fileWarnStateService.saveRecord(LOG_INFO_LIST);
            }

            if (BatchData.APP_STATE_LIST.size() > 0) {
               LOG_INFO_LIST = new ArrayList();
               LOG_INFO_LIST.addAll(BatchData.APP_STATE_LIST);
               BatchData.APP_STATE_LIST.clear();
               this.appStateService.saveRecord(LOG_INFO_LIST);
            }

            if (BatchData.CUSTOM_STATE_LIST.size() > 0) {
               LOG_INFO_LIST = new ArrayList();
               LOG_INFO_LIST.addAll(BatchData.CUSTOM_STATE_LIST);
               BatchData.CUSTOM_STATE_LIST.clear();
               this.customStateService.saveRecord(LOG_INFO_LIST);
            }

            if (BatchData.HEATH_STATE_LIST.size() > 0) {
               LOG_INFO_LIST = new ArrayList();
               LOG_INFO_LIST.addAll(BatchData.HEATH_STATE_LIST);
               BatchData.HEATH_STATE_LIST.clear();
               this.heathStateService.saveRecord(LOG_INFO_LIST);
            }

            if (BatchData.DCE_STATE_LIST.size() > 0) {
               LOG_INFO_LIST = new ArrayList();
               LOG_INFO_LIST.addAll(BatchData.DCE_STATE_LIST);
               BatchData.DCE_STATE_LIST.clear();
               this.dceStateService.saveRecord(LOG_INFO_LIST);
            }

            if (BatchData.DBTABLE_COUNT_LIST.size() > 0) {
               LOG_INFO_LIST = new ArrayList();
               LOG_INFO_LIST.addAll(BatchData.DBTABLE_COUNT_LIST);
               BatchData.DBTABLE_COUNT_LIST.clear();
               this.dbTableCountService.saveRecord(LOG_INFO_LIST);
            }

            if (BatchData.SNMP_STATE_LIST.size() > 0) {
               LOG_INFO_LIST = new ArrayList();
               LOG_INFO_LIST.addAll(BatchData.SNMP_STATE_LIST);
               BatchData.SNMP_STATE_LIST.clear();
               this.snmpStateService.saveRecord(LOG_INFO_LIST);
            }

            if (BatchData.DOCKER_STATE_LIST.size() > 0) {
               LOG_INFO_LIST = new ArrayList();
               LOG_INFO_LIST.addAll(BatchData.DOCKER_STATE_LIST);
               BatchData.DOCKER_STATE_LIST.clear();
               this.dockerStateService.saveRecord(LOG_INFO_LIST);
            }

            if (BatchData.CPU_STATE_LIST.size() > 0) {
               LOG_INFO_LIST = new ArrayList();
               LOG_INFO_LIST.addAll(BatchData.CPU_STATE_LIST);
               BatchData.CPU_STATE_LIST.clear();
               this.cpuStateService.saveRecord(LOG_INFO_LIST);
            }

            if (BatchData.MEM_STATE_LIST.size() > 0) {
               LOG_INFO_LIST = new ArrayList();
               LOG_INFO_LIST.addAll(BatchData.MEM_STATE_LIST);
               BatchData.MEM_STATE_LIST.clear();
               this.memStateService.saveRecord(LOG_INFO_LIST);
            }

            if (BatchData.NETIO_STATE_LIST.size() > 0) {
               LOG_INFO_LIST = new ArrayList();
               LOG_INFO_LIST.addAll(BatchData.NETIO_STATE_LIST);
               BatchData.NETIO_STATE_LIST.clear();
               this.netIoStateService.saveRecord(LOG_INFO_LIST);
            }

            if (BatchData.SYSLOAD_STATE_LIST.size() > 0) {
               LOG_INFO_LIST = new ArrayList();
               LOG_INFO_LIST.addAll(BatchData.SYSLOAD_STATE_LIST);
               BatchData.SYSLOAD_STATE_LIST.clear();
               this.sysLoadStateService.saveRecord(LOG_INFO_LIST);
            }

            if (BatchData.LOG_INFO_LIST.size() > 0) {
               LOG_INFO_LIST = new ArrayList();
               LOG_INFO_LIST.addAll(BatchData.LOG_INFO_LIST);
               BatchData.LOG_INFO_LIST.clear();
               this.logInfoService.saveRecord(LOG_INFO_LIST);
            }

            if (BatchData.DISK_STATE_LIST.size() > 0) {
               this.batchCommitDeskState();
            }

            if (BatchData.DESK_IO_LIST.size() > 0) {
               this.batchCommitDeskIo();
            }

            if (BatchData.DISK_SMART_LIST.size() > 0) {
               this.batchCommitDiskSmart();
            }

            if (BatchData.CPU_TEMPERATURES_LIST.size() > 0) {
               this.batchCommitCpuTemperatures();
            }
         } catch (Exception var2) {
            this.logger.error("批量提交监控数据错误", var2);
            this.logInfoService.save("批量提交监控数据错误", var2.toString(), "2");
         }

         this.logger.info("批量提交监控数据任务结束----------" + DateUtil.getCurrentDateTime());
      }
   }

   private void batchCommitCpuTemperatures() throws Exception {
      new HashMap();
      List<CpuTemperatures> CPU_TEMPERATURES_LIST = new ArrayList();
      CPU_TEMPERATURES_LIST.addAll(BatchData.CPU_TEMPERATURES_LIST);
      BatchData.CPU_TEMPERATURES_LIST.clear();
      List<String> hostnameList = new ArrayList();
      Iterator var4 = CPU_TEMPERATURES_LIST.iterator();

      while(var4.hasNext()) {
         CpuTemperatures deskState = (CpuTemperatures)var4.next();
         if (!hostnameList.contains(deskState.getHostname())) {
            hostnameList.add(deskState.getHostname());
         }
      }

      if (!CollectionUtil.isEmpty(hostnameList)) {
         this.cpuTemperaturesService.deleteByAccHname(hostnameList);
      }

      this.cpuTemperaturesService.saveRecord(CPU_TEMPERATURES_LIST);
   }

   private void batchCommitDiskSmart() throws Exception {
      List<DiskSmart> DISK_SMART_LIST = new ArrayList();
      DISK_SMART_LIST.addAll(BatchData.DISK_SMART_LIST);
      BatchData.DISK_SMART_LIST.clear();
      List<String> hostnameList = new ArrayList();
      Iterator var3 = DISK_SMART_LIST.iterator();

      while(var3.hasNext()) {
         DiskSmart diskSmart = (DiskSmart)var3.next();
         if (!hostnameList.contains(diskSmart.getHostname())) {
            hostnameList.add(diskSmart.getHostname());
         }
      }

      if (!CollectionUtil.isEmpty(hostnameList)) {
         this.diskSmartService.deleteByAccHname(hostnameList);
      }

      this.diskSmartService.saveRecord(DISK_SMART_LIST);
   }

   private void batchCommitDeskIo() throws Exception {
      List<DeskIo> DESK_IO_LIST = new ArrayList();
      DESK_IO_LIST.addAll(BatchData.DESK_IO_LIST);
      BatchData.DESK_IO_LIST.clear();
      List<String> hostnameList = new ArrayList();
      Iterator var3 = DESK_IO_LIST.iterator();

      while(var3.hasNext()) {
         DeskIo deskState = (DeskIo)var3.next();
         if (!hostnameList.contains(deskState.getHostname())) {
            hostnameList.add(deskState.getHostname());
         }
      }

      if (!CollectionUtil.isEmpty(hostnameList)) {
         this.deskIoService.deleteByAccHname(hostnameList);
      }

      this.deskIoService.saveRecord(DESK_IO_LIST);
   }

   private void batchCommitDeskState() throws Exception {
      List<DiskState> DISK_STATE_LIST = new ArrayList();
      DISK_STATE_LIST.addAll(BatchData.DISK_STATE_LIST);
      BatchData.DISK_STATE_LIST.clear();
      List<String> hostnameList = new ArrayList();
      Iterator var3 = DISK_STATE_LIST.iterator();

      while(var3.hasNext()) {
         DiskState diskState = (DiskState)var3.next();
         if (!hostnameList.contains(diskState.getHostname())) {
            hostnameList.add(diskState.getHostname());
         }
      }

      if (!CollectionUtil.isEmpty(hostnameList)) {
         this.diskStateService.deleteByAccHname(hostnameList);
      }

      this.diskStateService.saveRecord(DISK_STATE_LIST);
   }

   private void batchCommitDockerInfo() throws Exception {
      new HashMap();
      List<DockerInfo> DOCKER_INFO_LIST = new ArrayList();
      DOCKER_INFO_LIST.addAll(BatchData.DOCKER_INFO_LIST);
      BatchData.DOCKER_INFO_LIST.clear();
      this.dockerInfoService.updateRecord(DOCKER_INFO_LIST);
   }

   private void batchCommitAppInfo() throws Exception {
      new HashMap();
      List<AppInfo> APP_INFO_LIST = new ArrayList();
      APP_INFO_LIST.addAll(BatchData.APP_INFO_LIST);
      BatchData.APP_INFO_LIST.clear();
      this.appInfoService.updateRecord(APP_INFO_LIST);
   }

   private void batchCommitCustomInfo() throws Exception {
      new HashMap();
      List<CustomInfo> CUSTOM_INFO_LIST = new ArrayList();
      CUSTOM_INFO_LIST.addAll(BatchData.CUSTOM_INFO_LIST);
      BatchData.CUSTOM_INFO_LIST.clear();
      this.customInfoService.updateRecord(CUSTOM_INFO_LIST);
   }

   private void batchCommitFileWarnInfo() throws Exception {
      new HashMap();
      List<FileWarnInfo> FILEWARN_INFO_LIST = new ArrayList();
      FILEWARN_INFO_LIST.addAll(BatchData.FILEWARN_INFO_LIST);
      BatchData.FILEWARN_INFO_LIST.clear();
      this.fileWarnInfoService.updateRecord(FILEWARN_INFO_LIST);
   }

   private void batchCommitPortInfo() throws Exception {
      new HashMap();
      List<PortInfo> PORT_INFO_LIST = new ArrayList();
      PORT_INFO_LIST.addAll(BatchData.PORT_INFO_LIST);
      BatchData.PORT_INFO_LIST.clear();
      this.portInfoService.updateRecord(PORT_INFO_LIST);
   }

   private void batchCommitFileSafes() throws Exception {
      new HashMap();
      List<FileSafe> FILE_SAFE_LIST = new ArrayList();
      FILE_SAFE_LIST.addAll(BatchData.FILE_SAFE_LIST);
      BatchData.FILE_SAFE_LIST.clear();
      this.fileSafeService.updateRecord(FILE_SAFE_LIST);
   }

   private void batchCommitSystemInfo() throws Exception {
      Map<String, Object> paramsDel = new HashMap();
      List<SystemInfo> SYSTEM_INFO_LIST = new ArrayList();
      SYSTEM_INFO_LIST.addAll(BatchData.SYSTEM_INFO_LIST);
      BatchData.SYSTEM_INFO_LIST.clear();
      List<SystemInfo> updateList = new ArrayList();
      List<SystemInfo> insertList = new ArrayList();
      List<SystemInfo> savedList = this.systemInfoService.selectAllHostNameByParams(paramsDel);
      Iterator var6 = SYSTEM_INFO_LIST.iterator();

      while(true) {
         SystemInfo systemInfo;
         do {
            if (!var6.hasNext()) {
               this.systemInfoService.updateRecord(updateList);
               this.systemInfoService.saveRecord(insertList);
               var6 = insertList.iterator();

               while(var6.hasNext()) {
                  systemInfo = (SystemInfo)var6.next();
                  SystemInfo finalSystemInfo = systemInfo;
                  Runnable runnable = () -> {
                     WarnMailUtil.sendHostDown(finalSystemInfo, false);
                  };
                  ThreadPoolUtil.executor.execute(runnable);
               }

               return;
            }

            systemInfo = (SystemInfo)var6.next();
         } while(StringUtils.isEmpty(systemInfo.getHostname()));

         boolean issaved = false;
         Iterator var9 = savedList.iterator();

         while(var9.hasNext()) {
            SystemInfo systemInfoS = (SystemInfo)var9.next();
            if (systemInfoS.getHostname().equals(systemInfo.getHostname())) {
               systemInfo.setId(systemInfoS.getId());
               updateList.add(systemInfo);
               issaved = true;
               break;
            }
         }

         if (!issaved) {
            insertList.add(systemInfo);
         }
      }
   }

   private void addHostDiskPer() throws Exception {
      Date nowtime = DateUtil.getNowTime();
      List<HostDiskPer> hostDiskPerList = new ArrayList();
      Map<String, Object> params = new HashMap();
      List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
      Iterator var5 = systemInfoList.iterator();

      while(var5.hasNext()) {
         SystemInfo systemInfo1 = (SystemInfo)var5.next();
         params.put("hostname", systemInfo1.getHostname());
         List<DiskState> deskStates = this.diskStateService.selectAllByParams(params);
         HostUtil.setDiskSumPer(deskStates, systemInfo1);
         HostDiskPer hostDiskPer = new HostDiskPer();
         hostDiskPer.setCreateTime(nowtime);
         hostDiskPer.setDiskSumPer(systemInfo1.getDiskPer());
         hostDiskPer.setHostname(systemInfo1.getHostname());
         hostDiskPerList.add(hostDiskPer);
      }

      this.hostDiskPerService.saveRecord(hostDiskPerList);
   }

   @Scheduled(
      cron = "55 24 8 * * ?"
   )
   public void refreshCommitDate() {
      try {
         this.taskUtilService.refreshCommitDate();
      } catch (Exception var2) {
         this.logger.error("刷新监控数据更新时间错误", var2);
      }

   }

   @Scheduled(
      cron = "0 10 8 * * ?"
   )
   public void clearHisdataTask() {
      if (!"master".equals(this.commonConfig.getNodeType())) {
         this.logger.info("slave节点不执行清空历史趋势图数据任务");
      } else {
         this.logger.info("清空" + this.commonConfig.getHistoryDataOut() + "天前的监控历史趋势图数据");
         this.logger.info("定时清空历史趋势图数据任务开始----------" + DateUtil.getCurrentDateTime());
         String thrityDayBefore = DateUtil.getDateBefore(this.commonConfig.getHistoryDataOut());
         HashMap paramsDel = new HashMap();

         try {
            paramsDel.put("endTime", thrityDayBefore);
            if (paramsDel.get("endTime") != null && !"".equals(paramsDel.get("endTime"))) {
               this.cpuStateService.deleteByAccountAndDate(paramsDel);
               this.memStateService.deleteByAccountAndDate(paramsDel);
               this.netIoStateService.deleteByAccountAndDate(paramsDel);
               this.sysLoadStateService.deleteByAccountAndDate(paramsDel);
               this.appStateService.deleteByDate(paramsDel);
               this.dockerStateService.deleteByDate(paramsDel);
               this.diskStateService.deleteByAccountAndDate(paramsDel);
               this.deskIoService.deleteByAccountAndDate(paramsDel);
               this.diskSmartService.deleteByAccountAndDate(paramsDel);
               this.cpuTemperaturesService.deleteByAccountAndDate(paramsDel);
               this.heathStateService.deleteByDate(paramsDel);
               this.dceStateService.deleteByDate(paramsDel);
               this.logInfoService.deleteByDate(paramsDel);
               this.dbTableCountService.deleteByDate(paramsDel);
               this.fileWarnStateService.deleteByDate(paramsDel);
               this.shellStateService.deleteByDate(paramsDel);
               this.hostDiskPerService.deleteByAccountAndDate(paramsDel);
               this.snmpStateService.deleteByDate(paramsDel);
               this.customStateService.deleteByDate(paramsDel);
               this.addHostDiskPer();
               this.logger.info("清空告警标记");
               WarnPools.clearOldData();
               this.logger.info("清空登录账号登录错误缓存记录");
               StaticKeys.LOGIN_BLOCK_MAP.clear();
               StaticKeys.LOGIN_ERROR_MAP.clear();
               this.logger.info("清空web ssh终端缓存");
               NettytHandler.clearOldData();
               HostUtil.clearCacheMap();
               ServerBackupUtil.clearCacheIdList();
               this.logInfoService.save("定时清空历史趋势图数据完成", "每天8:10清空" + this.commonConfig.getHistoryDataOut() + "天前的监控历史趋势图数据，任务完成", "2");
            }
         } catch (Exception var4) {
            this.logger.error("定时清空历史趋势图数据任务出错", var4);
            this.logInfoService.save("定时清空历史趋势图数据错误", var4.toString(), "2");
         }

         this.logger.info("定时清空历史趋势图数据任务结束----------" + DateUtil.getCurrentDateTime());
      }
   }

   @Scheduled(
      cron = "0 0 4 ? * MON-FRI"
   )
   public void generateReportWeekTask() {
      if (!"master".equals(this.commonConfig.getNodeType())) {
         this.logger.info("slave节点不执行生成巡检周报任务");
      } else {
         this.logger.info("定时生成巡检周报任务----------" + DateUtil.getCurrentDateTime());

         try {
            this.reportInfoService.taskWeekThreadHandler();
         } catch (Exception var2) {
            this.logger.error("定时生成巡检周报任务错误", var2);
         }

      }
   }

   @Scheduled(
      cron = "0 0 1 1-8 * ?"
   )
   public void generateReportMonTask() {
      if (!"master".equals(this.commonConfig.getNodeType())) {
         this.logger.info("slave节点不执行生成巡检月报任务");
      } else {
         this.logger.info("定时生成巡检月报任务----------" + DateUtil.getCurrentDateTime());

         try {
            this.reportInfoService.taskMonThreadHandler();
         } catch (Exception var2) {
            this.logger.error("定时生成巡检月报任务错误", var2);
         }

      }
   }
}
