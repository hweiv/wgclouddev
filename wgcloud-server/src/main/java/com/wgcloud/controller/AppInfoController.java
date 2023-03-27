package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.AppState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.AppStateService;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
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
@RequestMapping({"/appInfo"})
public class AppInfoController {
   private static final Logger logger = LoggerFactory.getLogger(AppInfoController.class);
   @Resource
   private AppInfoService appInfoService;
   @Resource
   private AppStateService appStateService;
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private SystemInfoService systemInfoService;
   @Resource
   private DashboardService dashboardService;
   @Resource
   private ExcelExportService excelExportService;
   @Resource
   private HostGroupService hostGroupService;
   @Autowired
   private TokenUtils tokenUtils;
   @Autowired
   private CommonConfig commonConfig;

   @ResponseBody
   @RequestMapping({"agentList"})
   public String agentList(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
         logger.error("token is error");
         return ResDataUtils.resetErrorJson("token is error");
      } else {
         Map<String, Object> params = new HashMap();
         if (null != agentJsonObject.get("hostname") && !StringUtils.isEmpty(agentJsonObject.get("hostname").toString())) {
            params.put("hostname", agentJsonObject.get("hostname").toString());
            try {
               params.put("active", "1");
               List<AppInfo> appInfoList = this.appInfoService.selectAllByParams(params);
               logger.info("the AppInfoController-agentList.appInfoList:{}", appInfoList);
               return ResDataUtils.resetSuccessJson(appInfoList);
            } catch (Exception var5) {
               logger.error("agent获取进程信息错误", var5);
               this.logInfoService.save("agent获取进程信息错误", var5.toString(), "2");
               return ResDataUtils.resetErrorJson(var5.toString());
            }
         } else {
            return "";
         }
      }
   }

   @RequestMapping({"list"})
   public String appInfoList(AppInfo appInfo, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         LicenseUtil.maxLicense_10(model, request, appInfo);
         StringBuffer url = new StringBuffer();
         String hostname = null;
         if (!StringUtils.isEmpty(appInfo.getHostname())) {
            hostname = appInfo.getHostname();
            params.put("hostname", hostname.trim());
            url.append("&hostname=").append(hostname);
         }

         if (!StringUtils.isEmpty(appInfo.getAccount())) {
            params.put("account", appInfo.getAccount());
            url.append("&account=").append(appInfo.getAccount());
         }

         if (!StringUtils.isEmpty(appInfo.getGroupId())) {
            params.put("groupId", appInfo.getGroupId());
            url.append("&groupId=").append(appInfo.getGroupId());
         }

         if (!StringUtils.isEmpty(appInfo.getOrderBy())) {
            params.put("orderBy", appInfo.getOrderBy());
            params.put("orderType", appInfo.getOrderType());
            url.append("&orderBy=").append(appInfo.getOrderBy());
            url.append("&orderType=").append(appInfo.getOrderType());
         }

         if (!StringUtils.isEmpty(appInfo.getState())) {
            params.put("state", appInfo.getState());
            url.append("&state=").append(appInfo.getState());
         }

         HostUtil.addAccountquery(request, params);
         PageInfo<AppInfo> pageInfo = this.appInfoService.selectByParams(params, appInfo.getPage(), appInfo.getPageSize());
         Iterator var8 = pageInfo.getList().iterator();

         while(var8.hasNext()) {
            AppInfo appInfo1 = (AppInfo)var8.next();
            appInfo1.setWritesBytes(FormatUtil.mToG(appInfo1.getWritesBytes()));
            appInfo1.setReadBytes(FormatUtil.mToG(appInfo1.getReadBytes()));
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
               appInfo1.setAccount(HostUtil.getAccount(appInfo1.getHostname()));
            }

            appInfo1.setHostname(appInfo1.getHostname() + HostUtil.addRemark(appInfo1.getHostname()));
            if ("true".equals(this.commonConfig.getShowWarnCount())) {
               String warnQueryWd = appInfo1.getAppName() + "，" + appInfo1.getHostname();
               this.logInfoService.warnQueryHandle(appInfo1, warnQueryWd);
            }
         }

         if ("true".equals(this.commonConfig.getHostGroup())) {
            this.appInfoService.setGroupInList(pageInfo.getList(), model);
         }

         HostUtil.addAccountListModel(model);
         PageUtil.initPageNumber(pageInfo, model);
         model.addAttribute("pageUrl", "/appInfo/list?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
         model.addAttribute("appInfo", appInfo);
      } catch (Exception var11) {
         logger.error("查询进程信息错误", var11);
         this.logInfoService.save("查询进程信息错误", var11.toString(), "2");
      }

      return "app/list";
   }

   @ResponseBody
   @RequestMapping({"saveGroupId"})
   public String saveGroupId(Model model, HttpServletRequest request) {
      try {
         String ids = request.getParameter("ids");
         String[] groupIdsArr = request.getParameterValues("groupId");
         this.appInfoService.saveGroupId(ids, groupIdsArr, request);
      } catch (Exception var5) {
         logger.error("保存进程标签信息错误", var5);
         this.logInfoService.save("保存进程标签信息错误", var5.toString(), "2");
      }

      return "redirect:/appInfo/list";
   }

   @RequestMapping({"save"})
   public String saveAppInfo(AppInfo AppInfo, Model model, HttpServletRequest request) {
      String errorMsg = "保存进程错误";

      try {
         if (StringUtils.isEmpty(AppInfo.getId())) {
            this.appInfoService.save(AppInfo, request);
            this.appInfoService.saveLog(request, "添加", AppInfo);
         } else {
            this.appInfoService.updateById(AppInfo);
            this.appInfoService.saveLog(request, "修改", AppInfo);
         }
      } catch (Exception var6) {
         logger.error(errorMsg, var6);
         this.logInfoService.save(errorMsg, var6.toString(), "2");
      }

      return "redirect:/appInfo/list";
   }

   @RequestMapping({"saveBatch"})
   public String saveBatchAppInfo(AppInfo AppInfo, Model model, HttpServletRequest request) {
      String errorMsg = "批量保存进程错误";

      try {
         String[] hostnames = request.getParameterValues("hostnames");
         if (null == hostnames || hostnames.length < 1) {
            return "redirect:/appInfo/list";
         }

         String[] var6 = hostnames;
         int var7 = hostnames.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String selectedHost = var6[var8];
            AppInfo.setHostname(selectedHost);
            this.appInfoService.save(AppInfo, request);
            this.appInfoService.saveLog(request, "添加", AppInfo);
         }
      } catch (Exception var10) {
         logger.error(errorMsg, var10);
         this.logInfoService.save(errorMsg, var10.toString(), "2");
      }

      return "redirect:/appInfo/list";
   }

   @RequestMapping({"edit"})
   public String edit(Model model, HttpServletRequest request) {
      String errorMsg = "添加进程错误";
      String id = request.getParameter("id");
      AppInfo appInfo = new AppInfo();

      try {
         Map<String, Object> params = new HashMap();
         HostUtil.addAccountquery(request, params);
         List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
         model.addAttribute("systemInfoList", systemInfoList);
         if (StringUtils.isEmpty(id)) {
            model.addAttribute("appInfo", appInfo);
            if (!this.isAddContinue()) {
               return "redirect:/appInfo/list?liceFlage=1";
            }

            return "app/add";
         }

         appInfo = this.appInfoService.selectById(id);
         model.addAttribute("appInfo", appInfo);
      } catch (Exception var8) {
         logger.error(errorMsg, var8);
         this.logInfoService.save(errorMsg, var8.toString(), "2");
      }

      return "app/add";
   }

   @RequestMapping({"editBatch"})
   public String editBatch(Model model, HttpServletRequest request) {
      String errorMsg = "批量添加进程错误";
      AppInfo appInfo = new AppInfo();

      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return "redirect:/appInfo/list?liceFlage=2";
         }

         Map<String, Object> params = new HashMap();
         HostUtil.addAccountquery(request, params);
         List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
         model.addAttribute("systemInfoList", systemInfoList);
         model.addAttribute("appInfo", appInfo);
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "app/addBatch";
   }

   @RequestMapping({"view"})
   public String viewChart(Model model, HttpServletRequest request) {
      String errorMsg = "查看进程图表错误";
      String id = request.getParameter("id");
      String startTime = request.getParameter("startTime");
      String endTime = request.getParameter("endTime");
      String am = request.getParameter("am");
      new AppInfo();

      try {
         AppInfo appInfo = this.appInfoService.selectById(id);
         appInfo.setHostname(appInfo.getHostname() + HostUtil.addRemark(appInfo.getHostname()));
         Map<String, Object> params = new HashMap();
         this.dashboardService.setDateParam(am, startTime, endTime, params, model);
         model.addAttribute("amList", this.dashboardService.getAmList());
         params.put("appInfoId", appInfo.getId());
         model.addAttribute("appInfo", appInfo);
         List<AppState> appStateList = this.appStateService.selectAllByParams(params);
         this.appStateService.setSubtitle(model, appStateList);
         model.addAttribute("appStateList", JSONUtil.parseArray(appStateList));
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "app/view";
   }

   @RequestMapping({"chartExcel"})
   public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
      String errorMsg = "进程统计图导出excel错误";
      String id = request.getParameter("id");
      String startTime = request.getParameter("startTime");
      String endTime = request.getParameter("endTime");
      String am = request.getParameter("am");

      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            response.setContentType("text/html;charset=UTF-8");
            response.getOutputStream().write("The module needs to wgcloud-pro version. Please contact us at www.wgstart.com".getBytes());
            return;
         }

         if (StringUtils.isEmpty(id)) {
            response.setContentType("text/html;charset=UTF-8");
            response.getOutputStream().write("Missing require parameters".getBytes());
            return;
         }

         Map<String, Object> params = new HashMap();
         params.put("appInfoId", id);
         this.dashboardService.setDateParam(am, startTime, endTime, params, model);
         this.excelExportService.exportAppExcel(params, response);
      } catch (Exception var10) {
         logger.error(errorMsg, var10);
         this.logInfoService.save(errorMsg, var10.toString(), "2");
      }

   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "删除进程信息错误";
      new AppInfo();

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            String[] ids = request.getParameter("id").split(",");
            String[] var7 = ids;
            int var8 = ids.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String id = var7[var9];
               AppInfo appInfo = this.appInfoService.selectById(id);
               this.appInfoService.saveLog(request, "删除", appInfo);
            }

            this.appInfoService.deleteById(request.getParameter("id").split(","));
         }
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "redirect:/appInfo/list";
   }

   private boolean isAddContinue() {
      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            Map<String, Object> params = new HashMap();
            int dbSize = this.appInfoService.countByParams(params);
            if (dbSize >= 10) {
               return false;
            }
         }
      } catch (Exception var3) {
         logger.error("isAddContinue error", var3);
      }

      return true;
   }
}
