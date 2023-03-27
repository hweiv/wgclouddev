package com.wgcloud.util;

import cn.hutool.core.collection.CollectionUtil;
import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.ChartInfo;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

public class HostUtil {
   private static final Logger logger = LoggerFactory.getLogger(HostUtil.class);
   private static CommonConfig commonConfig = (CommonConfig)ApplicationContextHelper.getBean(CommonConfig.class);
   private static AccountInfoService accountInfoService = (AccountInfoService)ApplicationContextHelper.getBean(AccountInfoService.class);
   private static SystemInfoService systemInfoService = (SystemInfoService)ApplicationContextHelper.getBean(SystemInfoService.class);

   public static void addAccountListModel(Model model) {
      try {
         if (!"true".equals(commonConfig.getUserInfoManage())) {
            return;
         }

         Map<String, Object> params = new HashMap();
         List<AccountInfo> accountInfoList = accountInfoService.selectAllByParams(params);
         if (!CollectionUtil.isEmpty(accountInfoList)) {
            model.addAttribute("accountList", accountInfoList);
         }
      } catch (Exception var3) {
         logger.error("addAccountListModel", var3);
      }

   }

   public static void addAccountquery(HttpServletRequest request, Map<String, Object> params) {
      if (null != request && null != params) {
         AccountInfo accountInfo = getAccountByRequest(request);
         if (null != accountInfo && !"admin".equals(accountInfo.getRole()) && !"guest".equals(accountInfo.getRole())) {
            params.put("account", accountInfo.getAccount());
         }

      }
   }

   public static void setDiskSumPer(List<DiskState> diskStates, SystemInfo systemInfo) {
      try {
         Double sumSize = 0.0D;
         Double useSize = 0.0D;
         Iterator var4 = diskStates.iterator();

         while(var4.hasNext()) {
            DiskState diskState = (DiskState)var4.next();
            if (!StringUtils.isEmpty(diskState.getDiskSize()) && !StringUtils.isEmpty(diskState.getUsed())) {
               sumSize = sumSize + Double.valueOf(diskState.getDiskSize().replace("G", ""));
               useSize = useSize + Double.valueOf(diskState.getUsed().replace("G", ""));
            }
         }

         systemInfo.setDiskPer(0.0D);
         if (sumSize != 0.0D) {
            systemInfo.setDiskPer(FormatUtil.formatDouble((Double)(useSize / sumSize * 100.0D), 2));
         }
      } catch (Exception var6) {
         logger.error("设置磁盘总使用率错误", var6);
      }

   }

   public static void setSysImage(SystemInfo systemInfo) {
      if (!StringUtils.isEmpty(systemInfo.getPlatForm())) {
         String platForm = systemInfo.getPlatForm().toLowerCase();
         if (platForm.contains("windows")) {
            systemInfo.setImage("/wgcloud/static/images/windows.png");
         } else if (platForm.contains("centos")) {
            systemInfo.setImage("/wgcloud/static/images/centos.png");
         } else if (platForm.contains("hat")) {
            systemInfo.setImage("/wgcloud/static/images/redhat.png");
         } else if (platForm.contains("ubuntu")) {
            systemInfo.setImage("/wgcloud/static/images/ubuntu.png");
         } else if (platForm.contains("debian")) {
            systemInfo.setImage("/wgcloud/static/images/debian.png");
         } else if (platForm.contains("darwin")) {
            systemInfo.setImage("/wgcloud/static/images/darwin.png");
         } else if (platForm.contains("android")) {
            systemInfo.setImage("/wgcloud/static/images/android.png");
         } else if (platForm.contains("suse")) {
            systemInfo.setImage("/wgcloud/static/images/suse.png");
         } else if (platForm.contains("fedora")) {
            systemInfo.setImage("/wgcloud/static/images/fedora.png");
         } else {
            systemInfo.setImage("/wgcloud/static/images/linux.png");
         }
      } else {
         systemInfo.setImage("/wgcloud/static/images/linux.png");
      }

   }

   public static List<ChartInfo> getSystemTypeList(List<SystemInfo> systemInfoList) {
      List<ChartInfo> chartInfoList = new ArrayList();
      Map<String, Integer> map = getSystemTypeMap(systemInfoList);
      Iterator var3 = map.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, Integer> entry = (Entry)var3.next();
         ChartInfo chartInfo = new ChartInfo();
         chartInfo.setItem((String)entry.getKey());
         chartInfo.setCount((Integer)entry.getValue());
         chartInfoList.add(chartInfo);
      }

      return chartInfoList;
   }

   public static Map<String, Integer> getSystemTypeMap(List<SystemInfo> systemInfoList) {
      Map<String, Integer> map = new HashMap();
      Iterator var2 = systemInfoList.iterator();

      while(var2.hasNext()) {
         SystemInfo systemInfo = (SystemInfo)var2.next();
         if (!StringUtils.isEmpty(systemInfo.getPlatForm())) {
            String platForm = systemInfo.getPlatForm().toLowerCase();
            if (platForm.contains("windows")) {
               if (map.get("windows") != null) {
                  map.put("windows", (Integer)map.get("windows") + 1);
               } else {
                  map.put("windows", 1);
               }
            } else if (platForm.contains("centos")) {
               if (map.get("centos") != null) {
                  map.put("centos", (Integer)map.get("centos") + 1);
               } else {
                  map.put("centos", 1);
               }
            } else if (platForm.contains("redhat")) {
               if (map.get("redhat") != null) {
                  map.put("redhat", (Integer)map.get("redhat") + 1);
               } else {
                  map.put("redhat", 1);
               }
            } else if (platForm.contains("ubuntu")) {
               if (map.get("ubuntu") != null) {
                  map.put("ubuntu", (Integer)map.get("ubuntu") + 1);
               } else {
                  map.put("ubuntu", 1);
               }
            } else if (platForm.contains("debian")) {
               if (map.get("debian") != null) {
                  map.put("debian", (Integer)map.get("debian") + 1);
               } else {
                  map.put("debian", 1);
               }
            } else if (platForm.contains("darwin")) {
               if (map.get("macOS") != null) {
                  map.put("macOS", (Integer)map.get("macOS") + 1);
               } else {
                  map.put("macOS", 1);
               }
            } else if (platForm.contains("android")) {
               if (map.get("android") != null) {
                  map.put("android", (Integer)map.get("android") + 1);
               } else {
                  map.put("android", 1);
               }
            } else if (map.get("其他Linux") != null) {
               map.put("其他Linux", (Integer)map.get("其他Linux") + 1);
            } else {
               map.put("其他Linux", 1);
            }
         } else if (map.get("其他Linux") != null) {
            map.put("其他Linux", (Integer)map.get("其他Linux") + 1);
         } else {
            map.put("其他Linux", 1);
         }
      }

      return map;
   }

   public static String addRemark(String hostname) {
      if (StringUtils.isEmpty(hostname)) {
         return "";
      } else {
         String remark = (String)StaticKeys.HOST_MAP.get(hostname);
         if (!StringUtils.isEmpty(remark)) {
            remark = "(" + remark + ")";
         } else {
            remark = "";
         }

         return remark;
      }
   }

   public static String getAccount(String hostname) {
      if (StringUtils.isEmpty(hostname)) {
         return "";
      } else {
         String account = (String)StaticKeys.HOST_ACCOUNT_MAP.get(hostname);
         return account;
      }
   }

   public static AccountInfo getAccountByRequest(HttpServletRequest request) {
      AccountInfo accountInfo = (AccountInfo)request.getSession().getAttribute("LOGIN_KEY");
      return accountInfo == null ? new AccountInfo() : accountInfo;
   }

   public static void clearCacheMap() {
      logger.info("清空缓存主机缓存的map");
      StaticKeys.HOST_MAP.clear();
      StaticKeys.HOST_ACCOUNT_MAP.clear();

      try {
         Map<String, Object> params = new HashMap();
         List<SystemInfo> list = systemInfoService.selectAllByParams(params);
         initHostCacheMap(list);
      } catch (Exception var2) {
         logger.error("clearCacheMap错误", var2);
      }

   }

   public static void initHostCacheMap(List<SystemInfo> list) throws Exception {
      try {
         if (!CollectionUtil.isEmpty(list)) {
            Iterator var1 = list.iterator();

            while(var1.hasNext()) {
               SystemInfo systemInfo = (SystemInfo)var1.next();
               if (!StringUtils.isEmpty(systemInfo.getRemark())) {
                  StaticKeys.HOST_MAP.put(systemInfo.getHostname(), systemInfo.getRemark());
               }

               if ("true".equals(commonConfig.getUserInfoManage()) && !StringUtils.isEmpty(systemInfo.getAccount()) && StaticKeys.LICENSE_STATE.equals("1")) {
                  StaticKeys.HOST_ACCOUNT_MAP.put(systemInfo.getHostname(), systemInfo.getAccount());
               }
            }
         }
      } catch (Exception var3) {
         logger.error("initHostCacheMap错误", var3);
      }

   }
}
