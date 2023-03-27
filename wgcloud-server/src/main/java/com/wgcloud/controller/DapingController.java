package com.wgcloud.controller;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.ChartInfo;
import com.wgcloud.entity.HostDiskPer;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.CpuStateService;
import com.wgcloud.service.CpuTemperaturesService;
import com.wgcloud.service.DeskIoService;
import com.wgcloud.service.HeathMonitorService;
import com.wgcloud.service.HostDiskPerService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.MemStateService;
import com.wgcloud.service.NetIoStateService;
import com.wgcloud.service.SysLoadStateService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.service.TaskUtilService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/daping"})
public class DapingController {
   private static final Logger logger = LoggerFactory.getLogger(DapingController.class);
   @Resource
   private SystemInfoService systemInfoService;
   @Resource
   private LogInfoService logInfoService;
   @Autowired
   private HeathMonitorService heathMonitorService;
   @Autowired
   private HostGroupService hostInfoService;
   @Autowired
   private CpuStateService cpuStateService;
   @Autowired
   private MemStateService memStateService;
   @Autowired
   private SysLoadStateService sysLoadStateService;
   @Autowired
   private NetIoStateService netIoStateService;
   @Autowired
   private DeskIoService deskIoService;
   @Autowired
   private TaskUtilService taskUtilService;
   @Resource
   private CpuTemperaturesService cpuTemperaturesService;
   @Resource
   private HostDiskPerService hostDiskPerService;
   @Autowired
   private CommonConfig commonConfig;

   private void testThread() {
      Runnable runnable = () -> {
         logger.info("DapingController----------testThread");
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
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            List<ChartInfo> systemInfoTypeList = HostUtil.getSystemTypeList(systemInfoList);
            List<String> systemInfoTypeNameList = new ArrayList();
            List<Integer> systemInfoTypeValList = new ArrayList();
            Iterator var8 = systemInfoTypeList.iterator();

            while(var8.hasNext()) {
               ChartInfo chartInfo = (ChartInfo)var8.next();
               systemInfoTypeNameList.add(chartInfo.getItem());
               systemInfoTypeValList.add(chartInfo.getCount());
            }

            model.addAttribute("systemInfoTypeNameList", JSONUtil.parseArray(systemInfoTypeNameList));
            model.addAttribute("systemInfoTypeValList", JSONUtil.parseArray(systemInfoTypeValList));
            int totalSystemInfoSize = this.systemInfoService.countByParams(params);
            model.addAttribute("totalSystemInfoSize", totalSystemInfoSize);
            params.clear();
            params.put("state", "2");
            int hostDownSize = this.systemInfoService.countByParams(params);
            model.addAttribute("hostDownSize", hostDownSize);
            this.setSysPie(model);
            this.systemTop10(model);
            this.setMiddleData(systemInfoList, model);
            this.warnInfoInit(model);
            return "daping/index";
         } catch (Exception var10) {
            logger.error("大屏展示错误", var10);
            return "";
         }
      }
   }

   private void setSysPie(Model model) {
      try {
         Map<String, Object> params = new HashMap();
         int totalSystemInfoSize = this.systemInfoService.countByParams(params);
         params.put("state", "2");
         int hostDownSize = this.systemInfoService.countByParams(params);
         model.addAttribute("hostDownSize", hostDownSize);
         model.addAttribute("hostOnSize", totalSystemInfoSize - hostDownSize);
      } catch (Exception var5) {
         logger.error("设置主机在线/下线饼图错误", var5);
      }

   }

   private void warnInfoInit(Model model) {
      try {
         Map<String, Object> params = new HashMap();
         params.put("hostname", "主机下线告警");
         int hostDownWarnSize = this.logInfoService.countByParams(params);
         model.addAttribute("hostDownWarnSize", hostDownWarnSize);
         params.clear();
         params.put("hostname", "内存告警");
         int memWarnSize = this.logInfoService.countByParams(params);
         model.addAttribute("memWarnSize", memWarnSize);
         params.clear();
         params.put("hostname", "CPU告警");
         int cpuWarnSize = this.logInfoService.countByParams(params);
         model.addAttribute("cpuWarnSize", cpuWarnSize);
         params.clear();
         params.put("hostname", "传输速率告警");
         int netWarnSize = this.logInfoService.countByParams(params);
         model.addAttribute("netWarnSize", netWarnSize);
         params.clear();
         params.put("hostname", "磁盘告警");
         int diskWarnSize = this.logInfoService.countByParams(params);
         model.addAttribute("diskWarnSize", diskWarnSize);
         params.clear();
         params.put("hostname", "系统负载(5分钟)告警");
         int load5WarnSize = this.logInfoService.countByParams(params);
         model.addAttribute("load5WarnSize", load5WarnSize);
      } catch (Exception var9) {
         logger.error("设置告警统计信息雷达图错误", var9);
      }

   }

   private void systemTop10(Model model) {
      try {
         Map<String, Object> params = new HashMap();
         params.put("orderBy", "CREATE_TIME");
         params.put("orderType", "DESC");
         PageInfo<SystemInfo> pageInfo = this.systemInfoService.selectByParams(params, 1, 10);
         List<String> systemTop10NameList = new ArrayList();
         List<Double> cpuTop10ValList = new ArrayList();
         List<String> rxbytTop10List = new ArrayList();
         List<String> txbytTop10List = new ArrayList();
         List<Double> memTop10ValList = new ArrayList();
         Iterator var9 = pageInfo.getList().iterator();

         while(var9.hasNext()) {
            SystemInfo systemInfo = (SystemInfo)var9.next();
            systemTop10NameList.add(systemInfo.getHostname());
            cpuTop10ValList.add(systemInfo.getCpuPer());
            rxbytTop10List.add(systemInfo.getRxbyt());
            txbytTop10List.add(systemInfo.getTxbyt());
            memTop10ValList.add(systemInfo.getMemPer());
         }

         model.addAttribute("systemTop10NameList", JSONUtil.parseArray(systemTop10NameList));
         model.addAttribute("cpuTop10ValList", JSONUtil.parseArray(cpuTop10ValList));
         model.addAttribute("rxbytTop10List", JSONUtil.parseArray(rxbytTop10List));
         model.addAttribute("txbytTop10List", JSONUtil.parseArray(txbytTop10List));
         model.addAttribute("memTop10ValList", JSONUtil.parseArray(memTop10ValList));
      } catch (Exception var11) {
         logger.error("cpu使用率最新上报前10错误", var11);
      }

   }

   private void setMiddleData(List<SystemInfo> systemInfoList, Model model) {
      Double maxCpu = 0.0D;
      String maxCpuIp = "";
      Double avgCpu = 0.0D;
      Double minCpu = 1000.0D;
      String minCpuIp = "";
      Double sumCpu = 0.0D;
      Double maxMem = 0.0D;
      String maxMemIp = "";
      Double minMem = 1000.0D;
      String minMemIp = "";
      Double avgMem = 0.0D;
      Double sumMem = 0.0D;
      Double maxDiskPer = 0.0D;
      String maxDiskPerIp = "";
      Double minDiskPer = 1000.0D;
      String minDiskPerIp = "";
      Double avgDiskPer = 0.0D;
      Double sumDiskPer = 0.0D;
      int systemSize = 0;
      int cpuCoresSum = 0;
      double memSum = 0.0D;
      Iterator var25 = systemInfoList.iterator();

      while(var25.hasNext()) {
         SystemInfo systemInfo = (SystemInfo)var25.next();
         ++systemSize;
         if (null != systemInfo.getCpuPer() && systemInfo.getCpuPer() > maxCpu) {
            maxCpu = systemInfo.getCpuPer();
            maxCpuIp = systemInfo.getHostname();
         }

         if (null != systemInfo.getCpuPer() && systemInfo.getCpuPer() < minCpu) {
            minCpu = systemInfo.getCpuPer();
            minCpuIp = systemInfo.getHostname();
         }

         if (null != systemInfo.getCpuPer()) {
            sumCpu = sumCpu + systemInfo.getCpuPer();
         }

         if (null != systemInfo.getMemPer() && systemInfo.getMemPer() > maxMem) {
            maxMem = systemInfo.getMemPer();
            maxMemIp = systemInfo.getHostname();
         }

         if (null != systemInfo.getMemPer() && systemInfo.getMemPer() < minMem) {
            minMem = systemInfo.getMemPer();
            minMemIp = systemInfo.getHostname();
         }

         if (null != systemInfo.getMemPer()) {
            sumMem = sumMem + systemInfo.getMemPer();
         }

         try {
            cpuCoresSum += Integer.valueOf(systemInfo.getCpuCoreNum());
         } catch (Exception var31) {
            logger.error("统计所有主机总核数错误", var31);
         }

         try {
            memSum += Double.valueOf(systemInfo.getTotalMem().replace("G", ""));
         } catch (Exception var30) {
            logger.error("统计所有主机总内存错误", var30);
         }
      }

      try {
         Map<String, Object> params = new HashMap();
         String oneDayBefore = DateUtil.getDateBefore(1);
         params.put("startTime", oneDayBefore.substring(0, 10) + " 00:00:00");
         params.put("endTime", oneDayBefore.substring(0, 10) + " 23:59:59");
         List<HostDiskPer> hostDiskPerList = this.hostDiskPerService.selectAllByParams(params);
         Iterator var28 = hostDiskPerList.iterator();

         while(var28.hasNext()) {
            HostDiskPer hostDiskPer = (HostDiskPer)var28.next();
            if (null != hostDiskPer.getDiskSumPer() && hostDiskPer.getDiskSumPer() > maxDiskPer) {
               maxDiskPer = hostDiskPer.getDiskSumPer();
               maxDiskPerIp = hostDiskPer.getHostname();
            }

            if (null != hostDiskPer.getDiskSumPer() && hostDiskPer.getDiskSumPer() < minDiskPer) {
               minDiskPer = hostDiskPer.getDiskSumPer();
               minDiskPerIp = hostDiskPer.getHostname();
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
      } catch (Exception var32) {
         logger.error("组织磁盘总使用率最大平均最低值错误", var32);
      }

      if (systemSize > 0) {
         avgCpu = sumCpu / (double)systemInfoList.size();
         avgMem = sumMem / (double)systemInfoList.size();
      } else {
         minCpu = 0.0D;
         minMem = 0.0D;
      }

      model.addAttribute("cpuCoresSum", cpuCoresSum);
      model.addAttribute("memSum", FormatUtil.gToT(memSum + ""));
      List<String> listIp = new ArrayList();
      List<Double> list = new ArrayList();
      list.add(maxCpu);
      listIp.add(maxCpuIp);
      list.add(FormatUtil.formatDouble((Double)avgCpu, 2));
      listIp.add("avgCpu");
      list.add(minCpu);
      listIp.add(minCpuIp);
      list.add(maxMem);
      listIp.add(maxMemIp);
      list.add(FormatUtil.formatDouble((Double)avgMem, 2));
      listIp.add("avgCpu");
      list.add(minMem);
      listIp.add(minMemIp);
      list.add(maxDiskPer);
      listIp.add(maxDiskPerIp);
      list.add(FormatUtil.formatDouble((Double)avgDiskPer, 2));
      listIp.add("avgDiskPer");
      list.add(minDiskPer);
      listIp.add(minDiskPerIp);
      model.addAttribute("middleDataList", JSONUtil.parseArray(list));
      model.addAttribute("middleDataListIp", JSONUtil.parseArray(listIp));
   }
}
