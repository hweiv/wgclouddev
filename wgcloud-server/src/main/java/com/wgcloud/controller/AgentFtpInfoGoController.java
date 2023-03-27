package com.wgcloud.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.FtpInfo;
import com.wgcloud.service.FtpInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
@RequestMapping({"/agentFtpInfoGo"})
public class AgentFtpInfoGoController {
   private static final Logger logger = LoggerFactory.getLogger(AgentFtpInfoGoController.class);
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private FtpInfoService ftpInfoService;
   @Autowired
   private TokenUtils tokenUtils;
   @Autowired
   private CommonConfig commonConfig;

   @ResponseBody
   @RequestMapping({"/minTask"})
   public String minTask(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
      if (!StringUtils.isEmpty(checkResult)) {
         return checkResult;
      } else {
         logger.debug("server-backup监控ftp/sftp上报数据-------------" + agentJsonObject.toString());
         Date nowtime = new Date();

         try {
            JSONArray ftpInfosJsonArr = agentJsonObject.getJSONArray("ftpInfosUpdate");
            if (ftpInfosJsonArr == null) {
               logger.error("ftpInfosUpdate is null");
               return ResDataUtils.resetErrorJson("ftpInfosUpdate is null");
            }

            List<FtpInfo> ftpInfoList = JSONUtil.toList(ftpInfosJsonArr, FtpInfo.class);
            Iterator var7 = ftpInfoList.iterator();

            while(var7.hasNext()) {
               FtpInfo f = (FtpInfo)var7.next();
               FtpInfo ftpInfoSaved = this.ftpInfoService.selectById(f.getId());
               f.setCreateTime(nowtime);
               if ("2".equals(f.getState())) {
                  f.setCreateTime((Date)null);
               }

               try {
                  this.ftpInfoService.updateById(f);
               } catch (Exception var11) {
                  var11.printStackTrace();
               }

               ftpInfoSaved.setState(f.getState());
               Runnable runnable;
               if ("2".equals(f.getState())) {
                  runnable = () -> {
                     WarnOtherUtil.sendFtpInfo(ftpInfoSaved, true);
                  };
                  ThreadPoolUtil.executor.execute(runnable);
               } else if (null != WarnPools.MEM_WARN_MAP.get(ftpInfoSaved.getId())) {
                  runnable = () -> {
                     WarnOtherUtil.sendFtpInfo(ftpInfoSaved, false);
                  };
                  ThreadPoolUtil.executor.execute(runnable);
               }
            }
         } catch (Exception var12) {
            logger.error("解析server-backup监控ftp/sftp上报数据错误", var12);
            return ResDataUtils.resetErrorJson(var12.toString());
         }

         return ResDataUtils.resetSuccessJson((Object)null);
      }
   }
}
