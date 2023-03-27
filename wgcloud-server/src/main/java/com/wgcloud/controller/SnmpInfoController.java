package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.SnmpInfo;
import com.wgcloud.entity.SnmpState;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SnmpStateService;
import com.wgcloud.util.FormatUtil;
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
@RequestMapping({"/snmpInfo"})
public class SnmpInfoController {
   private static final Logger logger = LoggerFactory.getLogger(SnmpInfoController.class);
   @Resource
   private SnmpInfoService snmpInfoService;
   @Resource
   private SnmpStateService snmpStateService;
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private DashboardService dashboardService;
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
            if (null != agentJsonObject.get("snmpHostNames") && !StringUtils.isEmpty(agentJsonObject.get("snmpHostNames").toString())) {
               params.put("snmpHostNames", agentJsonObject.get("snmpHostNames").toString().split(","));
            }

            params.put("active", "1");
            List<SnmpInfo> snmpInfoList = this.snmpInfoService.selectAllByParams(params);
            ServerBackupUtil.cacheSaveSnmpInfoId(snmpInfoList);
            return ResDataUtils.resetSuccessJson(snmpInfoList);
         } catch (Exception var6) {
            logger.error("agent获取snmp设备监测信息错误", var6);
            this.logInfoService.save("agent获取snmp设备监测信息错误", var6.toString(), "2");
            return ResDataUtils.resetErrorJson(var6.toString());
         }
      }
   }

   @RequestMapping({"list"})
   public String snmpInfoList(SnmpInfo SnmpInfo, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         LicenseUtil.maxLicense_10(model, request, SnmpInfo);
         StringBuffer url = new StringBuffer();
         String hostName = null;
         if (!StringUtils.isEmpty(SnmpInfo.getHostname())) {
            hostName = SnmpInfo.getHostname();
            params.put("hostname", hostName.trim());
            url.append("&hostname=").append(hostName);
         }

         if (!StringUtils.isEmpty(SnmpInfo.getAccount())) {
            params.put("account", SnmpInfo.getAccount());
            url.append("&account=").append(SnmpInfo.getAccount());
         }

         if (!StringUtils.isEmpty(SnmpInfo.getOrderBy())) {
            params.put("orderBy", SnmpInfo.getOrderBy());
            params.put("orderType", SnmpInfo.getOrderType());
            url.append("&orderBy=").append(SnmpInfo.getOrderBy());
            url.append("&orderType=").append(SnmpInfo.getOrderType());
         }

         if (!StringUtils.isEmpty(SnmpInfo.getState())) {
            params.put("state", SnmpInfo.getState());
            url.append("&state=").append(SnmpInfo.getState());
         }

         HostUtil.addAccountquery(request, params);
         PageInfo<SnmpInfo> pageInfo = this.snmpInfoService.selectByParams(params, SnmpInfo.getPage(), SnmpInfo.getPageSize());
         Iterator var8 = pageInfo.getList().iterator();

         while(var8.hasNext()) {
            SnmpInfo snmpInfoTmp = (SnmpInfo)var8.next();
            if ("true".equals(this.commonConfig.getShowWarnCount())) {
               String warnQueryWd = "snmp设备监测告警：" + snmpInfoTmp.getHostname();
               this.logInfoService.warnQueryHandle(snmpInfoTmp, warnQueryWd);
            }

            snmpInfoTmp.setBytesRecv(FormatUtil.bytesFormatUnit(snmpInfoTmp.getBytesRecv(), snmpInfoTmp.getSnmpUnit()));
            snmpInfoTmp.setBytesSent(FormatUtil.bytesFormatUnit(snmpInfoTmp.getBytesSent(), snmpInfoTmp.getSnmpUnit()));
            snmpInfoTmp.setRecvAvg(FormatUtil.bytesFormatUnit(snmpInfoTmp.getRecvAvg(), snmpInfoTmp.getSnmpUnit()) + "/s");
            snmpInfoTmp.setSentAvg(FormatUtil.bytesFormatUnit(snmpInfoTmp.getSentAvg(), snmpInfoTmp.getSnmpUnit()) + "/s");
            if (StringUtils.isEmpty(snmpInfoTmp.getCpuPerOID())) {
               snmpInfoTmp.setCpuPer("NaN");
            }

            if (StringUtils.isEmpty(snmpInfoTmp.getMemSizeOID()) || StringUtils.isEmpty(snmpInfoTmp.getMemTotalSizeOID())) {
               snmpInfoTmp.setMemPer("NaN");
            }

            if (StringUtils.isEmpty(snmpInfoTmp.getDiskPerOid())) {
               snmpInfoTmp.setDiskPer("NaN");
            }
         }

         PageUtil.initPageNumber(pageInfo, model);
         HostUtil.addAccountListModel(model);
         model.addAttribute("pageUrl", "/snmpInfo/list?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
         model.addAttribute("snmpInfo", SnmpInfo);
      } catch (Exception var11) {
         logger.error("查询snmp设备监测错误", var11);
         this.logInfoService.save("查询snmp设备监测错误", var11.toString(), "2");
      }

      return "snmp/list";
   }

   @RequestMapping({"save"})
   public String saveSnmpInfo(SnmpInfo SnmpInfo, Model model, HttpServletRequest request) {
      String errorMsg = "保存snmp设备监测错误";

      try {
         String methodBlock = request.getParameter("methodBlock");
         if ("2".equals(methodBlock)) {
            SnmpInfo.setMemTotalSizeOID("block");
         }

         if (StringUtils.isEmpty(SnmpInfo.getId())) {
            AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
            if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
               SnmpInfo.setAccount(accountInfo.getAccount());
            }

            this.snmpInfoService.save(SnmpInfo);
            this.snmpInfoService.saveLog(request, "添加", SnmpInfo);
         } else {
            this.snmpInfoService.updateById(SnmpInfo);
            this.snmpInfoService.saveLog(request, "修改", SnmpInfo);
         }
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "redirect:/snmpInfo/list";
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
            this.snmpInfoService.updateActive(params);
         }
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "redirect:/snmpInfo/list";
   }

   @RequestMapping({"edit"})
   public String edit(Model model, HttpServletRequest request) {
      String errorMsg = "编辑snmp设备监测";
      String id = request.getParameter("id");
      SnmpInfo SnmpInfo = new SnmpInfo();

      try {
         if (StringUtils.isEmpty(id)) {
            SnmpInfo.setSnmpPort("161");
            SnmpInfo.setSnmpCommunity("public");
            SnmpInfo.setSnmpVersion("1");
            model.addAttribute("snmpInfo", SnmpInfo);
            if (!this.isAddContinue()) {
               return "redirect:/snmpInfo/list?liceFlage=1";
            }

            return "snmp/add";
         }

         SnmpInfo = this.snmpInfoService.selectById(id);
         model.addAttribute("snmpInfo", SnmpInfo);
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "snmp/add";
   }

   @RequestMapping({"view"})
   public String view(Model model, HttpServletRequest request) {
      String errorMsg = "查看snmp设备图表错误";
      String id = request.getParameter("id");
      String startTime = request.getParameter("startTime");
      String endTime = request.getParameter("endTime");
      String am = request.getParameter("am");
      new SnmpInfo();

      try {
         SnmpInfo SnmpInfo = this.snmpInfoService.selectById(id);
         Map<String, Object> params = new HashMap();
         model.addAttribute("snmpInfo", SnmpInfo);
         this.dashboardService.setDateParam(am, startTime, endTime, params, model);
         model.addAttribute("amList", this.dashboardService.getAmList());
         params.put("snmpInfoId", SnmpInfo.getId());
         List<SnmpState> snmpStateList = this.snmpStateService.selectAllByParams(params);
         this.snmpStateService.setSubtitle(model, snmpStateList, SnmpInfo);
         model.addAttribute("snmpStateList", JSONUtil.parseArray(snmpStateList));
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "snmp/view";
   }

   @RequestMapping({"chartExcel"})
   public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
      String errorMsg = "snmp设备统计图导出excel错误";
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
         params.put("snmpInfoId", id);
         this.dashboardService.setDateParam(am, startTime, endTime, params, model);
         this.excelExportService.exportSnmpExcel(params, response);
      } catch (Exception var10) {
         logger.error(errorMsg, var10);
         this.logInfoService.save(errorMsg, var10.toString(), "2");
      }

   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "删除snmp设备监测错误";
      new SnmpInfo();

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            String[] ids = request.getParameter("id").split(",");
            String[] var7 = ids;
            int var8 = ids.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String id = var7[var9];
               SnmpInfo SnmpInfo = this.snmpInfoService.selectById(id);
               this.snmpInfoService.saveLog(request, "删除", SnmpInfo);
            }

            this.snmpInfoService.deleteById(ids);
         }
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "redirect:/snmpInfo/list";
   }

   private boolean isAddContinue() {
      try {
         Map<String, Object> params = new HashMap();
         int size = this.snmpInfoService.countByParams(params);
         if (size >= 10 && (size >= StaticKeys.LICENSE_NUM || StaticKeys.LICENSE_STATE.equals("2"))) {
            return false;
         }
      } catch (Exception var3) {
         logger.error("isAddContinue error", var3);
      }

      return true;
   }
}
