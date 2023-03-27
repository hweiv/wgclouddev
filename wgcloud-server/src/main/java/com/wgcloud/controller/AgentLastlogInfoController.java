package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnOtherUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/agentLastlogInfoGo"})
public class AgentLastlogInfoController {
   private static final Logger logger = LoggerFactory.getLogger(AgentLastlogInfoController.class);
   @Autowired
   private TokenUtils tokenUtils;

   @ResponseBody
   @RequestMapping({"/minTask"})
   public JSONObject minTask(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      logger.debug("agent上报主机最后的登录信息-------------" + agentJsonObject.toString());
      JSONObject resultJson = new JSONObject();
      if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
         logger.error("token is error");
         resultJson.set("result", "token is error");
         return resultJson;
      } else {
         String bindIp = agentJsonObject.getStr("bindIp");
         String lastlogWarnInfos = agentJsonObject.getStr("lastlogWarnInfos");
         new Date();

         try {
            if (!StringUtils.isEmpty(lastlogWarnInfos)) {
               Runnable runnable = () -> {
                  try {
                     if (!StringUtils.isEmpty(lastlogWarnInfos)) {
                        WarnOtherUtil.sendLastlogWarnInfo(lastlogWarnInfos, bindIp);
                     }
                  } catch (Exception var3) {
                     var3.printStackTrace();
                  }

               };
               ThreadPoolUtil.executor.execute(runnable);
            }

            resultJson.set("result", "success");
         } catch (Exception var8) {
            logger.error("解析最新登录主机信息上报数据错误", var8);
            resultJson.set("result", "error：" + var8.toString());
         }

         return resultJson;
      }
   }
}
