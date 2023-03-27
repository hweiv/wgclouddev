package com.wgcloud.util.msg;

import com.sun.mail.util.MailSSLSocketFactory;
import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.entity.DbTable;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.FtpInfo;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.entity.MailSet;
import com.wgcloud.entity.ShellInfo;
import com.wgcloud.entity.SnmpInfo;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.ExecUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarnOtherUtil {
   private static final Logger logger = LoggerFactory.getLogger(WarnMailUtil.class);
   private static CommonConfig commonConfig = (CommonConfig)ApplicationContextHelper.getBean(CommonConfig.class);
   private static LogInfoService logInfoService = (LogInfoService)ApplicationContextHelper.getBean(LogInfoService.class);
   private static MailConfig mailConfig = (MailConfig)ApplicationContextHelper.getBean(MailConfig.class);

   public static boolean sendHeathInfo(HeathMonitor heathMonitor, boolean isDown) {
      if (!"false".equals(mailConfig.getAllWarnMail()) && !"false".equals(mailConfig.getHeathWarnMail())) {
         String key = heathMonitor.getId();
         String title;
         String commContent;
         if (isDown) {
            if (WarnPools.isExpireWarnTime(key, commonConfig.getWarnCacheTimes())) {
               return false;
            }

            try {
               if (WarnPools.HEATH_WARN_COUNT_MAP.get(key) == null) {
                  WarnPools.HEATH_WARN_COUNT_MAP.put(key, 1);
               } else {
                  WarnPools.HEATH_WARN_COUNT_MAP.put(key, (Integer)WarnPools.HEATH_WARN_COUNT_MAP.get(key) + 1);
               }

               if ((Integer)WarnPools.HEATH_WARN_COUNT_MAP.get(key) < mailConfig.getHeathWarnCount()) {
                  logger.info(heathMonitor.getAppName() + "---服务接口没有达到告警次数---" + WarnPools.HEATH_WARN_COUNT_MAP.get(key));
                  return false;
               }

               title = "服务接口检测告警：" + heathMonitor.getAppName();
               commContent = "服务接口已连续" + mailConfig.getHeathWarnCount() + "次检测失败：" + heathMonitor.getAppName() + "，url：" + heathMonitor.getHeathUrl() + "，响应状态码为" + heathMonitor.getHeathStatus();
               sendUtil(title, commContent, heathMonitor.getAccount(), key, isDown);
            } catch (Exception var6) {
               logger.error("发送服务健康检测告警邮件错误：", var6);
               logInfoService.save("发送服务健康检测告警邮件错误", var6.toString(), "1");
            }
         } else {
            try {
               title = "服务接口已恢复：" + heathMonitor.getAppName();
               commContent = "服务接口已恢复：" + heathMonitor.getAppName() + "，url：" + heathMonitor.getHeathUrl() + "，响应状态码为" + heathMonitor.getHeathStatus() + "";
               sendUtil(title, commContent, heathMonitor.getAccount(), key, isDown);
            } catch (Exception var5) {
               logger.error("发送服务接口已恢复邮件错误：", var5);
               logInfoService.save("发送服务接口已恢复邮件错误", var5.toString(), "1");
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean sendDceInfo(DceInfo dceInfo, boolean isDown) {
      if (!"false".equals(mailConfig.getAllWarnMail()) && !"false".equals(mailConfig.getDceWarnMail())) {
         String key = dceInfo.getId();
         String remark = dceInfo.getRemark();
         if (StringUtils.isEmpty(remark)) {
            remark = "";
         } else {
            remark = "(" + dceInfo.getRemark() + ")";
         }

         String title;
         String commContent;
         if (isDown) {
            if (WarnPools.isExpireWarnTime(key, commonConfig.getWarnCacheTimes())) {
               return false;
            }

            try {
               if (WarnPools.PING_WARN_COUNT_MAP.get(key) == null) {
                  WarnPools.PING_WARN_COUNT_MAP.put(key, 1);
               } else {
                  WarnPools.PING_WARN_COUNT_MAP.put(key, (Integer)WarnPools.PING_WARN_COUNT_MAP.get(key) + 1);
               }

               if ((Integer)WarnPools.PING_WARN_COUNT_MAP.get(key) < mailConfig.getDceWarnCount()) {
                  logger.info(dceInfo.getHostname() + "---数通设备PING超时没有达到告警次数---" + WarnPools.PING_WARN_COUNT_MAP.get(key));
                  return false;
               }

               title = "数通设备PING超时告警：" + dceInfo.getHostname() + remark;
               commContent = "数通设备已连续" + mailConfig.getDceWarnCount() + "次检测失败：" + dceInfo.getHostname() + remark + "，PING超时或失败";
               sendUtil(title, commContent, dceInfo.getAccount(), key, isDown);
            } catch (Exception var7) {
               logger.error("发送数通设备PING告警邮件错误：", var7);
               logInfoService.save("发送数通设备PING告警邮件错误", var7.toString(), "1");
            }
         } else {
            try {
               title = "数通设备PING已恢复：" + dceInfo.getHostname() + remark;
               commContent = "数通设备PING已恢复：" + dceInfo.getHostname() + remark + "，响应时间ms：" + dceInfo.getResTimes();
               sendUtil(title, commContent, dceInfo.getAccount(), key, isDown);
            } catch (Exception var6) {
               logger.error("发送数通设备PING已恢复邮件错误：", var6);
               logInfoService.save("发送数通设备PING已恢复邮件错误", var6.toString(), "1");
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean sendSnmpInfo(SnmpInfo snmpInfo, boolean isDown) {
      if (!"false".equals(mailConfig.getAllWarnMail()) && !"false".equals(mailConfig.getSnmpWarnMail())) {
         String key = snmpInfo.getId();
         String remark = snmpInfo.getRemark();
         if (StringUtils.isEmpty(remark)) {
            remark = "";
         } else {
            remark = "(" + snmpInfo.getRemark() + ")";
         }

         String title;
         String commContent;
         if (isDown) {
            if (WarnPools.isExpireWarnTime(key, commonConfig.getWarnCacheTimes())) {
               return false;
            }

            try {
               title = "snmp设备监测告警：" + snmpInfo.getHostname() + remark;
               commContent = "snmp设备：" + snmpInfo.getHostname() + remark + "，可能已下线";
               sendUtil(title, commContent, snmpInfo.getAccount(), key, isDown);
            } catch (Exception var7) {
               logger.error("发送snmp设备监测告警邮件错误：", var7);
               logInfoService.save("发送snmp设备监测告警邮件错误", var7.toString(), "1");
            }
         } else {
            try {
               title = "snmp设备监测已恢复：" + snmpInfo.getHostname() + remark;
               commContent = "snmp设备监测已恢复：" + snmpInfo.getHostname() + remark;
               sendUtil(title, commContent, snmpInfo.getAccount(), key, isDown);
            } catch (Exception var6) {
               logger.error("发送snmp设备监测已恢复邮件错误：", var6);
               logInfoService.save("发送snmp设备监测已恢复邮件错误", var6.toString(), "1");
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean sendDbDown(DbInfo dbInfo, boolean isDown) {
      if (!"false".equals(mailConfig.getAllWarnMail()) && !"false".equals(mailConfig.getDbDownWarnMail())) {
         String key = dbInfo.getId();
         String title;
         String commContent;
         if (isDown) {
            if (WarnPools.isExpireWarnTime(key, commonConfig.getWarnCacheTimes())) {
               return false;
            }

            try {
               title = "数据源连接失败告警：" + dbInfo.getAliasName();
               commContent = "数据源连接失败：" + dbInfo.getAliasName();
               sendUtil(title, commContent, dbInfo.getAccount(), key, isDown);
            } catch (Exception var6) {
               logger.error("发送数据源连接失败邮件错误：", var6);
               logInfoService.save("发送数据源连接失败告警错误", var6.toString(), "1");
            }
         } else {
            WarnPools.MEM_WARN_MAP.remove(key);

            try {
               title = "数据源已恢复上线：" + dbInfo.getAliasName();
               commContent = "数据源已恢复上线：" + dbInfo.getAliasName();
               sendUtil(title, commContent, dbInfo.getAccount(), key, isDown);
            } catch (Exception var5) {
               logger.error("发送数据源已恢复上线邮件错误：", var5);
               logInfoService.save("发送数据源已恢复上线错误", var5.toString(), "1");
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean sendDbTableDown(DbTable dbTable, String account, Boolean result) {
      if (!"false".equals(mailConfig.getAllWarnMail()) && !"false".equals(mailConfig.getDbDownWarnMail())) {
         String key = dbTable.getId();

         try {
            String title;
            String commContent;
            if (!result && null != WarnPools.MEM_WARN_MAP.get(key)) {
               title = "数据表告警恢复：" + dbTable.getRemark();
               commContent = "数据表告警恢复：" + dbTable.getRemark() + "，告警表达式不成立：" + dbTable.getResultExp() + "，result当前值为：" + dbTable.getTableCount();
               sendUtil(title, commContent, account, key, false);
               return false;
            }

            if (WarnPools.isExpireWarnTime(key, commonConfig.getWarnCacheTimes())) {
               return false;
            }

            if (result) {
               title = "数据表告警：" + dbTable.getRemark();
               commContent = "数据表告警：" + dbTable.getRemark() + "，告警表达式成立：" + dbTable.getResultExp() + "，result当前值为：" + dbTable.getTableCount();
               sendUtil(title, commContent, account, key, true);
            }
         } catch (Exception var6) {
            logger.error("发送数据表告警邮件错误", var6);
            logInfoService.save("发送数据表告警邮件错误", var6.toString(), "1");
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean sendShellInfo(ShellInfo shellInfo, String titlePrefix) {
      if (!"false".equals(mailConfig.getAllWarnMail()) && !"false".equals(mailConfig.getShellWarnMail())) {
         try {
            String shellTime = "立即下发";
            if ("2".equals(shellInfo.getShellType())) {
               shellTime = shellInfo.getShellTime();
            }

            String title = titlePrefix + "：" + shellInfo.getShellName();
            String commContent = titlePrefix + "：" + shellInfo.getShellName() + "，指令内容：" + shellInfo.getShell() + "，下发时间：" + shellTime;
            sendUtil(title, commContent, shellInfo.getAccount(), "", false);
         } catch (Exception var5) {
            logger.error("下发指令信息邮件错误：", var5);
            logInfoService.save("下发指令信息错误", var5.toString(), "2");
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean sendLastlogWarnInfo(String lastlogWarnInfos, String bindIp) {
      if (!"false".equals(mailConfig.getAllWarnMail()) && !"false".equals(mailConfig.getHostLoginWarnMail())) {
         if (!StringUtils.isEmpty(mailConfig.getBlockIps()) && mailConfig.getBlockIps().contains(bindIp)) {
            return false;
         } else {
            try {
               String remark = (String)StaticKeys.HOST_MAP.get(bindIp);
               if (!StringUtils.isEmpty(remark)) {
                  remark = "(" + remark + ")";
               } else {
                  remark = "";
               }

               String title = "主机登录信息提醒：" + bindIp + remark;
               String commContent = "主机登录信息提醒：" + bindIp + remark + "，" + lastlogWarnInfos;
               sendUtil(title, commContent, WarnMailUtil.getAccount(bindIp), "", false);
            } catch (Exception var5) {
               logger.error("主机登录信息提醒邮件错误：", var5);
               logInfoService.save("主机登录信息提醒错误", var5.toString(), "2");
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean sendFtpInfo(FtpInfo ftpInfo, boolean isDown) {
      if (!"false".equals(mailConfig.getAllWarnMail()) && !"false".equals(mailConfig.getFtpWarnMail())) {
         String key = ftpInfo.getId();
         String title;
         String commContent;
         if (isDown) {
            if (WarnPools.isExpireWarnTime(key, commonConfig.getWarnCacheTimes())) {
               return false;
            }

            try {
               title = "FTP检测告警：" + ftpInfo.getFtpName();
               commContent = "FTP服务连接失败：" + ftpInfo.getFtpName() + "，" + ftpInfo.getFtpHost() + ":" + ftpInfo.getPort();
               sendUtil(title, commContent, ftpInfo.getAccount(), key, isDown);
            } catch (Exception var6) {
               logger.error("发送FTP检测告警邮件错误：", var6);
               logInfoService.save("发送FTP检测告警邮件错误", var6.toString(), "1");
            }
         } else {
            try {
               title = "FTP检测已恢复：" + ftpInfo.getFtpName();
               commContent = "FTP服务连接已恢复：" + ftpInfo.getFtpName() + "，" + ftpInfo.getFtpHost() + ":" + ftpInfo.getPort() + "，响应时间为" + ftpInfo.getResTimes() + "ms";
               sendUtil(title, commContent, ftpInfo.getAccount(), key, isDown);
            } catch (Exception var5) {
               logger.error("发送FTP已恢复邮件错误：", var5);
               logInfoService.save("发送FTP口已恢复邮件错误", var5.toString(), "1");
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static void sendUtil(String title, String commContent, String account, String key, boolean isDown) {
      if (StaticKeys.WARN_CRON_TIME_SIGN) {
         if (title.indexOf("已恢复") > -1) {
            logInfoService.save(title, commContent, "3");
         } else {
            logInfoService.save(title, commContent, "1");
         }

         if (!StringUtils.isEmpty(title) && title.indexOf("告警") > -1) {
            WarnPools.WARN_COUNT_LIST.add(1);
         }

         MailSet mailSet = StaticKeys.mailSet;
         if (!StringUtils.isEmpty(account) && "true".equals(commonConfig.getUserInfoManage()) && StaticKeys.LICENSE_STATE.equals("1")) {
            if ("true".equals(commonConfig.getUserInfoManage())) {
               String accountMail = "";
               String accountKey = "";
               if (null != StaticKeys.ACCOUNT_INFO_MAP.get(account)) {
                  accountMail = ((AccountInfo)StaticKeys.ACCOUNT_INFO_MAP.get(account)).getEmail();
                  accountKey = ((AccountInfo)StaticKeys.ACCOUNT_INFO_MAP.get(account)).getAccountKey();
               }

               if (StaticKeys.mailSet != null) {
                  String addMail = StringUtils.isEmpty(accountMail) ? "" : ";" + accountMail;
                  sendMail(mailSet.getToMail() + addMail, title, commContent);
               }

               ExecUtil.runScript(mailConfig.getWarnScript(), commContent, accountKey);
            }
         } else {
            if (StaticKeys.mailSet != null) {
               sendMail(mailSet.getToMail(), title, commContent);
            }

            ExecUtil.runScript(mailConfig.getWarnScript(), commContent, "");
         }

         if (!StringUtils.isEmpty(key)) {
            if (isDown) {
               WarnPools.MEM_WARN_MAP.put(key, "1");
            } else {
               WarnPools.MEM_WARN_MAP.remove(key);
            }
         }

      }
   }

   public static String sendMail(String mails, String mailTitle, String mailContent) {
      if (StringUtils.isEmpty(mails)) {
         return "error";
      } else {
         if (mails.startsWith(";")) {
            mails = mails.substring(1);
         }

         if ("true".equals(mailConfig.getJavaXmail())) {
            return sendMailByJavax(mails, mailTitle, mailContent);
         } else {
            try {
               String mailTitlePrefix = "[WGCLOUD]";
               String mailContentSuffix = "<p><p><p><a target='_blank' href='http://www.wgstart.com'>WGCLOUD</a>敬上";
               if (StaticKeys.LICENSE_STATE.equals("1")) {
                  mailTitlePrefix = commonConfig.getMailTitlePrefix();
                  mailContentSuffix = commonConfig.getMailContentSuffix();
               }

               HtmlEmail email = new HtmlEmail();
               email.setHostName(StaticKeys.mailSet.getSmtpHost());
               email.setSmtpPort(Integer.valueOf(StaticKeys.mailSet.getSmtpPort()));
               if ("1".equals(StaticKeys.mailSet.getSmtpSSL())) {
                  email.setSSLOnConnect(true);
               }

               email.setAuthenticator(new DefaultAuthenticator(StaticKeys.mailSet.getFromMailName(), StaticKeys.mailSet.getFromPwd()));
               email.setFrom(StaticKeys.mailSet.getFromMailName());
               email.setSubject(mailTitlePrefix + mailTitle);
               email.setCharset("UTF-8");
               email.setHtmlMsg(mailContent + "<p><p><p><p>" + DateUtil.getCurrentDateTime() + mailContentSuffix);
               email.addTo(mails.split(";"));
               email.setSentDate(new Date());
               email.send();
               return "success";
            } catch (Exception var6) {
               logger.error("发送邮件错误", var6);
               logInfoService.save("发送邮件错误", var6.toString(), "1");
               return "error";
            }
         }
      }
   }

   private static String sendMailByJavax(String mails, String mailTitle, String mailContent) {
      if (StringUtils.isEmpty(mails)) {
         return "error";
      } else {
         if (mails.startsWith(";")) {
            mails = mails.substring(1);
         }

         try {
            String mailTitlePrefix = "[WGCLOUD]";
            String mailContentSuffix = "<p><p><p><a target='_blank' href='http://www.wgstart.com'>WGCLOUD</a>敬上";
            if (StaticKeys.LICENSE_STATE.equals("1")) {
               mailTitlePrefix = commonConfig.getMailTitlePrefix();
               mailContentSuffix = commonConfig.getMailContentSuffix();
            }

            Properties prop = new Properties();
            prop.setProperty("mail.host", StaticKeys.mailSet.getSmtpHost());
            prop.setProperty("mail.transport.protocol", "smtp");
            prop.setProperty("mail.smtp.auth", "true");
            prop.setProperty("mail.smtp.port", StaticKeys.mailSet.getSmtpPort());
            if ("1".equals(StaticKeys.mailSet.getSmtpSSL())) {
               prop.setProperty("mail.smtp.starttls.enable", "true");
            } else {
               prop.setProperty("mail.smtp.starttls.enable", "false");
            }

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.smtp.ssl.socketFactory", sf);
            prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
            Session session = Session.getInstance(prop, (Authenticator)null);
            session.setDebug(false);
            Transport ts = session.getTransport();
            ts.connect(StaticKeys.mailSet.getSmtpHost(), StaticKeys.mailSet.getFromMailName(), StaticKeys.mailSet.getFromPwd());
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(StaticKeys.mailSet.getFromMailName()));
            String[] mailArr = mails.split(";");
            InternetAddress[] internetAddressArr = new InternetAddress[mailArr.length];

            for(int i = 0; i < mailArr.length; ++i) {
               internetAddressArr[i] = new InternetAddress(mailArr[i]);
            }

            message.setRecipients(RecipientType.TO, internetAddressArr);
            message.setSubject(mailTitlePrefix + mailTitle);
            message.setContent(mailContent + "<p><p><p><p>" + DateUtil.getCurrentDateTime() + mailContentSuffix, "text/html;charset=UTF-8");
            message.saveChanges();
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
            return "success";
         } catch (Exception var13) {
            logger.error("发送邮件错误", var13);
            logInfoService.save("发送邮件错误", var13.toString(), "1");
            return "error";
         }
      }
   }
}
