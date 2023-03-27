package com.wgcloud.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.NetIoStateDto;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.entity.DeskIo;
import com.wgcloud.entity.DiskSmart;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.HostDiskPer;
import com.wgcloud.entity.MemState;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.CpuStateService;
import com.wgcloud.service.CpuTemperaturesService;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.DeskIoService;
import com.wgcloud.service.DiskSmartService;
import com.wgcloud.service.DiskStateService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.HostDiskPerService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.MemStateService;
import com.wgcloud.service.NetIoStateService;
import com.wgcloud.service.SysLoadStateService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"/systemInfo"})
public class SystemInfoController {
   private static final Logger logger = LoggerFactory.getLogger(SystemInfoController.class);
   @Resource
   private SystemInfoService systemInfoService;
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private DashboardService dashboardService;
   @Resource
   private CpuStateService cpuStateService;
   @Resource
   private DiskStateService diskStateService;
   @Resource
   private DeskIoService deskIoService;
   @Resource
   private DiskSmartService diskSmartService;
   @Resource
   private CpuTemperaturesService cpuTemperaturesService;
   @Resource
   private HostDiskPerService hostDiskPerService;
   @Resource
   private MemStateService memStateService;
   @Resource
   private NetIoStateService netIoStateService;
   @Resource
   private SysLoadStateService sysLoadStateService;
   @Resource
   private ExcelExportService excelExportService;
   @Resource
   private HostGroupService hostGroupService;
   @Resource
   private AccountInfoService accountInfoService;
   @Autowired
   private TokenUtils tokenUtils;
   @Autowired
   private CommonConfig commonConfig;

   private void testThread() {
      Runnable runnable = () -> {
         logger.info("SystemInfoController----------testThread");
      };
      ThreadPoolUtil.executor.execute(runnable);
   }

   @ResponseBody
   @RequestMapping({"agentList"})
   public String agentList(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
      if (!StringUtils.isEmpty(checkResult)) {
         return checkResult;
      } else {
         try {
            Map<String, Object> params = new HashMap();
            if (!StringUtils.isEmpty(agentJsonObject.getStr("hostname"))) {
               params.put("hostname", agentJsonObject.getStr("hostname").trim());
            }

            if (!StringUtils.isEmpty(agentJsonObject.getStr("account"))) {
               params.put("account", agentJsonObject.getStr("account").trim());
            }

            if (!StringUtils.isEmpty(agentJsonObject.getStr("orderBy"))) {
               params.put("orderBy", agentJsonObject.getStr("orderBy"));
               params.put("orderType", agentJsonObject.getStr("orderType"));
            }

            PageInfo<SystemInfo> pageInfo = this.systemInfoService.selectByParams(params, agentJsonObject.getInt("page"), agentJsonObject.getInt("pageSize"));
            Map<String, Object> paramsAppInfo = new HashMap();
            Iterator var7 = pageInfo.getList().iterator();

            while(var7.hasNext()) {
               SystemInfo systemInfo1 = (SystemInfo)var7.next();
               paramsAppInfo.put("hostname", systemInfo1.getHostname());
               List<DiskState> deskStates = this.diskStateService.selectAllByParams(paramsAppInfo);
               HostUtil.setDiskSumPer(deskStates, systemInfo1);
            }

            return ResDataUtils.resetSuccessJson(pageInfo);
         } catch (Exception var10) {
            logger.error("agent获取主机列表信息错误", var10);
            this.logInfoService.save("agent获取主机列表信息错误", var10.toString(), "2");
            return ResDataUtils.resetErrorJson(var10.toString());
         }
      }
   }

   @ResponseBody
   @RequestMapping({"save"})
   public String saveSystemInfo(SystemInfo SystemInfo, Model model, HttpServletRequest request) {
      try {
         if (!StringUtils.isEmpty(SystemInfo.getId())) {
            SystemInfo ho = this.systemInfoService.selectById(SystemInfo.getId());
            ho.setRemark(SystemInfo.getRemark());
            this.systemInfoService.updateById(ho);
            this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "修改主机备注：" + ho.getHostname(), "主机备注：" + ho.getRemark(), "2");
         }
      } catch (Exception var5) {
         logger.error("保存主机备注信息错误", var5);
         this.logInfoService.save("保存主机备注信息错误", var5.toString(), "2");
      }

      return "redirect:/systemInfo/systemInfoList";
   }

   @ResponseBody
   @RequestMapping({"saveWinConsole"})
   public String saveWinConsole(SystemInfo SystemInfo, Model model, HttpServletRequest request) {
      try {
         if (!StringUtils.isEmpty(SystemInfo.getId())) {
            SystemInfo ho = this.systemInfoService.selectById(SystemInfo.getId());
            ho.setWinConsole(SystemInfo.getWinConsole());
            this.systemInfoService.updateById(ho);
            this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "修改主机服务：" + ho.getHostname(), "主机服务：" + ho.getWinConsole(), "2");
         }
      } catch (Exception var5) {
         logger.error("保存主机服务url信息错误", var5);
         this.logInfoService.save("保存主机主机服务url信息错误", var5.toString(), "2");
      }

      return SystemInfo.getWinConsole();
   }

   @ResponseBody
   @RequestMapping({"saveHostListHideCols"})
   public String saveHostListHideCols(Model model, HttpServletRequest request) {
      try {
         String[] hostListHideCols = request.getParameterValues("hostListHideCols");
         if (null != hostListHideCols) {
            request.getSession().setAttribute("HostListHideColsInfo", StringUtils.join(hostListHideCols, ","));
         } else {
            request.getSession().setAttribute("HostListHideColsInfo", "");
         }
      } catch (Exception var4) {
         logger.error("保存主机列表需要隐藏的列错误", var4);
         this.logInfoService.save("保存主机列表需要隐藏的列错误", var4.toString(), "2");
      }

      return "redirect:/systemInfo/systemInfoList";
   }

   @ResponseBody
   @RequestMapping({"saveGroupId"})
   public String saveGroupId(Model model, HttpServletRequest request) {
      try {
         String ids = request.getParameter("ids");
         String[] groupIdsArr = request.getParameterValues("groupId");
         this.systemInfoService.saveGroupId(ids, groupIdsArr, request);
      } catch (Exception var5) {
         logger.error("保存主机标签信息错误", var5);
         this.logInfoService.save("保存主机标签信息错误", var5.toString(), "2");
      }

      return "redirect:/systemInfo/systemInfoList";
   }

   /**
    * 显示所有主机
    *
    * @param systemInfo
    * @param model
    * @param request
    * @return
    */
   @RequestMapping({"systemInfoList"})
   public String systemInfoList(SystemInfo systemInfo, Model model, HttpServletRequest request) {
      logger.info("=SystemInfoController-systemInfoList.systemInfo:{}, model:{}, request:{}=", JSONUtil.toJsonStr(systemInfo),
              JSONUtil.toJsonStr(model), JSONUtil.toJsonStr(request));
      HashMap params = new HashMap();

      try {
         StringBuffer url = new StringBuffer();
         String hostname = null;
         if (!StringUtils.isEmpty(systemInfo.getHostname())) {
            hostname = systemInfo.getHostname();
            params.put("hostname", hostname.trim());
            url.append("&hostname=").append(hostname);
         }

         if (!StringUtils.isEmpty(systemInfo.getState())) {
            params.put("state", systemInfo.getState());
            url.append("&state=").append(systemInfo.getState());
         }

         if (!StringUtils.isEmpty(systemInfo.getAccount())) {
            params.put("account", systemInfo.getAccount());
            url.append("&account=").append(systemInfo.getAccount());
         }

         if (!StringUtils.isEmpty(systemInfo.getGroupId())) {
            params.put("groupId", systemInfo.getGroupId());
            url.append("&groupId=").append(systemInfo.getGroupId());
         }

         if (!StringUtils.isEmpty(systemInfo.getOrderBy())) {
            params.put("orderBy", systemInfo.getOrderBy());
            params.put("orderType", systemInfo.getOrderType());
            url.append("&orderBy=").append(systemInfo.getOrderBy());
            url.append("&orderType=").append(systemInfo.getOrderType());
         }

         if (request.getParameter("dashView") != null) {
            url.append("&dashView=1");
         }

         LicenseUtil.checkHostList(systemInfo, model);
         HostUtil.addAccountquery(request, params);
         PageInfo<SystemInfo> pageInfo = this.systemInfoService.selectByParams(params, systemInfo.getPage(), systemInfo.getPageSize());
         if ("true".equals(this.commonConfig.getUserInfoManage())) {
            params.clear();
            List<AccountInfo> accountInfoList = this.accountInfoService.selectAllByParams(params);
            model.addAttribute("accountList", accountInfoList);
         }

         this.systemInfoService.hostAddVal(pageInfo, model);
         if (request.getParameter("dashView") != null) {
            this.systemInfoService.hideLeftIp(pageInfo, request);
         }

         PageUtil.initPageNumber(pageInfo, model);
         model.addAttribute("pageUrl", "/systemInfo/systemInfoList?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
         model.addAttribute("systemInfo", systemInfo);
         if (null == request.getSession().getAttribute("HostListHideColsInfo")) {
            request.getSession().setAttribute("HostListHideColsInfo", "");
         }
      } catch (Exception var9) {
         logger.error("查询主机列表错误", var9);
         this.logInfoService.save("查询主机列表错误", var9.toString(), "2");
      }

      if (request.getParameter("dashView") != null) {
         model.addAttribute("dashViewListAutoData", this.commonConfig.getDashViewListAutoData());
         return "dashView/list";
      } else {
         return "host/list";
      }
   }

   @ResponseBody
   @RequestMapping({"systemInfoListAjax"})
   public String systemInfoListAjax(SystemInfo systemInfo, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
         Iterator var6 = systemInfoList.iterator();

         while(var6.hasNext()) {
            SystemInfo sys = (SystemInfo)var6.next();
            if ("2".equals(sys.getState())) {
               sys.setHostname("<span  class='badge bg-danger'>" + FormatUtil.getString(sys.getHostname(), 20) + "</span>");
            }

            if (sys.getMemPer() >= 90.0D) {
               sys.setImage("<span class='badge bg-danger'>" + sys.getMemPer() + "</span>");
            }

            if (sys.getMemPer() >= 70.0D && sys.getMemPer() < 90.0D) {
               sys.setImage("<span class='badge bg-warning'>" + sys.getMemPer() + "</span>");
            }

            if (sys.getMemPer() < 70.0D) {
               sys.setImage("<span class='badge bg-primary'>" + sys.getMemPer() + "</span>");
            }

            if (sys.getCpuPer() >= 90.0D) {
               sys.setHostnameExt("<span class='badge bg-danger'>" + sys.getCpuPer() + "</span>");
            }

            if (sys.getCpuPer() >= 70.0D && sys.getCpuPer() < 90.0D) {
               sys.setHostnameExt("<span class='badge bg-warning'>" + sys.getCpuPer() + "</span>");
            }

            if (sys.getCpuPer() < 70.0D) {
               sys.setHostnameExt("<span class='badge bg-primary'>" + sys.getCpuPer() + "</span>");
            }

            sys.setRxbyt(FormatUtil.kbToM(sys.getRxbyt()) + "/s");
            sys.setTxbyt(FormatUtil.kbToM(sys.getTxbyt()) + "/s");
            sys.setRemark(DateUtil.getDateTimeString(sys.getCreateTime()));
         }

         return JSONUtil.toJsonStr(systemInfoList);
      } catch (Exception var8) {
         logger.error("ajax查询主机列表错误", var8);
         this.logInfoService.save("ajax查询主机列表错误", var8.toString(), "2");
         return "";
      }
   }

   @RequestMapping({"detail"})
   public String hostDetail(Model model, HttpServletRequest request) {
      String id = request.getParameter("id");
      if (StringUtils.isEmpty(id)) {
         return "error/500";
      } else {
         try {
            SystemInfo systemInfo = this.systemInfoService.selectById(id);
            model.addAttribute("systemInfo", systemInfo);
            Map<String, Object> params = new HashMap();
            params.put("hostname", systemInfo.getHostname());
            List<DiskState> diskStateList = this.diskStateService.selectAllByParams(params);
            model.addAttribute("diskStateList", diskStateList);
            List<DeskIo> deskIoList = this.deskIoService.selectAllByParams(params);
            model.addAttribute("deskIoList", deskIoList);
            List<DiskSmart> diskSmartList = this.diskSmartService.selectAllByParams(params);
            model.addAttribute("diskSmartList", diskSmartList);
            List<CpuTemperatures> cpuTemperaturesList = this.cpuTemperaturesService.selectAllByParams(params);
            model.addAttribute("cpuTemperaturesList", cpuTemperaturesList);
            List<HostDiskPer> hostDiskPerList = this.hostDiskPerService.selectAllByParams(params);
            this.hostDiskPerService.setSubtitle(model, hostDiskPerList);
            model.addAttribute("hostDiskPerList", JSONUtil.parseArray(hostDiskPerList));
            if (request.getParameter("dashView") != null) {
               this.systemInfoService.hideLeftIp(systemInfo, request);
            }
         } catch (Exception var11) {
            logger.error("主机详细信息错误", var11);
            this.logInfoService.save("主机详细信息错误", var11.toString(), "2");
         }

         return request.getParameter("dashView") != null ? "dashView/view" : "host/view";
      }
   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "删除主机信息错误";

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            String[] ids = request.getParameter("id").split(",");
            String[] var6 = ids;
            int var7 = ids.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String id = var6[var8];
               SystemInfo sys = this.systemInfoService.selectById(id);
               this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "删除主机：" + sys.getHostname(), "主机：" + sys.getRemark(), "2");
            }

            this.systemInfoService.deleteById(ids);
         }
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "redirect:/systemInfo/systemInfoList";
   }

   @RequestMapping({"chart"})
   public String hostChart(Model model, HttpServletRequest request) {
      String id = request.getParameter("id");
      String startTime = request.getParameter("startTime");
      String endTime = request.getParameter("endTime");
      String am = request.getParameter("am");
      if (StringUtils.isEmpty(id)) {
         logger.error("hostChart id is  null");
         return "error/500";
      } else {
         try {
            SystemInfo systemInfo = this.systemInfoService.selectById(id);
            model.addAttribute("systemInfo", systemInfo);
            Map<String, Object> params = new HashMap();
            params.put("hostname", systemInfo.getHostname());
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            model.addAttribute("amList", this.dashboardService.getAmList());
            List<CpuState> cpuStateList = this.cpuStateService.selectAllByParams(params);
            model.addAttribute("cpuStateList", JSONUtil.parseArray(cpuStateList));
            List<MemState> memStateList = this.memStateService.selectAllByParams(params);
            model.addAttribute("memStateList", JSONUtil.parseArray(memStateList));
            List<SysLoadState> sysLoadStateList = this.sysLoadStateService.selectAllByParams(params);
            model.addAttribute("sysLoadStateList", JSONUtil.parseArray(sysLoadStateList));
            this.systemInfoService.findLoadMaxVal(sysLoadStateList, model);
            List<NetIoState> netIoStateList = this.netIoStateService.selectAllByParams(params);
            List<NetIoStateDto> netIoStateDtoList = this.systemInfoService.toNetIoStateDto(netIoStateList);
            model.addAttribute("netIoStateList", JSONUtil.parseArray(netIoStateDtoList));
            this.systemInfoService.findNetIoStateBytMaxVal(netIoStateDtoList, model);
            this.systemInfoService.setSubtitle(model, cpuStateList, memStateList);
            if (request.getParameter("dashView") != null) {
               this.systemInfoService.hideLeftIp(systemInfo, request);
            }

            this.systemInfoService.setChartWarnElement(systemInfo, model);
         } catch (Exception var14) {
            logger.error("主机图形报表错误", var14);
            this.logInfoService.save("主机图形报表错误", var14.toString(), "2");
         }

         return request.getParameter("dashView") != null ? "dashView/viewChart" : "host/viewChart";
      }
   }

   @RequestMapping({"chartExcel"})
   public void hostChartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
      String id = request.getParameter("id");
      String startTime = request.getParameter("startTime");
      String endTime = request.getParameter("endTime");
      String am = request.getParameter("am");
      if (!StringUtils.isEmpty(id)) {
         try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
               response.setContentType("text/html;charset=UTF-8");
               response.getOutputStream().write("The module needs to wgcloud-pro version. Please contact us at www.wgstart.com".getBytes());
               return;
            }

            SystemInfo systemInfo = this.systemInfoService.selectById(id);
            model.addAttribute("systemInfo", systemInfo);
            Map<String, Object> params = new HashMap();
            params.put("hostname", systemInfo.getHostname());
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            this.excelExportService.exportExcel(params, response);
         } catch (Exception var10) {
            logger.error("主机图形报表导出excel错误", var10);
            this.logInfoService.save("主机图形报表导出excel错误", var10.toString(), "2");
         }

      }
   }

   @RequestMapping({"hostListExcel"})
   public void hostListExcel(SystemInfo systemInfo, Model model, HttpServletRequest request, HttpServletResponse response) {
      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            response.setContentType("text/html;charset=UTF-8");
            response.getOutputStream().write("The module needs to wgcloud-pro version. Please contact us at www.wgstart.com".getBytes());
            return;
         }

         Map<String, Object> params = new HashMap();
         if (!StringUtils.isEmpty(systemInfo.getState())) {
            params.put("state", systemInfo.getState());
         }

         if (!StringUtils.isEmpty(systemInfo.getGroupId())) {
            params.put("groupId", systemInfo.getGroupId());
         }

         if (!StringUtils.isEmpty(systemInfo.getOrderBy())) {
            params.put("orderBy", systemInfo.getOrderBy());
            params.put("orderType", systemInfo.getOrderType());
         }

         HostUtil.addAccountquery(request, params);
         PageInfo<SystemInfo> pageInfo = this.systemInfoService.selectByParams(params, 1, 10000);
         this.systemInfoService.hostAddVal(pageInfo, model);
         this.excelExportService.exportHostListExcel(pageInfo.getList(), response);
      } catch (Exception var7) {
         logger.error("所有主机列表导出excel错误", var7);
         this.logInfoService.save("所有主机列表导出excel错误", var7.toString(), "2");
      }

   }
}
