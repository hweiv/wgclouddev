package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.HostDiskPer;
import com.wgcloud.entity.MemState;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.entity.ReportInfo;
import com.wgcloud.entity.ReportInstance;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.mapper.ReportInfoMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportInfoService {
   private static final Logger logger = LoggerFactory.getLogger(ReportInfoService.class);
   @Autowired
   private ReportInfoMapper reportInfoMapper;
   @Autowired
   private ReportInstanceService reportInstanceService;
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
   private EquipmentService equipmentService;
   @Autowired
   private AccountInfoService accountInfoService;
   @Autowired
   private TaskUtilService taskUtilService;
   @Autowired
   private CustomInfoService customInfoService;
   @Autowired
   CpuStateService cpuStateService;
   @Autowired
   MemStateService memStateService;
   @Autowired
   SysLoadStateService sysLoadStateService;
   @Autowired
   NetIoStateService netIoStateService;
   @Autowired
   private DbTableService dbTableService;
   @Resource
   DbInfoService dbInfoService;
   @Resource
   FileWarnInfoService fileWarnInfoService;
   @Autowired
   private FtpInfoService ftpInfoService;
   @Autowired
   private HostGroupService hostGroupService;
   @Autowired
   private HeathMonitorService heathMonitorService;
   @Autowired
   private DceInfoService dceInfoService;
   @Autowired
   private SnmpInfoService snmpInfoService;
   @Resource
   private HostDiskPerService hostDiskPerService;
   @Autowired
   private ServletContext servletContext;
   @Autowired
   private CommonConfig commonConfig;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<ReportInfo> list = this.reportInfoMapper.selectByParams(params);
      PageInfo<ReportInfo> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(ReportInfo reportInfo) {
      reportInfo.setId(UUIDUtil.getUUID());
      reportInfo.setCreateTime(new Date());

      try {
         this.reportInfoMapper.save(reportInfo);
      } catch (Exception var3) {
         logger.error("保存巡检报告信息异常：", var3);
      }

   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.reportInfoMapper.countByParams(params);
   }

   public int deleteById(String[] id) throws Exception {
      return this.reportInfoMapper.deleteById(id);
   }

   public ReportInfo selectById(String id) throws Exception {
      return this.reportInfoMapper.selectById(id);
   }

   public List<ReportInfo> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.reportInfoMapper.selectAllByParams(params);
   }

   public int deleteByDate(Map<String, Object> map) throws Exception {
      return this.reportInfoMapper.deleteByDate(map);
   }

   public void taskWeekThreadHandler() {
      try {
         Map<String, Object> params = new HashMap();
         String timePart = DateUtil.setLastWeekBeginEndTime(params);
         logger.info("周报查询条件：" + params.toString());
         Map<String, Object> paramsTimePart = new HashMap();
         paramsTimePart.put("timePart", timePart);
         int reportNum = this.countByParams(paramsTimePart);
         if (reportNum > 0) {
            logger.info("已经生成过周报：" + timePart);
            return;
         }

         Date date = new Date();
         List<ReportInstance> reportInstanceList = new ArrayList();
         ReportInfo reportInfo = new ReportInfo();
         reportInfo.setReportType("1");
         reportInfo.setTimePart(timePart);
         reportInfo.setCreateTime(date);
         this.save(reportInfo);
         this.monitorDataHandle(reportInstanceList, params, reportInfo);
         this.reportInstanceService.saveRecord(reportInstanceList, date);
      } catch (Exception var8) {
         logger.error("巡检周报错误", var8);
         this.logInfoService.save("巡检周报错误", var8.toString(), "2");
      }

   }

   public void taskMonThreadHandler() {
      try {
         Map<String, Object> params = new HashMap();
         String timePart = DateUtil.setLastMonBeginEndTime(params);
         logger.info("月报查询条件：" + params.toString());
         Map<String, Object> paramsTimePart = new HashMap();
         paramsTimePart.put("timePart", timePart);
         int reportNum = this.countByParams(paramsTimePart);
         if (reportNum > 0) {
            logger.info("已经生成过月报：" + timePart);
            return;
         }

         Date date = new Date();
         List<ReportInstance> reportInstanceList = new ArrayList();
         ReportInfo reportInfo = new ReportInfo();
         reportInfo.setReportType("2");
         reportInfo.setTimePart(timePart);
         reportInfo.setCreateTime(date);
         this.save(reportInfo);
         this.monitorDataHandle(reportInstanceList, params, reportInfo);
         this.reportInstanceService.saveRecord(reportInstanceList, date);
      } catch (Exception var8) {
         logger.error("巡检月报错误", var8);
         this.logInfoService.save("巡检月报错误", var8.toString(), "2");
      }

   }

   public void monitorDataHandle(List<ReportInstance> reportInstanceList, Map<String, Object> params, ReportInfo reportInfo) throws Exception {
      int totalSizeApp = this.appInfoService.countByParams(params);
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "监控进程总数量", totalSizeApp + "", reportInfo.getId());
      int portSize = this.portInfoService.countByParams(params);
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "监控端口总数量", portSize + "", reportInfo.getId());
      int heathSize = this.heathMonitorService.countByParams(params);
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "监控服务接口总数量", heathSize + "", reportInfo.getId());
      int dockerSize = this.dockerInfoService.countByParams(params);
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "监控docker总数量", dockerSize + "", reportInfo.getId());
      int dceSize = this.dceInfoService.countByParams(params);
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "监控PING设备总数量", dceSize + "", reportInfo.getId());
      Integer fileWarnSize = this.fileWarnInfoService.countByParams(params);
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "监控日志文件总数量", fileWarnSize + "", reportInfo.getId());
      int dbInfoSize = this.dbInfoService.countByParams(params);
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "监控数据源总数量", dbInfoSize + "", reportInfo.getId());
      int dbTableSize = this.dbTableService.countByParams(params);
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "监控数据表总数量", dbTableSize + "", reportInfo.getId());
      int snmpInfoSize = this.snmpInfoService.countByParams(params);
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "监控SNMP设备总数量", snmpInfoSize + "", reportInfo.getId());
      int customInfoSize = this.customInfoService.countByParams(params);
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "自定义监控项总数量", customInfoSize + "", reportInfo.getId());
      int fileSafeSize = this.fileSafeService.countByParams(params);
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "文件防篡改总数量", fileSafeSize + "", reportInfo.getId());
      int ftpInfoSize = this.ftpInfoService.countByParams(params);
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "ftp/sftp监测数量", ftpInfoSize + "", reportInfo.getId());
      int equipmentSize = this.equipmentService.countByParams(params);
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "资产总数量", equipmentSize + "", reportInfo.getId());
      int totalSizeAccount = this.accountInfoService.countByParams(params);
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "成员账号数量", totalSizeAccount + "", reportInfo.getId());
      int totalSizeHostGroup = this.hostGroupService.countByParams(params);
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "标签数量", totalSizeHostGroup + "", reportInfo.getId());
      Map<String, Object> paramsUptime = new HashMap();
      paramsUptime.put("orderBy", "UPTIME");
      paramsUptime.put("orderType", "DESC");
      List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(paramsUptime);
      if (systemInfoList.size() > 0) {
         SystemInfo systemInfoMaxUptime = (SystemInfo)systemInfoList.get(0);
         String systemInfoMaxUptimeContent = systemInfoMaxUptime.getHostname() + HostUtil.addRemark(systemInfoMaxUptime.getHostname()) + "，已运行" + systemInfoMaxUptime.getUptimeStr();
         this.reportInstanceService.mergeReportInsToList(reportInstanceList, "运行时间最长的主机", systemInfoMaxUptimeContent, reportInfo.getId());
      }

      Integer maxProcs = 0;
      Double avgProcs = 0.0D;
      Integer minProcs = 1000;
      Integer sumProcs = 0;
      Integer maxNetConnections = 0;
      Double avgNetConnections = 0.0D;
      Integer minNetConnections = 1000;
      Integer sumNetConnections = 0;
      CpuState maxAvgCpuState = this.cpuStateService.selectMaxAvgByHostname(params);
      if (null == maxAvgCpuState) {
         maxAvgCpuState = new CpuState();
         maxAvgCpuState.setSys(0.0D);
         maxAvgCpuState.setIdle(0.0D);
         maxAvgCpuState.setIowait(0.0D);
      }

      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机CPU使用率最高", maxAvgCpuState.getSys() + "%", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机CPU使用率平均", FormatUtil.formatDouble((Double)maxAvgCpuState.getIdle(), 2) + "%", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机CPU使用率最低", maxAvgCpuState.getIowait() + "%", reportInfo.getId());
      MemState maxAvgMemState = this.memStateService.selectMaxAvgByHostname(params);
      if (null == maxAvgMemState) {
         maxAvgMemState = new MemState();
         maxAvgMemState.setUsePer(0.0D);
         maxAvgMemState.setUsed("0");
         maxAvgMemState.setFree("0");
      }

      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机内存使用率最高", maxAvgMemState.getUsePer() + "%", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机内存使用率平均", FormatUtil.formatDouble((Double)Double.valueOf(maxAvgMemState.getUsed()), 2) + "%", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机内存使用率最低", maxAvgMemState.getFree() + "%", reportInfo.getId());
      NetIoState netIoStateMax = this.netIoStateService.selectMaxByHostname(params);
      if (null == netIoStateMax) {
         netIoStateMax = new NetIoState();
         netIoStateMax.setTxbyt("0");
         netIoStateMax.setRxbyt("0");
         netIoStateMax.setRxpck("0");
         netIoStateMax.setTxpck("0");
      }

      NetIoState netIoStateAvg = this.netIoStateService.selectAvgByHostname(params);
      if (null == netIoStateAvg) {
         netIoStateAvg = new NetIoState();
         netIoStateAvg.setTxbyt("0");
         netIoStateAvg.setRxbyt("0");
         netIoStateAvg.setRxpck("0");
         netIoStateAvg.setTxpck("0");
      }

      NetIoState netIoStateMin = this.netIoStateService.selectMinByHostname(params);
      if (null == netIoStateMin) {
         netIoStateMin = new NetIoState();
         netIoStateMin.setTxbyt("0");
         netIoStateMin.setRxbyt("0");
         netIoStateMin.setRxpck("0");
         netIoStateMin.setTxpck("0");
      }

      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机下行速率最高", FormatUtil.kbToM(netIoStateMax.getRxbyt()) + "/s", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机下行速率最低", FormatUtil.kbToM(netIoStateMin.getRxbyt()) + "/s", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机下行速率平均", FormatUtil.kbToM(FormatUtil.formatDouble((String)netIoStateAvg.getRxbyt(), 2) + "") + "/s", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机上行速率最高", FormatUtil.kbToM(netIoStateMax.getTxbyt()) + "/s", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机上行速率最低", FormatUtil.kbToM(netIoStateMin.getTxbyt()) + "/s", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机上行速率平均", FormatUtil.kbToM(FormatUtil.formatDouble((String)netIoStateAvg.getTxbyt(), 2) + "") + "/s", reportInfo.getId());
      SysLoadState sysLoadStateMax = this.sysLoadStateService.selectMaxByHostname(params);
      if (null == sysLoadStateMax) {
         sysLoadStateMax = new SysLoadState();
         sysLoadStateMax.setOneLoad(0.0D);
         sysLoadStateMax.setFiveLoad(0.0D);
         sysLoadStateMax.setFifteenLoad(0.0D);
      }

      SysLoadState sysLoadStateAvg = this.sysLoadStateService.selectAvgByHostname(params);
      if (null == sysLoadStateAvg) {
         sysLoadStateAvg = new SysLoadState();
         sysLoadStateAvg.setOneLoad(0.0D);
         sysLoadStateAvg.setFiveLoad(0.0D);
         sysLoadStateAvg.setFifteenLoad(0.0D);
      }

      SysLoadState sysLoadStateMin = this.sysLoadStateService.selectMinByHostname(params);
      if (null == sysLoadStateMin) {
         sysLoadStateMin = new SysLoadState();
         sysLoadStateMin.setOneLoad(0.0D);
         sysLoadStateMin.setFiveLoad(0.0D);
         sysLoadStateMin.setFifteenLoad(0.0D);
      }

      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机1分钟系统负载最高", sysLoadStateMax.getOneLoad() + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机1分钟系统负载最低", sysLoadStateMin.getOneLoad() + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机1分钟系统负载平均", FormatUtil.formatDouble((Double)sysLoadStateAvg.getOneLoad(), 2) + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机5分钟系统负载最高", sysLoadStateMax.getFiveLoad() + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机5分钟系统负载最低", sysLoadStateMin.getFiveLoad() + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机5分钟系统负载平均", FormatUtil.formatDouble((Double)sysLoadStateAvg.getFiveLoad(), 2) + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机15分钟系统负载最高", sysLoadStateMax.getFifteenLoad() + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机15分钟系统负载最低", sysLoadStateMin.getFifteenLoad() + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机15分钟系统负载平均", FormatUtil.formatDouble((Double)sysLoadStateAvg.getFifteenLoad(), 2) + "", reportInfo.getId());
      int systemSize = 0;
      int cpuCoresSum = 0;
      double memSum = 0.0D;
      int submitSecondsSum = 0;
      double avgSubmitSeconds = 120.0D;
      Iterator var44 = systemInfoList.iterator();

      while(var44.hasNext()) {
         SystemInfo systemInfo = (SystemInfo)var44.next();
         ++systemSize;

         try {
            cpuCoresSum += Integer.valueOf(systemInfo.getCpuCoreNum());
         } catch (Exception var55) {
            logger.error("统计所有主机总核数错误", var55);
         }

         try {
            memSum += Double.valueOf(systemInfo.getTotalMem().replace("G", ""));
         } catch (Exception var54) {
            logger.error("统计所有主机总内存错误", var54);
         }

         try {
            submitSecondsSum += Integer.valueOf(systemInfo.getSubmitSeconds());
         } catch (Exception var53) {
            logger.error("统计所有主机组装上报数据频率总和错误", var53);
         }

         if (!StringUtils.isEmpty(systemInfo.getProcs())) {
            if (Integer.valueOf(systemInfo.getProcs()) > maxProcs) {
               maxProcs = Integer.valueOf(systemInfo.getProcs());
            }

            if (Integer.valueOf(systemInfo.getProcs()) < minProcs) {
               minProcs = Integer.valueOf(systemInfo.getProcs());
            }

            sumProcs = sumProcs + Integer.valueOf(systemInfo.getProcs());
         }

         if (!StringUtils.isEmpty(systemInfo.getNetConnections())) {
            if (Integer.valueOf(systemInfo.getNetConnections()) > maxNetConnections) {
               maxNetConnections = Integer.valueOf(systemInfo.getNetConnections());
            }

            if (Integer.valueOf(systemInfo.getNetConnections()) < minNetConnections) {
               minNetConnections = Integer.valueOf(systemInfo.getNetConnections());
            }

            sumNetConnections = sumNetConnections + Integer.valueOf(systemInfo.getNetConnections());
         }
      }

      if (systemSize > 0) {
         avgSubmitSeconds = (double)submitSecondsSum / (double)systemInfoList.size();
         avgProcs = (double)sumProcs / (double)systemInfoList.size();
         avgNetConnections = (double)sumNetConnections / (double)systemInfoList.size();
      } else {
         avgProcs = 0.0D;
         avgNetConnections = 0.0D;
      }

      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "监控主机总数量", systemSize + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "CPU核数总和", cpuCoresSum + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "内存总和", FormatUtil.formatDouble((Double)memSum, 2) + "G", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "磁盘容量总和", this.servletContext.getAttribute("sumDiskSizeCache") + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "Agent平均监控上报频率", FormatUtil.formatDouble((Double)avgSubmitSeconds, 2) + "秒", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机连接数量最高", maxNetConnections + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机连接数量平均", FormatUtil.formatDouble((Double)avgNetConnections, 2) + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机连接数量最低", minNetConnections + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机运行进程数量最高", maxProcs + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机运行进程数量平均", FormatUtil.formatDouble((Double)avgProcs, 2) + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机运行进程数量最低", minProcs + "", reportInfo.getId());
      params.put("hostname", "开始下发指令");
      int shellSize = this.logInfoService.countByParams(params);
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "累积下发指令数量", shellSize + "", reportInfo.getId());
      params.put("hostname", "告警");
      int warnSize = this.logInfoService.countByParams(params);
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "累积告警数量", warnSize + "", reportInfo.getId());
      Double maxDiskPer = 0.0D;
      Double minDiskPer = 1000.0D;
      Double avgDiskPer = 0.0D;
      Double sumDiskPer = 0.0D;
      params.remove("hostname");
      List<HostDiskPer> hostDiskPerList = this.hostDiskPerService.selectAllByParams(params);
      Iterator var51 = hostDiskPerList.iterator();

      while(var51.hasNext()) {
         HostDiskPer hostDiskPer = (HostDiskPer)var51.next();
         if (null != hostDiskPer.getDiskSumPer() && hostDiskPer.getDiskSumPer() > maxDiskPer) {
            maxDiskPer = hostDiskPer.getDiskSumPer();
         }

         if (null != hostDiskPer.getDiskSumPer() && hostDiskPer.getDiskSumPer() < minDiskPer) {
            minDiskPer = hostDiskPer.getDiskSumPer();
         }

         if (null != hostDiskPer.getDiskSumPer()) {
            sumDiskPer = sumDiskPer + hostDiskPer.getDiskSumPer();
         }
      }

      if (hostDiskPerList.size() > 0) {
         avgDiskPer = sumDiskPer / (double)hostDiskPerList.size();
      } else {
         minDiskPer = 0.0D;
      }

      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机磁盘总使用率最高", maxDiskPer + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机磁盘总使用率平均", FormatUtil.formatDouble((Double)avgDiskPer, 2) + "", reportInfo.getId());
      this.reportInstanceService.mergeReportInsToList(reportInstanceList, "主机磁盘总使用率最低", minDiskPer + "", reportInfo.getId());
   }
}
