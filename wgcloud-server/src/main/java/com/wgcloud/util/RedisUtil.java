package com.wgcloud.util;

import com.wgcloud.entity.DbInfo;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class RedisUtil {
   private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private DbInfoService dbInfoService;

   public String connectRedis(DbInfo dbInfo) throws Exception {
      try {
         dbInfo.setDbState("1");
         Jedis jedis = new Jedis(dbInfo.getDbUrl(), Integer.valueOf(dbInfo.getUserName()), 10000);
         if (!StringUtils.isEmpty(dbInfo.getPasswd())) {
            jedis.auth(dbInfo.getPasswd());
         }

         String pong = jedis.ping();
         logger.info("connectRedis-------" + pong);
         jedis.close();
         if (null != WarnPools.MEM_WARN_MAP && null != WarnPools.MEM_WARN_MAP.get(dbInfo.getId())) {
            Runnable runnable = () -> {
               WarnOtherUtil.sendDbDown(dbInfo, false);
            };
            ThreadPoolUtil.executor.execute(runnable);
         }

         dbInfo.setCreateTime(DateUtil.getNowTime());
         this.dbInfoService.updateById(dbInfo);
         return pong;
      } catch (Exception var5) {
         dbInfo.setDbState("2");
         logger.error("连接redis错误", var5);
         this.logInfoService.save("连接redis错误：" + dbInfo.getAliasName(), "数据库别名：" + dbInfo.getAliasName() + "，" + var5.toString(), "2");
         this.dbInfoService.updateById(dbInfo);
         Runnable runnable = () -> {
            WarnOtherUtil.sendDbDown(dbInfo, true);
         };
         ThreadPoolUtil.executor.execute(runnable);
         return null;
      }
   }
}
