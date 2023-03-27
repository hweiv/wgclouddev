package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.DockerInfo;
import com.wgcloud.entity.DockerState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.DockerStateService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
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
@RequestMapping({"/dockerInfo"})
public class DockerInfoController {
   private static final Logger logger = LoggerFactory.getLogger(DockerInfoController.class);
   @Resource
   private DockerInfoService dockerInfoService;
   @Resource
   private DockerStateService dockerStateService;
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
               List<DockerInfo> DockerInfoList = this.dockerInfoService.selectAllByParams(params);
               return ResDataUtils.resetSuccessJson(DockerInfoList);
            } catch (Exception var5) {
               logger.error("agent获取docker信息错误", var5);
               this.logInfoService.save("agent获取docker信息错误", var5.toString(), "2");
               return ResDataUtils.resetErrorJson(var5.toString());
            }
         } else {
            return "";
         }
      }
   }

   @RequestMapping({"list"})
   public String dockerInfoList(DockerInfo dockerInfo, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         LicenseUtil.maxLicense_10(model, request, dockerInfo);
         StringBuffer url = new StringBuffer();
         String hostname = null;
         if (!StringUtils.isEmpty(dockerInfo.getHostname())) {
            hostname = dockerInfo.getHostname();
            params.put("hostname", hostname.trim());
            url.append("&hostname=").append(hostname);
         }

         if (!StringUtils.isEmpty(dockerInfo.getAccount())) {
            params.put("account", dockerInfo.getAccount());
            url.append("&account=").append(dockerInfo.getAccount());
         }

         if (!StringUtils.isEmpty(dockerInfo.getState())) {
            params.put("state", dockerInfo.getState());
            url.append("&state=").append(dockerInfo.getState());
         }

         if (!StringUtils.isEmpty(dockerInfo.getGroupId())) {
            params.put("groupId", dockerInfo.getGroupId());
            url.append("&groupId=").append(dockerInfo.getGroupId());
         }

         if (!StringUtils.isEmpty(dockerInfo.getOrderBy())) {
            params.put("orderBy", dockerInfo.getOrderBy());
            params.put("orderType", dockerInfo.getOrderType());
            url.append("&orderBy=").append(dockerInfo.getOrderBy());
            url.append("&orderType=").append(dockerInfo.getOrderType());
         }

         HostUtil.addAccountquery(request, params);
         PageInfo<DockerInfo> pageInfo = this.dockerInfoService.selectByParams(params, dockerInfo.getPage(), dockerInfo.getPageSize());
         Iterator var8 = pageInfo.getList().iterator();

         while(var8.hasNext()) {
            DockerInfo dockerInfo1 = (DockerInfo)var8.next();
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
               dockerInfo1.setAccount(HostUtil.getAccount(dockerInfo1.getHostname()));
            }

            dockerInfo1.setHostname(dockerInfo1.getHostname() + HostUtil.addRemark(dockerInfo1.getHostname()));
            if ("true".equals(this.commonConfig.getShowWarnCount())) {
               String warnQueryWd = dockerInfo1.getDockerName() + "，" + dockerInfo1.getHostname();
               this.logInfoService.warnQueryHandle(dockerInfo1, warnQueryWd);
            }
         }

         if ("true".equals(this.commonConfig.getHostGroup())) {
            this.dockerInfoService.setGroupInList(pageInfo.getList(), model);
         }

         HostUtil.addAccountListModel(model);
         PageUtil.initPageNumber(pageInfo, model);
         model.addAttribute("pageUrl", "/dockerInfo/list?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
         model.addAttribute("dockerInfo", dockerInfo);
      } catch (Exception var11) {
         logger.error("查询docker信息错误", var11);
         this.logInfoService.save("查询docker信息错误", var11.toString(), "2");
      }

      return "docker/list";
   }

   @ResponseBody
   @RequestMapping({"saveGroupId"})
   public String saveGroupId(Model model, HttpServletRequest request) {
      try {
         String ids = request.getParameter("ids");
         String[] groupIdsArr = request.getParameterValues("groupId");
         this.dockerInfoService.saveGroupId(ids, groupIdsArr, request);
      } catch (Exception var5) {
         logger.error("保存docker分组信息错误", var5);
         this.logInfoService.save("保存docker分组信息错误", var5.toString(), "2");
      }

      return "redirect:/dockerInfo/list";
   }

   @RequestMapping({"save"})
   public String saveDockerInfo(DockerInfo dockerInfo, Model model, HttpServletRequest request) {
      String errorMsg = "保存docker信息错误";

      try {
         if (StringUtils.isEmpty(dockerInfo.getId())) {
            this.dockerInfoService.save(dockerInfo, request);
            this.dockerInfoService.saveLog(request, "添加", dockerInfo);
         } else {
            this.dockerInfoService.updateById(dockerInfo);
            this.dockerInfoService.saveLog(request, "修改", dockerInfo);
         }
      } catch (Exception var6) {
         logger.error(errorMsg, var6);
         this.logInfoService.save(errorMsg, var6.toString(), "2");
      }

      return "redirect:/dockerInfo/list";
   }

   @RequestMapping({"saveBatch"})
   public String saveBatchDockerInfo(DockerInfo dockerInfo, Model model, HttpServletRequest request) {
      String errorMsg = "批量保存docker信息错误";

      try {
         String[] hostnames = request.getParameterValues("hostnames");
         if (null == hostnames || hostnames.length < 1) {
            return "redirect:/dockerInfo/list";
         }

         String[] var6 = hostnames;
         int var7 = hostnames.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String selectedHost = var6[var8];
            dockerInfo.setHostname(selectedHost);
            this.dockerInfoService.save(dockerInfo, request);
            this.dockerInfoService.saveLog(request, "添加", dockerInfo);
         }
      } catch (Exception var10) {
         logger.error(errorMsg, var10);
         this.logInfoService.save(errorMsg, var10.toString(), "2");
      }

      return "redirect:/dockerInfo/list";
   }

   @RequestMapping({"edit"})
   public String edit(Model model, HttpServletRequest request) {
      String errorMsg = "添加docker信息";
      String id = request.getParameter("id");
      DockerInfo DockerInfo = new DockerInfo();

      try {
         Map<String, Object> params = new HashMap();
         HostUtil.addAccountquery(request, params);
         List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
         model.addAttribute("systemInfoList", systemInfoList);
         if (StringUtils.isEmpty(id)) {
            model.addAttribute("dockerInfo", DockerInfo);
            if (!this.isAddContinue()) {
               return "redirect:/dockerInfo/list?liceFlage=1";
            }

            return "docker/add";
         }

         DockerInfo = this.dockerInfoService.selectById(id);
         model.addAttribute("dockerInfo", DockerInfo);
      } catch (Exception var8) {
         logger.error(errorMsg, var8);
         this.logInfoService.save(errorMsg, var8.toString(), "2");
      }

      return "docker/add";
   }

   @RequestMapping({"editBatch"})
   public String editBatch(Model model, HttpServletRequest request) {
      String errorMsg = "批量添加docker信息";
      DockerInfo DockerInfo = new DockerInfo();

      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return "redirect:/dockerInfo/list?liceFlage=1";
         }

         Map<String, Object> params = new HashMap();
         HostUtil.addAccountquery(request, params);
         List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
         model.addAttribute("systemInfoList", systemInfoList);
         model.addAttribute("dockerInfo", DockerInfo);
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "docker/addBatch";
   }

   @RequestMapping({"view"})
   public String viewChart(Model model, HttpServletRequest request) {
      String errorMsg = "查看docker信息图表错误";
      String id = request.getParameter("id");
      String startTime = request.getParameter("startTime");
      String endTime = request.getParameter("endTime");
      String am = request.getParameter("am");
      new DockerInfo();

      try {
         DockerInfo dockerInfo = this.dockerInfoService.selectById(id);
         dockerInfo.setHostname(dockerInfo.getHostname() + HostUtil.addRemark(dockerInfo.getHostname()));
         Map<String, Object> params = new HashMap();
         this.dashboardService.setDateParam(am, startTime, endTime, params, model);
         model.addAttribute("amList", this.dashboardService.getAmList());
         params.put("dockerInfoId", dockerInfo.getId());
         model.addAttribute("dockerInfo", dockerInfo);
         List<DockerState> DockerStateList = this.dockerStateService.selectAllByParams(params);
         this.dockerStateService.setSubtitle(model, DockerStateList);
         model.addAttribute("dockerStateList", JSONUtil.parseArray(DockerStateList));
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "docker/view";
   }

   @RequestMapping({"chartExcel"})
   public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
      String errorMsg = "docker信息统计图导出excel";
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
         params.put("dockerInfoId", id);
         this.dashboardService.setDateParam(am, startTime, endTime, params, model);
         this.excelExportService.exportDockerExcel(params, response);
      } catch (Exception var10) {
         logger.error(errorMsg, var10);
         this.logInfoService.save(errorMsg, var10.toString(), "2");
      }

   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "删除docker信息错误";
      new DockerInfo();

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            String[] ids = request.getParameter("id").split(",");
            String[] var7 = ids;
            int var8 = ids.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String id = var7[var9];
               DockerInfo DockerInfo = this.dockerInfoService.selectById(id);
               this.dockerInfoService.saveLog(request, "删除", DockerInfo);
            }

            this.dockerInfoService.deleteById(ids);
         }
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "redirect:/dockerInfo/list";
   }

   private boolean isAddContinue() {
      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            Map<String, Object> params = new HashMap();
            int size = this.dockerInfoService.countByParams(params);
            if (size >= 10) {
               return false;
            }
         }
      } catch (Exception var3) {
         logger.error("isAddContinue error", var3);
      }

      return true;
   }
}
