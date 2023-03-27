package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.DceState;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.DceStateService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
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
@RequestMapping({"/dceInfo"})
public class DceInfoController {
   private static final Logger logger = LoggerFactory.getLogger(DceInfoController.class);
   @Resource
   private DceInfoService dceInfoService;
   @Resource
   private SystemInfoService systemInfoService;
   @Resource
   private DceStateService dceStateService;
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private DashboardService dashboardService;
   @Resource
   private HostGroupService hostGroupService;
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
            if (null != agentJsonObject.get("dceHostNames") && !StringUtils.isEmpty(agentJsonObject.get("dceHostNames").toString())) {
               params.put("dceHostNames", agentJsonObject.get("dceHostNames").toString().split(","));
            }

            if (null != agentJsonObject.get("groupName") && !StringUtils.isEmpty(agentJsonObject.get("groupName").toString())) {
               Map<String, Object> paramsGroup = new HashMap();
               paramsGroup.put("groupName", agentJsonObject.get("groupName").toString());
               List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(paramsGroup);
               if (hostGroupList.size() <= 0) {
                  logger.error("groupName is error");
                  return ResDataUtils.resetErrorJson("groupName is error");
               }

               params.put("groupId", ((HostGroup)hostGroupList.get(0)).getId());
            }

            params.put("active", "1");
            List<DceInfo> dceInfoList = this.dceInfoService.selectAllByParams(params);
            ServerBackupUtil.cacheSaveDceInfoId(dceInfoList);
            return ResDataUtils.resetSuccessJson(dceInfoList);
         } catch (Exception var7) {
            logger.error("agent获取数控设备PING监测信息错误", var7);
            this.logInfoService.save("agent获取数控设备PING监测信息错误", var7.toString(), "2");
            return ResDataUtils.resetErrorJson(var7.toString());
         }
      }
   }

   @RequestMapping({"list"})
   public String dceInfoList(DceInfo dceInfo, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         LicenseUtil.maxLicense_10(model, request, dceInfo);
         StringBuffer url = new StringBuffer();
         String hostName = null;
         String heathStatus = null;
         if (!StringUtils.isEmpty(dceInfo.getHostname())) {
            hostName = dceInfo.getHostname();
            params.put("hostname", hostName.trim());
            url.append("&hostname=").append(hostName);
         }

         if (!StringUtils.isEmpty(dceInfo.getAccount())) {
            params.put("account", dceInfo.getAccount());
            url.append("&account=").append(dceInfo.getAccount());
         }

         if (!StringUtils.isEmpty(dceInfo.getGroupId())) {
            params.put("groupId", dceInfo.getGroupId());
            url.append("&groupId=").append(dceInfo.getGroupId());
         }

         if (!StringUtils.isEmpty(dceInfo.getOrderBy())) {
            params.put("orderBy", dceInfo.getOrderBy());
            params.put("orderType", dceInfo.getOrderType());
            url.append("&orderBy=").append(dceInfo.getOrderBy());
            url.append("&orderType=").append(dceInfo.getOrderType());
         }

         if (!StringUtils.isEmpty(request.getParameter("heathStatus"))) {
            heathStatus = request.getParameter("heathStatus");
            if ("200".equals(heathStatus)) {
               params.put("resTimesGt", -1);
            }

            if ("500".equals(heathStatus)) {
               params.put("resTimes", -1);
            }

            url.append("&heathStatus=").append(heathStatus);
         }

         HostUtil.addAccountquery(request, params);
         PageInfo<DceInfo> pageInfo = this.dceInfoService.selectByParams(params, dceInfo.getPage(), dceInfo.getPageSize());
         if ("true".equals(this.commonConfig.getShowWarnCount())) {
            Iterator var9 = pageInfo.getList().iterator();

            while(var9.hasNext()) {
               DceInfo dceInfoTmp = (DceInfo)var9.next();
               String warnQueryWd = "数通设备PING超时告警：" + dceInfoTmp.getHostname();
               this.logInfoService.warnQueryHandle(dceInfoTmp, warnQueryWd);
            }
         }

         if ("true".equals(this.commonConfig.getHostGroup())) {
            this.dceInfoService.setGroupInList(pageInfo.getList(), model);
         }

         HostUtil.addAccountListModel(model);
         PageUtil.initPageNumber(pageInfo, model);
         model.addAttribute("pageUrl", "/dceInfo/list?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
         model.addAttribute("dceInfo", dceInfo);
      } catch (Exception var12) {
         logger.error("查询数通设备PING监测错误", var12);
         this.logInfoService.save("查询数通设备PING监测错误", var12.toString(), "2");
      }

      return "dce/list";
   }

   @ResponseBody
   @RequestMapping({"saveGroupId"})
   public String saveGroupId(Model model, HttpServletRequest request) {
      try {
         String ids = request.getParameter("ids");
         String[] groupIdsArr = request.getParameterValues("groupId");
         this.dceInfoService.saveGroupId(ids, groupIdsArr, request);
      } catch (Exception var5) {
         logger.error("保存数通设备分组信息错误", var5);
         this.logInfoService.save("保存数通设备分组信息错误", var5.toString(), "2");
      }

      return "redirect:/dceInfo/list";
   }

   @RequestMapping({"save"})
   public String saveDceInfo(DceInfo dceInfo, Model model, HttpServletRequest request) {
      String errorMsg = "保存数通设备PING监测错误";

      try {
         if (StringUtils.isEmpty(dceInfo.getId())) {
            AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
            if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
               dceInfo.setAccount(accountInfo.getAccount());
            }

            this.dceInfoService.save(dceInfo, request);
            this.dceInfoService.saveLog(request, "添加", dceInfo);
         } else {
            this.dceInfoService.updateById(dceInfo);
            this.dceInfoService.saveLog(request, "修改", dceInfo);
         }
      } catch (Exception var6) {
         logger.error(errorMsg, var6);
         this.logInfoService.save(errorMsg, var6.toString(), "2");
      }

      return "redirect:/dceInfo/list";
   }

   @ResponseBody
   @RequestMapping({"importHosts"})
   public String importHosts(HttpServletRequest request) {
      try {
         Map<String, Object> map = new HashMap();
         HostUtil.addAccountquery(request, map);
         List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(map);
         List<DceInfo> dceInfoQueryList = this.dceInfoService.selectAllByParams(map);
         List<DceInfo> dceInfoSaveList = new ArrayList();
         boolean sign = false;
         Iterator var7 = systemInfoList.iterator();

         while(var7.hasNext()) {
            SystemInfo systemInfo = (SystemInfo)var7.next();
            DceInfo dceInfo = new DceInfo();
            dceInfo.setHostname(systemInfo.getHostname());
            sign = false;
            Iterator var10 = dceInfoQueryList.iterator();

            while(true) {
               if (var10.hasNext()) {
                  DceInfo d = (DceInfo)var10.next();
                  if (!d.getHostname().equals(systemInfo.getHostname())) {
                     continue;
                  }

                  sign = true;
               }

               if (!sign) {
                  dceInfo.setActive("1");
                  dceInfo.setRemark(systemInfo.getRemark());
                  AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
                  if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
                     dceInfo.setAccount(accountInfo.getAccount());
                  }

                  dceInfoSaveList.add(dceInfo);
               }
               break;
            }
         }

         this.dceInfoService.saveRecord(dceInfoSaveList);
         this.dceInfoService.saveLog(request, "将监控主机导入", new DceInfo());
      } catch (Exception var12) {
         logger.error("将监控主机导入到数通设备PING列表错误", var12);
         this.logInfoService.save("将监控主机导入到数通设备PING列表错误", var12.toString(), "2");
      }

      return "sucess";
   }

   @RequestMapping({"edit"})
   public String edit(Model model, HttpServletRequest request) {
      String errorMsg = "编辑数通设备PING监测";
      String id = request.getParameter("id");
      DceInfo dceInfo = new DceInfo();

      try {
         if (StringUtils.isEmpty(id)) {
            model.addAttribute("dceInfo", dceInfo);
            if (!this.isAddContinue()) {
               return "redirect:/dceInfo/list?liceFlage=1";
            }

            return "dce/add";
         }

         dceInfo = this.dceInfoService.selectById(id);
         model.addAttribute("dceInfo", dceInfo);
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "dce/add";
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
            this.dceInfoService.updateActive(params);
         }
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "redirect:/dceInfo/list";
   }

   @RequestMapping({"view"})
   public String view(Model model, HttpServletRequest request) {
      String errorMsg = "查看数通设备PING图表错误";
      String id = request.getParameter("id");
      String startTime = request.getParameter("startTime");
      String endTime = request.getParameter("endTime");
      String am = request.getParameter("am");
      new DceInfo();

      try {
         DceInfo dceInfo = this.dceInfoService.selectById(id);
         Map<String, Object> params = new HashMap();
         model.addAttribute("dceInfo", dceInfo);
         this.dashboardService.setDateParam(am, startTime, endTime, params, model);
         model.addAttribute("amList", this.dashboardService.getAmList());
         params.put("dceId", dceInfo.getId());
         List<DceState> dceStateList = this.dceStateService.selectAllByParams(params);
         this.dceStateService.setSubtitle(model, dceStateList);
         model.addAttribute("dceStateList", JSONUtil.parseArray(dceStateList));
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "dce/view";
   }

   @RequestMapping({"chartExcel"})
   public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
      String errorMsg = "ping设备统计图导出excel错误";
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
         params.put("dceId", id);
         this.dashboardService.setDateParam(am, startTime, endTime, params, model);
         this.excelExportService.exportDceExcel(params, response);
      } catch (Exception var10) {
         logger.error(errorMsg, var10);
         this.logInfoService.save(errorMsg, var10.toString(), "2");
      }

   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "删除数通设备PING监测错误";
      new DceInfo();

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            String[] ids = request.getParameter("id").split(",");
            String[] var7 = ids;
            int var8 = ids.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String id = var7[var9];
               DceInfo dceInfo = this.dceInfoService.selectById(id);
               this.dceInfoService.saveLog(request, "删除", dceInfo);
            }

            this.dceInfoService.deleteById(ids);
         }
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "redirect:/dceInfo/list";
   }

   private boolean isAddContinue() {
      try {
         Map<String, Object> params = new HashMap();
         int dbSize = this.dceInfoService.countByParams(params);
         if (dbSize >= 10 && (dbSize >= StaticKeys.LICENSE_NUM || StaticKeys.LICENSE_STATE.equals("2"))) {
            return false;
         }
      } catch (Exception var3) {
         logger.error("isAddContinue error", var3);
      }

      return true;
   }
}
