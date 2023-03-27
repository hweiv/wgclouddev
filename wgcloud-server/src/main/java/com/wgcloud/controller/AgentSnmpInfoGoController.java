package com.wgcloud.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.SnmpInfo;
import com.wgcloud.entity.SnmpState;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SnmpStateService;
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
@RequestMapping({"/agentSnmpInfoGo"})
public class AgentSnmpInfoGoController {
   private static final Logger logger = LoggerFactory.getLogger(AgentSnmpInfoGoController.class);
   @Resource
   private LogInfoService logInfoService;
   @Autowired
   private SnmpInfoService snmpInfoService;
   @Autowired
   private SnmpStateService snmpStateService;
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
         logger.debug("server-backup监控数通SNMP上报数据-------------" + agentJsonObject.toString());
         Date nowtime = new Date();

         try {
            JSONArray snmpInfosJsonArr = agentJsonObject.getJSONArray("snmpInfosUpdate");
            if (snmpInfosJsonArr == null) {
               logger.error("snmpInfosUpdate is null");
               return ResDataUtils.resetErrorJson("snmpInfosUpdate is null");
            }

            List<SnmpInfo> snmpInfoList = JSONUtil.toList(snmpInfosJsonArr, SnmpInfo.class);
            Iterator var7 = snmpInfoList.iterator();

            while(var7.hasNext()) {
               SnmpInfo snmpInfo = (SnmpInfo)var7.next();
               SnmpInfo snmpInfoSaved = this.snmpInfoService.selectById(snmpInfo.getId());
               snmpInfo.setCreateTime(nowtime);
               snmpInfo.setHostname(snmpInfoSaved.getHostname());
               if ("2".equals(snmpInfo.getState())) {
                  snmpInfo.setCreateTime((Date)null);
               }

               try {
                  this.snmpInfoService.updateById(snmpInfo);
               } catch (Exception var11) {
                  var11.printStackTrace();
               }

               if ("1".equals(snmpInfo.getState())) {
                  SnmpState snmpState = new SnmpState();
                  snmpState.setRecvAvg(snmpInfo.getRecvAvg());
                  snmpState.setSentAvg(snmpInfo.getSentAvg());
                  snmpState.setCpuPer(snmpInfo.getCpuPer());
                  snmpState.setMemPer(snmpInfo.getMemPer());
                  snmpState.setSnmpInfoId(snmpInfo.getId());
                  snmpState.setCreateTime(nowtime);
                  BatchData.SNMP_STATE_LIST.add(snmpState);
               }

               if ("2".equals(snmpInfo.getState())) {
                  WarnOtherUtil.sendSnmpInfo(snmpInfoSaved, true);
               } else if (null != WarnPools.MEM_WARN_MAP.get(snmpInfoSaved.getId())) {
                  WarnOtherUtil.sendSnmpInfo(snmpInfoSaved, false);
               }
            }
         } catch (Exception var12) {
            logger.error("解析server-backup监控数通SNMP上报数据错误", var12);
            return ResDataUtils.resetErrorJson(var12.toString());
         }

         return ResDataUtils.resetSuccessJson((Object)null);
      }
   }
}
