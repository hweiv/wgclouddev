package com.wgcloud.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.DceState;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.DceStateService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.staticvar.BatchData;
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
@RequestMapping({"/agentDceInfoGo"})
public class AgentDceInfoGoController {
   private static final Logger logger = LoggerFactory.getLogger(AgentDceInfoGoController.class);
   @Resource
   private LogInfoService logInfoService;
   @Autowired
   private DceInfoService dceInfoService;
   @Autowired
   private DceStateService dceStateService;
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
         logger.debug("server-backup监控数通PING上报数据-------------" + agentJsonObject.toString());
         Date nowtime = new Date();

         try {
            JSONArray dceInfosJsonArr = agentJsonObject.getJSONArray("dceInfosUpdate");
            if (dceInfosJsonArr == null) {
               logger.error("dceInfosUpdate is null");
               return ResDataUtils.resetErrorJson("dceInfosUpdate is null");
            }

            List<DceInfo> dceInfoList = JSONUtil.toList(dceInfosJsonArr, DceInfo.class);
            Iterator var7 = dceInfoList.iterator();

            while(var7.hasNext()) {
               DceInfo dceInfo = (DceInfo)var7.next();
               DceInfo dceInfoSaved = this.dceInfoService.selectById(dceInfo.getId());
               dceInfo.setHostname(dceInfoSaved.getHostname());
               dceInfo.setCreateTime(nowtime);
               if (dceInfo.getResTimes() < 0) {
                  dceInfo.setCreateTime((Date)null);
               }

               try {
                  this.dceInfoService.updateById(dceInfo);
               } catch (Exception var11) {
                  var11.printStackTrace();
               }

               if (dceInfo.getResTimes() > 0) {
                  DceState dceState = new DceState();
                  dceState.setResTimes(dceInfo.getResTimes());
                  dceState.setDceId(dceInfo.getId());
                  dceState.setCreateTime(nowtime);
                  BatchData.DCE_STATE_LIST.add(dceState);
               }

               dceInfoSaved.setResTimes(dceInfo.getResTimes());
               if (dceInfo.getResTimes() < 0) {
                  WarnOtherUtil.sendDceInfo(dceInfoSaved, true);
               } else if (null != WarnPools.MEM_WARN_MAP.get(dceInfo.getId())) {
                  WarnOtherUtil.sendDceInfo(dceInfoSaved, false);
               }
            }
         } catch (Exception var12) {
            logger.error("解析server-backup监控数通PING上报数据错误", var12);
            return ResDataUtils.resetErrorJson(var12.toString());
         }

         return ResDataUtils.resetSuccessJson((Object)null);
      }
   }
}
