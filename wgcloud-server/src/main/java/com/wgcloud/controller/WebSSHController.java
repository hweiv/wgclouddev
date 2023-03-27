package com.wgcloud.controller;

import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.util.FileUtils;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/ssh2"})
public class WebSSHController {
   private static final Logger logger = LoggerFactory.getLogger(WebSSHController.class);
   @Autowired
   private CommonConfig commonConfig;

   @RequestMapping({"view"})
   public String view(Model model, HttpServletRequest request) {
      String hostname = request.getParameter("hostname");
      model.addAttribute("hostname", request.getParameter("hostname"));
      model.addAttribute("webSocketPort", this.commonConfig.getWebSshPort());
      model.addAttribute("remark", StaticKeys.HOST_MAP.get(hostname) == null ? "" : StaticKeys.HOST_MAP.get(hostname));
      List<String> keyFileList = new ArrayList();
      AccountInfo accountInfo = (AccountInfo)request.getSession().getAttribute("LOGIN_KEY");
      if ("admin".equals(accountInfo.getRole())) {
         FileUtils.getFileList(StaticKeys.JAR_PATH + "/template/", keyFileList);
         if (keyFileList.size() > 0) {
            for(int i = 0; i < keyFileList.size(); ++i) {
               String resStr = ((String)keyFileList.get(i)).substring(((String)keyFileList.get(i)).indexOf("template"));
               keyFileList.set(i, resStr);
            }
         }
      }

      model.addAttribute("keyFileList", keyFileList);
      return "ssh/ssh";
   }
}
