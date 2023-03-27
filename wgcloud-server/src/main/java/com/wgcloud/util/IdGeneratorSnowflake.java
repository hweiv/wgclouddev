package com.wgcloud.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.wgcloud.config.CommonConfig;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IdGeneratorSnowflake {
   private static final Logger logger = LoggerFactory.getLogger(IdGeneratorSnowflake.class);
   @Autowired
   CommonConfig commonConfig;
   private long workerId = 0L;
   private long datacenterId = 1L;
   private Snowflake snowflake = null;

   @PostConstruct
   public void init() {
      try {
         if (!"master".equals(this.commonConfig.getNodeType())) {
            String slaveId = this.commonConfig.getNodeType().replace("slave", "");
            this.workerId = Long.valueOf(slaveId);
         }

         this.snowflake = IdUtil.createSnowflake(this.workerId, this.datacenterId);
         logger.info("当前机器的workerId: {}", this.workerId);
      } catch (Exception var2) {
         this.snowflake = IdUtil.createSnowflake(this.workerId, this.datacenterId);
         logger.error("当前机器的workerId获取失败", var2);
      }

   }

   public synchronized long snowflakeId() {
      return this.snowflake.nextId();
   }
}
