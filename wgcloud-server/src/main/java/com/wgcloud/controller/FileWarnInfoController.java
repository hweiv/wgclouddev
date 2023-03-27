package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.FileWarnInfo;
import com.wgcloud.entity.FileWarnState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.FileWarnInfoService;
import com.wgcloud.service.FileWarnStateService;
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
@RequestMapping({"/fileWarnInfo"})
public class FileWarnInfoController {
   private static final Logger logger = LoggerFactory.getLogger(FileWarnInfoController.class);
   @Resource
   private FileWarnInfoService fileWarnInfoService;
   @Resource
   private FileWarnStateService fileWarnStateService;
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private SystemInfoService systemInfoService;
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
      if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
         logger.error("token is error");
         return ResDataUtils.resetErrorJson("token is error");
      } else {
         Map<String, Object> params = new HashMap();
         if (null != agentJsonObject.get("hostname") && !StringUtils.isEmpty(agentJsonObject.get("hostname").toString())) {
            params.put("hostname", agentJsonObject.get("hostname").toString());

            try {
               params.put("active", "1");
               List<FileWarnInfo> FileWarnInfoList = this.fileWarnInfoService.selectAllByParams(params);
               return ResDataUtils.resetSuccessJson(FileWarnInfoList);
            } catch (Exception var5) {
               logger.error("agent获取日志监控信息错误", var5);
               this.logInfoService.save("agent获取日志监控信息错误", var5.toString(), "2");
               return ResDataUtils.resetErrorJson(var5.toString());
            }
         } else {
            return "";
         }
      }
   }

   @ResponseBody
   @RequestMapping({"agentStateListList"})
   public String agentStateListList(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
      if (!StringUtils.isEmpty(checkResult)) {
         return checkResult;
      } else {
         Map<String, Object> params = new HashMap();
         if (null != agentJsonObject.get("fileWarnId") && !StringUtils.isEmpty(agentJsonObject.get("fileWarnId").toString())) {
            params.put("fileWarnId", agentJsonObject.get("fileWarnId").toString());

            try {
               PageInfo pageInfo = this.fileWarnStateService.selectByParams(params, agentJsonObject.getInt("page"), agentJsonObject.getInt("pageSize"));
               JSONObject pageJson = new JSONObject();
               pageJson.set("total", pageInfo.getTotal());
               pageJson.set("pages", pageInfo.getPages());
               pageJson.set("page", agentJsonObject.getInt("page"));
               pageJson.set("pageSize", agentJsonObject.getInt("pageSize"));
               pageJson.set("list", pageInfo.getList());
               return ResDataUtils.resetSuccessJson(pageJson);
            } catch (Exception var7) {
               logger.error("agent获取日志监控状态信息错误", var7);
               this.logInfoService.save("agent获取日志监控状态信息错误", var7.toString(), "2");
               return ResDataUtils.resetErrorJson(var7.toString());
            }
         } else {
            return ResDataUtils.resetErrorJson("Missing require parameters");
         }
      }
   }

   @RequestMapping({"list"})
   public String fileWarnInfoList(FileWarnInfo fileWarnInfo, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         LicenseUtil.maxLicense_10(model, request, fileWarnInfo);
         StringBuffer url = new StringBuffer();
         String hostname = null;
         if (!StringUtils.isEmpty(fileWarnInfo.getHostname())) {
            hostname = fileWarnInfo.getHostname();
            params.put("hostname", hostname.trim());
            url.append("&hostname=").append(hostname);
         }

         if (!StringUtils.isEmpty(fileWarnInfo.getAccount())) {
            params.put("account", fileWarnInfo.getAccount());
            url.append("&account=").append(fileWarnInfo.getAccount());
         }

         if (!StringUtils.isEmpty(fileWarnInfo.getOrderBy())) {
            params.put("orderBy", fileWarnInfo.getOrderBy());
            params.put("orderType", fileWarnInfo.getOrderType());
            url.append("&orderBy=").append(fileWarnInfo.getOrderBy());
            url.append("&orderType=").append(fileWarnInfo.getOrderType());
         }

         HostUtil.addAccountquery(request, params);
         PageInfo<FileWarnInfo> pageInfo = this.fileWarnInfoService.selectByParams(params, fileWarnInfo.getPage(), fileWarnInfo.getPageSize());
         Iterator var8 = pageInfo.getList().iterator();

         while(var8.hasNext()) {
            FileWarnInfo fileWarnInfo1 = (FileWarnInfo)var8.next();
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
               fileWarnInfo1.setAccount(HostUtil.getAccount(fileWarnInfo1.getHostname()));
            }

            fileWarnInfo1.setHostname(fileWarnInfo1.getHostname() + HostUtil.addRemark(fileWarnInfo1.getHostname()));
            if (!StringUtils.isEmpty(fileWarnInfo1.getFileSize())) {
               String fileFormatSize = FormatUtil.bytesFormatUnit(fileWarnInfo1.getFileSize(), "byte");
               fileWarnInfo1.setFileSize(fileFormatSize);
            }
         }

         HostUtil.addAccountListModel(model);
         PageUtil.initPageNumber(pageInfo, model);
         model.addAttribute("pageUrl", "/fileWarnInfo/list?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
         model.addAttribute("fileWarnInfo", fileWarnInfo);
      } catch (Exception var11) {
         logger.error("查询日志监控信息错误", var11);
         this.logInfoService.save("查询日志监控信息错误", var11.toString(), "2");
      }

      return "file/list";
   }

   @RequestMapping({"save"})
   public String saveFileWarnInfo(FileWarnInfo fileWarnInfo, Model model, HttpServletRequest request) {
      try {
         if (StringUtils.isEmpty(fileWarnInfo.getId())) {
            fileWarnInfo.setWarnRows("0");
            fileWarnInfo.setFileSize("0");
            this.fileWarnInfoService.save(fileWarnInfo);
            this.fileWarnInfoService.saveLog(request, "添加", fileWarnInfo);
         } else {
            this.fileWarnInfoService.updateById(fileWarnInfo);
            this.fileWarnInfoService.saveLog(request, "修改", fileWarnInfo);
         }
      } catch (Exception var5) {
         logger.error("保存日志监控错误", var5);
         this.logInfoService.save("保存日志监控错误", var5.toString(), "2");
      }

      return "redirect:/fileWarnInfo/list";
   }

   @RequestMapping({"saveBatch"})
   public String saveBatchFileWarnInfo(FileWarnInfo fileWarnInfo, Model model, HttpServletRequest request) {
      try {
         String[] hostnames = request.getParameterValues("hostnames");
         if (null == hostnames || hostnames.length < 1) {
            return "redirect:/fileWarnInfo/list";
         }

         String[] var5 = hostnames;
         int var6 = hostnames.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String selectedHost = var5[var7];
            fileWarnInfo.setHostname(selectedHost);
            fileWarnInfo.setWarnRows("0");
            fileWarnInfo.setFileSize("0");
            this.fileWarnInfoService.save(fileWarnInfo);
         }
      } catch (Exception var9) {
         logger.error("批量保存日志监控错误", var9);
         this.logInfoService.save("批量保存日志监控错误", var9.toString(), "2");
      }

      return "redirect:/fileWarnInfo/list";
   }

   @RequestMapping({"edit"})
   public String edit(Model model, HttpServletRequest request) {
      String errorMsg = "添加日志监控";
      String id = request.getParameter("id");
      FileWarnInfo fileWarnInfo = new FileWarnInfo();

      try {
         Map<String, Object> paramsAccount = new HashMap();
         HostUtil.addAccountquery(request, paramsAccount);
         List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(paramsAccount);
         model.addAttribute("systemInfoList", systemInfoList);
         if (StringUtils.isEmpty(id)) {
            fileWarnInfo.setFileType("1");
            model.addAttribute("fileWarnInfo", fileWarnInfo);
            if (!this.isAddContinue()) {
               return "redirect:/fileWarnInfo/list?liceFlage=1";
            }

            return "file/add";
         }

         fileWarnInfo = this.fileWarnInfoService.selectById(id);
         model.addAttribute("fileWarnInfo", fileWarnInfo);
      } catch (Exception var8) {
         logger.error(errorMsg, var8);
         this.logInfoService.save(errorMsg, var8.toString(), "2");
      }

      return "file/add";
   }

   @RequestMapping({"editBatch"})
   public String editBatch(Model model, HttpServletRequest request) {
      String errorMsg = "批量添加日志监控";
      FileWarnInfo fileWarnInfo = new FileWarnInfo();

      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return "redirect:/fileWarnInfo/list?liceFlage=2";
         }

         List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(new HashMap());
         model.addAttribute("systemInfoList", systemInfoList);
         fileWarnInfo.setFileType("1");
         model.addAttribute("fileWarnInfo", fileWarnInfo);
      } catch (Exception var6) {
         logger.error(errorMsg, var6);
         this.logInfoService.save(errorMsg, var6.toString(), "2");
      }

      return "file/addBatch";
   }

   @RequestMapping({"view"})
   public String view(Model model, HttpServletRequest request) {
      String errorMsg = "查看日志监控";
      String id = request.getParameter("id");
      new FileWarnInfo();

      try {
         FileWarnInfo fileWarnInfo = this.fileWarnInfoService.selectById(id);
         fileWarnInfo.setHostname(fileWarnInfo.getHostname() + HostUtil.addRemark(fileWarnInfo.getHostname()));
         model.addAttribute("fileWarnInfo", fileWarnInfo);
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "file/view";
   }

   @RequestMapping({"stateList"})
   public String stateList(FileWarnState fileWarnState, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         StringBuffer url = new StringBuffer();
         FileWarnInfo fileWarnInfo = null;
         if (!StringUtils.isEmpty(fileWarnState.getFileWarnId())) {
            fileWarnInfo = this.fileWarnInfoService.selectById(fileWarnState.getFileWarnId());
            params.put("fileWarnId", fileWarnState.getFileWarnId());
            url.append("&fileWarnId=").append(fileWarnState.getFileWarnId());
         }

         PageInfo pageInfo = this.fileWarnStateService.selectByParams(params, fileWarnState.getPage(), fileWarnState.getPageSize());
         PageUtil.initPageNumber(pageInfo, model);
         model.addAttribute("pageUrl", "/fileWarnInfo/stateList?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
         fileWarnInfo.setHostname(fileWarnInfo.getHostname() + HostUtil.addRemark(fileWarnInfo.getHostname()));
         model.addAttribute("fileWarnInfo", fileWarnInfo);
      } catch (Exception var8) {
         logger.error("查询日志监控信息错误", var8);
         this.logInfoService.save("查询日志监控信息错误", var8.toString(), "2");
      }

      return "file/stateList";
   }

   @RequestMapping({"chartExcel"})
   public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
      String errorMsg = "日志监控信息导出excel错误";
      String id = request.getParameter("id");

      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            response.setContentType("text/html;charset=UTF-8");
            response.getOutputStream().write("The module needs to wgcloud-pro version. Please contact us at www.wgstart.com".getBytes());
            return;
         }

         Map<String, Object> params = new HashMap();
         if (StringUtils.isEmpty(id)) {
            response.setContentType("text/html;charset=UTF-8");
            response.getOutputStream().write("Missing require parameters".getBytes());
            return;
         }

         FileWarnInfo fileWarnInfo = this.fileWarnInfoService.selectById(id);
         params.put("fileWarnId", id);
         this.excelExportService.exportFileWarnStateExcel(fileWarnInfo, params, response);
      } catch (Exception var8) {
         logger.error(errorMsg, var8);
         this.logInfoService.save(errorMsg, var8.toString(), "2");
      }

   }

   @RequestMapping({"stateView"})
   public String stateView(FileWarnState fileWarnState, Model model, HttpServletRequest request) {
      new HashMap();

      try {
         fileWarnState = this.fileWarnStateService.selectById(fileWarnState.getId());
         model.addAttribute("fileWarnState", fileWarnState);
         FileWarnInfo fileWarnInfo = this.fileWarnInfoService.selectById(fileWarnState.getFileWarnId());
         fileWarnInfo.setHostname(fileWarnInfo.getHostname() + HostUtil.addRemark(fileWarnInfo.getHostname()));
         model.addAttribute("fileWarnInfo", fileWarnInfo);
      } catch (Exception var6) {
         logger.error("查询日志监控信息详情错误", var6);
         this.logInfoService.save("查询日志监控信息详情错误", var6.toString(), "2");
      }

      return "file/stateView";
   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "删除日志监控信息错误";
      new FileWarnInfo();

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            String[] ids = request.getParameter("id").split(",");
            String[] var7 = ids;
            int var8 = ids.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String id = var7[var9];
               FileWarnInfo FileWarnInfo = this.fileWarnInfoService.selectById(id);
               this.logInfoService.save("删除日志监控：" + FileWarnInfo.getHostname(), "删除日志监控：" + FileWarnInfo.getHostname(), "2");
            }

            this.fileWarnInfoService.deleteById(ids);
         }
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "redirect:/fileWarnInfo/list";
   }

   private boolean isAddContinue() {
      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            Map<String, Object> params = new HashMap();
            int dbSize = this.fileWarnInfoService.countByParams(params);
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
