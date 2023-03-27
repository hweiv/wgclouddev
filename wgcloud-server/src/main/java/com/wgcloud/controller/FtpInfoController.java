package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.MessageDto;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.FtpInfo;
import com.wgcloud.service.FtpInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DESUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.JschUtil;
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
@RequestMapping({"/ftpInfo"})
public class FtpInfoController {
   private static final Logger logger = LoggerFactory.getLogger(FtpInfoController.class);
   @Resource
   private FtpInfoService ftpInfoService;
   @Resource
   private LogInfoService logInfoService;
   @Autowired
   private CommonConfig commonConfig;
   @Autowired
   private TokenUtils tokenUtils;

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
            if (null != agentJsonObject.get("ftpNames") && !StringUtils.isEmpty(agentJsonObject.get("ftpNames").toString())) {
               params.put("ftpNames", agentJsonObject.get("ftpNames").toString().split(","));
            }

            List<FtpInfo> ftpInfoList = this.ftpInfoService.selectAllByParams(params);
            Iterator var6 = ftpInfoList.iterator();

            while(var6.hasNext()) {
               FtpInfo ftpInfo = (FtpInfo)var6.next();
               ftpInfo.setUserName(DESUtil.encryption(ftpInfo.getUserName()));
               ftpInfo.setPasswd(DESUtil.encryption(ftpInfo.getPasswd()));
            }

            ServerBackupUtil.cacheSaveFtpInfoId(ftpInfoList);
            return ResDataUtils.resetSuccessJson(ftpInfoList);
         } catch (Exception var8) {
            logger.error("agent获取监控ftp/sftp列表信息错误", var8);
            this.logInfoService.save("agent获取监控ftp/sftp列表信息错误", var8.toString(), "2");
            return ResDataUtils.resetErrorJson(var8.toString());
         }
      }
   }

   @RequestMapping({"list"})
   public String FtpInfoList(FtpInfo ftpInfo, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         LicenseUtil.maxLicense_10(model, request, ftpInfo);
         StringBuffer url = new StringBuffer();
         if (!StringUtils.isEmpty(ftpInfo.getFtpHost())) {
            params.put("ftpHost", ftpInfo.getFtpHost().trim());
            url.append("&ftpHost=").append(ftpInfo.getFtpHost());
         }

         if (!StringUtils.isEmpty(ftpInfo.getAccount())) {
            params.put("account", ftpInfo.getAccount());
            url.append("&account=").append(ftpInfo.getAccount());
         }

         if (!StringUtils.isEmpty(ftpInfo.getState())) {
            params.put("state", ftpInfo.getState());
            url.append("&state=").append(ftpInfo.getState());
         }

         if (!StringUtils.isEmpty(ftpInfo.getOrderBy())) {
            params.put("orderBy", ftpInfo.getOrderBy());
            params.put("orderType", ftpInfo.getOrderType());
            url.append("&orderBy=").append(ftpInfo.getOrderBy());
            url.append("&orderType=").append(ftpInfo.getOrderType());
         }

         HostUtil.addAccountquery(request, params);
         PageInfo<FtpInfo> pageInfo = this.ftpInfoService.selectByParams(params, ftpInfo.getPage(), ftpInfo.getPageSize());
         Iterator var7 = pageInfo.getList().iterator();

         while(var7.hasNext()) {
            FtpInfo ftpInfo1 = (FtpInfo)var7.next();
            if ("true".equals(this.commonConfig.getShowWarnCount())) {
               String warnQueryWd = ftpInfo1.getFtpHost();
               this.logInfoService.warnQueryHandle(ftpInfo1, warnQueryWd);
            }
         }

         HostUtil.addAccountListModel(model);
         PageUtil.initPageNumber(pageInfo, model);
         model.addAttribute("pageUrl", "/ftpInfo/list?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
         model.addAttribute("ftpInfo", ftpInfo);
      } catch (Exception var10) {
         logger.error("查询ftp监测信息错误", var10);
         this.logInfoService.save("查询ftp监测信息错误", var10.toString(), "2");
      }

      return "ftpInfo/list";
   }

   @RequestMapping({"save"})
   public String saveFtpInfo(FtpInfo ftpInfo, Model model, HttpServletRequest request) {
      String errorMsg = "保存ftp监测信息错误";

      try {
         if (StringUtils.isEmpty(ftpInfo.getWarnType())) {
            ftpInfo.setWarnType("");
         }

         if (StringUtils.isEmpty(ftpInfo.getId())) {
            AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
            if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
               ftpInfo.setAccount(accountInfo.getAccount());
            }

            this.ftpInfoService.save(ftpInfo);
            this.ftpInfoService.saveLog(request, "添加", ftpInfo);
         } else {
            this.ftpInfoService.updateById(ftpInfo);
            this.ftpInfoService.saveLog(request, "修改", ftpInfo);
         }
      } catch (Exception var6) {
         logger.error(errorMsg, var6);
         this.logInfoService.save(errorMsg, var6.toString(), "2");
      }

      return "redirect:/ftpInfo/list";
   }

   @RequestMapping({"edit"})
   public String edit(Model model, HttpServletRequest request) {
      String errorMsg = "添加ftp监测信息错误";
      String id = request.getParameter("id");
      FtpInfo ftpInfo = new FtpInfo();

      try {
         Map<String, Object> params = new HashMap();
         HostUtil.addAccountquery(request, params);
         if (StringUtils.isEmpty(id)) {
            if (!this.isAddContinue()) {
               return "redirect:/ftpInfo/list?liceFlage=1";
            }

            ftpInfo.setPort("22");
            model.addAttribute("ftpInfo", ftpInfo);
            return "ftpInfo/add";
         }

         ftpInfo = this.ftpInfoService.selectById(id);
         model.addAttribute("ftpInfo", ftpInfo);
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "ftpInfo/add";
   }

   @ResponseBody
   @RequestMapping({"test"})
   public String test(FtpInfo ftpInfo, Model model, HttpServletRequest request) {
      MessageDto messageDto = new MessageDto();

      try {
         if ("SFTP".equals(ftpInfo.getFtpType())) {
            JschUtil.testSFtpSession(ftpInfo);
         } else {
            JschUtil.testFTPClient(ftpInfo);
         }

         if ("1".equals(ftpInfo.getState())) {
            messageDto.setCode("0");
            messageDto.setMsg("连接测试成功");
         } else {
            messageDto.setCode("1");
            messageDto.setMsg("连接测试失败，请检查参数是否正确。请在系统信息里查看日志");
         }
      } catch (Exception var6) {
         logger.error("测试ftp设置信息错误", var6);
         this.logInfoService.save("测试ftp设置信息错误", var6.toString(), "2");
      }

      return JSONUtil.toJsonStr(messageDto);
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
            this.ftpInfoService.updateActive(params);
         }
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "redirect:/ftpInfo/list";
   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "删除ftp监测信息错误";
      new FtpInfo();

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            String[] ids = request.getParameter("id").split(",");
            String[] var7 = ids;
            int var8 = ids.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String id = var7[var9];
               FtpInfo ftpInfo = this.ftpInfoService.selectById(id);
               this.ftpInfoService.saveLog(request, "删除", ftpInfo);
            }

            this.ftpInfoService.deleteById(ids);
         }
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "redirect:/ftpInfo/list";
   }

   private boolean isAddContinue() {
      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            Map<String, Object> params = new HashMap();
            int dbSize = this.ftpInfoService.countByParams(params);
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
