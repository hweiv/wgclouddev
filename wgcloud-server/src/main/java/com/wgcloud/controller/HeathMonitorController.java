package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.entity.HeathState;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.HeathMonitorService;
import com.wgcloud.service.HeathStateService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ServerBackupUtil;
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
@RequestMapping({"/heathMonitor"})
public class HeathMonitorController {
   private static final Logger logger = LoggerFactory.getLogger(HeathMonitorController.class);
   @Resource
   private HeathMonitorService heathMonitorService;
   @Resource
   private HeathStateService heathStateService;
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private DashboardService dashboardService;
   @Resource
   private AccountInfoService accountInfoService;
   @Resource
   private ExcelExportService excelExportService;
   @Autowired
   private TokenUtils tokenUtils;
   @Autowired
   private CommonConfig commonConfig;

   @ResponseBody
   @RequestMapping({"agentList"})
   public String agentList(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
      if (!StringUtils.isEmpty(checkResult)) {
         return checkResult;
      } else {
         HashMap params = new HashMap();

         try {
            if (null != agentJsonObject.get("heathNames") && !StringUtils.isEmpty(agentJsonObject.get("heathNames").toString())) {
               params.put("heathNames", agentJsonObject.get("heathNames").toString().split(","));
            }

            params.put("active", "1");
            List<HeathMonitor> heathMonitorList = this.heathMonitorService.selectAllByParams(params);
            ServerBackupUtil.cacheSaveHeathMontiorId(heathMonitorList);
            return ResDataUtils.resetSuccessJson(heathMonitorList);
         } catch (Exception var6) {
            logger.error("agent获取监控服务接口信息错误", var6);
            this.logInfoService.save("agent获取监控服务接口信息错误", var6.toString(), "2");
            return ResDataUtils.resetErrorJson(var6.toString());
         }
      }
   }

   @RequestMapping({"list"})
   public String heathMonitorList(HeathMonitor heathMonitor, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         LicenseUtil.maxLicense_10(model, request, heathMonitor);
         StringBuffer url = new StringBuffer();
         String appName = null;
         String heathStatus = null;
         if (!StringUtils.isEmpty(heathMonitor.getAppName())) {
            appName = heathMonitor.getAppName();
            params.put("appName", appName.trim());
            url.append("&appName=").append(appName);
         }

         if (!StringUtils.isEmpty(heathMonitor.getAccount())) {
            params.put("account", heathMonitor.getAccount());
            url.append("&account=").append(heathMonitor.getAccount());
         }

         if (!StringUtils.isEmpty(heathMonitor.getHeathStatus())) {
            heathStatus = heathMonitor.getHeathStatus();
            params.put("heathStatus", heathStatus.trim());
            url.append("&heathStatus=").append(heathStatus);
         }

         if (!StringUtils.isEmpty(heathMonitor.getOrderBy())) {
            params.put("orderBy", heathMonitor.getOrderBy());
            params.put("orderType", heathMonitor.getOrderType());
            url.append("&orderBy=").append(heathMonitor.getOrderBy());
            url.append("&orderType=").append(heathMonitor.getOrderType());
         }

         HostUtil.addAccountquery(request, params);
         PageInfo<HeathMonitor> pageInfo = this.heathMonitorService.selectByParams(params, heathMonitor.getPage(), heathMonitor.getPageSize());
         Iterator var9 = pageInfo.getList().iterator();

         while(var9.hasNext()) {
            HeathMonitor heathMonitorTmp = (HeathMonitor)var9.next();
            if (StringUtils.isEmpty(heathMonitorTmp.getResKeyword())) {
               heathMonitorTmp.setResKeyword("");
            }

            if ("true".equals(this.commonConfig.getShowWarnCount())) {
               String warnQueryWd = "服务接口检测告警：" + heathMonitorTmp.getAppName();
               this.logInfoService.warnQueryHandle(heathMonitorTmp, warnQueryWd);
            }
         }

         PageUtil.initPageNumber(pageInfo, model);
         HostUtil.addAccountListModel(model);
         model.addAttribute("pageUrl", "/heathMonitor/list?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
         model.addAttribute("heathMonitor", heathMonitor);
      } catch (Exception var12) {
         logger.error("查询服务接口监控错误", var12);
         this.logInfoService.save("查询服务接口监控错误", var12.toString(), "2");
      }

      return "heath/list";
   }

   @RequestMapping({"save"})
   public String saveHeathMonitor(HeathMonitor heathMonitor, Model model, HttpServletRequest request) {
      try {
         if (StringUtils.isEmpty(heathMonitor.getId())) {
            AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
            if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
               heathMonitor.setAccount(accountInfo.getAccount());
            }

            this.heathMonitorService.save(heathMonitor);
            this.heathMonitorService.saveLog(request, "添加", heathMonitor);
         } else {
            this.heathMonitorService.updateById(heathMonitor);
            this.heathMonitorService.saveLog(request, "修改", heathMonitor);
         }
      } catch (Exception var5) {
         logger.error("保存服务心跳监控错误", var5);
         this.logInfoService.save("保存服务心跳监控错误", var5.toString(), "2");
      }

      return "redirect:/heathMonitor/list";
   }

   @RequestMapping({"edit"})
   public String edit(Model model, HttpServletRequest request) {
      String errorMsg = "编辑服务接口监控";
      String id = request.getParameter("id");
      HeathMonitor heathMonitor = new HeathMonitor();

      try {
         if (StringUtils.isEmpty(id)) {
            model.addAttribute("heathMonitor", heathMonitor);
            if (!this.isAddContinue()) {
               return "redirect:/heathMonitor/list?liceFlage=1";
            }

            return "heath/add";
         }

         heathMonitor = this.heathMonitorService.selectById(id);
         this.heathMonitorService.displayHeaderJson(heathMonitor);
         this.heathMonitorService.displayFormJson(heathMonitor);
         model.addAttribute("heathMonitor", heathMonitor);
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "heath/add";
   }

   @RequestMapping({"view"})
   public String view(Model model, HttpServletRequest request) {
      String errorMsg = "查看服务接口统计图表错误";
      String id = request.getParameter("id");
      String startTime = request.getParameter("startTime");
      String endTime = request.getParameter("endTime");
      String am = request.getParameter("am");
      new HeathMonitor();

      try {
         HeathMonitor heathMonitor = this.heathMonitorService.selectById(id);
         Map<String, Object> params = new HashMap();
         model.addAttribute("heathMonitor", heathMonitor);
         this.dashboardService.setDateParam(am, startTime, endTime, params, model);
         model.addAttribute("amList", this.dashboardService.getAmList());
         params.put("heathId", heathMonitor.getId());
         List<HeathState> heathStateList = this.heathStateService.selectAllByParams(params);
         this.heathStateService.setSubtitle(model, heathStateList);
         model.addAttribute("heathStateList", JSONUtil.parseArray(heathStateList));
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "heath/view";
   }

   @RequestMapping({"chartExcel"})
   public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
      String errorMsg = "服务接口统计图导出excel错误";
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
         params.put("heathId", id);
         this.dashboardService.setDateParam(am, startTime, endTime, params, model);
         this.excelExportService.exportHeathExcel(params, response);
      } catch (Exception var10) {
         logger.error(errorMsg, var10);
         this.logInfoService.save(errorMsg, var10.toString(), "2");
      }

   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "删除服务接口监控错误";
      new HeathMonitor();

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            String[] ids = request.getParameter("id").split(",");
            String[] var7 = ids;
            int var8 = ids.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String id = var7[var9];
               HeathMonitor HeathMonitor = this.heathMonitorService.selectById(id);
               this.heathMonitorService.saveLog(request, "删除", HeathMonitor);
            }

            this.heathMonitorService.deleteById(ids);
         }
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "redirect:/heathMonitor/list";
   }

   @RequestMapping({"updateActive"})
   public String updateActive(Model model, HttpServletRequest request) {
      String errorMsg = "批量开始监控和停止监控错误";

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            String[] ids = request.getParameter("id").split(",");
            Map<String, Object> params = new HashMap();
            params.put("ids", ids);
            String activeValue = request.getParameter("active");
            params.put("active", activeValue);
            this.heathMonitorService.updateActive(params);
         }
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "redirect:/heathMonitor/list";
   }

   private boolean isAddContinue() {
      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            Map<String, Object> params = new HashMap();
            int dbSize = this.heathMonitorService.countByParams(params);
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
