package com.wgcloud.util;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.CommonConfig;
import org.apache.commons.lang3.StringUtils;

public class DESUtil {
   private static CommonConfig commonConfig = (CommonConfig)ApplicationContextHelper.getBean(CommonConfig.class);
   private static SymmetricCrypto des = null;

   private static void initDes() {
      if (null == des) {
         String wgTokenPack = MD5Utils.GetMD5Code(commonConfig.getWgToken());
         des = new SymmetricCrypto(SymmetricAlgorithm.DES, wgTokenPack.getBytes());
      }

   }

   public static String decrypt(String content) {
      if (StringUtils.isEmpty(content)) {
         return "";
      } else {
         if (des == null) {
            initDes();
         }

         return des.decryptStr(content);
      }
   }

   public static String encryption(String content) {
      if (StringUtils.isEmpty(content)) {
         return "";
      } else {
         if (des == null) {
            initDes();
         }

         return des.encryptHex(content);
      }
   }
}
