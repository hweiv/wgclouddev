package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.ShellInfo;
import com.wgcloud.entity.ShellState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.ShellInfoService;
import com.wgcloud.service.ShellStateService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.Date;
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
@RequestMapping({"/shellInfo"})
public class ShellInfoController {
   private static final Logger logger = LoggerFactory.getLogger(ShellInfoController.class);
   @Resource
   private ShellInfoService shellInfoService;
   @Resource
   private ShellStateService shellStateService;
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
      } else if (!"true".equals(this.commonConfig.getShellToRun())) {
         return ResDataUtils.resetErrorJson("shellToRun is no open");
      } else {
         Map<String, Object> params = new HashMap();
         if (null != agentJsonObject.get("hostname") && !StringUtils.isEmpty(agentJsonObject.get("hostname").toString())) {
            params.put("hostname", agentJsonObject.get("hostname").toString());

            try {
               params.put("state", "1");
               params.put("shellTime", System.currentTimeMillis());
               List<ShellState> shellStateList = this.shellStateService.selectAllByParams(params);
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
               List<ShellState> shellStateListResult = new ArrayList();
               Iterator var10 = shellStateList.iterator();

               while(var10.hasNext()) {
                  ShellState state = (ShellState)var10.next();
                  blockKey = FormatUtil.haveBlockDanger(state.getShell(), shellToRunBlock, "");
                  if (!StringUtils.isEmpty(blockKey)) {
                     logger.info(state.getShell() + "下发指令含有敏感字符" + blockKey + "，不进行下发");
                  } else {
                     state.setShell(state.getShell().replaceAll("\\r\\n", cmdSplitChar));
                     shellStateListResult.add(state);
                     this.shellStateService.updateSendByIds(state.getId().split(","));
                  }
               }

               return ResDataUtils.resetSuccessJson(shellStateListResult);
            } catch (Exception var12) {
               logger.error("agent获取下发指令信息错误", var12);
               this.logInfoService.save("agent获取下发指令信息错误", var12.toString(), "2");
               return ResDataUtils.resetErrorJson(var12.toString());
            }
         } else {
            return "";
         }
      }
   }

   @ResponseBody
   @RequestMapping({"/shellCallback"})
   public String appCallback(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      logger.info("shellCallback-------------" + agentJsonObject.toString());
      if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
         logger.error("token is error");
         return ResDataUtils.resetErrorJson("token is error");
      } else {
         try {
            String shellStateId = agentJsonObject.getStr("id");
            if (StringUtils.isEmpty(shellStateId)) {
               logger.error("shellStateId is null");
               return ResDataUtils.resetErrorJson("shellStateId is null");
            } else {
               String shellState = agentJsonObject.getStr("state");
               if (!StringUtils.isEmpty(shellState) && shellState.length() > 500) {
                  shellState = shellState.substring(0, 500);
               }

               ShellState shellStateCall = this.shellStateService.selectById(shellStateId);
               shellStateCall.setState(shellState);
               shellStateCall.setCreateTime(new Date());
               this.shellStateService.updateById(shellStateCall);
               return ResDataUtils.resetSuccessJson((Object)null);
            }
         } catch (Exception var6) {
            logger.error("agent执行指令完成回调错误", var6);
            return ResDataUtils.resetErrorJson(var6.toString());
         }
      }
   }

   @RequestMapping({"list"})
   public String shellInfoList(ShellInfo ShellInfo, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         LicenseUtil.maxLicense_10(model, request, ShellInfo);
         StringBuffer url = new StringBuffer();
         String shellName = null;
         if (!StringUtils.isEmpty(ShellInfo.getShellName())) {
            shellName = ShellInfo.getShellName();
            params.put("shellName", shellName.trim());
            url.append("&shellName=").append(shellName);
         }

         if (!StringUtils.isEmpty(ShellInfo.getAccount())) {
            params.put("account", ShellInfo.getAccount());
            url.append("&account=").append(ShellInfo.getAccount());
         }

         if (!StringUtils.isEmpty(ShellInfo.getState())) {
            params.put("state", ShellInfo.getState());
            url.append("&state=").append(ShellInfo.getState());
         }

         HostUtil.addAccountquery(request, params);
         PageInfo<ShellInfo> pageInfo = this.shellInfoService.selectByParams(params, ShellInfo.getPage(), ShellInfo.getPageSize());
         Map<String, Object> paramsStateCount = new HashMap();
         Iterator var9 = pageInfo.getList().iterator();

         while(var9.hasNext()) {
            ShellInfo shellInfo1 = (ShellInfo)var9.next();
            paramsStateCount.clear();
            paramsStateCount.put("shellId", shellInfo1.getId());
            Integer stateAllCount = this.shellStateService.countByParams(paramsStateCount);
            paramsStateCount.put("state", "1");
            Integer state1Count = this.shellStateService.countByParams(paramsStateCount);
            shellInfo1.setState1Count(state1Count);
            shellInfo1.setStateOtherCount(stateAllCount - state1Count);
            if (state1Count == 0 && !"2".equals(shellInfo1.getState())) {
               shellInfo1.setState("3");
            }
         }

         PageUtil.initPageNumber(pageInfo, model);
         HostUtil.addAccountListModel(model);
         model.addAttribute("pageUrl", "/shellInfo/list?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
         model.addAttribute("shellInfo", ShellInfo);
      } catch (Exception var13) {
         logger.error("查询下发指令信息错误", var13);
         this.logInfoService.save("查询下发指令信息错误", var13.toString(), "2");
      }

      return "shellInfo/list";
   }

   @RequestMapping({"save"})
   public String saveShellInfo(ShellInfo shellInfo, Model model, HttpServletRequest request) {
      if (!"true".equals(this.commonConfig.getShellToRun())) {
         return ResDataUtils.resetErrorJson("shellToRun is no open");
      } else {
         String[] selectedHostnames = request.getParameterValues("hostnames");

         try {
            String blockKey = FormatUtil.haveBlockDanger(shellInfo.getShell(), this.commonConfig.getShellToRunLinuxBlock(), this.commonConfig.getShellToRunWinBlock());
            if (!StringUtils.isEmpty(blockKey)) {
               model.addAttribute("shellInfo", shellInfo);
               model.addAttribute("selectedHosts", selectedHostnames);
               List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(new HashMap());
               Iterator var15 = systemInfoList.iterator();

               while(var15.hasNext()) {
                  SystemInfo systemInfo = (SystemInfo)var15.next();
                  String[] var9 = selectedHostnames;
                  int var10 = selectedHostnames.length;

                  for(int var11 = 0; var11 < var10; ++var11) {
                     String selectedHost = var9[var11];
                     if (selectedHost.equals(systemInfo.getHostname())) {
                        systemInfo.setSelected("selected");
                     }
                  }
               }

               model.addAttribute("systemInfoList", systemInfoList);
               model.addAttribute("msg", "下发指令含有敏感字符" + blockKey + "，请检查");
               this.shellInfoService.getBlockStr(model);
               return "shellInfo/add";
            }

            if ("2".equals(shellInfo.getShellType()) && !StringUtils.isEmpty(shellInfo.getShellTime())) {
               shellInfo.setShellTime(shellInfo.getShellTime() + ":00");
            }

            if (StringUtils.isEmpty(shellInfo.getId())) {
               if (null == selectedHostnames || selectedHostnames.length < 1) {
                  return "redirect:/shellInfo/list";
               }

               AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
               if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
                  shellInfo.setAccount(accountInfo.getAccount());
               }

               this.shellInfoService.save(shellInfo, selectedHostnames);
               Runnable runnable = () -> {
                  try {
                     if (shellInfo != null) {
                        WarnOtherUtil.sendShellInfo(shellInfo, "开始下发指令");
                     }
                  } catch (Exception var2) {
                     var2.printStackTrace();
                  }

               };
               ThreadPoolUtil.executor.execute(runnable);
            } else {
               this.shellInfoService.updateById(shellInfo);
            }
         } catch (Exception var13) {
            logger.error("保存下发指令错误", var13);
            this.logInfoService.save("保存下发指令错误", var13.toString(), "2");
         }

         return "redirect:/shellInfo/list";
      }
   }

   @RequestMapping({"edit"})
   public String edit(Model model, HttpServletRequest request) {
      String errorMsg = "添加下发指令";
      String id = request.getParameter("id");
      ShellInfo shellInfo = new ShellInfo();

      try {
         Map<String, Object> paramsAccount = new HashMap();
         HostUtil.addAccountquery(request, paramsAccount);
         List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(paramsAccount);
         model.addAttribute("systemInfoList", systemInfoList);
         this.shellInfoService.getBlockStr(model);
         if (StringUtils.isEmpty(id)) {
            model.addAttribute("shellInfo", shellInfo);
            if (!this.isAddContinue()) {
               return "redirect:/shellInfo/list?liceFlage=1";
            }

            return "shellInfo/add";
         }

         shellInfo = this.shellInfoService.selectById(id);
         model.addAttribute("shellInfo", shellInfo);
      } catch (Exception var8) {
         logger.error(errorMsg, var8);
         this.logInfoService.save(errorMsg, var8.toString(), "2");
      }

      return "shellInfo/add";
   }

   @RequestMapping({"view"})
   public String view(Model model, HttpServletRequest request) {
      String errorMsg = "查看指令下发执行状态信息错误";
      String id = request.getParameter("id");
      new ShellInfo();

      try {
         ShellInfo ShellInfo = this.shellInfoService.selectById(id);
         Map<String, Object> params = new HashMap();
         params.put("shellId", id);
         List<ShellState> shellStateList = this.shellStateService.selectAllByParams(params);
         Iterator var8 = shellStateList.iterator();

         while(var8.hasNext()) {
            ShellState shellState1 = (ShellState)var8.next();
            shellState1.setHostname(shellState1.getHostname() + HostUtil.addRemark(shellState1.getHostname()));
         }

         model.addAttribute("shellStateList", shellStateList);
         model.addAttribute("shellInfo", ShellInfo);
      } catch (Exception var10) {
         logger.error(errorMsg, var10);
         this.logInfoService.save(errorMsg, var10.toString(), "2");
      }

      return "shellInfo/stateList";
   }

   @ResponseBody
   @RequestMapping({"cancel"})
   public String cancel(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "取消下发指令信息错误";

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            ShellInfo shellInfo = this.shellInfoService.selectById(request.getParameter("id"));
            this.shellInfoService.cancelShell(request.getParameter("id"));
            Runnable runnable = () -> {
               try {
                  if (shellInfo != null) {
                     WarnOtherUtil.sendShellInfo(shellInfo, "取消下发指令");
                  }
               } catch (Exception var2) {
                  var2.printStackTrace();
               }

            };
            ThreadPoolUtil.executor.execute(runnable);
         }
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return ResDataUtils.resetSuccessJson((Object)null);
   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "删除下发执行信息错误";

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            String[] ids = request.getParameter("id").split(",");
            String[] var6 = ids;
            int var7 = ids.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String id = var6[var8];
               ShellInfo shellInfo = this.shellInfoService.selectById(id);
               Runnable runnable = () -> {
                  try {
                     if (shellInfo != null) {
                        WarnOtherUtil.sendShellInfo(shellInfo, "删除下发指令");
                     }
                  } catch (Exception var2) {
                     var2.printStackTrace();
                  }

               };
               ThreadPoolUtil.executor.execute(runnable);
            }

            this.shellInfoService.deleteById(ids);
         }
      } catch (Exception var12) {
         logger.error(errorMsg, var12);
         this.logInfoService.save(errorMsg, var12.toString(), "2");
      }

      return "redirect:/shellInfo/list";
   }

   private boolean isAddContinue() {
      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            Map<String, Object> params = new HashMap();
            int dbSize = this.shellInfoService.countByParams(params);
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
