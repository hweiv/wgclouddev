package com.wgcloud.controller;

import com.wgcloud.config.CommonConfig;
import com.wgcloud.util.RestUtil;
import com.wgcloud.util.msg.WarnPools;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/warnInfo"})
public class WarnInfoController {
   private static final Logger logger = LoggerFactory.getLogger(WarnInfoController.class);
   @Autowired
   private RestUtil restUtil;
   @Autowired
   private CommonConfig commonConfig;

   @ResponseBody
   @RequestMapping({"warnCountAjaxHandle"})
   public String warnCountAjaxHandle(Model model, HttpServletRequest request) {
      String timerWarnSound = request.getParameter("timerWarnSound");
      request.getSession().setAttribute("timerWarnSound", timerWarnSound);
      return "";
   }

   @ResponseBody
   @RequestMapping({"warnCountAjax"})
   public String warnCountAjax(HttpServletRequest request) {
      String timerWarnSound = (String)request.getSession().getAttribute("timerWarnSound");
      if ("1".equals(timerWarnSound)) {
         int count = WarnPools.WARN_COUNT_LIST.size();
         WarnPools.WARN_COUNT_LIST.clear();
         return count + "";
      } else {
         return "0";
      }
   }
}
