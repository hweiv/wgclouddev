package com.wgcloud.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.entity.DbTable;
import com.wgcloud.entity.DbTableCount;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.staticvar.BatchData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
@RequestMapping({"/agentDbTableGo"})
public class AgentDbTableGoController {
   private static final Logger logger = LoggerFactory.getLogger(AgentDbTableGoController.class);
   @Resource
   private LogInfoService logInfoService;
   @Autowired
   private DbInfoService dbInfoService;
   @Autowired
   private DbTableService dbTableService;
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
         logger.debug("server-backup监控数据源、数据表上报数据-------------" + agentJsonObject.toString());
         Date nowtime = new Date();

         try {
            JSONArray dbInfosJsonArr = agentJsonObject.getJSONArray("dbInfosUpdate");
            JSONArray dbTablesJsonArr = agentJsonObject.getJSONArray("dbTablesUpdate");
            if (dbInfosJsonArr == null) {
               logger.error("dbInfosUpdate is null");
               return ResDataUtils.resetErrorJson("dbInfosUpdate is null");
            }

            List<DbInfo> dbInfoList = JSONUtil.toList(dbInfosJsonArr, DbInfo.class);

            DbInfo dbInfo;
            for(Iterator var8 = dbInfoList.iterator(); var8.hasNext(); this.dbInfoService.updateById(dbInfo)) {
               dbInfo = (DbInfo)var8.next();
               DbInfo dbInfoSaved = this.dbInfoService.selectById(dbInfo.getId());
               Runnable runnable;
               if ("1".equals(dbInfo.getDbState())) {
                  dbInfo.setCreateTime(nowtime);
                  if (null != WarnPools.MEM_WARN_MAP && null != WarnPools.MEM_WARN_MAP.get(dbInfo.getId())) {
                     runnable = () -> {
                        WarnOtherUtil.sendDbDown(dbInfoSaved, false);
                     };
                     ThreadPoolUtil.executor.execute(runnable);
                  }
               } else {
                  runnable = () -> {
                     WarnOtherUtil.sendDbDown(dbInfoSaved, true);
                  };
                  ThreadPoolUtil.executor.execute(runnable);
               }
            }

            if (null != dbTablesJsonArr) {
               List<DbTable> dbTableList = JSONUtil.toList(dbTablesJsonArr, DbTable.class);
               Iterator var14 = dbTableList.iterator();

               while(var14.hasNext()) {
                  DbTable dbTable = (DbTable)var14.next();
                  dbTable.setCreateTime(nowtime);
                  DbTableCount dbTableCount = new DbTableCount();
                  dbTableCount.setCreateTime(nowtime);
                  dbTableCount.setDbTableId(dbTable.getId());
                  dbTableCount.setTableCount(dbTable.getTableCount());
                  BatchData.DBTABLE_COUNT_LIST.add(dbTableCount);
               }

               Map<String, Object> params = new HashMap();
               List<DbInfo> dbInfos = new ArrayList();
               if ("true".equals(this.commonConfig.getUserInfoManage())) {
                  dbInfos = this.dbInfoService.selectAllByParams(params);
               }

               this.dbTableService.warnCheckExp(dbTableList, (List)dbInfos);
               this.dbTableService.updateRecord(dbTableList);
            }
         } catch (Exception var12) {
            logger.error("解析server-backup监控数据表上报数据错误", var12);
            return ResDataUtils.resetErrorJson(var12.toString());
         }

         return ResDataUtils.resetSuccessJson((Object)null);
      }
   }
}
