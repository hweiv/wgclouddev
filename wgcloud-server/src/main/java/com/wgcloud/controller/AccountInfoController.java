package com.wgcloud.controller;

import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.MD5Utils;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"/accountInfo"})
public class AccountInfoController {
   private static final Logger logger = LoggerFactory.getLogger(AccountInfoController.class);
   @Resource
   private AccountInfoService accountInfoService;
   @Resource
   private SystemInfoService systemInfoService;
   @Resource
   private LogInfoService logInfoService;

   @RequestMapping({"list"})
   public String AccountInfoList(AccountInfo accountInfo, Model model, HttpServletRequest request) {
      String errorMsg = "查询成员账号信息错误";
      if (!StaticKeys.LICENSE_STATE.equals("1")) {
         return "daping/error";
      } else {
         HashMap params = new HashMap();

         try {
            PageInfo<AccountInfo> pageInfo = this.accountInfoService.selectByParams(params, Integer.valueOf(accountInfo.getPage()), Integer.valueOf(accountInfo.getPageSize()));
            new HashMap();
            Iterator var8 = pageInfo.getList().iterator();

            while(var8.hasNext()) {
               AccountInfo accountInfo1 = (AccountInfo)var8.next();
               Map<String, Object> paramsAccount = new HashMap();
               paramsAccount.put("account", accountInfo1.getAccount());
               Integer hostNum = this.systemInfoService.countByParams(paramsAccount);
               accountInfo1.setHostNum(hostNum);
            }

            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("page", pageInfo);
            model.addAttribute("pageUrl", "/accountInfo/list?1=1");
            model.addAttribute("accountInfo", accountInfo);
            model.addAttribute("accountAllList", this.accountInfoService.selectAllByParams(new HashMap()));
         } catch (Exception var12) {
            logger.error(errorMsg, var12);
            this.logInfoService.save(errorMsg, var12.toString(), "2");
         }

         return "accountInfo/list";
      }
   }

   @RequestMapping({"editPasswd"})
   public String editPasswd(Model model, HttpServletRequest request) {
      String errorMsg = "修改成员密码错误";
      if (!StaticKeys.LICENSE_STATE.equals("1")) {
         return "error/guestError";
      } else {
         String id = request.getParameter("id");
         new AccountInfo();

         try {
            AccountInfo accountInfo = this.accountInfoService.selectById(id);
            model.addAttribute("accountInfo", accountInfo);
         } catch (Exception var7) {
            logger.error(errorMsg, var7);
            this.logInfoService.save(errorMsg, var7.toString(), "2");
         }

         return "accountInfo/passwd";
      }
   }

   @RequestMapping({"edit"})
   public String edit(Model model, HttpServletRequest request) {
      String errorMsg = "添加成员账号信息错误";
      if (!StaticKeys.LICENSE_STATE.equals("1")) {
         return "error/guestError";
      } else {
         String id = request.getParameter("id");
         AccountInfo accountInfo = new AccountInfo();

         try {
            Map<String, Object> paramsAccount = new HashMap();
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(paramsAccount);
            List<SystemInfo> systemInfoListResult = new ArrayList();
            Iterator var9;
            SystemInfo systemInfo;
            if (StringUtils.isEmpty(id)) {
               model.addAttribute("accountInfo", accountInfo);
               var9 = systemInfoList.iterator();

               while(var9.hasNext()) {
                  systemInfo = (SystemInfo)var9.next();
                  if (StringUtils.isEmpty(systemInfo.getAccount())) {
                     systemInfoListResult.add(systemInfo);
                  }
               }

               model.addAttribute("systemInfoList", systemInfoListResult);
               return "accountInfo/add";
            }

            accountInfo = this.accountInfoService.selectById(id);
            var9 = systemInfoList.iterator();

            while(var9.hasNext()) {
               systemInfo = (SystemInfo)var9.next();
               if (StringUtils.isEmpty(systemInfo.getAccount())) {
                  systemInfoListResult.add(systemInfo);
               }

               if (!StringUtils.isEmpty(accountInfo.getAccount()) && accountInfo.getAccount().equals(systemInfo.getAccount())) {
                  systemInfoListResult.add(systemInfo);
               }
            }

            model.addAttribute("systemInfoList", systemInfoListResult);
            var9 = systemInfoList.iterator();

            while(var9.hasNext()) {
               systemInfo = (SystemInfo)var9.next();
               if (accountInfo.getAccount().equals(systemInfo.getAccount())) {
                  systemInfo.setSelected("selected");
               }
            }

            model.addAttribute("accountInfo", accountInfo);
         } catch (Exception var11) {
            logger.error(errorMsg, var11);
            this.logInfoService.save(errorMsg, var11.toString(), "2");
         }

         return "accountInfo/add";
      }
   }

   @RequestMapping({"save"})
   public String saveAccountInfo(AccountInfo accountInfo, Model model, HttpServletRequest request) {
      if (!StaticKeys.LICENSE_STATE.equals("1")) {
         return "error/guestError";
      } else {
         if (!StringUtils.isEmpty(accountInfo.getPasswd())) {
            accountInfo.setPasswd(MD5Utils.GetMD5Code(accountInfo.getPasswd()));
         }

         try {
            String[] hostnames = request.getParameterValues("hostnames");
            if (StringUtils.isEmpty(accountInfo.getId())) {
               this.accountInfoService.save(accountInfo);
               this.accountInfoService.saveLog(request, "添加", accountInfo);
            } else {
               this.accountInfoService.updateById(accountInfo);
               this.accountInfoService.saveLog(request, "修改", accountInfo);
            }

            this.accountInfoService.updateHostAccount(accountInfo, hostnames);
         } catch (Exception var5) {
            logger.error("保存成员账号信息错误：", var5);
            this.logInfoService.save("保存成员账号信息错误", "保存成员信息错误：" + var5.toString(), "2");
         }

         return "redirect:/accountInfo/list";
      }
   }

   @RequestMapping({"savePasswd"})
   public String savePasswd(AccountInfo accountInfo, Model model, HttpServletRequest request) {
      if (!StaticKeys.LICENSE_STATE.equals("1")) {
         return "error/guestError";
      } else {
         if (!StringUtils.isEmpty(accountInfo.getPasswd())) {
            accountInfo.setPasswd(MD5Utils.GetMD5Code(accountInfo.getPasswd()));
         }

         try {
            if (!StringUtils.isEmpty(accountInfo.getId())) {
               this.accountInfoService.updateById(accountInfo);
               this.accountInfoService.saveLog(request, "修改", accountInfo);
            }
         } catch (Exception var5) {
            logger.error("修改成员账号密码错误：", var5);
            this.logInfoService.save("修改成员账号密码错误", "修改成员账号密码错误：" + var5.toString(), "2");
         }

         return "redirect:/accountInfo/list";
      }
   }

   @ResponseBody
   @RequestMapping({"moveToAccount"})
   public String moveToAccount(Model model, HttpServletRequest request) {
      try {
         String accountSource = request.getParameter("ids");
         String accountTarget = request.getParameter("account");
         this.accountInfoService.moveToAccount(accountTarget, accountSource);
      } catch (Exception var5) {
         logger.error("迁移账号资源错误", var5);
         this.logInfoService.save("迁移账号资源错误", var5.toString(), "2");
      }

      return "redirect:/accountInfo/list";
   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      if (!StaticKeys.LICENSE_STATE.equals("1")) {
         return "error/guestError";
      } else {
         String errorMsg = "删除成员账号错误";

         try {
            if (!StringUtils.isEmpty(request.getParameter("id"))) {
               String[] ids = request.getParameter("id").split(",");
               String[] var6 = ids;
               int var7 = ids.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  String id = var6[var8];
                  AccountInfo accountInfo = this.accountInfoService.selectById(id);
                  Map<String, Object> params = new HashMap();
                  params.put("account", accountInfo.getAccount());
                  this.accountInfoService.clearOthersAccount(params);
                  this.accountInfoService.deleteById(request.getParameter("id").split(","));
                  this.accountInfoService.saveLog(request, "删除", accountInfo);
               }
            }
         } catch (Exception var12) {
            logger.error(errorMsg, var12);
            this.logInfoService.save(errorMsg, var12.toString(), "2");
         }

         return "redirect:/accountInfo/list";
      }
   }
}
