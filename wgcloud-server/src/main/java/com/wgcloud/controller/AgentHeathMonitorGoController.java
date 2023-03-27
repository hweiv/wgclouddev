package com.wgcloud.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.entity.HeathState;
import com.wgcloud.service.HeathMonitorService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ThreadPoolUtil;
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
@RequestMapping({"/agentHeathMonitorGo"})
public class AgentHeathMonitorGoController {
   private static final Logger logger = LoggerFactory.getLogger(AgentHeathMonitorGoController.class);
   @Resource
   private LogInfoService logInfoService;
   @Autowired
   private HeathMonitorService heathMonitorService;
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
         logger.debug("server-backup监控服务接口上报数据-------------" + agentJsonObject.toString());
         Date nowtime = new Date();

         try {
            JSONArray heathMonitorsJsonArr = agentJsonObject.getJSONArray("heathMonitorsUpdate");
            if (heathMonitorsJsonArr == null) {
               logger.error("heathMonitorsUpdate is null");
               return ResDataUtils.resetErrorJson("heathMonitorsUpdate is null");
            }

            List<HeathMonitor> heathMonitorList = JSONUtil.toList(heathMonitorsJsonArr, HeathMonitor.class);
            Iterator var7 = heathMonitorList.iterator();

            while(var7.hasNext()) {
               HeathMonitor h = (HeathMonitor)var7.next();
               HeathMonitor heathMonitorSaved = this.heathMonitorService.selectById(h.getId());
               h.setCreateTime(nowtime);
               if (!"200".equals(h.getHeathStatus())) {
                  h.setCreateTime((Date)null);
               }

               try {
                  this.heathMonitorService.updateForServerBackup(h);
               } catch (Exception var12) {
                  var12.printStackTrace();
               }

               HeathState heathState = new HeathState();
               heathState.setResTimes(h.getResTimes());
               heathState.setHeathId(h.getId());
               heathState.setCreateTime(nowtime);
               BatchData.HEATH_STATE_LIST.add(heathState);
               heathMonitorSaved.setHeathStatus(h.getHeathStatus());
               Runnable runnable;
               if (!"200".equals(h.getHeathStatus())) {
                  runnable = () -> {
                     WarnOtherUtil.sendHeathInfo(heathMonitorSaved, true);
                  };
                  ThreadPoolUtil.executor.execute(runnable);
               } else if (null != WarnPools.MEM_WARN_MAP.get(h.getId())) {
                  runnable = () -> {
                     WarnOtherUtil.sendHeathInfo(heathMonitorSaved, false);
                  };
                  ThreadPoolUtil.executor.execute(runnable);
               }
            }
         } catch (Exception var13) {
            logger.error("解析server-backup监控服务接口上报数据错误", var13);
            return ResDataUtils.resetErrorJson(var13.toString());
         }

         return ResDataUtils.resetSuccessJson((Object)null);
      }
   }
}
