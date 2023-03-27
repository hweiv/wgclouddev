package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.LogInfo;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.TokenUtils;
import java.util.HashMap;
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

@Controller
@RequestMapping({"/log"})
public class LogInfoController {
   private static final Logger logger = LoggerFactory.getLogger(LogInfoController.class);
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
            if (null != agentJsonObject.get("hostname") && !StringUtils.isEmpty(agentJsonObject.get("hostname").toString())) {
               params.put("hostname", agentJsonObject.get("hostname").toString().trim());
            }

            if (null != agentJsonObject.get("startTime")) {
               params.put("startTime", agentJsonObject.get("startTime").toString().trim());
            }

            if (null != agentJsonObject.get("endTime")) {
               params.put("endTime", agentJsonObject.get("endTime").toString().trim());
            }

            if (null != agentJsonObject.get("state")) {
               params.put("state", agentJsonObject.get("state").toString().trim());
            }

            PageInfo logInfoList = this.logInfoService.selectByParams(params, agentJsonObject.getInt("page"), agentJsonObject.getInt("pageSize"));
            return ResDataUtils.resetSuccessJson(logInfoList);
         } catch (Exception var6) {
            logger.error("agent获取系统日志信息列表错误", var6);
            this.logInfoService.save("agent获取系统日志信息列表错误", var6.toString(), "2");
            return ResDataUtils.resetErrorJson(var6.toString());
         }
      }
   }

   @RequestMapping({"list"})
   public String LogInfoList(LogInfo logInfo, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         StringBuffer url = new StringBuffer();
         String startTime = request.getParameter("startTime");
         String endTime = request.getParameter("endTime");
         String hostname = null;
         if (!StringUtils.isEmpty(logInfo.getHostname())) {
            hostname = logInfo.getHostname();
            params.put("hostname", hostname.trim());
            url.append("&hostname=").append(hostname);
         }

         if (!StringUtils.isEmpty(startTime)) {
            params.put("startTime", startTime + " 00:00:00");
            url.append("&startTime=").append(startTime);
            model.addAttribute("startTime", startTime);
         }

         if (!StringUtils.isEmpty(endTime)) {
            params.put("endTime", endTime + " 23:59:59");
            url.append("&endTime=").append(endTime);
            model.addAttribute("endTime", endTime);
         }

         if (!StringUtils.isEmpty(logInfo.getState())) {
            params.put("state", logInfo.getState());
            url.append("&state=").append(logInfo.getState());
         }

         PageInfo pageInfo = this.logInfoService.selectByParams(params, logInfo.getPage(), logInfo.getPageSize());
         PageUtil.initPageNumber(pageInfo, model);
         model.addAttribute("pageUrl", "/log/list?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
         model.addAttribute("logInfo", logInfo);
      } catch (Exception var10) {
         logger.error("查询日志列表错误", var10);
      }

      return "log/list";
   }

   @RequestMapping({"view"})
   public String viewLogInfo(Model model, HttpServletRequest request) {
      String id = request.getParameter("id");
      new LogInfo();

      try {
         LogInfo logInfo = this.logInfoService.selectById(id);
         model.addAttribute("logInfo", logInfo);
      } catch (Exception var6) {
         logger.error("查看日志信息错误", var6);
      }

      return "log/view";
   }
}
