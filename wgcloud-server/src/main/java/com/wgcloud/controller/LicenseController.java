package com.wgcloud.controller;

import com.wgcloud.config.CommonConfig;
import com.wgcloud.util.RestUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/license"})
public class LicenseController {
   private static final Logger logger = LoggerFactory.getLogger(LicenseController.class);
   @Autowired
   private RestUtil restUtil;
   @Autowired
   private CommonConfig commonConfig;

   @ResponseBody
   @RequestMapping({"get"})
   public String agentList() {
      return StaticKeys.LICENSE_STATE;
   }

   @ResponseBody
   @RequestMapping({"getDaemon"})
   public String getDaemon() {
      return this.restUtil.get(this.commonConfig.getDaemonUrl());
   }
}
