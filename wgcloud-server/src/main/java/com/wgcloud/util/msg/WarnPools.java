package com.wgcloud.util.msg;

import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.MailConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarnPools {
   private static final Logger logger = LoggerFactory.getLogger(WarnPools.class);
   private static CommonConfig commonConfig = (CommonConfig)ApplicationContextHelper.getBean(CommonConfig.class);
   private static MailConfig mailConfig = (MailConfig)ApplicationContextHelper.getBean(MailConfig.class);
   public static ExpiringMap<String, String> MEM_WARN_MAP = null;
   public static List<Integer> WARN_COUNT_LIST = Collections.synchronizedList(new ArrayList());
   public static ExpiringMap<String, Integer> HEATH_WARN_COUNT_MAP = null;
   public static ExpiringMap<String, Integer> PING_WARN_COUNT_MAP = null;

   public static void clearOldData() {
      logger.info("清空告警缓存map");
      if (null != MEM_WARN_MAP) {
         MEM_WARN_MAP.clear();
      }

      logger.info("清空告警信息计数器");
      WARN_COUNT_LIST.clear();
      logger.info("清空服务接口，PING告警失败次数的缓存记录");
      if (null != HEATH_WARN_COUNT_MAP) {
         HEATH_WARN_COUNT_MAP.clear();
      }

      if (null != PING_WARN_COUNT_MAP) {
         PING_WARN_COUNT_MAP.clear();
      }

   }

   public static boolean isExpireWarnTime(String key, Integer warnCacheTimes) {
      if (StringUtils.isEmpty(key)) {
         return false;
      } else {
         if (null == warnCacheTimes) {
            warnCacheTimes = 7200;
         }

         try {
            if (null == MEM_WARN_MAP) {
               MEM_WARN_MAP = ExpiringMap.builder().expiration((long)warnCacheTimes, TimeUnit.SECONDS).expirationPolicy(ExpirationPolicy.CREATED).build();
            }

            if (null != MEM_WARN_MAP.get(key)) {
               return true;
            }
         } catch (Exception var3) {
            logger.error("判断告警缓存时间错误", var3);
         }

         return false;
      }
   }

   public static void initWarnCountMap() {
      try {
         long pingOutTimes;
         if (null == HEATH_WARN_COUNT_MAP) {
            pingOutTimes = (long)(commonConfig.getHeathTimes() * mailConfig.getHeathWarnCount());
            pingOutTimes += 10L;
            HEATH_WARN_COUNT_MAP = ExpiringMap.builder().expiration(pingOutTimes, TimeUnit.SECONDS).expirationPolicy(ExpirationPolicy.CREATED).build();
         }

         if (null == PING_WARN_COUNT_MAP) {
            pingOutTimes = (long)(commonConfig.getDceTimes() * mailConfig.getDceWarnCount());
            pingOutTimes += 10L;
            PING_WARN_COUNT_MAP = ExpiringMap.builder().expiration(pingOutTimes, TimeUnit.SECONDS).expirationPolicy(ExpirationPolicy.CREATED).build();
         }
      } catch (Exception var2) {
         logger.error("判断告警缓存时间错误", var2);
      }

   }
}
