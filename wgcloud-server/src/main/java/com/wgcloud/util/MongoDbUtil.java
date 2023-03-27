package com.wgcloud.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MongoDbUtil {
   private static final Logger logger = LoggerFactory.getLogger(MongoDbUtil.class);
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private DbInfoService dbInfoService;

   public void connectMongoDb(DbInfo dbInfo) throws Exception {
      MongoClient mongoClient = null;

      Runnable runnable;
      try {
         dbInfo.setDbState("1");
         mongoClient = MongoClients.create(dbInfo.getDbUrl());
         logger.info("mongoClient----------" + mongoClient);
         if (null == mongoClient) {
            dbInfo.setDbState("2");
         } else {
            MongoIterable<String> listDataBaseNames = mongoClient.listDatabaseNames();
            MongoCursor var7 = listDataBaseNames.iterator();

            while(var7.hasNext()) {
               String dbName = (String)var7.next();
               logger.info("连接mongodb dbName-------" + dbName);
            }

            mongoClient.close();
            mongoClient = null;
            if (null != WarnPools.MEM_WARN_MAP && null != WarnPools.MEM_WARN_MAP.get(dbInfo.getId())) {
               runnable = () -> {
                  WarnOtherUtil.sendDbDown(dbInfo, false);
               };
               ThreadPoolUtil.executor.execute(runnable);
            }

            dbInfo.setCreateTime(DateUtil.getNowTime());
         }

         this.dbInfoService.updateById(dbInfo);
      } catch (Exception var6) {
         dbInfo.setDbState("2");
         logger.error("连接mongodb错误", var6);
         this.logInfoService.save("连接mongodb错误：" + dbInfo.getAliasName(), "数据库别名：" + dbInfo.getAliasName() + "，" + var6.toString(), "2");
         this.dbInfoService.updateById(dbInfo);
         runnable = () -> {
            WarnOtherUtil.sendDbDown(dbInfo, true);
         };
         ThreadPoolUtil.executor.execute(runnable);
         if (null != mongoClient) {
            mongoClient.close();
            mongoClient = null;
         }
      }

   }
}
