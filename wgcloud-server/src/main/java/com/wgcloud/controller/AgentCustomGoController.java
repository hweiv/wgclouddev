package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.entity.CustomInfo;
import com.wgcloud.entity.CustomState;
import com.wgcloud.service.CustomInfoService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnMailUtil;
import com.wgcloud.util.staticvar.BatchData;
import java.util.Date;
import java.util.Iterator;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/agentCustomGo"})
public class AgentCustomGoController {
   private static final Logger logger = LoggerFactory.getLogger(AgentCustomGoController.class);
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private SystemInfoService systemInfoService;
   @Autowired
   private DockerInfoService dockerInfoService;
   @Autowired
   private CustomInfoService customInfoService;
   @Autowired
   private TokenUtils tokenUtils;

   @ResponseBody
   @RequestMapping({"/minTask"})
   public JSONObject minTask(@RequestBody String paramBean) {
      logger.info("=AgentCustomGoController-minTask.paramBean:{}=", paramBean);
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      logger.debug("agent上报自定义监控项数据-------------" + agentJsonObject.toString());
      JSONObject resultJson = new JSONObject();
      if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
         logger.error("token is error");
         resultJson.set("result", "token is error");
         return resultJson;
      } else {
         Date nowtime = new Date();

         try {
            JSONObject customInfosJson = agentJsonObject.getJSONObject("customInfos");
            if (customInfosJson == null) {
               logger.error("customInfoList is null");
               resultJson.set("result", "error：customInfoList is null");
               return resultJson;
            }

            Iterator iter = customInfosJson.keySet().iterator();

            while(iter.hasNext()) {
               String id = (String)iter.next();
               JSONObject jsonObject = customInfosJson.getJSONObject(id);
               String customValue = jsonObject.getStr("customValue");
               CustomState state = new CustomState();
               state.setCreateTime(nowtime);
               state.setCustomInfoId(id);
               state.setCustomValue(customValue);
               if (!StringUtils.isEmpty(customValue)) {
                  BatchData.CUSTOM_STATE_LIST.add(state);
               }

               CustomInfo info = new CustomInfo();
               info.setId(id);
               info.setCreateTime(nowtime);
               info.setCustomValue(customValue);
               info.setState("1");
               BatchData.CUSTOM_INFO_LIST.add(info);
               if (!StringUtils.isEmpty(customValue)) {
                  Runnable runnable = () -> {
                     try {
                        CustomInfo customInfo = this.customInfoService.selectById(info.getId());
                        if (customInfo != null && !StringUtils.isEmpty(customInfo.getResultExp())) {
                           Double customValueDouble = Double.valueOf(customValue);
                           Boolean result = FormatUtil.validateExpression(customInfo.getResultExp(), customValueDouble);
                           if (result) {
                              info.setState("2");
                              customInfo.setCustomValue(customValue);
                              WarnMailUtil.sendCustomInfoDown(customInfo);
                           }
                        }
                     } catch (Exception var6) {
                        var6.printStackTrace();
                     }

                  };
                  ThreadPoolUtil.executor.execute(runnable);
               }
            }

            resultJson.set("result", "success");
         } catch (Exception var13) {
            logger.error("解析自定义监控项上报数据错误", var13);
            resultJson.set("result", "error：" + var13.toString());
         }

         return resultJson;
      }
   }
}
