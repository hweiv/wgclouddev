package com.wgcloud.util;

import com.wgcloud.controller.LoginController;
import java.net.InetAddress;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IpUtil {
   private static final String UNKNOWN = "unknown";
   private static final String LOCALHOST = "127.0.0.1";
   private static final String SEPARATOR = ",";
   private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

   public static String getIpAddr(HttpServletRequest request) {
      String ipAddress;
      try {
         ipAddress = request.getHeader("x-forwarded-for");
         if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
         }

         if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
         }

         if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress)) {
               InetAddress inet = InetAddress.getLocalHost();
               ipAddress = inet.getHostAddress();
            }
         }

         if (ipAddress != null && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
         }
      } catch (Exception var3) {
         ipAddress = "";
         logger.error("获取登录请求IP错误", var3);
      }

      return ipAddress;
   }
}
