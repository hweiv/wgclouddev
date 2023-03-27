package com.wgcloud.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.ChartDapingNewInfo;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.entity.HostDiskPer;
import com.wgcloud.entity.LogInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.CustomInfoService;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.DiskStateService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.FileSafeService;
import com.wgcloud.service.FileWarnInfoService;
import com.wgcloud.service.HeathMonitorService;
import com.wgcloud.service.HostDiskPerService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.ShellInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/dapingNew"})
public class DapingNewController {
   private static final Logger logger = LoggerFactory.getLogger(DapingNewController.class);
   @Resource
   private DbTableService dbTableService;
   @Resource
   private PortInfoService portInfoService;
   @Resource
   private FileSafeService fileSafeService;
   @Resource
   private DceInfoService dceInfoService;
   @Resource
   private SnmpInfoService snmpInfoService;
   @Resource
   private DbInfoService dbInfoService;
   @Resource
   private SystemInfoService systemInfoService;
   @Resource
   private AppInfoService appInfoService;
   @Resource
   private FileWarnInfoService fileWarnInfoService;
   @Resource
   private CustomInfoService customInfoService;
   @Resource
   private HostDiskPerService hostDiskPerService;
   @Resource
   private DockerInfoService dockerInfoService;
   @Resource
   private LogInfoService logInfoService;
   @Autowired
   private HeathMonitorService heathMonitorService;
   @Autowired
   private ShellInfoService shellInfoService;
   @Resource
   private AccountInfoService accountInfoService;
   @Resource
   private DiskStateService diskStateService;
   @Autowired
   private CommonConfig commonConfig;

   private void testThread() {
      Runnable runnable = () -> {
         logger.info("DapingNewController----------testThread");
      };
      ThreadPoolUtil.executor.execute(runnable);
   }

   @RequestMapping({"index"})
   public String index(Model model, HttpServletRequest request) {
      if (!StaticKeys.LICENSE_STATE.equals("1")) {
         return "daping/error";
      } else {
         HashMap params = new HashMap();

         try {
            int totalSystemInfoSize = this.systemInfoService.countByParams(params);
            model.addAttribute("totalSystemInfoSize", totalSystemInfoSize);
            params.put("state", "2");
            int hostDownSize = this.systemInfoService.countByParams(params);
            model.addAttribute("hostDownSize", hostDownSize);
            params.clear();
            int totalSizeApp = this.appInfoService.countByParams(params);
            model.addAttribute("totalSizeApp", totalSizeApp);
            params.put("state", "2");
            int downAppSize = this.appInfoService.countByParams(params);
            model.addAttribute("downAppSize", downAppSize);
            params.clear();
            int dockerSize = this.dockerInfoService.countByParams(params);
            model.addAttribute("dockerSize", dockerSize);
            params.put("state", "2");
            int downDockerSize = this.dockerInfoService.countByParams(params);
            model.addAttribute("downDockerSize", downDockerSize);
            params.clear();
            int portSize = this.portInfoService.countByParams(params);
            model.addAttribute("portSize", portSize);
            params.put("state", "2");
            int portDownSize = this.portInfoService.countByParams(params);
            model.addAttribute("portDownSize", portDownSize);
            params.clear();
            int dceSize = this.dceInfoService.countByParams(params);
            model.addAttribute("dceSize", dceSize);
            params.put("resTimes", -1);
            int dceDownSize = this.dceInfoService.countByParams(params);
            model.addAttribute("dceDownSize", dceDownSize);
            params.clear();
            int snmpSize = this.snmpInfoService.countByParams(params);
            model.addAttribute("snmpSize", snmpSize);
            params.put("state", "2");
            int snmpDownSize = this.snmpInfoService.countByParams(params);
            model.addAttribute("snmpDownSize", snmpDownSize);
            params.clear();
            Integer fileWarnSize = this.fileWarnInfoService.countByParams(params);
            model.addAttribute("fileWarnSize", fileWarnSize == null ? 0 : fileWarnSize);
            params.clear();
            Integer customInfoSize = this.customInfoService.countByParams(params);
            model.addAttribute("customInfoSize", customInfoSize == null ? 0 : customInfoSize);
            params.clear();
            Integer fileSafeSize = this.fileSafeService.countByParams(params);
            model.addAttribute("fileSafeSize", fileSafeSize == null ? 0 : fileSafeSize);
            params.put("state", "2");
            int fileSafeDownSize = this.fileSafeService.countByParams(params);
            model.addAttribute("fileSafeDownSize", fileSafeDownSize);
            params.clear();
            Integer dbTableSize = this.dbTableService.countByParams(params);
            model.addAttribute("dbTableSize", dbTableSize);
            params.clear();
            int dbInfoSize = this.dbInfoService.countByParams(params);
            model.addAttribute("dbInfoSize", dbInfoSize);
            params.clear();
            params.put("dbState", "2");
            int dbInfoDownSize = this.dbInfoService.countByParams(params);
            model.addAttribute("dbInfoDownSize", dbInfoDownSize);
            params.clear();
            int heathSize = this.heathMonitorService.countByParams(params);
            model.addAttribute("heathSize", heathSize);
            params.put("heathStatus", "200");
            int heath200Size = this.heathMonitorService.countByParams(params);
            model.addAttribute("heath200Size", heath200Size);
            model.addAttribute("heatherrSize", heathSize - heath200Size);
            params.clear();
            int shellInfoSize = this.shellInfoService.countByParams(params);
            model.addAttribute("shellInfoSize", shellInfoSize);
            Integer resourceAllSize = totalSystemInfoSize + totalSizeApp + dockerSize + portSize + dceSize + snmpSize + fileWarnSize + dbInfoSize + heathSize + fileSafeSize + dbTableSize + customInfoSize;
            model.addAttribute("resourceAllSize", resourceAllSize);
            params.clear();
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            this.setSystemType(systemInfoList, model);
            params.clear();
            params.put("hostname", "告警");
            params.put("startTime", DateUtil.beforeMinutesToNowDate(10));
            params.put("endTime", DateUtil.getCurrentDateTime());
            model.addAttribute("warnCount", this.logInfoService.countByParams(params));
            this.setTopDownHostChart(model);
            this.setMiddleData(systemInfoList, model);
            this.setLogInfo(model);
            this.setHeathMonitorType(model, heathSize);
            this.hostPowerChart(model, totalSystemInfoSize, totalSizeApp, dockerSize, portSize, fileWarnSize, fileSafeSize, customInfoSize);
            this.hostInfoTop6(model);
            return "daping/indexNew";
         } catch (Exception var28) {
            logger.error("大屏展示错误", var28);
            return "";
         }
      }
   }

   private void setHeathMonitorType(Model model, Integer heathSize) {
      HashMap params = new HashMap();

      try {
         params.put("orderBy", "RES_TIMES");
         params.put("orderType", "DESC");
         PageInfo<HeathMonitor> pageInfo = this.heathMonitorService.selectByParams(params, 1, 5);
         List<ChartDapingNewInfo> chartList = new ArrayList();
         Iterator var6 = pageInfo.getList().iterator();

         while(var6.hasNext()) {
            HeathMonitor heathMonitor = (HeathMonitor)var6.next();
            ChartDapingNewInfo chartDapingNewInfo = new ChartDapingNewInfo();
            chartDapingNewInfo.setName(heathMonitor.getAppName());
            chartDapingNewInfo.setValue(heathMonitor.getResTimes());
            chartList.add(chartDapingNewInfo);
         }

         if (CollectionUtil.isEmpty(chartList)) {
            ChartDapingNewInfo chartDapingNewInfo = new ChartDapingNewInfo();
            chartDapingNewInfo.setName("暂无服务接口");
            chartDapingNewInfo.setValue(1);
            chartList.add(chartDapingNewInfo);
         }

         model.addAttribute("heathMonitorTop5List", JSONUtil.parseArray(chartList));
      } catch (Exception var9) {
         logger.error("setHeathMonitorType错误", var9);
      }

   }

   private void setSystemType(List<SystemInfo> systemInfoList, Model model) {
      Map<String, Integer> map = HostUtil.getSystemTypeMap(systemInfoList);
      Integer windows = 0;
      Integer centos = 0;
      Integer redhat = 0;
      Integer ubuntu = 0;
      Integer debian = 0;
      Integer others = 0;
      if (null != map.get("windows")) {
         windows = (Integer)map.get("windows");
      }

      if (null != map.get("centos")) {
         centos = (Integer)map.get("centos");
      }

      if (null != map.get("redhat")) {
         redhat = (Integer)map.get("redhat");
      }

      if (null != map.get("ubuntu")) {
         ubuntu = (Integer)map.get("ubuntu");
      }

      if (null != map.get("debian")) {
         debian = (Integer)map.get("debian");
      }

      if (null != map.get("其他Linux")) {
         others = (Integer)map.get("其他Linux");
      }

      model.addAttribute("windowsSize", windows);
      model.addAttribute("centosSize", centos);
      model.addAttribute("redhatSize", redhat);
      model.addAttribute("ubuntuSize", ubuntu);
      model.addAttribute("debianSize", debian);
      model.addAttribute("othersSize", others);
   }

   private void setTopDownHostChart(Model model) {
      Map<String, Object> params = new HashMap();
      ArrayList chartInfoList = new ArrayList();

      try {
         int days = 7;
         List<String> dateList = this.getDateList(days);
         String startDay = (String)dateList.get(0);
         String endDay = (String)dateList.get(dateList.size() - 1);
         params.put("startTime", startDay + " 00:00:00");
         params.put("endTime", endDay + " 23:59:59");
         params.put("state", "1");
         int size = this.logInfoService.countByParams(params);
         ChartDapingNewInfo chartInfoYwgj = new ChartDapingNewInfo();
         chartInfoYwgj.setName("业务告警");
         chartInfoYwgj.setValue(size);
         chartInfoList.add(chartInfoYwgj);
         params.put("state", "3");
         size = this.logInfoService.countByParams(params);
         ChartDapingNewInfo chartInfoYwgjhf = new ChartDapingNewInfo();
         chartInfoYwgjhf.setName("业务告警恢复");
         chartInfoYwgjhf.setValue(size);
         chartInfoList.add(chartInfoYwgjhf);
         params.put("state", "2");
         params.put("hostname", "错误");
         size = this.logInfoService.countByParams(params);
         ChartDapingNewInfo chartInfoXtcw = new ChartDapingNewInfo();
         chartInfoXtcw.setName("系统错误");
         chartInfoXtcw.setValue(size);
         chartInfoList.add(chartInfoXtcw);
         params.put("state", "2");
         params.put("hostnameNe", "错误");
         size = this.logInfoService.countByParams(params);
         ChartDapingNewInfo chartInfoXtcz = new ChartDapingNewInfo();
         chartInfoXtcz.setName("系统操作");
         chartInfoXtcz.setValue(size);
         chartInfoList.add(chartInfoXtcz);
         model.addAttribute("topDownHostChart", JSONUtil.parseArray(chartInfoList));
      } catch (Exception var13) {
         logger.error("新版大屏最近一周系统日志分类错误", var13);
      }

   }

   private void setLogInfo(Model model) throws Exception {
      List<LogInfo> recordList = new ArrayList();
      Map<String, Object> params = new HashMap();
      PageInfo logListPage = this.logInfoService.selectByParams(params, 1, 10);
      if (logListPage.getList().size() <= 5) {
         model.addAttribute("logLeftList", logListPage.getList());
         model.addAttribute("logRightList", recordList);
      }

      if (logListPage.getList().size() > 5) {
         model.addAttribute("logLeftList", logListPage.getList().subList(0, 5));
         model.addAttribute("logRightList", logListPage.getList().subList(5, logListPage.getList().size()));
      }

   }

   private void hostPowerChart(Model model, Integer totalSystemInfoSize, Integer totalSizeApp, Integer dockerSize, Integer portSize, Integer fileWarnSize, Integer fileSafeSize, Integer customInfoSize) {
      try {
         int hostPowerMaxSize = 1;
         if (totalSystemInfoSize > hostPowerMaxSize) {
            hostPowerMaxSize = totalSystemInfoSize;
         }

         if (totalSizeApp > hostPowerMaxSize) {
            hostPowerMaxSize = totalSizeApp;
         }

         if (dockerSize > hostPowerMaxSize) {
            hostPowerMaxSize = dockerSize;
         }

         if (portSize > hostPowerMaxSize) {
            hostPowerMaxSize = portSize;
         }

         if (fileWarnSize > hostPowerMaxSize) {
            hostPowerMaxSize = fileWarnSize;
         }

         if (fileSafeSize > hostPowerMaxSize) {
            hostPowerMaxSize = fileSafeSize;
         }

         if (customInfoSize > hostPowerMaxSize) {
            hostPowerMaxSize = customInfoSize;
         }

         model.addAttribute("hostPowerMaxSize", hostPowerMaxSize);
      } catch (Exception var10) {
         logger.error("设置蜘蛛网图错误", var10);
      }

   }

   private void setMiddleData(List<SystemInfo> list, Model model) {
      Double maxCpu = 0.0D;
      Double avgCpu = 0.0D;
      Double minCpu = 1000.0D;
      Double sumCpu = 0.0D;
      Double maxMem = 0.0D;
      Double minMem = 1000.0D;
      Double avgMem = 0.0D;
      Double sumMem = 0.0D;
      Double maxFiveLoad = 0.0D;
      Double avgFiveLoad = 0.0D;
      Double minFiveLoad = 1000.0D;
      Double sumFiveLoad = 0.0D;
      Double maxFifteenLoad = 0.0D;
      Double minFifteenLoad = 1000.0D;
      Double avgFifteenLoad = 0.0D;
      Double sumFifteenLoad = 0.0D;
      int systemSize = 0;
      int cpuCoresSum = 0;
      double memSum = 0.0D;
      long uptimeSum = 0L;
      Iterator var25 = list.iterator();

      while(var25.hasNext()) {
         SystemInfo systemInfo = (SystemInfo)var25.next();
         ++systemSize;
         if (null != systemInfo.getCpuPer()) {
            if (systemInfo.getCpuPer() > maxCpu) {
               maxCpu = systemInfo.getCpuPer();
            }

            if (systemInfo.getCpuPer() < minCpu) {
               minCpu = systemInfo.getCpuPer();
            }

            sumCpu = sumCpu + systemInfo.getCpuPer();
         }

         if (null != systemInfo.getMemPer()) {
            if (systemInfo.getMemPer() > maxMem) {
               maxMem = systemInfo.getMemPer();
            }

            if (systemInfo.getMemPer() < minMem) {
               minMem = systemInfo.getMemPer();
            }

            sumMem = sumMem + systemInfo.getMemPer();
         }

         if (null != systemInfo.getFiveLoad()) {
            if (Double.valueOf(systemInfo.getFiveLoad()) > maxFiveLoad) {
               maxFiveLoad = systemInfo.getFiveLoad();
            }

            if (Double.valueOf(systemInfo.getFiveLoad()) < minFiveLoad) {
               minFiveLoad = systemInfo.getFiveLoad();
            }

            sumFiveLoad = sumFiveLoad + Double.valueOf(systemInfo.getFiveLoad());
         }

         if (null != systemInfo.getFifteenLoad()) {
            if (Double.valueOf(systemInfo.getFifteenLoad()) > maxFifteenLoad) {
               maxFifteenLoad = systemInfo.getFifteenLoad();
            }

            if (Double.valueOf(systemInfo.getFifteenLoad()) < minFifteenLoad) {
               minFifteenLoad = systemInfo.getFifteenLoad();
            }

            sumFifteenLoad = sumFifteenLoad + Double.valueOf(systemInfo.getFifteenLoad());
         }

         try {
            cpuCoresSum += Integer.valueOf(systemInfo.getCpuCoreNum());
         } catch (Exception var37) {
            logger.error("统计所有主机总核数错误", var37);
         }

         try {
            memSum += Double.valueOf(systemInfo.getTotalMem().replace("G", ""));
         } catch (Exception var36) {
            logger.error("统计所有主机总内存错误", var36);
         }

         if (null != systemInfo.getUptime()) {
            uptimeSum += systemInfo.getUptime();
         }
      }

      model.addAttribute("cpuCoresSum", cpuCoresSum);
      model.addAttribute("memSum", FormatUtil.gToT(memSum + ""));
      model.addAttribute("uptimeSum", FormatUtil.secondsToDays(uptimeSum));
      if (systemSize > 0) {
         avgCpu = sumCpu / (double)list.size();
         avgMem = sumMem / (double)list.size();
         avgFifteenLoad = sumFifteenLoad / (double)list.size();
         avgFiveLoad = sumFiveLoad / (double)list.size();
      } else {
         minCpu = 0.0D;
         minMem = 0.0D;
         avgFifteenLoad = 0.0D;
         avgFiveLoad = 0.0D;
      }

      model.addAttribute("maxCpuInfo", maxCpu);
      model.addAttribute("avgCpuInfo", FormatUtil.formatDouble((Double)avgCpu, 2));
      model.addAttribute("minCpuInfo", minCpu);
      model.addAttribute("maxMemInfo", maxMem);
      model.addAttribute("avgMemInfo", FormatUtil.formatDouble((Double)avgMem, 2));
      model.addAttribute("minMemInfo", minMem);
      model.addAttribute("maxFiveLoad", maxFiveLoad);
      model.addAttribute("minFiveLoad", minFiveLoad);
      model.addAttribute("avgFiveLoad", FormatUtil.formatDouble((Double)avgFiveLoad, 2));
      model.addAttribute("maxFifteenLoad", maxFifteenLoad);
      model.addAttribute("minFifteenLoad", minFifteenLoad);
      model.addAttribute("avgFifteenLoad", FormatUtil.formatDouble((Double)avgFifteenLoad, 2));
      Double maxDiskPer = 0.0D;
      String maxDiskPerIp = "";
      Double minDiskPer = 1000.0D;
      String minDiskPerIp = "";
      Double avgDiskPer = 0.0D;
      Double sumDiskPer = 0.0D;

      try {
         Map<String, Object> params = new HashMap();
         String oneDayBefore = DateUtil.getDateBefore(1);
         params.put("startTime", oneDayBefore.substring(0, 10) + " 00:00:00");
         params.put("endTime", oneDayBefore.substring(0, 10) + " 23:59:59");
         List<HostDiskPer> hostDiskPerList = this.hostDiskPerService.selectAllByParams(params);
         Iterator var34 = hostDiskPerList.iterator();

         while(var34.hasNext()) {
            HostDiskPer hostDiskPer = (HostDiskPer)var34.next();
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

         model.addAttribute("maxDiskPer", maxDiskPer);
         model.addAttribute("avgDiskPer", avgDiskPer);
         model.addAttribute("minDiskPer", minDiskPer);
      } catch (Exception var38) {
         logger.error("组装磁盘总使用率最大平均最低值错误", var38);
      }

   }

   private void hostInfoTop6(Model model) {
      try {
         Double maxSystemValue = 1.0D;
         Map<String, Object> params = new HashMap();
         params.put("orderBy", "CREATE_TIME");
         params.put("orderType", "DESC");
         PageInfo<SystemInfo> pageInfo = this.systemInfoService.selectByParams(params, 1, 6);
         List<String> hostNameTop6List = new ArrayList();
         List<Double> hostMemTop6List = new ArrayList();
         List<Double> hostCpuTop6List = new ArrayList();
         List<Integer> hostProcsTop6List = new ArrayList();
         List<Integer> hostNetConnectionsTop6List = new ArrayList();
         List<Double> hostDiskPerTop6List = new ArrayList();
         Map<String, Object> paramsAppInfo = new HashMap();
         Iterator var12 = pageInfo.getList().iterator();

         while(var12.hasNext()) {
            SystemInfo systemInfo = (SystemInfo)var12.next();
            hostNameTop6List.add(systemInfo.getHostname());
            hostMemTop6List.add(systemInfo.getMemPer());
            hostCpuTop6List.add(systemInfo.getCpuPer());
            if (!StringUtils.isEmpty(systemInfo.getProcs())) {
               hostProcsTop6List.add(Integer.valueOf(systemInfo.getProcs()));
            } else {
               hostProcsTop6List.add(0);
            }

            if (!StringUtils.isEmpty(systemInfo.getNetConnections())) {
               hostNetConnectionsTop6List.add(Integer.valueOf(systemInfo.getNetConnections()));
            } else {
               hostNetConnectionsTop6List.add(0);
            }

            paramsAppInfo.put("hostname", systemInfo.getHostname());
            List<DiskState> deskStates = this.diskStateService.selectAllByParams(paramsAppInfo);
            HostUtil.setDiskSumPer(deskStates, systemInfo);
            hostDiskPerTop6List.add(systemInfo.getDiskPer());
         }

         var12 = hostMemTop6List.iterator();

         Double diskPer;
         while(var12.hasNext()) {
            diskPer = (Double)var12.next();
            if (diskPer > maxSystemValue) {
               maxSystemValue = diskPer;
            }
         }

         var12 = hostCpuTop6List.iterator();

         while(var12.hasNext()) {
            diskPer = (Double)var12.next();
            if (diskPer > maxSystemValue) {
               maxSystemValue = diskPer;
            }
         }

         var12 = hostDiskPerTop6List.iterator();

         while(var12.hasNext()) {
            diskPer = (Double)var12.next();
            if (diskPer > maxSystemValue) {
               maxSystemValue = diskPer;
            }
         }

         maxSystemValue = FormatUtil.formatDouble((Double)(maxSystemValue * 1.2D), 2);
         model.addAttribute("hostNameTop6List", JSONUtil.parseArray(hostNameTop6List));
         model.addAttribute("hostMemTop6List", JSONUtil.parseArray(hostMemTop6List));
         model.addAttribute("hostCpuTop6List", JSONUtil.parseArray(hostCpuTop6List));
         model.addAttribute("hostProcsTop6List", JSONUtil.parseArray(hostProcsTop6List));
         model.addAttribute("hostNetConnectionsTop6List", JSONUtil.parseArray(hostNetConnectionsTop6List));
         model.addAttribute("hostDiskPerTop6List", JSONUtil.parseArray(hostDiskPerTop6List));
         model.addAttribute("maxSystemValue", maxSystemValue);
      } catch (Exception var15) {
         logger.error("主机最新上报前10错误", var15);
      }

   }

   public List<String> getDateList(int days) {
      List<String> dateList = new ArrayList();
      String sevenDayBefore = DateUtil.getDateBefore(days);

      for(int i = 1; i < days + 1; ++i) {
         sevenDayBefore = DateUtil.getDateBefore(i);
         dateList.add(sevenDayBefore.substring(0, 10));
      }

      CollectionUtil.reverse(dateList);
      return dateList;
   }
}
