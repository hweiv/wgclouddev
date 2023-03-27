package com.wgcloud.controller;

import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.IpUtil;
import com.wgcloud.util.MD5Utils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.msg.WarnMailUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/login"})
public class LoginController {
   private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
   private static final String USER_BLOCK = "block";
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private AccountInfoService accountInfoService;
   @Resource
   private CommonConfig commonConfig;

   private void testThread() {
      Runnable runnable = () -> {
         logger.info("LoginCotroller----------testThread");
      };
      ThreadPoolUtil.executor.execute(runnable);
   }

   @RequestMapping({"toLogin"})
   public String toLogin(Model model, HttpServletRequest request) {
      return "login/login";
   }

   @RequestMapping({"loginOut"})
   public String loginOut(Model model, HttpServletRequest request) {
      HttpSession session = request.getSession();
      session.invalidate();
      return "redirect:/login/toLogin";
   }

   @RequestMapping({"login"})
   public String login(Model model, HttpServletRequest request) {
      String userName = request.getParameter("userName");
      String userBlock = (String)StaticKeys.LOGIN_BLOCK_MAP.get(userName);
      if ("block".equals(userBlock)) {
         model.addAttribute("error", "请10分钟后登录该账号或者联系管理员");
         return "login/login";
      } else {
         String passwd = request.getParameter("md5pwd");
         HttpSession var6 = request.getSession();

         try {
            if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(passwd)) {
               passwd = passwd.toLowerCase();
               AccountInfo accountInfo = new AccountInfo();
               if (MD5Utils.GetMD5Code(this.commonConfig.getAccountPwd()).equals(passwd) && this.commonConfig.getAccount().equals(userName)) {
                  accountInfo.setAccount(userName);
                  accountInfo.setId(userName);
                  accountInfo.setRole("admin");
                  request.getSession().setAttribute("LOGIN_KEY", accountInfo);
                  return "redirect:/dash/main";
               }

               if (MD5Utils.GetMD5Code(this.commonConfig.getGuestAccountPwd()).equals(passwd) && this.commonConfig.getGuestAccount().equals(userName) && StaticKeys.LICENSE_STATE.equals("1")) {
                  accountInfo.setAccount(userName);
                  accountInfo.setId(userName);
                  accountInfo.setRole("guest");
                  request.getSession().setAttribute("LOGIN_KEY", accountInfo);
                  return "redirect:/dash/main";
               }

               if (StaticKeys.LICENSE_STATE.equals("1") && "true".equals(this.commonConfig.getUserInfoManage())) {
                  Map<String, Object> params = new HashMap();
                  params.put("account", userName);
                  params.put("passwd", passwd);
                  List<AccountInfo> userList = this.accountInfoService.selectAllByParams(params);
                  if (userList.size() > 0) {
                     accountInfo.setAccount(userName);
                     accountInfo.setId(userName);
                     accountInfo.setRole("user");
                     request.getSession().setAttribute("LOGIN_KEY", accountInfo);
                     return "redirect:/dash/main";
                  }
               }
            }
         } catch (Exception var10) {
            logger.error("登录异常", var10);
         }

         model.addAttribute("error", "账号或者密码错误");
         this.loginErrorHandle(request, model);
         return "login/login";
      }
   }

   private void loginErrorHandle(HttpServletRequest request, Model model) {
      String requestIp = IpUtil.getIpAddr(request);
      String userName = request.getParameter("userName");
      Integer errorCount = (Integer)StaticKeys.LOGIN_ERROR_MAP.get(userName);
      if (errorCount != null) {
         StaticKeys.LOGIN_ERROR_MAP.put(userName, errorCount + 1);
      } else {
         errorCount = 1;
         StaticKeys.LOGIN_ERROR_MAP.put(userName, errorCount);
      }

      if (errorCount >= 2) {
         model.addAttribute("error", "账号或密码错误，若连续5次输入错误，需等10分钟后才能再登录该账号");
      }

      if (errorCount >= 5) {
         StaticKeys.LOGIN_BLOCK_MAP.put(userName, "block");
         String errMsg = userName + "密码已连续5次输入错误，10分钟内禁止登录，登录IP：" + requestIp;
         Runnable runnable = () -> {
            try {
               WarnMailUtil.sendUtil(errMsg, errMsg, userName, userName + "_longError", true);
            } catch (Exception var3) {
               var3.printStackTrace();
            }

         };
         ThreadPoolUtil.executor.execute(runnable);
         model.addAttribute("error", "密码已连续5次输入错误，请10分钟后再登录该账号");
         logger.error(errMsg);
      }

   }
}
