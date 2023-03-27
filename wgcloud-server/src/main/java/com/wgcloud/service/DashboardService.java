package com.wgcloud.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.dto.ChartInfo;
import com.wgcloud.dto.MessageDto;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.entity.CustomInfo;
import com.wgcloud.entity.DeskIo;
import com.wgcloud.entity.DiskSmart;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.DockerInfo;
import com.wgcloud.entity.FileSafe;
import com.wgcloud.entity.FileWarnInfo;
import com.wgcloud.entity.MemState;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.entity.PortInfo;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class DashboardService {
   private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);
   @Resource
   DbTableService dbTableService;
   @Resource
   SnmpInfoService snmpInfoService;
   @Resource
   FileSafeService fileSafeService;
   @Resource
   DceInfoService dceInfoService;
   @Resource
   DbInfoService dbInfoService;
   @Resource
   SystemInfoService systemInfoService;
   @Resource
   AppInfoService appInfoService;
   @Resource
   FileWarnInfoService fileWarnInfoService;
   @Resource
   FileWarnStateService fileWarnStateService;
   @Resource
   DockerInfoService dockerInfoService;
   @Resource
   LogInfoService logInfoService;
   @Autowired
   HeathMonitorService heathMonitorService;
   @Autowired
   HostGroupService hostInfoService;
   @Autowired
   CpuStateService cpuStateService;
   @Autowired
   MemStateService memStateService;
   @Autowired
   SysLoadStateService sysLoadStateService;
   @Autowired
   NetIoStateService netIoStateService;
   @Autowired
   DeskIoService deskIoService;
   @Autowired
   DiskStateService diskStateService;
   @Autowired
   DiskSmartService diskSmartService;
   @Resource
   CpuTemperaturesService cpuTemperaturesService;
   @Resource
   private CustomInfoService customInfoService;
   @Resource
   private PortInfoService portInfoService;
   @Autowired
   CommonConfig commonConfig;
   @Autowired
   MailConfig mailConfig;

   public List<MessageDto> getAmList() {
      MessageDto dto1 = new MessageDto();
      dto1.setCode("am1");
      dto1.setMsg("最近1小时");
      MessageDto dto2 = new MessageDto();
      dto2.setCode("am2");
      dto2.setMsg("最近2小时");
      MessageDto dto3 = new MessageDto();
      dto3.setCode("am3");
      dto3.setMsg("最近6小时");
      MessageDto dto4 = new MessageDto();
      dto4.setCode("am4");
      dto4.setMsg("最近12小时");
      MessageDto dto5 = new MessageDto();
      dto5.setCode("am5");
      dto5.setMsg("最近24小时");
      List<MessageDto> timeList = new ArrayList();
      timeList.add(dto1);
      timeList.add(dto2);
      timeList.add(dto3);
      timeList.add(dto4);
      timeList.add(dto5);
      return timeList;
   }

   public void setDateParam(String am, String startTime, String endTime, Map<String, Object> params, Model model) {
      if ("null".equals(am)) {
         am = "";
      }

      try {
         String nowTime = DateUtil.getCurrentDateTime();
         if (!StringUtils.isEmpty(am) && (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime))) {
            if ("am1".equals(am)) {
               params.put("startTime", DateUtil.beforeHourToNowDate(1));
               params.put("endTime", nowTime);
            }

            if ("am2".equals(am)) {
               params.put("startTime", DateUtil.beforeHourToNowDate(2));
               params.put("endTime", nowTime);
            }

            if ("am3".equals(am)) {
               params.put("startTime", DateUtil.beforeHourToNowDate(6));
               params.put("endTime", nowTime);
            }

            if ("am4".equals(am)) {
               params.put("startTime", DateUtil.beforeHourToNowDate(12));
               params.put("endTime", nowTime);
            }

            if ("am5".equals(am)) {
               params.put("startTime", DateUtil.beforeHourToNowDate(24));
               params.put("endTime", nowTime);
            }

            model.addAttribute("am", am);
            return;
         }

         if (StringUtils.isEmpty(am) && !StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
            params.put("startTime", startTime + ":00");
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
               Date dateTmp = DateUtil.getDate(startTime + ":00");
               Date nowDate = new Date();
               long diff = 432000000L;
               if (nowDate.getTime() - dateTmp.getTime() > diff) {
                  params.put("startTime", DateUtil.beforeHourToNowDate(120));
                  model.addAttribute("msg", "个人版最多查看最近5天数据，请联系我们升级到专业版");
               }
            }

            params.put("endTime", endTime + ":59");
            model.addAttribute("startTime", startTime);
            model.addAttribute("endTime", endTime);
            return;
         }

         params.put("startTime", DateUtil.beforeHourToNowDate(1));
         params.put("endTime", nowTime);
         model.addAttribute("am", "am1");
      } catch (Exception var11) {
         logger.error("查看图表组装日期查询条件错误", var11);
      }

   }

   public void setDateParam(String date, Map<String, Object> params) {
      params.put("startTime", date + " 00:00:00");
      params.put("endTime", date + " 23:59:59");
   }

   public void setDashboardTopData(Model model, int totalSystemInfoSize, HttpServletRequest request) throws Exception {
      Map<String, Object> params = new HashMap();
      HostUtil.addAccountquery(request, params);
      int totalSizeApp = this.appInfoService.countByParams(params);
      model.addAttribute("totalSizeApp", totalSizeApp);
      params.put("state", "1");
      int onLineAppSize = this.appInfoService.countByParams(params);
      model.addAttribute("onLineAppSize", onLineAppSize);
      double onLineAppPer = 0.0D;
      if (totalSizeApp != 0) {
         onLineAppPer = (double)onLineAppSize / (double)totalSizeApp;
      }

      model.addAttribute("onLineAppPer", FormatUtil.formatDouble((Double)(onLineAppPer * 100.0D), 2));
      params.clear();
      HostUtil.addAccountquery(request, params);
      int portSize = this.portInfoService.countByParams(params);
      model.addAttribute("portSize", portSize);
      params.put("state", "1");
      int portOnLineSize = this.portInfoService.countByParams(params);
      model.addAttribute("portOnLineSize", portOnLineSize);
      double portOnLinePer = 0.0D;
      if (portSize != 0) {
         portOnLinePer = (double)portOnLineSize / (double)portSize;
      }

      model.addAttribute("portOnLinePer", FormatUtil.formatDouble((Double)(portOnLinePer * 100.0D), 2));
      params.clear();
      HostUtil.addAccountquery(request, params);
      int heathSize = this.heathMonitorService.countByParams(params);
      model.addAttribute("heathSize", heathSize);
      params.put("heathStatus", "200");
      int heath200Size = this.heathMonitorService.countByParams(params);
      model.addAttribute("heath200Size", heath200Size);
      double heathOnLinePer = 0.0D;
      if (heathSize != 0) {
         heathOnLinePer = (double)heath200Size / (double)heathSize;
      }

      model.addAttribute("heathOnLinePer", FormatUtil.formatDouble((Double)(heathOnLinePer * 100.0D), 2));
      params.clear();
      HostUtil.addAccountquery(request, params);
      int dockerSize = this.dockerInfoService.countByParams(params);
      model.addAttribute("dockerSize", dockerSize);
      params.put("state", "1");
      int dockerOnLineSize = this.dockerInfoService.countByParams(params);
      model.addAttribute("dockerOnLineSize", dockerOnLineSize);
      double dockerOnLinePer = 0.0D;
      if (dockerSize != 0) {
         dockerOnLinePer = (double)dockerOnLineSize / (double)dockerSize;
      }

      model.addAttribute("dockerOnLinePer", FormatUtil.formatDouble((Double)(dockerOnLinePer * 100.0D), 2));
      params.clear();
      HostUtil.addAccountquery(request, params);
      int dceSize = this.dceInfoService.countByParams(params);
      model.addAttribute("dceSize", dceSize);
      params.put("resTimes", -1);
      int dceDownSize = this.dceInfoService.countByParams(params);
      double dceOnLinePer = 0.0D;
      if (dceSize != 0) {
         dceOnLinePer = (double)(dceSize - dceDownSize) / (double)dceSize;
      }

      model.addAttribute("dceOnLineSize", dceSize - dceDownSize);
      model.addAttribute("dceOnLinePer", FormatUtil.formatDouble((Double)(dceOnLinePer * 100.0D), 2));
      params.clear();
      HostUtil.addAccountquery(request, params);
      Integer fileWarnSize = this.fileWarnInfoService.countByParams(params);
      model.addAttribute("fileWarnSize", fileWarnSize == null ? 0 : fileWarnSize);
      params.clear();
      HostUtil.addAccountquery(request, params);
      int dbInfoSize = this.dbInfoService.countByParams(params);
      model.addAttribute("dbInfoSize", dbInfoSize);
      params.put("dbState", "1");
      int dbInfoOnLineSize = this.dbInfoService.countByParams(params);
      model.addAttribute("dbInfoOnLineSize", dbInfoOnLineSize);
      double dbInfoOnLinePer = 0.0D;
      if (dbInfoOnLineSize != 0) {
         dbInfoOnLinePer = (double)dbInfoOnLineSize / (double)dbInfoSize;
      }

      model.addAttribute("dbInfoOnLinePer", FormatUtil.formatDouble((Double)(dbInfoOnLinePer * 100.0D), 2));
      this.setHuantu(model, totalSystemInfoSize, totalSizeApp, dockerSize, portSize, dceSize, fileWarnSize, dbInfoSize, heathSize, request);
   }

   public void setHuantu(Model model, int totalSystemInfoSize, int totalSizeApp, int dockerSize, int portSize, int dceSize, int fileWarnSize, int dbInfoSize, int heathSize, HttpServletRequest request) throws Exception {
      Map<String, Object> params = new HashMap();
      HostUtil.addAccountquery(request, params);
      int dbTableSize = this.dbTableService.countByParams(params);
      model.addAttribute("dbTableSize", dbTableSize);
      params.clear();
      HostUtil.addAccountquery(request, params);
      int snmpInfoSize = this.snmpInfoService.countByParams(params);
      model.addAttribute("snmpInfoSize", snmpInfoSize);
      params.clear();
      HostUtil.addAccountquery(request, params);
      int customInfoSize = this.customInfoService.countByParams(params);
      model.addAttribute("customInfoSize", customInfoSize);
      params.clear();
      HostUtil.addAccountquery(request, params);
      int fileSafeSize = this.fileSafeService.countByParams(params);
      model.addAttribute("fileSafeSize", fileSafeSize);
      int huantuTotalSize = totalSystemInfoSize + totalSizeApp + dockerSize + portSize + dceSize + fileWarnSize + dbInfoSize + heathSize + dbTableSize + snmpInfoSize + customInfoSize + fileSafeSize;
      double total = (double)huantuTotalSize;
      if (0.0D == total) {
         total = 1.0D;
      }

      List<ChartInfo> chartInfos = new ArrayList();
      double totalSystemInfoSizePercent = (double)totalSystemInfoSize / total;
      ChartInfo totalSystemInfoChart = new ChartInfo();
      totalSystemInfoChart.setCount(totalSystemInfoSize);
      totalSystemInfoChart.setItem("主机");
      totalSystemInfoChart.setPercent(FormatUtil.formatDouble((Double)totalSystemInfoSizePercent, 2));
      if (totalSystemInfoSize > 0) {
         chartInfos.add(totalSystemInfoChart);
      }

      double totalSizeAppPercent = (double)totalSizeApp / total;
      ChartInfo totalSizeAppChart = new ChartInfo();
      totalSizeAppChart.setCount(totalSizeApp);
      totalSizeAppChart.setItem("进程");
      totalSizeAppChart.setPercent(FormatUtil.formatDouble((Double)totalSizeAppPercent, 2));
      if (totalSizeApp > 0) {
         chartInfos.add(totalSizeAppChart);
      }

      double dockerSizePercent = (double)dockerSize / total;
      ChartInfo dockerSizeChart = new ChartInfo();
      dockerSizeChart.setCount(dockerSize);
      dockerSizeChart.setItem("DOCKER");
      dockerSizeChart.setPercent(FormatUtil.formatDouble((Double)dockerSizePercent, 2));
      if (dockerSize > 0) {
         chartInfos.add(dockerSizeChart);
      }

      double portSizePercent = (double)portSize / total;
      ChartInfo portSizeChart = new ChartInfo();
      portSizeChart.setCount(portSize);
      portSizeChart.setItem("端口");
      portSizeChart.setPercent(FormatUtil.formatDouble((Double)portSizePercent, 2));
      if (portSize > 0) {
         chartInfos.add(portSizeChart);
      }

      double dceSizePercent = (double)dceSize / total;
      ChartInfo dceSizeChart = new ChartInfo();
      dceSizeChart.setCount(dceSize);
      dceSizeChart.setItem("数通PING");
      dceSizeChart.setPercent(FormatUtil.formatDouble((Double)dceSizePercent, 2));
      if (dceSize > 0) {
         chartInfos.add(dceSizeChart);
      }

      double snmpInfoPercent = (double)snmpInfoSize / total;
      ChartInfo snmpInfoChart = new ChartInfo();
      snmpInfoChart.setCount(snmpInfoSize);
      snmpInfoChart.setItem("数通SNMP");
      snmpInfoChart.setPercent(FormatUtil.formatDouble((Double)snmpInfoPercent, 2));
      if (snmpInfoSize > 0) {
         chartInfos.add(snmpInfoChart);
      }

      double fileWarnSizePercent = (double)fileWarnSize / total;
      ChartInfo fileWarnSizeChart = new ChartInfo();
      fileWarnSizeChart.setCount(fileWarnSize);
      fileWarnSizeChart.setItem("日志");
      fileWarnSizeChart.setPercent(FormatUtil.formatDouble((Double)fileWarnSizePercent, 2));
      if (fileWarnSize > 0) {
         chartInfos.add(fileWarnSizeChart);
      }

      double dbInfoSizePercent = (double)dbInfoSize / total;
      ChartInfo dbInfoSizeChart = new ChartInfo();
      dbInfoSizeChart.setCount(dbInfoSize);
      dbInfoSizeChart.setItem("数据源");
      dbInfoSizeChart.setPercent(FormatUtil.formatDouble((Double)dbInfoSizePercent, 2));
      if (dbInfoSize > 0) {
         chartInfos.add(dbInfoSizeChart);
      }

      double heathSizePercent = (double)heathSize / total;
      ChartInfo heathSizeChart = new ChartInfo();
      heathSizeChart.setCount(heathSize);
      heathSizeChart.setItem("服务接口");
      heathSizeChart.setPercent(FormatUtil.formatDouble((Double)heathSizePercent, 2));
      if (heathSize > 0) {
         chartInfos.add(heathSizeChart);
      }

      double dbTableSizePercent = (double)dbTableSize / total;
      ChartInfo dbTableSizeChart = new ChartInfo();
      dbTableSizeChart.setCount(dbTableSize);
      dbTableSizeChart.setItem("数据表");
      dbTableSizeChart.setPercent(FormatUtil.formatDouble((Double)dbTableSizePercent, 2));
      if (dbTableSize > 0) {
         chartInfos.add(dbTableSizeChart);
      }

      double customInfoSizePercent = (double)customInfoSize / total;
      ChartInfo customInfoSizePercentChart = new ChartInfo();
      customInfoSizePercentChart.setCount(customInfoSize);
      customInfoSizePercentChart.setItem("自定义监控项");
      customInfoSizePercentChart.setPercent(FormatUtil.formatDouble((Double)customInfoSizePercent, 2));
      if (customInfoSize > 0) {
         chartInfos.add(customInfoSizePercentChart);
      }

      double fileSafeSizePercent = (double)fileSafeSize / total;
      ChartInfo fileSafeSizePercentChart = new ChartInfo();
      fileSafeSizePercentChart.setCount(fileSafeSize);
      fileSafeSizePercentChart.setItem("文件防篡改");
      fileSafeSizePercentChart.setPercent(FormatUtil.formatDouble((Double)fileSafeSizePercent, 2));
      if (customInfoSize > 0) {
         chartInfos.add(fileSafeSizePercentChart);
      }

      model.addAttribute("huantuChartInfos", JSONUtil.parseArray(chartInfos));
      model.addAttribute("huantuTotalSize", huantuTotalSize);
   }

   public void setMiddleData(Model model, HttpServletRequest request) throws Exception {
      Map<String, Object> params = new HashMap();
      HostUtil.addAccountquery(request, params);
      List<SystemInfo> list = this.systemInfoService.selectAllByParams(params);
      Double maxCpu = 0.0D;
      Double avgCpu = 0.0D;
      Double minCpu = 1000.0D;
      Double sumCpu = 0.0D;
      Double maxMem = 0.0D;
      Double minMem = 1000.0D;
      Double avgMem = 0.0D;
      Double sumMem = 0.0D;
      Double maxRxbyt = 0.0D;
      Double avgRxbyt = 0.0D;
      Double minRxbyt = 1000.0D;
      Double sumRxbyt = 0.0D;
      Double maxTxbyt = 0.0D;
      Double minTxbyt = 1000.0D;
      Double avgTxbyt = 0.0D;
      Double sumTxbyt = 0.0D;
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
      int submitSecondsSum = 0;
      double avgSubmitSeconds = 120.0D;
      Iterator var36 = list.iterator();

      while(var36.hasNext()) {
         SystemInfo systemInfo = (SystemInfo)var36.next();
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

         if (null != systemInfo.getRxbyt()) {
            if (Double.valueOf(systemInfo.getRxbyt()) > maxRxbyt) {
               maxRxbyt = Double.valueOf(systemInfo.getRxbyt());
            }

            if (Double.valueOf(systemInfo.getRxbyt()) < minRxbyt) {
               minRxbyt = Double.valueOf(systemInfo.getRxbyt());
            }

            sumRxbyt = sumRxbyt + Double.valueOf(systemInfo.getRxbyt());
         }

         if (null != systemInfo.getTxbyt()) {
            if (Double.valueOf(systemInfo.getTxbyt()) > maxTxbyt) {
               maxTxbyt = Double.valueOf(systemInfo.getTxbyt());
            }

            if (Double.valueOf(systemInfo.getTxbyt()) < minTxbyt) {
               minTxbyt = Double.valueOf(systemInfo.getTxbyt());
            }

            sumTxbyt = sumTxbyt + Double.valueOf(systemInfo.getTxbyt());
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
         } catch (Exception var41) {
            logger.error("统计所有主机总核数错误", var41);
         }

         try {
            memSum += Double.valueOf(systemInfo.getTotalMem().replace("G", ""));
         } catch (Exception var40) {
            logger.error("统计所有主机总内存错误", var40);
         }

         try {
            submitSecondsSum += Integer.valueOf(systemInfo.getSubmitSeconds());
         } catch (Exception var39) {
            logger.error("统计所有主机组装上报数据频率总和错误", var39);
         }
      }

      if (systemSize > 0) {
         avgCpu = sumCpu / (double)list.size();
         avgMem = sumMem / (double)list.size();
         avgRxbyt = sumRxbyt / (double)list.size();
         avgTxbyt = sumTxbyt / (double)list.size();
         avgFifteenLoad = sumFifteenLoad / (double)list.size();
         avgFiveLoad = sumFiveLoad / (double)list.size();
         avgSubmitSeconds = (double)submitSecondsSum / (double)list.size();
      } else {
         minCpu = 0.0D;
         minMem = 0.0D;
         minRxbyt = 0.0D;
         minTxbyt = 0.0D;
         avgFifteenLoad = 0.0D;
         avgFiveLoad = 0.0D;
      }

      model.addAttribute("cpuCoresSum", cpuCoresSum);
      model.addAttribute("memSum", FormatUtil.formatDouble((Double)memSum, 2));
      model.addAttribute("avgSubmitSeconds", FormatUtil.formatDouble((Double)avgSubmitSeconds, 2));
      model.addAttribute("maxCpuVal", maxCpu);
      model.addAttribute("avgCpuVal", FormatUtil.formatDouble((Double)avgCpu, 2));
      model.addAttribute("minCpuVal", minCpu);
      model.addAttribute("maxMemVal", maxMem);
      model.addAttribute("avgMemVal", FormatUtil.formatDouble((Double)avgMem, 2));
      model.addAttribute("minMemVal", minMem);
      model.addAttribute("maxRxbyt", FormatUtil.kbToM(maxRxbyt + "") + "/s");
      model.addAttribute("minRxbyt", FormatUtil.kbToM(minRxbyt + "") + "/s");
      model.addAttribute("avgRxbyt", FormatUtil.kbToM(FormatUtil.formatDouble((Double)avgRxbyt, 2) + "") + "/s");
      model.addAttribute("sumRxbyt", FormatUtil.kbToM(FormatUtil.formatDouble((Double)sumRxbyt, 2) + "") + "/s");
      model.addAttribute("maxTxbyt", FormatUtil.kbToM(maxTxbyt + "") + "/s");
      model.addAttribute("minTxbyt", FormatUtil.kbToM(minTxbyt + "") + "/s");
      model.addAttribute("avgTxbyt", FormatUtil.kbToM(FormatUtil.formatDouble((Double)avgTxbyt, 2) + "") + "/s");
      model.addAttribute("sumTxbyt", FormatUtil.kbToM(FormatUtil.formatDouble((Double)sumTxbyt, 2) + "") + "/s");
      model.addAttribute("maxFiveLoad", maxFiveLoad);
      model.addAttribute("minFiveLoad", minFiveLoad);
      model.addAttribute("avgFiveLoad", FormatUtil.formatDouble((Double)avgFiveLoad, 2));
      model.addAttribute("maxFifteenLoad", maxFifteenLoad);
      model.addAttribute("minFifteenLoad", minFifteenLoad);
      model.addAttribute("avgFifteenLoad", FormatUtil.formatDouble((Double)avgFifteenLoad, 2));
   }

   public void setPieChart(Model model, int totalSystemInfoSize, HttpServletRequest request) throws Exception {
      Map<String, Object> params = new HashMap();
      List<ChartInfo> chartInfoList1 = new ArrayList();
      List<ChartInfo> chartInfoList2 = new ArrayList();
      HostUtil.addAccountquery(request, params);
      params.put("memPer", 95);
      int memPerSize_95 = this.systemInfoService.countByParams(params);
      double a = 0.0D;
      if (totalSystemInfoSize != 0) {
         a = (double)memPerSize_95 / (double)totalSystemInfoSize;
      }

      ChartInfo memPerSize_95_chart = new ChartInfo();
      memPerSize_95_chart.setItem("内存>=95%");
      memPerSize_95_chart.setCount(memPerSize_95);
      memPerSize_95_chart.setPercent(FormatUtil.formatDouble((Double)a, 2));
      chartInfoList1.add(memPerSize_95_chart);
      params.clear();
      HostUtil.addAccountquery(request, params);
      params.put("memPer", 90);
      params.put("memPerLe", 95);
      int memPerSize_90_95 = this.systemInfoService.countByParams(params);
      double a_90_95 = 0.0D;
      if (totalSystemInfoSize != 0) {
         a_90_95 = (double)memPerSize_90_95 / (double)totalSystemInfoSize;
      }

      ChartInfo memPerSize_90_95_chart = new ChartInfo();
      memPerSize_90_95_chart.setItem("内存>=90%且<95%");
      memPerSize_90_95_chart.setCount(memPerSize_90_95);
      memPerSize_90_95_chart.setPercent(FormatUtil.formatDouble((Double)a_90_95, 2));
      chartInfoList1.add(memPerSize_90_95_chart);
      params.clear();
      HostUtil.addAccountquery(request, params);
      params.put("memPer", 50);
      params.put("memPerLe", 90);
      int memPerSize_50_90 = this.systemInfoService.countByParams(params);
      double b = 0.0D;
      if (totalSystemInfoSize != 0) {
         b = (double)memPerSize_50_90 / (double)totalSystemInfoSize;
      }

      ChartInfo memPerSize_50_90_chart = new ChartInfo();
      memPerSize_50_90_chart.setItem("内存>=50%且<90%");
      memPerSize_50_90_chart.setCount(memPerSize_50_90);
      memPerSize_50_90_chart.setPercent(FormatUtil.formatDouble((Double)b, 2));
      chartInfoList1.add(memPerSize_50_90_chart);
      params.clear();
      HostUtil.addAccountquery(request, params);
      params.put("memPerLe", 50);
      int memPerSize_50 = this.systemInfoService.countByParams(params);
      double bb = 0.0D;
      if (totalSystemInfoSize != 0) {
         bb = (double)memPerSize_50 / (double)totalSystemInfoSize;
      }

      ChartInfo memPerSize_50_chart = new ChartInfo();
      memPerSize_50_chart.setItem("内存<50%");
      memPerSize_50_chart.setCount(memPerSize_50);
      memPerSize_50_chart.setPercent(FormatUtil.formatDouble((Double)bb, 2));
      chartInfoList1.add(memPerSize_50_chart);
      params.clear();
      model.addAttribute("chartInfoList1", JSONUtil.parseArray(chartInfoList1));
      HostUtil.addAccountquery(request, params);
      params.put("cpuPer", 95);
      int cpuPerSize_95 = this.systemInfoService.countByParams(params);
      double c_95 = 0.0D;
      if (totalSystemInfoSize != 0) {
         c_95 = (double)cpuPerSize_95 / (double)totalSystemInfoSize;
      }

      ChartInfo cpuPerSize_95_chart = new ChartInfo();
      cpuPerSize_95_chart.setItem("CPU>=95%");
      cpuPerSize_95_chart.setCount(cpuPerSize_95);
      cpuPerSize_95_chart.setPercent(FormatUtil.formatDouble((Double)c_95, 2));
      chartInfoList2.add(cpuPerSize_95_chart);
      params.clear();
      HostUtil.addAccountquery(request, params);
      params.put("cpuPer", 90);
      params.put("cpuPerLe", 95);
      int cpuPerSize_90_95 = this.systemInfoService.countByParams(params);
      double c_90_95 = 0.0D;
      if (totalSystemInfoSize != 0) {
         c_90_95 = (double)cpuPerSize_90_95 / (double)totalSystemInfoSize;
      }

      ChartInfo cpuPerSize_90_95_chart = new ChartInfo();
      cpuPerSize_90_95_chart.setItem("CPU>=90%且<95%");
      cpuPerSize_90_95_chart.setCount(cpuPerSize_90_95);
      cpuPerSize_90_95_chart.setPercent(FormatUtil.formatDouble((Double)c_90_95, 2));
      chartInfoList2.add(cpuPerSize_90_95_chart);
      params.clear();
      HostUtil.addAccountquery(request, params);
      params.put("cpuPerLe", 90);
      params.put("cpuPer", 50);
      int perSize_50_90 = this.systemInfoService.countByParams(params);
      double e = 0.0D;
      if (totalSystemInfoSize != 0) {
         e = (double)perSize_50_90 / (double)totalSystemInfoSize;
      }

      ChartInfo perSize_50_90_chart = new ChartInfo();
      perSize_50_90_chart.setItem("CPU>=50%且<90%");
      perSize_50_90_chart.setCount(perSize_50_90);
      perSize_50_90_chart.setPercent(FormatUtil.formatDouble((Double)e, 2));
      chartInfoList2.add(perSize_50_90_chart);
      params.clear();
      HostUtil.addAccountquery(request, params);
      params.put("cpuPerLe", 50);
      int perSize_50 = this.systemInfoService.countByParams(params);
      double f = 0.0D;
      if (totalSystemInfoSize != 0) {
         f = (double)perSize_50 / (double)totalSystemInfoSize;
      }

      ChartInfo perSize_50_chart = new ChartInfo();
      perSize_50_chart.setItem("CPU<50%");
      perSize_50_chart.setCount(perSize_50);
      perSize_50_chart.setPercent(FormatUtil.formatDouble((Double)f, 2));
      chartInfoList2.add(perSize_50_chart);
      params.clear();
      model.addAttribute("chartInfoList2", JSONUtil.parseArray(chartInfoList2));
      params.clear();
   }

   public void setMiddleApplicationData(Model model) throws Exception {
      Map<String, Object> params = new HashMap();
      params.put("hostname", "告警");
      params.put("startTime", DateUtil.beforeMinutesToNowDate(60));
      params.put("endTime", DateUtil.getCurrentDateTime());
      model.addAttribute("warnCount", this.logInfoService.countByParams(params));
      model.addAttribute("historyDataOut", this.commonConfig.getHistoryDataOut());
      String warnType = "未设置";
      if (StaticKeys.mailSet != null && !StringUtils.isEmpty(this.mailConfig.getWarnScript())) {
         warnType = "邮件+脚本";
      }

      if (StaticKeys.mailSet == null && !StringUtils.isEmpty(this.mailConfig.getWarnScript())) {
         warnType = "脚本";
      }

      if (StaticKeys.mailSet != null && StringUtils.isEmpty(this.mailConfig.getWarnScript())) {
         warnType = "邮件";
      }

      model.addAttribute("warnType", warnType);
      String dapingView = "已关闭";
      if ("true".equals(this.commonConfig.getDashView())) {
         dapingView = "已开启";
      }

      model.addAttribute("dapingView", dapingView);
      String adminPwd = "默认";
      if (!"111111".equals(this.commonConfig.getAccountPwd())) {
         adminPwd = "已修改";
      }

      model.addAttribute("adminPwd", adminPwd);
      String guestAccountPwd = "默认";
      if (!"111111".equals(this.commonConfig.getGuestAccountPwd())) {
         guestAccountPwd = "已修改";
      }

      model.addAttribute("guestAccountPwd", guestAccountPwd);
      String wgToken = "默认";
      if (!"wgcloud".equals(this.commonConfig.getWgToken())) {
         wgToken = "已修改";
      }

      model.addAttribute("wgToken", wgToken);
      String webSSH = "已关闭";
      if ("true".equals(this.commonConfig.getWebSsh())) {
         webSSH = "已开启";
      }

      model.addAttribute("webSSH", webSSH);
   }

   public void hostDraw(Model model, String systemInfoId) {
      try {
         SystemInfo systemInfo = this.systemInfoService.selectById(systemInfoId);
         String hostname = systemInfo.getHostname();
         Map<String, Object> params = new HashMap();
         params.put("hostname", hostname);
         model.addAttribute("systemInfo", systemInfo);
         DateUtil.setBeginEndTime(params, 7);
         CpuState maxAvgCpuState = this.cpuStateService.selectMaxAvgByHostname(params);
         if (null == maxAvgCpuState) {
            maxAvgCpuState = new CpuState();
            maxAvgCpuState.setSys(0.0D);
            maxAvgCpuState.setIdle(0.0D);
            maxAvgCpuState.setIowait(0.0D);
         }

         model.addAttribute("maxCpuState", maxAvgCpuState.getSys());
         model.addAttribute("avgCpuState", FormatUtil.formatDouble((Double)maxAvgCpuState.getIdle(), 2));
         model.addAttribute("minCpuState", maxAvgCpuState.getIowait());
         MemState maxAvgMemState = this.memStateService.selectMaxAvgByHostname(params);
         if (null == maxAvgMemState) {
            maxAvgMemState = new MemState();
            maxAvgMemState.setUsePer(0.0D);
            maxAvgMemState.setUsed("0");
            maxAvgMemState.setFree("0");
         }

         model.addAttribute("maxMemState", maxAvgMemState.getUsePer());
         model.addAttribute("avgMemState", FormatUtil.formatDouble((Double)Double.valueOf(maxAvgMemState.getUsed()), 2));
         model.addAttribute("minMemState", maxAvgMemState.getFree());
         model.addAttribute("historyDataOut", this.commonConfig.getHistoryDataOut());
         params.clear();
         params.put("hostname", hostname);
         DateUtil.setBeginEndTime(params, 7);
         PageInfo pageInfoLoad = this.sysLoadStateService.selectByParams(params, 1, 1);
         if (!CollectionUtil.isEmpty(pageInfoLoad.getList())) {
            model.addAttribute("sysLoadState", pageInfoLoad.getList().get(0));
         } else {
            model.addAttribute("sysLoadState", new SysLoadState());
         }

         SysLoadState sysLoadStateMax = this.sysLoadStateService.selectMaxByHostname(params);
         if (null == sysLoadStateMax) {
            sysLoadStateMax = new SysLoadState();
            sysLoadStateMax.setOneLoad(0.0D);
            sysLoadStateMax.setFiveLoad(0.0D);
            sysLoadStateMax.setFifteenLoad(0.0D);
         }

         model.addAttribute("maxOneLoad", sysLoadStateMax.getOneLoad());
         model.addAttribute("maxFiveLoad", sysLoadStateMax.getFiveLoad());
         model.addAttribute("maxFifteenLoad", sysLoadStateMax.getFifteenLoad());
         SysLoadState sysLoadStateAvg = this.sysLoadStateService.selectAvgByHostname(params);
         if (null == sysLoadStateAvg) {
            sysLoadStateAvg = new SysLoadState();
            sysLoadStateAvg.setOneLoad(0.0D);
            sysLoadStateAvg.setFiveLoad(0.0D);
            sysLoadStateAvg.setFifteenLoad(0.0D);
         }

         model.addAttribute("avgOneLoad", FormatUtil.formatDouble((Double)sysLoadStateAvg.getOneLoad(), 2));
         model.addAttribute("avgFiveLoad", FormatUtil.formatDouble((Double)sysLoadStateAvg.getFiveLoad(), 2));
         model.addAttribute("avgFifteenLoad", FormatUtil.formatDouble((Double)sysLoadStateAvg.getFifteenLoad(), 2));
         SysLoadState sysLoadStateMin = this.sysLoadStateService.selectMinByHostname(params);
         if (null == sysLoadStateMin) {
            sysLoadStateMin = new SysLoadState();
            sysLoadStateMin.setOneLoad(0.0D);
            sysLoadStateMin.setFiveLoad(0.0D);
            sysLoadStateMin.setFifteenLoad(0.0D);
         }

         model.addAttribute("minOneLoad", sysLoadStateMin.getOneLoad());
         model.addAttribute("minFiveLoad", sysLoadStateMin.getFiveLoad());
         model.addAttribute("minFifteenLoad", sysLoadStateMin.getFifteenLoad());
         PageInfo pageInfoNetIo = this.netIoStateService.selectByParams(params, 1, 1);
         NetIoState netIoStateNew = new NetIoState();
         if (!CollectionUtil.isEmpty(pageInfoNetIo.getList())) {
            netIoStateNew = (NetIoState)pageInfoNetIo.getList().get(0);
            netIoStateNew.setRxbyt(FormatUtil.kbToM(netIoStateNew.getRxbyt()) + "/s");
            netIoStateNew.setTxbyt(FormatUtil.kbToM(netIoStateNew.getTxbyt()) + "/s");
            model.addAttribute("netIoState", netIoStateNew);
         } else {
            model.addAttribute("netIoState", netIoStateNew);
         }

         NetIoState netIoStateMax = this.netIoStateService.selectMaxByHostname(params);
         if (null == netIoStateMax) {
            netIoStateMax = new NetIoState();
            netIoStateMax.setTxbyt("0");
            netIoStateMax.setRxbyt("0");
            netIoStateMax.setRxpck("0");
            netIoStateMax.setTxpck("0");
         }

         model.addAttribute("maxRxbyt", FormatUtil.kbToM(netIoStateMax.getRxbyt()) + "/s");
         model.addAttribute("maxTxbyt", FormatUtil.kbToM(netIoStateMax.getTxbyt()) + "/s");
         model.addAttribute("maxRxpck", netIoStateMax.getRxpck());
         model.addAttribute("maxTxpck", netIoStateMax.getTxpck());
         NetIoState netIoStateAvg = this.netIoStateService.selectAvgByHostname(params);
         if (null == netIoStateAvg) {
            netIoStateAvg = new NetIoState();
            netIoStateAvg.setTxbyt("0");
            netIoStateAvg.setRxbyt("0");
            netIoStateAvg.setRxpck("0");
            netIoStateAvg.setTxpck("0");
         }

         model.addAttribute("avgRxbyt", FormatUtil.kbToM(FormatUtil.formatDouble((String)netIoStateAvg.getRxbyt(), 2) + "") + "/s");
         model.addAttribute("avgTxbyt", FormatUtil.kbToM(FormatUtil.formatDouble((String)netIoStateAvg.getTxbyt(), 2) + "") + "/s");
         model.addAttribute("avgRxpck", FormatUtil.formatDouble((String)netIoStateAvg.getRxpck(), 2));
         model.addAttribute("avgTxpck", FormatUtil.formatDouble((String)netIoStateAvg.getTxpck(), 2));
         NetIoState netIoStateMin = this.netIoStateService.selectMinByHostname(params);
         if (null == netIoStateMin) {
            netIoStateMin = new NetIoState();
            netIoStateMin.setTxbyt("0");
            netIoStateMin.setRxbyt("0");
            netIoStateMin.setRxpck("0");
            netIoStateMin.setTxpck("0");
         }

         model.addAttribute("minRxbyt", FormatUtil.kbToM(netIoStateMin.getRxbyt()) + "/s");
         model.addAttribute("minTxbyt", FormatUtil.kbToM(netIoStateMin.getTxbyt()) + "/s");
         model.addAttribute("minRxpck", netIoStateMin.getRxpck());
         model.addAttribute("minTxpck", netIoStateMin.getTxpck());
         params.clear();
         params.put("hostname", "主机下线告警：" + hostname);
         DateUtil.setBeginEndTime(params, 7);
         int hostDownSize = this.logInfoService.countByParams(params);
         model.addAttribute("hostDownSize", hostDownSize);
         params.clear();
         params.put("hostname", hostname);
         List<AppInfo> appInfoList = this.appInfoService.selectAllByParams(params);
         Iterator var19 = appInfoList.iterator();

         while(var19.hasNext()) {
            AppInfo appInfo1 = (AppInfo)var19.next();
            appInfo1.setWritesBytes(FormatUtil.mToG(appInfo1.getWritesBytes()));
            appInfo1.setReadBytes(FormatUtil.mToG(appInfo1.getReadBytes()));
         }

         this.appInfoService.setGroupInList(appInfoList, model);
         model.addAttribute("appInfoList", appInfoList);
         List<PortInfo> portInfoList = this.portInfoService.selectAllByParams(params);
         this.portInfoService.setGroupInList(portInfoList, model);
         model.addAttribute("portInfoList", portInfoList);
         List<FileSafe> fileSafeInfoList = this.fileSafeService.selectAllByParams(params);
         model.addAttribute("fileSafeInfoList", fileSafeInfoList);
         List<FileWarnInfo> fileWarnInfoList = this.fileWarnInfoService.selectAllByParams(params);
         Iterator var22 = fileWarnInfoList.iterator();

         while(var22.hasNext()) {
            FileWarnInfo info = (FileWarnInfo)var22.next();
            params.put("fileWarnId", info.getId());
            Integer count = this.fileWarnStateService.countByParams(params);
            info.setWarnDatas(count + "");
            if (!StringUtils.isEmpty(info.getFileSize())) {
               String fileFormatSize = FormatUtil.bytesFormatUnit(info.getFileSize(), "byte");
               info.setFileSize(fileFormatSize);
            }
         }

         model.addAttribute("fileWarnInfoList", fileWarnInfoList);
         params.clear();
         params.put("hostname", hostname);
         List<DockerInfo> dockerInfoList = this.dockerInfoService.selectAllByParams(params);
         this.dockerInfoService.setGroupInList(dockerInfoList, model);
         model.addAttribute("dockerInfoList", dockerInfoList);
         List<DiskState> diskStateList = this.diskStateService.selectAllByParams(params);
         model.addAttribute("diskStateList", diskStateList);
         HostUtil.setDiskSumPer(diskStateList, systemInfo);
         List<DeskIo> deskIoList = this.deskIoService.selectAllByParams(params);
         model.addAttribute("deskIoList", deskIoList);
         List<DiskSmart> diskSmartList = this.diskSmartService.selectAllByParams(params);
         model.addAttribute("diskSmartList", diskSmartList);
         List<CpuTemperatures> cpuTemperaturesList = this.cpuTemperaturesService.selectAllByParams(params);
         model.addAttribute("cpuTemperaturesList", cpuTemperaturesList);
         List<CustomInfo> customInfoList = this.customInfoService.selectAllByParams(params);
         model.addAttribute("customInfoList", customInfoList);
      } catch (Exception var28) {
         logger.error("主机画像错误", var28);
         this.logInfoService.save("主机画像错误", var28.toString(), "2");
      }

   }
}
