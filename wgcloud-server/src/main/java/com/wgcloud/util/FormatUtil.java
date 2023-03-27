package com.wgcloud.util;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormatUtil {
   private static Logger logger = LoggerFactory.getLogger(FormatUtil.class);

   public static String timesToDay(Long l) {
      if (l == null) {
         return "";
      } else {
         StringBuffer sb = new StringBuffer();
         long seconds = 1L;
         long minutes = 60L * seconds;
         long hours = 60L * minutes;
         long days = 24L * hours;
         if (l / days >= 1L) {
            sb.append((int)(l / days) + "天");
         }

         if (l % days / hours >= 1L) {
            sb.append((int)(l % days / hours) + "小时");
         }

         if (l % days % hours / minutes >= 1L) {
            sb.append((int)(l % days % hours / minutes) + "分钟");
         }

         if (l % days % hours % minutes / seconds >= 1L) {
            sb.append((int)(l % days % hours % minutes / seconds) + "秒");
         }

         return sb.toString();
      }
   }

   public static String secondsToDays(Long l) {
      if (l == null) {
         return "";
      } else {
         StringBuffer sb = new StringBuffer();
         long seconds = 1L;
         long minutes = 60L * seconds;
         long hours = 60L * minutes;
         long days = 24L * hours;
         if (l / days >= 1L) {
            sb.append((int)(l / days) + "天");
         }

         return sb.toString();
      }
   }

   public static String getString(String str, int len) {
      if (StringUtils.isEmpty(str)) {
         return "";
      } else {
         return str.length() <= len ? str : str.substring(0, len);
      }
   }

   public static String kbToM(String str) {
      if (StringUtils.isEmpty(str)) {
         return "0KB";
      } else {
         double result = 0.0D;
         double mod = 1024.0D;

         try {
            double strDouble = Double.valueOf(str);
            if (strDouble > 1024.0D) {
               result = strDouble / mod;
               return formatDouble((Double)result, 2) + "MB";
            }
         } catch (Exception var7) {
            logger.error("kb转为M错误：", var7);
            return str + "KB";
         }

         return str + "KB";
      }
   }

   public static String mToG(String str) {
      if (StringUtils.isEmpty(str)) {
         return "0M";
      } else {
         double result = 0.0D;
         double mod = 1024.0D;

         try {
            double strDouble = Double.valueOf(str);
            if (strDouble > 1024.0D) {
               result = strDouble / mod;
               return formatDouble((Double)result, 2) + "G";
            }
         } catch (Exception var7) {
            logger.error("m转为g错误：", var7);
            return str + "M";
         }

         return str + "M";
      }
   }

   public static String gToT(String str) {
      if (StringUtils.isEmpty(str)) {
         return "0G";
      } else {
         double result = 0.0D;
         double mod = 1024.0D;

         try {
            double strDouble = Double.valueOf(str);
            if (strDouble > 1024.0D) {
               result = strDouble / mod;
               return formatDouble((Double)result, 1) + "T";
            }
         } catch (Exception var7) {
            logger.error("G转为T错误：", var7);
            return str + "G";
         }

         return formatDouble((Double)Double.valueOf(str), 1) + "G";
      }
   }

   public static String bytesFormatUnit(String str, String snmpUnit) {
      if (!StringUtils.isEmpty(str) && !"0".equals(str)) {
         if (str.indexOf(".") > -1) {
            str = str.substring(0, str.indexOf("."));
         }

         try {
            long bytes = Long.valueOf(str);
            if (!"byte".equals(snmpUnit)) {
               bytes *= 1024L;
            }

            int k = 1024;
            String[] sizes = new String[]{"B", "KB", "M", "G", "T", "P", "E", "Z", "Y"};
            int i = (int)Math.floor(Math.log((double)bytes) / Math.log((double)k));
            if (i > sizes.length - 1) {
               i = sizes.length - 1;
            }

            return formatDouble((Double)((double)bytes / Math.pow((double)k, (double)i)), 2) + sizes[i];
         } catch (Exception var7) {
            logger.error("bytesFormatUnit错误", var7);
            return str + "B";
         }
      } else {
         return "0B";
      }
   }

   public static String byteToM(String str, String snmpUnit) {
      if (StringUtils.isEmpty(str)) {
         return "0B";
      } else {
         double result = 0.0D;
         double mod = 1024.0D;

         try {
            double strDouble = Double.valueOf(str);
            if (!"byte".equals(snmpUnit)) {
               strDouble *= 1024.0D;
            }

            result = strDouble / mod / mod;
            return formatDouble((Double)result, 2) + "MB";
         } catch (Exception var8) {
            logger.error("kb转为M错误：", var8);
            return str + "KB";
         }
      }
   }

   public static double formatDouble(Double str, int num) {
      if (str == null) {
         return 0.0D;
      } else {
         BigDecimal b = new BigDecimal(str);
         double myNum3 = b.setScale(num, 4).doubleValue();
         return myNum3;
      }
   }

   public static double formatDouble(String str, int num) {
      if (StringUtils.isEmpty(str)) {
         return 0.0D;
      } else {
         Double strDou = Double.valueOf(str);
         BigDecimal b = new BigDecimal(strDou);
         double myNum3 = b.setScale(num, 4).doubleValue();
         return myNum3;
      }
   }

   public static String toLowerStr(String str) {
      return StringUtils.isEmpty(str) ? "" : str.toLowerCase();
   }

   public static String haveSqlDanger(String sql, String sqlInKeys) {
      if (StringUtils.isEmpty(sql)) {
         return "";
      } else {
         sql = sql.toLowerCase();
         sqlInKeys = sqlInKeys.toLowerCase();
         String[] sqlinkeys = sqlInKeys.split(",");
         String[] var3 = sqlinkeys;
         int var4 = sqlinkeys.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String sqlinkey = var3[var5];
            if (sql.indexOf(sqlinkey) > -1) {
               return sqlinkey;
            }
         }

         return "";
      }
   }

   public static String haveBlockDanger(String shell, String linuxBlock, String winBlock) {
      if (StringUtils.isEmpty(shell)) {
         return "";
      } else if (StringUtils.isEmpty(linuxBlock) && StringUtils.isEmpty(winBlock)) {
         return "";
      } else {
         shell = shell.toLowerCase();
         String[] blocks = linuxBlock.split(",");
         String[] var4 = blocks;
         int var5 = blocks.length;

         int var6;
         String blockStr;
         for(var6 = 0; var6 < var5; ++var6) {
            blockStr = var4[var6];
            if (shell.indexOf(blockStr) > -1) {
               return blockStr;
            }
         }

         blocks = winBlock.split(",");
         var4 = blocks;
         var5 = blocks.length;

         for(var6 = 0; var6 < var5; ++var6) {
            blockStr = var4[var6];
            if (shell.indexOf(blockStr) > -1) {
               return blockStr;
            }
         }

         return "";
      }
   }

   public static boolean validateExpression(String expression, Object value) {
      if (!StringUtils.isEmpty(expression) && null != value) {
         Expression compiledExp = AviatorEvaluator.compile(expression);
         Map<String, Object> env = new HashMap();
         env.put("result", value);
         Boolean result = (Boolean)compiledExp.execute(env);
         return result;
      } else {
         return false;
      }
   }
}
