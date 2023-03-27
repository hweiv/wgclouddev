package com.wgcloud.util.license;

import cn.hutool.core.codec.Base64;
import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

public class LicenseUtil {
   private static final Logger logger = LoggerFactory.getLogger(LicenseUtil.class);
   private static LogInfoService logInfoService = (LogInfoService)ApplicationContextHelper.getBean(LogInfoService.class);
   public static final String RPO_MSG = "个人版最多监控10项，请点击页面底部网站，联系我们升级到专业版";
   public static final String RPO_OUT_TIME_MSG = "授权已过期，请点击页面底部网站，联系我们获取授权";
   public static final String RPO_REQUEST_MSG = "此功能需升级到专业版，请点击页面底部网站联系我们";

   private static String readLicFile(String path) {
      File file = new File(path);
      if (!file.exists()) {
         logger.info("未检测到授权文件----------------");
         return "";
      } else {
         BufferedReader br = null;

         try {
            br = new BufferedReader(new FileReader(path));
            String readLine = null;
            StringBuilder sb = new StringBuilder();

            while((readLine = br.readLine()) != null) {
               sb.append(readLine);
            }

            String var5 = sb.toString().trim();
            return var5;
         } catch (Exception var15) {
            var15.printStackTrace();
         } finally {
            if (br != null) {
               try {
                  br.close();
               } catch (IOException var14) {
                  var14.printStackTrace();
               }
            }

         }

         return "";
      }
   }

   public static String validateLicense(int agentNum, int pageSize, int pingNum) {
      try {
         String path = System.getProperty("user.dir");
         String licenseStr = readLicFile(path + "/license.txt");
         if (StringUtils.isEmpty(licenseStr)) {
            logger.info("没有检测到授权文件/server/license.txt，当前版本重置为个人版");
            return "0";
         } else {
            byte[] res = RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStr("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJgRaOg/qygEtsVWTl+35+RN77mmT6Pjl2v/UZExxH4LmVMFx2U2vHz53tSfWJ0sshuPbn2Y5QOst6BEwrKHNL4/w/Iqc/Rv4Q8hiUIQZtd/M1Yq82+uRKsdmIi6gXLZXQmoUjRTmssGutNonRcnyeowpE4LX6YV1B8Dh8vdgXuwIDAQAB"), Base64.decode(licenseStr));
            String restr = new String(res);
            if (StringUtils.isEmpty(restr)) {
               logger.info("license解密公钥为空");
               return "0";
            } else {
               String[] restrs = restr.split("-");
               String date = restrs[0];
               String num = restrs[1];
               String name = restrs[2];
               StaticKeys.LICENSE_DATE = date;
               StaticKeys.LICENSE_NUM = Integer.valueOf(num);
               StaticKeys.LICENSE_NAME = name;
               logger.info("license解析成功：到期时间" + (StaticKeys.LICENSE_DATE.startsWith("2099") ? "永久授权" : StaticKeys.LICENSE_DATE) + "，授权节点数量" + StaticKeys.LICENSE_NUM + "，客户名称" + StaticKeys.LICENSE_NAME);
               long LICENSE_DATE = Long.valueOf(date);
               if (agentNum <= StaticKeys.LICENSE_NUM && pingNum <= StaticKeys.LICENSE_NUM) {
                  Long nowDate = Long.valueOf(DateUtil.getCurrentDate().replace("-", ""));
                  if (nowDate > LICENSE_DATE) {
                     logInfoService.save("授权解析错误", "授权已经到期：" + date, "2");
                     logger.info("授权已经到期");
                     return "2";
                  } else {
                     StaticKeys.LICENSE_PAGE = StaticKeys.LICENSE_NUM / pageSize + 1;
                     return "1";
                  }
               } else {
                  logInfoService.save("授权解析错误", "监控节点超过授权节点数量：" + num + "，当前监控节点数量：" + agentNum, "2");
                  logger.info("监控节点超过授权节点数量，当前监控节点数量" + agentNum + "，数通PING数量" + pingNum);
                  return "3";
               }
            }
         }
      } catch (Exception var14) {
         logger.error("解析授权文件错误：", var14);
         return "0";
      }
   }

   public static void checkHostList(SystemInfo systemInfo, Model model) {
      if (systemInfo.getPage() > StaticKeys.LICENSE_PAGE && !StaticKeys.LICENSE_STATE.equals("1")) {
         if (StaticKeys.LICENSE_STATE.equals("0")) {
            systemInfo.setPage(1);
            systemInfo.setPageSize(10);
            model.addAttribute("msg", "个人版最多监控10项，请点击页面底部网站，联系我们升级到专业版");
         } else if (StaticKeys.LICENSE_STATE.equals("2")) {
            systemInfo.setPage(1);
            systemInfo.setPageSize(10);
            model.addAttribute("msg", "授权已过期，请点击页面底部网站，联系我们获取授权");
         } else if (StaticKeys.LICENSE_STATE.equals("3")) {
            systemInfo.setPage(StaticKeys.LICENSE_PAGE);
            model.addAttribute("msg", "监控节点数量已超过授权节点数量，授权节点数量" + StaticKeys.LICENSE_NUM + "，请点击页面底部网站，联系我们升级授权");
         }
      }

      if (StaticKeys.LICENSE_STATE.equals("0") || StaticKeys.LICENSE_STATE.equals("2")) {
         systemInfo.setPageSize(10);
      }

   }

   public static void maxLicense_10(Model model, HttpServletRequest request, Object obj) {
      try {
         String liceFlage = request.getParameter(StaticKeys.LICENSE_LICE_FLAGE);
         Class superClazz = obj.getClass().getSuperclass();
         Method pageGetMethod = superClazz.getDeclaredMethod("getPage");
         Integer page = (Integer)pageGetMethod.invoke(obj);
         Method pageSetMethod = superClazz.getDeclaredMethod("setPage", Integer.class);
         Method pageSizeSetMethod = superClazz.getDeclaredMethod("setPageSize", Integer.class);
         if (!StringUtils.isEmpty(liceFlage)) {
            if ("2".equals(liceFlage)) {
               model.addAttribute("msg", "此功能需升级到专业版，请点击页面底部网站联系我们");
               pageSizeSetMethod.invoke(obj, 10);
               return;
            }

            if (!StaticKeys.LICENSE_STATE.equals("1")) {
               if (StaticKeys.LICENSE_STATE.equals("0")) {
                  model.addAttribute("msg", "个人版最多监控10项，请点击页面底部网站，联系我们升级到专业版");
                  pageSizeSetMethod.invoke(obj, 10);
               } else if (StaticKeys.LICENSE_STATE.equals("2")) {
                  model.addAttribute("msg", "授权已过期，请点击页面底部网站，联系我们获取授权");
                  pageSizeSetMethod.invoke(obj, 10);
               } else {
                  model.addAttribute("msg", "监控节点数量已超过授权节点数量，授权节点数量" + StaticKeys.LICENSE_NUM + "，请点击页面底部网站，联系我们升级授权");
               }
            }
         } else {
            if (page > StaticKeys.LICENSE_PAGE && !StaticKeys.LICENSE_STATE.equals("1")) {
               if (StaticKeys.LICENSE_STATE.equals("0")) {
                  pageSetMethod.invoke(obj, 1);
                  pageSizeSetMethod.invoke(obj, 10);
                  model.addAttribute("msg", "个人版最多监控10项，请点击页面底部网站，联系我们升级到专业版");
               } else if (StaticKeys.LICENSE_STATE.equals("2")) {
                  pageSetMethod.invoke(obj, 1);
                  pageSizeSetMethod.invoke(obj, 10);
                  model.addAttribute("msg", "授权已过期，请点击页面底部网站，联系我们获取授权");
               }
            }

            if (StaticKeys.LICENSE_STATE.equals("0") || StaticKeys.LICENSE_STATE.equals("2")) {
               pageSizeSetMethod.invoke(obj, 10);
            }
         }
      } catch (Exception var9) {
         logger.error("解析通用资源监控数量错误：", var9);
      }

   }
}
