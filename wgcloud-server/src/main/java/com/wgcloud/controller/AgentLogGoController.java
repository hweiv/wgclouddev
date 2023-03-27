package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.entity.FileWarnInfo;
import com.wgcloud.entity.FileWarnState;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.FileWarnInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.SystemInfoService;
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
@RequestMapping({"/agentLogGo"})
public class AgentLogGoController {
   private static final Logger logger = LoggerFactory.getLogger(AgentLogGoController.class);
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private SystemInfoService systemInfoService;
   @Autowired
   private DockerInfoService dockerInfoService;
   @Autowired
   private FileWarnInfoService fileWarnInfoService;
   @Autowired
   private AppInfoService appInfoService;
   @Autowired
   private PortInfoService portInfoService;
   @Autowired
   private TokenUtils tokenUtils;

   @ResponseBody
   @RequestMapping({"/minTask"})
   public JSONObject minTask(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      logger.debug("agent上报日志监控数据-------------" + agentJsonObject.toString());
      JSONObject resultJson = new JSONObject();
      if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
         logger.error("token is error");
         resultJson.set("result", "token is error");
         return resultJson;
      } else {
         Date nowtime = new Date();

         try {
            JSONObject fileWarnInfosJson = agentJsonObject.getJSONObject("fileWarnInfos");
            if (fileWarnInfosJson == null) {
               logger.error("fileWarnList is null");
               resultJson.set("result", "error：fileWarnList is null");
               return resultJson;
            }

            Iterator iter = fileWarnInfosJson.keySet().iterator();

            while(iter.hasNext()) {
               String id = (String)iter.next();
               JSONObject jsonObject = fileWarnInfosJson.getJSONObject(id);
               String filePath = jsonObject.getStr("filePath");
               String fileSize = jsonObject.getStr("fileSize");
               String warnRows = jsonObject.getStr("warnRows");
               String warnContent = jsonObject.getStr("warnContent");
               FileWarnState state = new FileWarnState();
               state.setCreateTime(nowtime);
               state.setFilePath(filePath);
               state.setFileWarnId(id);
               state.setWarContent(warnContent);
               if (!StringUtils.isEmpty(warnContent)) {
                  BatchData.FILEWARN_STATE_LIST.add(state);
               }

               FileWarnInfo info = new FileWarnInfo();
               info.setId(id);
               info.setCreateTime(nowtime);
               info.setFileSize(fileSize);
               info.setWarnRows(warnRows);
               BatchData.FILEWARN_INFO_LIST.add(info);
               if (!StringUtils.isEmpty(warnContent)) {
                  Runnable runnable = () -> {
                     try {
                        FileWarnInfo fileWarnInfo = this.fileWarnInfoService.selectById(info.getId());
                        if (fileWarnInfo != null) {
                           WarnMailUtil.sendFileWarnDown(fileWarnInfo, filePath, warnContent, true);
                        }
                     } catch (Exception var5) {
                        var5.printStackTrace();
                     }

                  };
                  ThreadPoolUtil.executor.execute(runnable);
               }
            }

            resultJson.set("result", "success");
         } catch (Exception var16) {
            logger.error("解析日志监控上报数据错误", var16);
            resultJson.set("result", "error：" + var16.toString());
         }

         return resultJson;
      }
   }
}
