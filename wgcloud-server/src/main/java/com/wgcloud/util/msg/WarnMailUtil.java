package com.wgcloud.util.msg;

import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.dto.HostWarnDiyDto;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.entity.CustomInfo;
import com.wgcloud.entity.DiskSmart;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.DockerInfo;
import com.wgcloud.entity.FileSafe;
import com.wgcloud.entity.FileWarnInfo;
import com.wgcloud.entity.MemState;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.entity.PortInfo;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class WarnMailUtil {
   private static final Logger logger = LoggerFactory.getLogger(WarnMailUtil.class);
   private static CommonConfig commonConfig = (CommonConfig)ApplicationContextHelper.getBean(CommonConfig.class);
   private static LogInfoService logInfoService = (LogInfoService)ApplicationContextHelper.getBean(LogInfoService.class);
   private static MailConfig mailConfig = (MailConfig)ApplicationContextHelper.getBean(MailConfig.class);

   private static boolean preWarnInit(String hostname, String warnMail, String warnKey) {
      if (!StringUtils.isEmpty(mailConfig.getBlockIps()) && mailConfig.getBlockIps().contains(hostname)) {
         return false;
      } else if (!"false".equals(mailConfig.getAllWarnMail()) && !"false".equals(warnMail)) {
         return !WarnPools.isExpireWarnTime(warnKey, commonConfig.getWarnCacheTimes());
      } else {
         return false;
      }
   }

   public static boolean sendWarnInfo(MemState memState) {
      String key = memState.getHostname() + "_mem";
      boolean sign = preWarnInit(memState.getHostname(), mailConfig.getMemWarnMail(), key);
      if (!sign) {
         return false;
      } else {
         Double memWarnVal = mailConfig.getMemWarnVal();
         HostWarnDiyDto hostWarnDiyDto = (HostWarnDiyDto)StaticKeys.HOST_WARN_MAP.get(memState.getHostname());
         if (null != hostWarnDiyDto) {
            if ("no".equals(hostWarnDiyDto.getMemWarnMail())) {
               return false;
            }

            if (null != hostWarnDiyDto.getMemWarnVal()) {
               memWarnVal = hostWarnDiyDto.getMemWarnVal();
            }
         }

         if (memState.getUsePer() != null && memState.getUsePer() >= memWarnVal) {
            try {
               String remark = HostUtil.addRemark(memState.getHostname());
               String title = "内存告警：" + memState.getHostname() + remark;
               String commContent = "主机：" + memState.getHostname() + remark + "，当前内存使用率为" + memState.getUsePer() + "，超过告警值" + memWarnVal;
               String account = getAccount(memState.getHostname());
               sendUtil(title, commContent, account, key, true);
            } catch (Exception var9) {
               logger.error("发送内存告警邮件错误：", var9);
               logInfoService.save("发送内存告警邮件错误", var9.toString(), "1");
            }
         }

         return false;
      }
   }

   public static boolean sendSysLoadWarnInfo(SysLoadState sysLoadState) {
      String key = sysLoadState.getHostname() + "_load";
      boolean sign = preWarnInit(sysLoadState.getHostname(), mailConfig.getSysLoadWarnMail(), key);
      if (!sign) {
         return false;
      } else {
         Double sysLoadWarnVal = mailConfig.getSysLoadWarnVal();
         HostWarnDiyDto hostWarnDiyDto = (HostWarnDiyDto)StaticKeys.HOST_WARN_MAP.get(sysLoadState.getHostname());
         if (null != hostWarnDiyDto) {
            if ("no".equals(hostWarnDiyDto.getSysLoadWarnMail())) {
               return false;
            }

            if (null != hostWarnDiyDto.getSysLoadWarnVal()) {
               sysLoadWarnVal = hostWarnDiyDto.getSysLoadWarnVal();
            }
         }

         if (sysLoadState.getFiveLoad() != null && sysLoadState.getFiveLoad() >= sysLoadWarnVal) {
            try {
               String remark = HostUtil.addRemark(sysLoadState.getHostname());
               String title = "系统负载(5分钟)告警：" + sysLoadState.getHostname() + remark;
               String commContent = "主机：" + sysLoadState.getHostname() + remark + "，当前系统负载(5分钟)为" + sysLoadState.getFiveLoad() + "，超过告警值" + sysLoadWarnVal;
               String account = getAccount(sysLoadState.getHostname());
               sendUtil(title, commContent, account, key, true);
            } catch (Exception var9) {
               logger.error("发送系统负载(5分钟)告警邮件错误：", var9);
               logInfoService.save("发送系统负载(5分钟)告警邮件错误", var9.toString(), "1");
            }
         }

         return false;
      }
   }

   public static boolean sendCpuWarnInfo(CpuState cpuState) {
      String key = cpuState.getHostname() + "_cpu";
      boolean sign = preWarnInit(cpuState.getHostname(), mailConfig.getCpuWarnMail(), key);
      if (!sign) {
         return false;
      } else {
         Double cpuWarnVal = mailConfig.getCpuWarnVal();
         HostWarnDiyDto hostWarnDiyDto = (HostWarnDiyDto)StaticKeys.HOST_WARN_MAP.get(cpuState.getHostname());
         if (null != hostWarnDiyDto) {
            if ("no".equals(hostWarnDiyDto.getCpuWarnMail())) {
               return false;
            }

            if (null != hostWarnDiyDto.getCpuWarnVal()) {
               cpuWarnVal = hostWarnDiyDto.getCpuWarnVal();
            }
         }

         if (cpuState.getSys() != null && cpuState.getSys() >= cpuWarnVal) {
            try {
               String remark = HostUtil.addRemark(cpuState.getHostname());
               String title = "CPU告警：" + cpuState.getHostname() + remark;
               String commContent = "主机：" + cpuState.getHostname() + remark + "，当前CPU使用率为" + cpuState.getSys() + "，超过告警值" + cpuWarnVal;
               String account = getAccount(cpuState.getHostname());
               sendUtil(title, commContent, account, key, true);
            } catch (Exception var9) {
               logger.error("发送内存告警邮件错误：", var9);
               logInfoService.save("发送内存告警邮件错误", var9.toString(), "1");
            }
         }

         return false;
      }
   }

   public static boolean sendUpSpeedWarnInfo(NetIoState netIoState) {
      String key = netIoState.getHostname() + "_txbyt";
      Double upSpeedVal = mailConfig.getUpSpeedVal();
      Double upSpeedMinVal = mailConfig.getUpSpeedMinVal();
      HostWarnDiyDto hostWarnDiyDto = (HostWarnDiyDto)StaticKeys.HOST_WARN_MAP.get(netIoState.getHostname());
      if (null != hostWarnDiyDto) {
         if ("no".equals(hostWarnDiyDto.getUpSpeedMail())) {
            return false;
         }

         if (null != hostWarnDiyDto.getUpSpeedVal()) {
            upSpeedVal = hostWarnDiyDto.getUpSpeedVal();
         }

         if (null != hostWarnDiyDto.getUpSpeedMinVal()) {
            upSpeedMinVal = hostWarnDiyDto.getUpSpeedMinVal();
         }
      }

      boolean sign;
      String remark;
      String title;
      String commContent;
      String account;
      if (!StringUtils.isEmpty(netIoState.getTxbyt()) && Double.valueOf(netIoState.getTxbyt()) >= upSpeedVal) {
         try {
            sign = preWarnInit(netIoState.getHostname(), mailConfig.getUpSpeedMail(), key + "_max");
            if (!sign) {
               return false;
            }

            remark = HostUtil.addRemark(netIoState.getHostname());
            title = "超过上行传输速率告警：" + netIoState.getHostname() + remark;
            commContent = "主机：" + netIoState.getHostname() + remark + "，当前上行传输速率为" + FormatUtil.kbToM(netIoState.getTxbyt()) + "/s，超过告警值" + FormatUtil.kbToM(upSpeedVal + "") + "/s";
            account = getAccount(netIoState.getHostname());
            sendUtil(title, commContent, account, key + "_max", true);
         } catch (Exception var11) {
            logger.error("发送超过上行传输速率告警邮件错误：", var11);
            logInfoService.save("发送超过上行传输速率告警邮件错误", var11.toString(), "1");
         }
      }

      if (!StringUtils.isEmpty(netIoState.getTxbyt()) && Double.valueOf(netIoState.getTxbyt()) < upSpeedMinVal) {
         try {
            sign = preWarnInit(netIoState.getHostname(), mailConfig.getUpSpeedMail(), key + "_min");
            if (!sign) {
               return false;
            }

            remark = HostUtil.addRemark(netIoState.getHostname());
            title = "低于上行传输速率告警：" + netIoState.getHostname() + remark;
            commContent = "主机：" + netIoState.getHostname() + remark + "，当前上行传输速率为" + FormatUtil.kbToM(netIoState.getTxbyt()) + "/s，低于告警值" + FormatUtil.kbToM(upSpeedVal + "") + "/s";
            account = getAccount(netIoState.getHostname());
            sendUtil(title, commContent, account, key + "_min", true);
         } catch (Exception var10) {
            logger.error("发送低于上行传输速率告警邮件错误：", var10);
            logInfoService.save("发送低于上行传输速率告警邮件错误", var10.toString(), "1");
         }
      }

      return false;
   }

   public static boolean sendDownSpeedWarnInfo(NetIoState netIoState) {
      String key = netIoState.getHostname() + "_rxbyt";
      Double downSpeedVal = mailConfig.getDownSpeedVal();
      Double downSpeedMinVal = mailConfig.getDownSpeedMinVal();
      HostWarnDiyDto hostWarnDiyDto = (HostWarnDiyDto)StaticKeys.HOST_WARN_MAP.get(netIoState.getHostname());
      if (null != hostWarnDiyDto) {
         if ("no".equals(hostWarnDiyDto.getDownSpeedMail())) {
            return false;
         }

         if (null != hostWarnDiyDto.getDownSpeedVal()) {
            downSpeedVal = hostWarnDiyDto.getDownSpeedVal();
         }

         if (null != hostWarnDiyDto.getDownSpeedMinVal()) {
            downSpeedMinVal = hostWarnDiyDto.getDownSpeedMinVal();
         }
      }

      boolean sign;
      String remark;
      String title;
      String commContent;
      String account;
      if (!StringUtils.isEmpty(netIoState.getRxbyt()) && Double.valueOf(netIoState.getRxbyt()) >= downSpeedVal) {
         try {
            sign = preWarnInit(netIoState.getHostname(), mailConfig.getDownSpeedMail(), key + "_max");
            if (!sign) {
               return false;
            }

            remark = HostUtil.addRemark(netIoState.getHostname());
            title = "超过下行传输速率告警：" + netIoState.getHostname() + remark;
            commContent = "主机：" + netIoState.getHostname() + remark + "，当前下行传输速率为" + FormatUtil.kbToM(netIoState.getRxbyt()) + "/s，超过告警值" + FormatUtil.kbToM(downSpeedVal + "") + "/s";
            account = getAccount(netIoState.getHostname());
            sendUtil(title, commContent, account, key + "_max", true);
         } catch (Exception var11) {
            logger.error("发送超过下行传输速率告警邮件错误：", var11);
            logInfoService.save("发送超过下行传输速率告警邮件错误", var11.toString(), "1");
         }
      }

      if (!StringUtils.isEmpty(netIoState.getRxbyt()) && Double.valueOf(netIoState.getRxbyt()) < downSpeedMinVal) {
         try {
            sign = preWarnInit(netIoState.getHostname(), mailConfig.getDownSpeedMail(), key + "_min");
            if (!sign) {
               return false;
            }

            remark = HostUtil.addRemark(netIoState.getHostname());
            title = "低于下行传输速率告警：" + netIoState.getHostname() + remark;
            commContent = "主机：" + netIoState.getHostname() + remark + "，当前下行传输速率为" + FormatUtil.kbToM(netIoState.getRxbyt()) + "/s，低于告警值" + FormatUtil.kbToM(downSpeedMinVal + "") + "/s";
            account = getAccount(netIoState.getHostname());
            sendUtil(title, commContent, account, key + "_min", true);
         } catch (Exception var10) {
            logger.error("发送低于下行传输速率告警邮件错误：", var10);
            logInfoService.save("发送低于下行传输速率告警邮件错误", var10.toString(), "1");
         }
      }

      return false;
   }

   public static boolean sendDiskSmartWarnInfo(DiskSmart smartBean) {
      String key = smartBean.getHostname() + "_smart";
      boolean sign = preWarnInit(smartBean.getHostname(), mailConfig.getSmartWarnMail(), key);
      if (!sign) {
         return false;
      } else {
         HostWarnDiyDto hostWarnDiyDto = (HostWarnDiyDto)StaticKeys.HOST_WARN_MAP.get(smartBean.getHostname());
         if (null != hostWarnDiyDto && "no".equals(hostWarnDiyDto.getSmartWarnMail())) {
            return false;
         } else {
            if (!StringUtils.isEmpty(smartBean.getDiskState()) && "FAILED".equals(smartBean.getDiskState())) {
               try {
                  String remark = HostUtil.addRemark(smartBean.getHostname());
                  String title = "磁盘告警SMART：" + smartBean.getHostname() + remark;
                  String commContent = "主机：" + smartBean.getHostname() + remark + "，磁盘" + smartBean.getFileSystem() + "，SMART健康检测结果为" + "FAILED";
                  String account = getAccount(smartBean.getHostname());
                  sendUtil(title, commContent, account, key, true);
               } catch (Exception var8) {
                  logger.error("发送磁盘告警SMART邮件错误：", var8);
                  logInfoService.save("发送磁盘告警SMART邮件错误", var8.toString(), "1");
               }
            }

            return false;
         }
      }
   }

   public static boolean sendDiskWarnInfo(DiskState deskState) {
      logger.debug("告警磁盘-------------" + deskState.getFileSystem());
      String key = deskState.getHostname() + "_disk_" + deskState.getFileSystem();
      Double diskWarnVal = mailConfig.getDiskWarnVal();
      HostWarnDiyDto hostWarnDiyDto = (HostWarnDiyDto)StaticKeys.HOST_WARN_MAP.get(deskState.getHostname());
      if (null != hostWarnDiyDto) {
         if ("no".equals(hostWarnDiyDto.getDiskWarnMail())) {
            return false;
         }

         if (null != hostWarnDiyDto.getDiskWarnVal()) {
            diskWarnVal = hostWarnDiyDto.getDiskWarnVal();
         }
      }

      Double usePer = Double.valueOf(deskState.getUsePer().replace("%", ""));
      String remark;
      String title;
      String commContent;
      if (usePer != null && usePer < diskWarnVal && null != WarnPools.MEM_WARN_MAP.get(key)) {
         try {
//            String remark = HostUtil.addRemark(deskState.getHostname());
            String remake1 = HostUtil.addRemark(deskState.getHostname());
            remark = "磁盘已恢复：" + deskState.getHostname() + remake1;
            title = "主机磁盘已恢复：" + deskState.getHostname() + remake1 + "，磁盘" + deskState.getFileSystem() + "使用率为" + usePer + "，未达到告警值" + diskWarnVal;
            commContent = getAccount(deskState.getHostname());
            sendUtil(remark, title, commContent, key, false);
         } catch (Exception var10) {
            logger.error("发送磁盘已恢复邮件错误：", var10);
            logInfoService.save("发送磁盘已恢复邮件错误", var10.toString(), "1");
         }

         return false;
      } else {
         boolean sign = preWarnInit(deskState.getHostname(), mailConfig.getDiskWarnMail(), key);
         if (!sign) {
            return false;
         } else if (blockDisk(deskState)) {
            return false;
         } else {
            if (usePer != null && usePer >= diskWarnVal) {
               try {
                  remark = HostUtil.addRemark(deskState.getHostname());
                  title = "磁盘告警：" + deskState.getHostname() + remark;
                  commContent = "主机磁盘告警：" + deskState.getHostname() + remark + "，磁盘" + deskState.getFileSystem() + "使用率为" + usePer + "，超过告警值" + diskWarnVal;
                  String account = getAccount(deskState.getHostname());
                  sendUtil(title, commContent, account, key, true);
               } catch (Exception var11) {
                  logger.error("发送磁盘告警邮件错误：", var11);
                  logInfoService.save("发送磁盘告警邮件错误", var11.toString(), "1");
               }
            }

            return false;
         }
      }
   }

   private static boolean blockDisk(DiskState deskState) {
      String diskBlock = mailConfig.getDiskBlock();
      HostWarnDiyDto hostWarnDiyDto = (HostWarnDiyDto)StaticKeys.HOST_WARN_MAP.get(deskState.getHostname());
      if (null != hostWarnDiyDto && !StringUtils.isEmpty(hostWarnDiyDto.getDiskBlock())) {
         diskBlock = hostWarnDiyDto.getDiskBlock();
      }

      if (!StringUtils.isEmpty(diskBlock)) {
         String[] blocks = diskBlock.split(",");
         PathMatcher pm = new AntPathMatcher();
         String[] var5 = blocks;
         int var6 = blocks.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String diskBlcok = var5[var7];
            diskBlcok = diskBlcok.replace("'", "");
            if ("/".equals(deskState.getFileSystem())) {
               if (diskBlcok.equals(deskState.getFileSystem())) {
                  return true;
               }
            } else {
               boolean matchStart = pm.matchStart(diskBlcok, deskState.getFileSystem());
               if (matchStart) {
                  return matchStart;
               }
            }
         }
      }

      return false;
   }

   public static boolean sendCpuTemperatures(CpuTemperatures cpuTemperatures) {
      String key = cpuTemperatures.getHostname() + "_temperatures";
      boolean sign = preWarnInit(cpuTemperatures.getHostname(), mailConfig.getCpuTemperatureWarnMail(), key);
      if (!sign) {
         return false;
      } else {
         Double cpuTemperatureWarnVal = mailConfig.getCpuTemperatureWarnVal();
         HostWarnDiyDto hostWarnDiyDto = (HostWarnDiyDto)StaticKeys.HOST_WARN_MAP.get(cpuTemperatures.getHostname());
         if (null != hostWarnDiyDto) {
            if ("no".equals(hostWarnDiyDto.getCpuTemperatureWarnMail())) {
               return false;
            }

            if (null != hostWarnDiyDto.getCpuTemperatureWarnVal()) {
               cpuTemperatureWarnVal = hostWarnDiyDto.getCpuTemperatureWarnVal();
            }
         }

         Double inputVal = Double.valueOf(cpuTemperatures.getInput().replace("℃", "").replace("+", ""));
         if (inputVal != null && inputVal >= cpuTemperatureWarnVal) {
            try {
               String remark = HostUtil.addRemark(cpuTemperatures.getHostname());
               String title = "CPU温度告警：" + cpuTemperatures.getHostname() + remark;
               String commContent = "主机：" + cpuTemperatures.getHostname() + remark + "，CPU当前温度为" + cpuTemperatures.getInput() + "，超过告警值" + cpuTemperatureWarnVal + "℃";
               String account = getAccount(cpuTemperatures.getHostname());
               sendUtil(title, commContent, account, key, true);
            } catch (Exception var10) {
               logger.error("发送CPU温度告警邮件错误：", var10);
               logInfoService.save("发送CPU温度告警邮件错误", var10.toString(), "1");
            }
         }

         return false;
      }
   }

   public static boolean sendHostDown(SystemInfo systemInfo, boolean isDown) {
      if (!StringUtils.isEmpty(mailConfig.getBlockIps()) && mailConfig.getBlockIps().contains(systemInfo.getHostname())) {
         return false;
      } else if (!"false".equals(mailConfig.getAllWarnMail()) && !"false".equals(mailConfig.getHostDownWarnMail())) {
         HostWarnDiyDto hostWarnDiyDto = (HostWarnDiyDto)StaticKeys.HOST_WARN_MAP.get(systemInfo.getHostname());
         if (null != hostWarnDiyDto && "no".equals(hostWarnDiyDto.getHostDownWarnMail())) {
            return false;
         } else {
            String key = systemInfo.getId();
            String remark = (String)StaticKeys.HOST_MAP.get(systemInfo.getHostname());
            if (!StringUtils.isEmpty(remark)) {
               remark = "(" + remark + ")";
            } else {
               remark = "";
            }

            String title;
            String commContent;
            if (isDown) {
               if (WarnPools.isExpireWarnTime(key, commonConfig.getWarnCacheTimes())) {
                  return false;
               }

               try {
                  title = "主机下线告警：" + systemInfo.getHostname() + remark;
                  commContent = "主机可能已下线：" + systemInfo.getHostname() + remark;
                  sendUtil(title, commContent, systemInfo.getAccount(), key, isDown);
               } catch (Exception var8) {
                  logger.error("发送主机下线告警邮件失败：", var8);
                  logInfoService.save("发送主机下线告警邮件错误", var8.toString(), "1");
               }
            } else {
               try {
                  title = "主机已恢复上线：" + systemInfo.getHostname() + remark;
                  commContent = "主机已恢复上线：" + systemInfo.getHostname() + remark;
                  sendUtil(title, commContent, systemInfo.getAccount(), key, isDown);
               } catch (Exception var7) {
                  logger.error("发送主机恢复上线通知邮件错误：", var7);
                  logInfoService.save("发送主机恢复上线通知邮件错误", var7.toString(), "1");
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean sendAppDown(AppInfo appInfo, boolean isDown) {
      if (!StringUtils.isEmpty(mailConfig.getBlockIps()) && mailConfig.getBlockIps().contains(appInfo.getHostname())) {
         return false;
      } else if (!"false".equals(mailConfig.getAllWarnMail()) && !"false".equals(mailConfig.getAppDownWarnMail())) {
         String key = appInfo.getId();
         String remark = (String)StaticKeys.HOST_MAP.get(appInfo.getHostname());
         if (!StringUtils.isEmpty(remark)) {
            remark = "(" + remark + ")";
         } else {
            remark = "";
         }

         String title;
         String commContent;
         String account;
         if (isDown) {
            if (WarnPools.isExpireWarnTime(key, commonConfig.getWarnCacheTimes())) {
               return false;
            }

            try {
               title = "进程下线告警：" + appInfo.getAppName() + "，" + appInfo.getHostname() + remark;
               commContent = "进程可能已下线：" + appInfo.getHostname() + remark + "，进程：" + appInfo.getAppName();
               account = getAccount(appInfo.getHostname());
               sendUtil(title, commContent, account, key, isDown);
            } catch (Exception var8) {
               logger.error("发送进程下线告警邮件失败：", var8);
               logInfoService.save("发送进程下线告警错误", var8.toString(), "1");
            }
         } else {
            try {
               title = "进程已恢复上线：" + appInfo.getAppName() + "，" + appInfo.getHostname() + remark;
               commContent = "进程已恢复上线：" + appInfo.getHostname() + remark + "，进程：" + appInfo.getAppName();
               account = getAccount(appInfo.getHostname());
               sendUtil(title, commContent, account, key, isDown);
            } catch (Exception var7) {
               logger.error("发送进程恢复上线通知邮件失败：", var7);
               logInfoService.save("发送进程恢复上线通知错误", var7.toString(), "1");
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean sendDockerDown(DockerInfo dockerInfo, boolean isDown) {
      if (!StringUtils.isEmpty(mailConfig.getBlockIps()) && mailConfig.getBlockIps().contains(dockerInfo.getHostname())) {
         return false;
      } else if (!"false".equals(mailConfig.getAllWarnMail()) && !"false".equals(mailConfig.getDockerDownWarnMail())) {
         String key = dockerInfo.getId();
         String remark = (String)StaticKeys.HOST_MAP.get(dockerInfo.getHostname());
         if (!StringUtils.isEmpty(remark)) {
            remark = "(" + remark + ")";
         } else {
            remark = "";
         }

         String dockerTypeStr = "CONTAINER ID";
         if ("2".equals(dockerInfo.getAppType())) {
            dockerTypeStr = "CONTAINER NAME";
         }

         String title;
         String commContent;
         String account;
         if (isDown) {
            if (WarnPools.isExpireWarnTime(key, commonConfig.getWarnCacheTimes())) {
               return false;
            }

            try {
               title = "docker下线告警：" + dockerInfo.getDockerName() + "，" + dockerInfo.getHostname() + remark;
               commContent = "docker可能已下线：" + dockerInfo.getHostname() + remark + "，名称：" + dockerInfo.getDockerName() + "，" + dockerTypeStr + "：" + dockerInfo.getDockerId();
               account = getAccount(dockerInfo.getHostname());
               sendUtil(title, commContent, account, key, isDown);
            } catch (Exception var9) {
               logger.error("发送docker下线告警邮件失败：", var9);
               logInfoService.save("发送docker下线告警错误", var9.toString(), "1");
            }
         } else {
            WarnPools.MEM_WARN_MAP.remove(key);

            try {
               title = "docker已恢复上线：" + dockerInfo.getDockerName() + "，" + dockerInfo.getHostname() + remark;
               commContent = "docker已恢复上线：" + dockerInfo.getHostname() + remark + "，名称：" + dockerInfo.getDockerName() + "，" + dockerTypeStr + "：" + dockerInfo.getDockerId();
               account = getAccount(dockerInfo.getHostname());
               sendUtil(title, commContent, account, key, isDown);
            } catch (Exception var8) {
               logger.error("发送docker已恢复上线邮件失败：", var8);
               logInfoService.save("发送docker已恢复上线错误", var8.toString(), "1");
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean sendPortDown(PortInfo portInfo, boolean isDown) {
      if (!StringUtils.isEmpty(mailConfig.getBlockIps()) && mailConfig.getBlockIps().contains(portInfo.getHostname())) {
         return false;
      } else if (!"false".equals(mailConfig.getAllWarnMail()) && !"false".equals(mailConfig.getPortWarnMail())) {
         String key = portInfo.getId();
         String remark = (String)StaticKeys.HOST_MAP.get(portInfo.getHostname());
         if (!StringUtils.isEmpty(remark)) {
            remark = "(" + remark + ")";
         } else {
            remark = "";
         }

         String telnetIp = portInfo.getTelnetIp();
         if (!StringUtils.isEmpty(telnetIp) && telnetIp.length() > 50) {
            telnetIp = telnetIp.substring(0, 50);
         }

         String title;
         String commContent;
         String account;
         if (isDown) {
            if (WarnPools.isExpireWarnTime(key, commonConfig.getWarnCacheTimes())) {
               return false;
            }

            try {
               title = "端口telnet不通告警：" + portInfo.getPortName() + "，telnet-" + telnetIp + "-" + portInfo.getPort() + "，" + portInfo.getHostname() + remark;
               commContent = "端口telnet不通，名称：" + portInfo.getPortName() + "，telnet-" + telnetIp + "-" + portInfo.getPort() + "，监控主机：" + portInfo.getHostname() + remark;
               account = getAccount(portInfo.getHostname());
               sendUtil(title, commContent, account, key, isDown);
            } catch (Exception var9) {
               logger.error("发送端口telnet不通告警邮件错误：", var9);
               logInfoService.save("发送端口telnet不通告警邮件错误", var9.toString(), "1");
            }
         } else {
            WarnPools.MEM_WARN_MAP.remove(key);

            try {
               title = "端口已恢复上线：" + portInfo.getPortName() + "，telnet-" + telnetIp + "-" + portInfo.getPort() + "，" + portInfo.getHostname() + remark;
               commContent = "端口已恢复上线，名称：" + portInfo.getPortName() + "，telnet-" + telnetIp + "-" + portInfo.getPort() + "，监控主机：" + portInfo.getHostname() + remark;
               account = getAccount(portInfo.getHostname());
               sendUtil(title, commContent, account, key, isDown);
            } catch (Exception var8) {
               logger.error("发送端口telnet已恢复告警邮件错误：", var8);
               logInfoService.save("发送端口telnet已恢复告警邮件错误", var8.toString(), "1");
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean sendFileSafeDown(FileSafe fileSafe, boolean isDown) {
      if (!StringUtils.isEmpty(mailConfig.getBlockIps()) && mailConfig.getBlockIps().contains(fileSafe.getHostname())) {
         return false;
      } else if (!"false".equals(mailConfig.getAllWarnMail()) && !"false".equals(mailConfig.getFileSafeWarnMail())) {
         String key = fileSafe.getId();
         String remark = (String)StaticKeys.HOST_MAP.get(fileSafe.getHostname());
         if (!StringUtils.isEmpty(remark)) {
            remark = "(" + remark + ")";
         } else {
            remark = "";
         }

         String title;
         String commContent;
         String account;
         if (isDown) {
            if (WarnPools.isExpireWarnTime(key, commonConfig.getWarnCacheTimes())) {
               return false;
            }

            try {
               title = "文件防篡改监测告警：" + fileSafe.getFileName() + "，" + fileSafe.getHostname() + remark;
               commContent = "文件防篡改监测可能已下线：" + fileSafe.getHostname() + remark + "，文件：" + fileSafe.getFileName() + "，文件路径：" + fileSafe.getFilePath() + "，文件最后修改时间：" + fileSafe.getFileModtime();
               account = getAccount(fileSafe.getHostname());
               sendUtil(title, commContent, account, key, isDown);
            } catch (Exception var8) {
               logger.error("文件防篡改监测告警邮件错误：", var8);
               logInfoService.save("文件防篡改监测告警邮件错误", var8.toString(), "1");
            }
         } else {
            WarnPools.MEM_WARN_MAP.remove(key);

            try {
               title = "文件防篡改监测已恢复：" + fileSafe.getFileName() + "，" + fileSafe.getHostname() + remark;
               commContent = "文件防篡改监测已恢复上线：" + fileSafe.getHostname() + remark + "，文件：" + fileSafe.getFileName() + "，文件路径：" + fileSafe.getFilePath() + "，文件最后修改时间：" + fileSafe.getFileModtime();
               account = getAccount(fileSafe.getHostname());
               sendUtil(title, commContent, account, key, isDown);
            } catch (Exception var7) {
               logger.error("文件防篡改监测已恢复告警邮件错误：", var7);
               logInfoService.save("文件防篡改监测已恢复警邮件错误", var7.toString(), "1");
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean sendCustomInfoDown(CustomInfo customInfo) {
      if (!StringUtils.isEmpty(mailConfig.getBlockIps()) && mailConfig.getBlockIps().contains(customInfo.getHostname())) {
         return false;
      } else if (!"false".equals(mailConfig.getAllWarnMail()) && !"false".equals(mailConfig.getCustomInfoWarnMail())) {
         String key = customInfo.getId();
         String remark = (String)StaticKeys.HOST_MAP.get(customInfo.getHostname());
         if (!StringUtils.isEmpty(remark)) {
            remark = "(" + remark + ")";
         } else {
            remark = "";
         }

         if (WarnPools.isExpireWarnTime(key, commonConfig.getWarnCacheTimes())) {
            return false;
         } else {
            try {
               String title = "自定义监控项告警：" + customInfo.getCustomName() + "，" + customInfo.getHostname() + remark;
               String commContent = "自定义监控项告警：" + customInfo.getHostname() + remark + "，" + customInfo.getCustomName() + "，告警表达式成立：" + customInfo.getResultExp() + "，result当前值为：" + customInfo.getCustomValue();
               String account = getAccount(customInfo.getHostname());
               sendUtil(title, commContent, account, key, true);
            } catch (Exception var6) {
               logger.error("发送自定义监控项告警邮件错误", var6);
               logInfoService.save("发送自定义监控项告警邮件错误", var6.toString(), "1");
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean sendFileWarnDown(FileWarnInfo fileWarnInfo, String filePath, String warnContent, boolean isDown) {
      if (!StringUtils.isEmpty(mailConfig.getBlockIps()) && mailConfig.getBlockIps().contains(fileWarnInfo.getHostname())) {
         return false;
      } else if (!"false".equals(mailConfig.getAllWarnMail()) && !"false".equals(mailConfig.getFileLogWarnMail())) {
         String key = fileWarnInfo.getId();
         String remark = (String)StaticKeys.HOST_MAP.get(fileWarnInfo.getHostname());
         if (!StringUtils.isEmpty(remark)) {
            remark = "(" + remark + ")";
         } else {
            remark = "";
         }

         String fileRemark = "";
         if (!StringUtils.isEmpty(fileWarnInfo.getRemark())) {
            fileRemark = fileWarnInfo.getRemark();
         }

         if (isDown) {
            try {
               String title = "日志监控告警：" + fileWarnInfo.getHostname() + remark;
               String commContent = "日志监控告警：" + fileWarnInfo.getHostname() + remark + "，日志备注：" + fileRemark + "，日志文件：" + filePath + "，" + warnContent;
               String account = getAccount(fileWarnInfo.getHostname());
               sendUtil(title, commContent, account, "", false);
            } catch (Exception var10) {
               logger.error("发送日志监控告警邮件错误", var10);
               logInfoService.save("发送日志监控告警错误", var10.toString(), "1");
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static String getAccount(String hostname) {
      return "true".equals(commonConfig.getUserInfoManage()) && !StringUtils.isEmpty(hostname) ? (String)StaticKeys.HOST_ACCOUNT_MAP.get(hostname) : "";
   }

   public static void sendUtil(String title, String commContent, String account, String key, boolean isDown) {
      WarnOtherUtil.sendUtil(title, commContent, account, key, isDown);
   }
}
