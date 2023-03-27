package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.PortInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.PortInfoService;
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
@RequestMapping({"/portInfo"})
public class PortInfoController {
   private static final Logger logger = LoggerFactory.getLogger(PortInfoController.class);
   @Resource
   private PortInfoService portInfoService;
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private SystemInfoService systemInfoService;
   @Resource
   private HostGroupService hostGroupService;
   @Autowired
   private CommonConfig commonConfig;
   @Autowired
   private TokenUtils tokenUtils;

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
               List<PortInfo> portInfoList = this.portInfoService.selectAllByParams(params);
               return ResDataUtils.resetSuccessJson(portInfoList);
            } catch (Exception var5) {
               logger.error("agent获取端口信息错误", var5);
               this.logInfoService.save("agent获取端口信息错误", var5.toString(), "2");
               return ResDataUtils.resetErrorJson(var5.toString());
            }
         } else {
            return "";
         }
      }
   }

   @RequestMapping({"list"})
   public String portInfoList(PortInfo portInfo, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         LicenseUtil.maxLicense_10(model, request, portInfo);
         StringBuffer url = new StringBuffer();
         String hostname = null;
         if (!StringUtils.isEmpty(portInfo.getHostname())) {
            hostname = portInfo.getHostname();
            params.put("hostname", hostname.trim());
            url.append("&hostname=").append(hostname);
         }

         if (!StringUtils.isEmpty(portInfo.getAccount())) {
            params.put("account", portInfo.getAccount());
            url.append("&account=").append(portInfo.getAccount());
         }

         if (!StringUtils.isEmpty(portInfo.getState())) {
            params.put("state", portInfo.getState());
            url.append("&state=").append(portInfo.getState());
         }

         if (!StringUtils.isEmpty(portInfo.getGroupId())) {
            params.put("groupId", portInfo.getGroupId());
            url.append("&groupId=").append(portInfo.getGroupId());
         }

         if (!StringUtils.isEmpty(portInfo.getOrderBy())) {
            params.put("orderBy", portInfo.getOrderBy());
            params.put("orderType", portInfo.getOrderType());
            url.append("&orderBy=").append(portInfo.getOrderBy());
            url.append("&orderType=").append(portInfo.getOrderType());
         }

         HostUtil.addAccountquery(request, params);
         PageInfo<PortInfo> pageInfo = this.portInfoService.selectByParams(params, portInfo.getPage(), portInfo.getPageSize());
         Iterator var8 = pageInfo.getList().iterator();

         while(var8.hasNext()) {
            PortInfo portInfo1 = (PortInfo)var8.next();
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
               portInfo1.setAccount(HostUtil.getAccount(portInfo1.getHostname()));
            }

            portInfo1.setHostname(portInfo1.getHostname() + HostUtil.addRemark(portInfo1.getHostname()));
            if ("true".equals(this.commonConfig.getShowWarnCount())) {
               String warnQueryWd = portInfo1.getPortName() + "，telnet-" + portInfo1.getTelnetIp() + "-" + portInfo1.getPort() + "，" + portInfo1.getHostname();
               this.logInfoService.warnQueryHandle(portInfo1, warnQueryWd);
            }
         }

         if ("true".equals(this.commonConfig.getHostGroup())) {
            this.portInfoService.setGroupInList(pageInfo.getList(), model);
         }

         HostUtil.addAccountListModel(model);
         PageUtil.initPageNumber(pageInfo, model);
         model.addAttribute("pageUrl", "/portInfo/list?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
         model.addAttribute("portInfo", portInfo);
      } catch (Exception var11) {
         logger.error("查询端口信息错误", var11);
         this.logInfoService.save("查询端口信息错误", var11.toString(), "2");
      }

      return "port/list";
   }

   @ResponseBody
   @RequestMapping({"saveGroupId"})
   public String saveGroupId(Model model, HttpServletRequest request) {
      try {
         String ids = request.getParameter("ids");
         String[] groupIdsArr = request.getParameterValues("groupId");
         this.portInfoService.saveGroupId(ids, groupIdsArr, request);
      } catch (Exception var5) {
         logger.error("保存端口标签信息错误", var5);
         this.logInfoService.save("保存端口标签信息错误", var5.toString(), "2");
      }

      return "redirect:/portInfo/list";
   }

   @RequestMapping({"save"})
   public String savePortInfo(PortInfo portInfo, Model model, HttpServletRequest request) {
      String errorMsg = "保存端口信息错误";

      try {
         if (StringUtils.isEmpty(portInfo.getId())) {
            this.portInfoService.save(portInfo, request);
            this.portInfoService.saveLog(request, "添加", portInfo);
         } else {
            this.portInfoService.updateById(portInfo);
            this.portInfoService.saveLog(request, "修改", portInfo);
         }
      } catch (Exception var6) {
         logger.error(errorMsg, var6);
         this.logInfoService.save(errorMsg, var6.toString(), "2");
      }

      return "redirect:/portInfo/list";
   }

   @RequestMapping({"saveBatch"})
   public String saveBatchPortInfo(PortInfo portInfo, Model model, HttpServletRequest request) {
      String errorMsg = "批量保存端口信息错误";

      try {
         String[] hostnames = request.getParameterValues("hostnames");
         if (null == hostnames || hostnames.length < 1) {
            return "redirect:/portInfo/list";
         }

         String[] var6 = hostnames;
         int var7 = hostnames.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String selectedHost = var6[var8];
            portInfo.setHostname(selectedHost);
            this.portInfoService.save(portInfo, request);
            this.portInfoService.saveLog(request, "添加", portInfo);
         }
      } catch (Exception var10) {
         logger.error(errorMsg, var10);
         this.logInfoService.save(errorMsg, var10.toString(), "2");
      }

      return "redirect:/portInfo/list";
   }

   @RequestMapping({"edit"})
   public String edit(Model model, HttpServletRequest request) {
      String errorMsg = "添加端口信息错误";
      String id = request.getParameter("id");
      PortInfo PortInfo = new PortInfo();

      try {
         Map<String, Object> params = new HashMap();
         HostUtil.addAccountquery(request, params);
         List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
         model.addAttribute("systemInfoList", systemInfoList);
         if (StringUtils.isEmpty(id)) {
            model.addAttribute("portInfo", PortInfo);
            if (!this.isAddContinue()) {
               return "redirect:/portInfo/list?liceFlage=1";
            }

            return "port/add";
         }

         PortInfo = this.portInfoService.selectById(id);
         model.addAttribute("portInfo", PortInfo);
      } catch (Exception var8) {
         logger.error(errorMsg, var8);
         this.logInfoService.save(errorMsg, var8.toString(), "2");
      }

      return "port/add";
   }

   @RequestMapping({"editBatch"})
   public String editBatch(Model model, HttpServletRequest request) {
      String errorMsg = "批量添加端口信息错误";
      PortInfo PortInfo = new PortInfo();

      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return "redirect:/portInfo/list?liceFlage=2";
         }

         Map<String, Object> params = new HashMap();
         HostUtil.addAccountquery(request, params);
         List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
         model.addAttribute("systemInfoList", systemInfoList);
         model.addAttribute("portInfo", PortInfo);
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "port/addBatch";
   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "删除端口信息错误";
      new PortInfo();

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            String[] ids = request.getParameter("id").split(",");
            String[] var7 = ids;
            int var8 = ids.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String id = var7[var9];
               PortInfo portInfo = this.portInfoService.selectById(id);
               this.portInfoService.saveLog(request, "删除", portInfo);
            }

            this.portInfoService.deleteById(ids);
         }
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "redirect:/portInfo/list";
   }

   private boolean isAddContinue() {
      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            Map<String, Object> params = new HashMap();
            int dbSize = this.portInfoService.countByParams(params);
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
