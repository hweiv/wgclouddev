package com.wgcloud.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingUtil {
   private static final Logger logger = LoggerFactory.getLogger(PingUtil.class);

   public static long ping(String ipAddress, int count, int timeOut) {
      BufferedReader in = null;
      Runtime r = Runtime.getRuntime();
      String osName = System.getProperty("os.name");
      String pingCommand;
      if (osName.contains("Windows")) {
         pingCommand = "ping " + ipAddress + " -n " + count + " -w " + timeOut * 1000;
      } else {
         pingCommand = "ping  -c " + count + " -w " + timeOut + " " + ipAddress;
      }

      Integer diffTimes = -1;

      long var9;
      try {
         Process p = r.exec(pingCommand);
         if (p != null) {
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));

            do {
               String line;
               if ((line = in.readLine()) == null) {
                  return (long)diffTimes;
               }

               diffTimes = getCheckResult(line);
            } while(diffTimes == -1);

            return (long)diffTimes;
         }

         var9 = -1L;
      } catch (Exception var21) {
         logger.error("数通设备ping错误：", var21);
         var9 = -1L;
         return var9;
      } finally {
         try {
            in.close();
         } catch (IOException var20) {
            var20.printStackTrace();
         }

      }

      return var9;
   }

   private static Integer getCheckResult(String line) {
      Integer times = -1;
      if (StringUtils.isEmpty(line)) {
         return times;
      } else {
         line = line.toLowerCase();
         if (line.contains("ttl=")) {
            line = line.replace(" ", "");
            String regInt = "(=\\d+ms)";
            Pattern patternInt = Pattern.compile(regInt);
            Matcher matcherInt = patternInt.matcher(line);
            String regDouble;
            if (matcherInt.find()) {
               regDouble = matcherInt.group(1);
               logger.debug(line + "--------------" + regDouble);
               regDouble = regDouble.replace("=", "").replace("ms", "");
               times = (new Double(regDouble)).intValue();
               if (times < 1) {
                  times = 1;
               }

               return times;
            }

            regDouble = "(=\\d+.\\d+ms)";
            Pattern patternDouble = Pattern.compile(regDouble);
            Matcher matcherDouble = patternDouble.matcher(line);
            if (matcherDouble.find()) {
               String groupStr = matcherDouble.group(1);
               logger.debug(line + "--------------" + groupStr);
               groupStr = groupStr.replace("=", "").replace("ms", "");
               times = (new Double(groupStr)).intValue();
               if (times < 1) {
                  times = 1;
               }

               return times;
            }

            times = 1;
         }

         return times;
      }
   }
}
