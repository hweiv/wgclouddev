package com.wgcloud.util;

import cn.hutool.json.JSONObject;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.util.staticvar.StaticKeys;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils {
   private static final Logger logger = LoggerFactory.getLogger(TokenUtils.class);
   @Autowired
   private CommonConfig commonConfig;

   public boolean checkAgentToken(JSONObject agentJsonObject) {
      if (null == agentJsonObject) {
         return false;
      } else {
         String wgToken = MD5Utils.GetMD5Code(this.commonConfig.getWgToken());
         String agentWgToken = agentJsonObject.getStr("wgToken");
         if (StringUtils.isEmpty(agentWgToken)) {
            return false;
         } else {
            agentWgToken = agentWgToken.toLowerCase();
            return wgToken.equals(agentWgToken);
         }
      }
   }

   public String preOpenDataAPICheck(JSONObject agentJsonObject) {
      if (!StaticKeys.LICENSE_STATE.equals("1")) {
         return ResDataUtils.resetErrorJson("The module needs to wgcloud-pro version. Please contact us at www.wgstart.com");
      } else if (!"true".equals(this.commonConfig.getOpenDataAPI())) {
         return ResDataUtils.resetErrorJson("API is no open");
      } else if (!this.checkAgentToken(agentJsonObject)) {
         logger.error("token is error");
         return ResDataUtils.resetErrorJson("token is error");
      } else {
         return "";
      }
   }
}
