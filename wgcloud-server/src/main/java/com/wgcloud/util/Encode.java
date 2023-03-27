package com.wgcloud.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Encode {
   private static final Logger logger = LoggerFactory.getLogger(Encode.class);

   public static String utf8ToSystem(String str) {
      return encode(str, "UTF-8", System.getProperty("file.encoding"));
   }

   public static String systemToUtf8(String str) {
      return encode(str, System.getProperty("file.encoding"), "UTF-8");
   }

   public static String gbkToSystem(String str) {
      return encode(str, "GBK", System.getProperty("file.encoding"));
   }

   public static String systemToGbk(String str) {
      return encode(str, System.getProperty("file.encoding"), "GBK");
   }

   public static String iso_8859_1ToSystem(String str) {
      return encode(str, "ISO_8859_1", System.getProperty("file.encoding"));
   }

   public static String systemToIso_8859_1(String str) {
      return encode(str, System.getProperty("file.encoding"), "ISO_8859_1");
   }

   public static String iso_8859_1ToGbk(String str) {
      return encode(str, "ISO_8859_1", "GBK");
   }

   public static String gbkToIso_8859_1(String str) {
      return encode(str, "GBK", "ISO_8859_1");
   }

   public static String utf8ToGbk(String str) {
      return encode(str, "UTF-8", "GBK");
   }

   public static String gbkToUtf8(String str) {
      return encode(str, "GBK", "UTF-8");
   }

   public static String utf8ToIso_8859_1(String str) {
      return encode(str, "UTF-8", "ISO_8859_1");
   }

   public static String iso_8859_1ToUtf8(String str) {
      return encode(str, "ISO_8859_1", "UTF-8");
   }

   public static String urlEncode(String str) {
      str = urlEncode(str, System.getProperty("file.encoding"));
      return str;
   }

   public static String urlEncode(String str, String encoding) {
      try {
         str = URLEncoder.encode(str, encoding);
         return str;
      } catch (UnsupportedEncodingException var3) {
         var3.printStackTrace(System.out);
         return null;
      }
   }

   public static String urlDecode(String str) {
      str = urlDecode(str, System.getProperty("file.encoding"));
      return str;
   }

   public static String urlDecode(String str, String encoding) {
      try {
         str = URLDecoder.decode(str, encoding);
         return str;
      } catch (UnsupportedEncodingException var3) {
         logger.error("处理乱码异常" + var3.toString());
         return "";
      }
   }

   public static String urlEncodeForLinux(String str, String encoding) {
      str = gbkToSystem(str);
      str = urlEncode(str, encoding);
      str = str.replaceAll("\\+", "%20");
      return str;
   }

   public static String encode(String str, String encodeStr, String decodeStr) {
      try {
         str = new String(str.getBytes(encodeStr), decodeStr);
         return str;
      } catch (UnsupportedEncodingException var4) {
         var4.printStackTrace(System.out);
         return null;
      }
   }

   public static String luanmaStr(String str) {
      try {
         str = new String(str.getBytes("ISO-8859-1"), "utf-8");
         return str;
      } catch (UnsupportedEncodingException var2) {
         logger.error("处理乱码异常" + var2.toString());
         return "";
      }
   }
}
