package com.wgcloud.controller;

import com.wgcloud.entity.MailSet;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.MailSetService;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"/mailset"})
public class MailSetController {
   private static final Logger logger = LoggerFactory.getLogger(MailSetController.class);
   @Resource
   private MailSetService mailSetService;
   @Resource
   private LogInfoService logInfoService;

   @RequestMapping({"list"})
   public String MailSetList(MailSet MailSet, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         List<MailSet> list = this.mailSetService.selectAllByParams(params);
         if (list.size() > 0) {
            model.addAttribute("mailSet", list.get(0));
         }
      } catch (Exception var7) {
         logger.error("查询邮件设置错误", var7);
         this.logInfoService.save("查询邮件设置错误", var7.toString(), "2");
      }

      String msg = request.getParameter("msg");
      if (!StringUtils.isEmpty(msg)) {
         if ("save".equals(msg)) {
            model.addAttribute("msg", "保存成功");
         } else if ("test".equals(msg)) {
            String result = request.getParameter("result");
            if ("success".equals(result)) {
               model.addAttribute("msg", "测试发送成功");
            } else {
               model.addAttribute("msg", "测试发送失败，请查看日志");
            }
         } else {
            model.addAttribute("msg", "删除成功");
         }
      } else {
         model.addAttribute("msg", "");
      }

      return "mail/view";
   }

   @RequestMapping({"save"})
   public String saveMailSet(MailSet mailSet, Model model, HttpServletRequest request) {
      try {
         if (StringUtils.isEmpty(mailSet.getId())) {
            this.mailSetService.save(mailSet);
            this.mailSetService.saveLog(request, "添加", mailSet);
         } else {
            this.mailSetService.updateById(mailSet);
            this.mailSetService.saveLog(request, "修改", mailSet);
         }
      } catch (Exception var5) {
         logger.error("保存邮件设置信息错误", var5);
         this.logInfoService.save("保存邮件设置信息错误", var5.toString(), "2");
      }

      return "redirect:/mailset/list?msg=save";
   }

   @RequestMapping({"test"})
   public String test(MailSet mailSet, Model model, HttpServletRequest request) {
      String result = "success";

      try {
         if (StringUtils.isEmpty(mailSet.getId())) {
            this.mailSetService.save(mailSet);
            this.mailSetService.saveLog(request, "添加", mailSet);
         } else {
            this.mailSetService.updateById(mailSet);
            this.mailSetService.saveLog(request, "修改", mailSet);
         }

         StaticKeys.mailSet = mailSet;
         result = WarnOtherUtil.sendMail(mailSet.getToMail(), "WGCLOUD测试邮件发送", "WGCLOUD测试邮件发送");
      } catch (Exception var6) {
         logger.error("测试邮件设置信息错误", var6);
         this.logInfoService.save("测试邮件设置信息错误", var6.toString(), "2");
      }

      return "redirect:/mailset/list?msg=test&result=" + result;
   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "删除告警邮件设置错误";

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            this.mailSetService.deleteById(request.getParameter("id").split(","));
            this.mailSetService.saveLog(request, "删除", new MailSet());
         }
      } catch (Exception var6) {
         logger.error(errorMsg, var6);
         this.logInfoService.save(errorMsg, var6.toString(), "2");
      }

      return "redirect:/mailset/list?msg=del";
   }
}
