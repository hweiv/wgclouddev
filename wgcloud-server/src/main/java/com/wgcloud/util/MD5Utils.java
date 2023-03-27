package com.wgcloud.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5Utils {
   private static final Logger logger = LoggerFactory.getLogger(MD5Utils.class);
   private static final String[] strDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
   private static final char[] hexdigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

   public static String getMD5ForFile(String filePath) {
      FileInputStream fis = null;
      MessageDigest md = null;

      String var4;
      try {
         md = MessageDigest.getInstance("MD5");
         File file = new File(filePath);
         if (!file.exists()) {
            var4 = "";
            return var4;
         }

         fis = new FileInputStream(file);
         byte[] buffer = new byte[4096];
         boolean var5 = true;

         int length;
         while((length = fis.read(buffer)) != -1) {
            md.update(buffer, 0, length);
         }

         byte[] b = md.digest();
         String var7 = byteToHexString(b);
         return var7;
      } catch (Exception var18) {
         logger.error("获取MD5信息发生异常！" + var18.toString());
         var4 = null;
      } finally {
         try {
            if (null != fis) {
               fis.close();
            }
         } catch (IOException var17) {
            logger.error("获取MD5信息发生异常！" + var17.toString());
         }

      }

      return var4;
   }

   private static String byteToHexString(byte[] tmp) {
      char[] str = new char[32];
      int k = 0;

      for(int i = 0; i < 16; ++i) {
         byte byte0 = tmp[i];
         str[k++] = hexdigits[byte0 >>> 4 & 15];
         str[k++] = hexdigits[byte0 & 15];
      }

      String s = new String(str);
      return s;
   }

   private static String byteToArrayString(byte bByte) {
      int iRet = bByte;
      if (bByte < 0) {
         iRet = bByte + 256;
      }

      int iD1 = iRet / 16;
      int iD2 = iRet % 16;
      return strDigits[iD1] + strDigits[iD2];
   }

   private static String byteToNum(byte bByte) {
      int iRet = bByte;
      System.out.println("iRet1=" + bByte);
      if (bByte < 0) {
         iRet = bByte + 256;
      }

      return String.valueOf(iRet);
   }

   private static String byteToString(byte[] bByte) {
      StringBuffer sBuffer = new StringBuffer();

      for(int i = 0; i < bByte.length; ++i) {
         sBuffer.append(byteToArrayString(bByte[i]));
      }

      return sBuffer.toString();
   }

   public static String GetMD5Code(String strObj) {
      if (StringUtils.isEmpty(strObj)) {
         return "";
      } else {
         String resultString = null;

         try {
            new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteToString(md.digest(strObj.getBytes()));
         } catch (NoSuchAlgorithmException var3) {
            var3.printStackTrace();
         }

         return resultString;
      }
   }
}
