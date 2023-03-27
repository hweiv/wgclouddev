package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.FileSafe;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.FileSafeService;
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
@RequestMapping({"/fileSafe"})
public class FileSafeController {
   private static final Logger logger = LoggerFactory.getLogger(FileSafeController.class);
   @Resource
   private FileSafeService fileSafeService;
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private SystemInfoService systemInfoService;
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
               List<FileSafe> FileSafeList = this.fileSafeService.selectAllByParams(params);
               return ResDataUtils.resetSuccessJson(FileSafeList);
            } catch (Exception var5) {
               logger.error("agent获取文件防篡改信息错误", var5);
               this.logInfoService.save("agent获取文件防篡改信息错误", var5.toString(), "2");
               return ResDataUtils.resetErrorJson(var5.toString());
            }
         } else {
            return "";
         }
      }
   }

   @RequestMapping({"list"})
   public String fileSafeList(FileSafe fileSafe, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         LicenseUtil.maxLicense_10(model, request, fileSafe);
         StringBuffer url = new StringBuffer();
         String hostname = null;
         if (!StringUtils.isEmpty(fileSafe.getHostname())) {
            hostname = fileSafe.getHostname();
            params.put("hostname", hostname.trim());
            url.append("&hostname=").append(hostname);
         }

         if (!StringUtils.isEmpty(fileSafe.getAccount())) {
            params.put("account", fileSafe.getAccount());
            url.append("&account=").append(fileSafe.getAccount());
         }

         if (!StringUtils.isEmpty(fileSafe.getState())) {
            params.put("state", fileSafe.getState());
            url.append("&state=").append(fileSafe.getState());
         }

         if (!StringUtils.isEmpty(fileSafe.getOrderBy())) {
            params.put("orderBy", fileSafe.getOrderBy());
            params.put("orderType", fileSafe.getOrderType());
            url.append("&orderBy=").append(fileSafe.getOrderBy());
            url.append("&orderType=").append(fileSafe.getOrderType());
         }

         HostUtil.addAccountquery(request, params);
         PageInfo<FileSafe> pageInfo = this.fileSafeService.selectByParams(params, fileSafe.getPage(), fileSafe.getPageSize());
         Iterator var8 = pageInfo.getList().iterator();

         while(var8.hasNext()) {
            FileSafe fileSafe1 = (FileSafe)var8.next();
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
               fileSafe1.setAccount(HostUtil.getAccount(fileSafe1.getHostname()));
            }

            fileSafe1.setHostname(fileSafe1.getHostname() + HostUtil.addRemark(fileSafe1.getHostname()));
            if ("true".equals(this.commonConfig.getShowWarnCount())) {
               String warnQueryWd = fileSafe1.getFileName() + "，" + fileSafe1.getHostname();
               this.logInfoService.warnQueryHandle(fileSafe1, warnQueryWd);
            }
         }

         HostUtil.addAccountListModel(model);
         PageUtil.initPageNumber(pageInfo, model);
         model.addAttribute("pageUrl", "/fileSafe/list?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
         model.addAttribute("fileSafe", fileSafe);
      } catch (Exception var11) {
         logger.error("查询文件防篡改信息错误", var11);
         this.logInfoService.save("查询文件防篡改信息错误", var11.toString(), "2");
      }

      return "fileSafe/list";
   }

   @RequestMapping({"save"})
   public String saveFileSafe(FileSafe fileSafe, Model model, HttpServletRequest request) {
      String errorMsg = "保存文件防篡改信息错误";

      try {
         if (StringUtils.isEmpty(fileSafe.getId())) {
            this.fileSafeService.save(fileSafe, request);
            this.fileSafeService.saveLog(request, "添加", fileSafe);
         } else {
            this.fileSafeService.updateById(fileSafe);
            this.fileSafeService.saveLog(request, "修改", fileSafe);
         }
      } catch (Exception var6) {
         logger.error(errorMsg, var6);
         this.logInfoService.save(errorMsg, var6.toString(), "2");
      }

      return "redirect:/fileSafe/list";
   }

   @RequestMapping({"saveBatch"})
   public String saveBatchFileSafe(FileSafe fileSafe, Model model, HttpServletRequest request) {
      String errorMsg = "批量保存文件防篡改信息错误";

      try {
         String[] hostnames = request.getParameterValues("hostnames");
         if (null == hostnames || hostnames.length < 1) {
            return "redirect:/fileSafe/list";
         }

         String[] var6 = hostnames;
         int var7 = hostnames.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String selectedHost = var6[var8];
            fileSafe.setHostname(selectedHost);
            this.fileSafeService.save(fileSafe, request);
            this.fileSafeService.saveLog(request, "添加", fileSafe);
         }
      } catch (Exception var10) {
         logger.error(errorMsg, var10);
         this.logInfoService.save(errorMsg, var10.toString(), "2");
      }

      return "redirect:/fileSafe/list";
   }

   @RequestMapping({"edit"})
   public String edit(Model model, HttpServletRequest request) {
      String errorMsg = "添加文件防篡改信息错误";
      String id = request.getParameter("id");
      FileSafe fileSafe = new FileSafe();

      try {
         Map<String, Object> paramsAccount = new HashMap();
         HostUtil.addAccountquery(request, paramsAccount);
         List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(paramsAccount);
         model.addAttribute("systemInfoList", systemInfoList);
         if (StringUtils.isEmpty(id)) {
            model.addAttribute("fileSafe", fileSafe);
            if (!this.isAddContinue()) {
               return "redirect:/fileSafe/list?liceFlage=1";
            }

            return "fileSafe/add";
         }

         fileSafe = this.fileSafeService.selectById(id);
         model.addAttribute("fileSafe", fileSafe);
      } catch (Exception var8) {
         logger.error(errorMsg, var8);
         this.logInfoService.save(errorMsg, var8.toString(), "2");
      }

      return "fileSafe/add";
   }

   @RequestMapping({"editBatch"})
   public String editBatch(Model model, HttpServletRequest request) {
      String errorMsg = "批量添加文件防篡改信息错误";
      FileSafe fileSafe = new FileSafe();

      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return "redirect:/fileSafe/list?liceFlage=2";
         }

         Map<String, Object> paramsAccount = new HashMap();
         HostUtil.addAccountquery(request, paramsAccount);
         List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(paramsAccount);
         model.addAttribute("systemInfoList", systemInfoList);
         model.addAttribute("fileSafe", fileSafe);
         if (!this.isAddContinue()) {
            return "redirect:/fileSafe/list?liceFlage=1";
         }
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "fileSafe/addBatch";
   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "删除文件防篡改监测信息错误";
      new FileSafe();

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            String[] ids = request.getParameter("id").split(",");
            String[] var7 = ids;
            int var8 = ids.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String id = var7[var9];
               FileSafe fileSafe = this.fileSafeService.selectById(id);
               this.fileSafeService.saveLog(request, "删除", fileSafe);
            }

            this.fileSafeService.deleteById(ids);
         }
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "redirect:/fileSafe/list";
   }

   private boolean isAddContinue() {
      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            Map<String, Object> params = new HashMap();
            int dbSize = this.fileSafeService.countByParams(params);
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
