package com.wgcloud.util;

import com.wgcloud.common.ApplicationContextHelper;
import java.util.Random;

public class UUIDUtil {
   private static IdGeneratorSnowflake idGeneratorSnowflake = (IdGeneratorSnowflake)ApplicationContextHelper.getBean(IdGeneratorSnowflake.class);

   public static String getUUID() {
      return idGeneratorSnowflake.snowflakeId() + "";
   }

   public static String getRandomSix() {
      return "" + (new Random()).nextInt(999999);
   }
}
