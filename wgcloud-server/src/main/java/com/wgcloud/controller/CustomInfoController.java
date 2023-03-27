package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.CustomInfo;
import com.wgcloud.entity.CustomState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.CustomInfoService;
import com.wgcloud.service.CustomStateService;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.ShellInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
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
@RequestMapping({"/customInfo"})
public class CustomInfoController {
   private static final Logger logger = LoggerFactory.getLogger(CustomInfoController.class);
   @Resource
   private CustomInfoService customInfoService;
   @Resource
   private CustomStateService customStateService;
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
   @Resource
   private ShellInfoService shellInfoService;
   @Autowired
   private TokenUtils tokenUtils;
   @Autowired
   private CommonConfig commonConfig;

   @ResponseBody
   @RequestMapping({"agentList"})
   public String agentList(@RequestBody String paramBean) {
      if (!StaticKeys.LICENSE_STATE.equals("1")) {
         List<CustomInfo> customInfoList = new ArrayList();
         return ResDataUtils.resetSuccessJson(customInfoList);
      } else {
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
                  List<CustomInfo> customInfoList = this.customInfoService.selectAllByParams(params);
                  String cmdSplitChar = " ; ";
                  SystemInfo systemInfo = this.systemInfoService.selectByHostname(agentJsonObject.get("hostname").toString());
                  String shellToRunBlock = this.commonConfig.getShellToRunLinuxBlock();
                  String blockKey;
                  if (systemInfo != null) {
                     blockKey = systemInfo.getPlatForm().toLowerCase();
                     if (blockKey.contains("windows")) {
                        shellToRunBlock = this.commonConfig.getShellToRunWinBlock();
                        cmdSplitChar = " & ";
                     }
                  }

                  blockKey = "";
                  List<CustomInfo> customInfoListResult = new ArrayList();
                  Iterator var10 = customInfoList.iterator();

                  while(var10.hasNext()) {
                     CustomInfo state = (CustomInfo)var10.next();
                     blockKey = FormatUtil.haveBlockDanger(state.getCustomShell(), shellToRunBlock, "");
                     if (!StringUtils.isEmpty(blockKey)) {
                        logger.error(state.getCustomShell() + "自定义监控项指令含有敏感字符" + blockKey + "，不进行下发");
                     } else {
                        state.setCustomShell(state.getCustomShell().replaceAll("\\r\\n", cmdSplitChar));
                        customInfoListResult.add(state);
                     }
                  }

                  return ResDataUtils.resetSuccessJson(customInfoListResult);
               } catch (Exception var12) {
                  logger.error("agent获取自定义监控项信息错误", var12);
                  this.logInfoService.save("agent获取自定义监控项信息错误", var12.toString(), "2");
                  return ResDataUtils.resetErrorJson(var12.toString());
               }
            } else {
               return "";
            }
         }
      }
   }

   @RequestMapping({"list"})
   public String customInfoList(CustomInfo customInfo, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         String liceFlage = request.getParameter(StaticKeys.LICENSE_LICE_FLAGE);
         if (!StringUtils.isEmpty(liceFlage)) {
            model.addAttribute("msg", "此功能需升级到专业版，请点击页面底部网站联系我们");
         }

         StringBuffer url = new StringBuffer();
         String hostname = null;
         if (!StringUtils.isEmpty(customInfo.getHostname())) {
            hostname = customInfo.getHostname();
            params.put("hostname", hostname.trim());
            url.append("&hostname=").append(hostname);
         }

         if (!StringUtils.isEmpty(customInfo.getAccount())) {
            params.put("account", customInfo.getAccount());
            url.append("&account=").append(customInfo.getAccount());
         }

         if (!StringUtils.isEmpty(customInfo.getGroupId())) {
            params.put("groupId", customInfo.getGroupId());
            url.append("&groupId=").append(customInfo.getGroupId());
         }

         if (!StringUtils.isEmpty(customInfo.getState())) {
            params.put("state", customInfo.getState());
            url.append("&state=").append(customInfo.getState());
         }

         HostUtil.addAccountquery(request, params);
         PageInfo<CustomInfo> pageInfo = this.customInfoService.selectByParams(params, customInfo.getPage(), customInfo.getPageSize());
         Iterator var9 = pageInfo.getList().iterator();

         while(var9.hasNext()) {
            CustomInfo customInfo1 = (CustomInfo)var9.next();
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
               customInfo1.setAccount(HostUtil.getAccount(customInfo1.getHostname()));
            }

            customInfo1.setHostname(customInfo1.getHostname() + HostUtil.addRemark(customInfo1.getHostname()));
            if ("true".equals(this.commonConfig.getShowWarnCount())) {
               String warnQueryWd = customInfo1.getCustomName() + "，" + customInfo1.getHostname();
               this.logInfoService.warnQueryHandle(customInfo1, warnQueryWd);
            }
         }

         if ("true".equals(this.commonConfig.getHostGroup())) {
            this.customInfoService.setGroupInList(pageInfo.getList(), model);
         }

         HostUtil.addAccountListModel(model);
         PageUtil.initPageNumber(pageInfo, model);
         model.addAttribute("pageUrl", "/customInfo/list?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
         model.addAttribute("customInfo", customInfo);
      } catch (Exception var12) {
         logger.error("查询自定义监控项信息错误", var12);
         this.logInfoService.save("查询自定义监控项信息错误", var12.toString(), "2");
      }

      return "customInfo/list";
   }

   @ResponseBody
   @RequestMapping({"saveGroupId"})
   public String saveGroupId(Model model, HttpServletRequest request) {
      try {
         String ids = request.getParameter("ids");
         String[] groupIdsArr = request.getParameterValues("groupId");
         this.customInfoService.saveGroupId(ids, groupIdsArr, request);
      } catch (Exception var5) {
         logger.error("保存进程标签信息错误", var5);
         this.logInfoService.save("保存进程标签信息错误", var5.toString(), "2");
      }

      return "redirect:/appInfo/list";
   }

   @RequestMapping({"save"})
   public String saveCustomInfo(CustomInfo customInfo, Model model, HttpServletRequest request) {
      String errorMsg = "保存自定义监控项错误";

      try {
         String blockKey = FormatUtil.haveBlockDanger(customInfo.getCustomShell(), this.commonConfig.getShellToRunLinuxBlock(), this.commonConfig.getShellToRunWinBlock());
         if (!StringUtils.isEmpty(blockKey)) {
            model.addAttribute("customInfo", customInfo);
            model.addAttribute("msg", "下发指令含有敏感字符" + blockKey + "，请检查");
            this.shellInfoService.getBlockStr(model);
            Map<String, Object> params = new HashMap();
            HostUtil.addAccountquery(request, params);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            model.addAttribute("systemInfoList", systemInfoList);
            return "customInfo/add";
         }

         if (StringUtils.isEmpty(customInfo.getId())) {
            this.customInfoService.save(customInfo, request);
            this.customInfoService.saveLog(request, "添加", customInfo);
         } else {
            this.customInfoService.updateById(customInfo);
            this.customInfoService.saveLog(request, "修改", customInfo);
         }
      } catch (Exception var8) {
         logger.error(errorMsg, var8);
         this.logInfoService.save(errorMsg, var8.toString(), "2");
      }

      return "redirect:/customInfo/list";
   }

   @RequestMapping({"edit"})
   public String edit(Model model, HttpServletRequest request) {
      String errorMsg = "添加自定义监控项错误";
      String id = request.getParameter("id");
      CustomInfo customInfo = new CustomInfo();

      try {
         Map<String, Object> params = new HashMap();
         HostUtil.addAccountquery(request, params);
         this.shellInfoService.getBlockStr(model);
         List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
         model.addAttribute("systemInfoList", systemInfoList);
         if (StringUtils.isEmpty(id)) {
            model.addAttribute("customInfo", customInfo);
            if (!this.isAddContinue()) {
               return "redirect:/customInfo/list?liceFlage=1";
            }

            return "customInfo/add";
         }

         customInfo = this.customInfoService.selectById(id);
         model.addAttribute("customInfo", customInfo);
      } catch (Exception var8) {
         logger.error(errorMsg, var8);
         this.logInfoService.save(errorMsg, var8.toString(), "2");
      }

      return "customInfo/add";
   }

   @RequestMapping({"view"})
   public String viewChart(Model model, HttpServletRequest request) {
      String errorMsg = "查看自定义监控项图表错误";
      String id = request.getParameter("id");
      String startTime = request.getParameter("startTime");
      String endTime = request.getParameter("endTime");
      String am = request.getParameter("am");
      new CustomInfo();

      try {
         CustomInfo customInfo = this.customInfoService.selectById(id);
         customInfo.setHostname(customInfo.getHostname() + HostUtil.addRemark(customInfo.getHostname()));
         Map<String, Object> params = new HashMap();
         this.dashboardService.setDateParam(am, startTime, endTime, params, model);
         model.addAttribute("amList", this.dashboardService.getAmList());
         params.put("customInfoId", customInfo.getId());
         model.addAttribute("customInfo", customInfo);
         List<CustomState> customStateList = this.customStateService.selectAllByParams(params);
         this.customStateService.setSubtitle(model, customStateList);
         model.addAttribute("customStateList", JSONUtil.parseArray(customStateList));
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "customInfo/view";
   }

   @RequestMapping({"chartExcel"})
   public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
      String errorMsg = "自定义监控项统计图导出excel错误";
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
         params.put("customInfoId", id);
         this.dashboardService.setDateParam(am, startTime, endTime, params, model);
         this.excelExportService.exportCustomExcel(params, response);
      } catch (Exception var10) {
         logger.error(errorMsg, var10);
         this.logInfoService.save(errorMsg, var10.toString(), "2");
      }

   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "删除自定义监控项信息错误";
      new CustomInfo();

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            String[] ids = request.getParameter("id").split(",");
            String[] var7 = ids;
            int var8 = ids.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String id = var7[var9];
               CustomInfo customInfo = this.customInfoService.selectById(id);
               this.customInfoService.saveLog(request, "删除", customInfo);
            }

            this.customInfoService.deleteById(request.getParameter("id").split(","));
         }
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "redirect:/customInfo/list";
   }

   private boolean isAddContinue() {
      try {
         if (StaticKeys.LICENSE_STATE.equals("0") || StaticKeys.LICENSE_STATE.equals("2")) {
            return false;
         }
      } catch (Exception var2) {
         logger.error("isAddContinue error", var2);
      }

      return true;
   }
}
