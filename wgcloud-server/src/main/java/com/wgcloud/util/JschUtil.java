package com.wgcloud.util;

import cn.hutool.extra.ssh.ChannelType;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.entity.FtpInfo;
import com.wgcloud.service.LogInfoService;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JschUtil {
   private static final Logger logger = LoggerFactory.getLogger(JschUtil.class);
   private static LogInfoService logInfoService = (LogInfoService)ApplicationContextHelper.getBean(LogInfoService.class);

   public static Session createSession(String userName, String password, String host, int port, String privateKeyFile, String passphrase) {
      return createSession(new JSch(), userName, password, host, port, privateKeyFile, passphrase);
   }

   public static Session createSession(JSch jSch, String userName, String password, String host, int port, String privateKeyFile, String passphrase) {
      try {
         if (!StringUtils.isEmpty(privateKeyFile)) {
            if (!StringUtils.isEmpty(passphrase)) {
               jSch.addIdentity(privateKeyFile, passphrase);
            } else {
               jSch.addIdentity(privateKeyFile);
            }
         }

         Session session = jSch.getSession(userName, host, port);
         if (!StringUtils.isEmpty(password)) {
            session.setPassword(password);
         }

         session.setConfig("StrictHostKeyChecking", "no");
         return session;
      } catch (Exception var8) {
         logger.error("create session error", var8);
         return null;
      }
   }

   public static Session createSession(JSch jSch, String userName, String password, String host, int port) {
      return createSession(jSch, userName, password, host, port, "", "");
   }

   private Session createSession(JSch jSch, String userName, String host, int port) {
      return createSession(jSch, userName, "", host, port, "", "");
   }

   public static Session openSession(JSch jSch, String userName, String password, String host, int port, String privateKeyFile, String passphrase, int timeout) {
      Session session = createSession(jSch, userName, password, host, port, privateKeyFile, passphrase);

      try {
         if (timeout >= 0) {
            session.connect(timeout);
         } else {
            session.connect();
         }

         return session;
      } catch (Exception var10) {
         logger.error("session connect error", var10);
         return null;
      }
   }

   public static Session openSession(String userName, String password, String host, int port, String privateKeyFile, String passphrase, int timeout) {
      Session session = createSession(userName, password, host, port, privateKeyFile, passphrase);

      try {
         if (timeout >= 0) {
            session.connect(timeout);
         } else {
            session.connect();
         }

         return session;
      } catch (Exception var9) {
         logger.error("session connect error", var9);
         return null;
      }
   }

   public static Session openSession(JSch jSch, String userName, String password, String host, int port, int timeout) {
      return openSession(jSch, userName, password, host, port, "", "", timeout);
   }

   public static Session openSession(String userName, String password, String host, int port, int timeout) {
      return openSession(userName, password, host, port, "", "", timeout);
   }

   public static Session openSession(JSch jSch, String userName, String host, int port, int timeout) {
      return openSession(jSch, userName, "", host, port, "", "", timeout);
   }

   public static Session openSession(String userName, String host, int port, int timeout) {
      return openSession(userName, "", host, port, "", "", timeout);
   }

   public static Channel createChannel(Session session, ChannelType channelType) {
      try {
         if (!session.isConnected()) {
            session.connect();
         }

         return session.openChannel(channelType.getValue());
      } catch (Exception var3) {
         logger.error("open channel error", var3);
         return null;
      }
   }

   public static ChannelSftp createSftp(Session session) {
      return (ChannelSftp)createChannel(session, ChannelType.SFTP);
   }

   public static ChannelShell createShell(Session session) {
      return (ChannelShell)createChannel(session, ChannelType.SHELL);
   }

   public static Channel openChannel(Session session, ChannelType channelType, int timeout) {
      Channel channel = createChannel(session, channelType);

      try {
         if (timeout >= 0) {
            channel.connect(timeout);
         } else {
            channel.connect();
         }

         return channel;
      } catch (Exception var5) {
         logger.error("connect channel error", var5);
         return null;
      }
   }

   public static ChannelSftp openSftpChannel(Session session, int timeout) {
      return (ChannelSftp)openChannel(session, ChannelType.SFTP, timeout);
   }

   public static ChannelShell openShellChannel(Session session, int timeout) {
      return (ChannelShell)openChannel(session, ChannelType.SHELL, timeout);
   }

   public static void closeSession(Session session) {
      if (null != session && session.isConnected()) {
         session.disconnect();
      }

   }

   public static String testSFtpSession(FtpInfo ftpInfo) {
      try {
         long startTimes = System.currentTimeMillis();
         Session session = openSession((String)ftpInfo.getUserName(), ftpInfo.getPasswd(), ftpInfo.getFtpHost(), Integer.valueOf(ftpInfo.getPort()), 20000);
         long endTimes = System.currentTimeMillis();
         String resTimes = endTimes - startTimes + "";
         ftpInfo.setResTimes(Integer.valueOf(resTimes));
         if (null != session && session.isConnected()) {
            ftpInfo.setState("1");
         } else {
            ftpInfo.setState("2");
         }

         closeSession(session);
         return "success";
      } catch (Exception var7) {
         ftpInfo.setResTimes(20000);
         ftpInfo.setState("2");
         logger.error("testFtpSession connect error:" + ftpInfo.getFtpHost(), var7);
         logInfoService.save("测试sftp连通错误:" + ftpInfo.getFtpHost(), var7.toString(), "2");
         return "error";
      }
   }

   public static String testFTPClient(FtpInfo ftpInfo) {
      try {
         FTPClient ftpClient = new FTPClient();
         ftpClient.setControlEncoding("UTF-8");
         long startTimes = System.currentTimeMillis();
         ftpClient.setConnectTimeout(20000);
         ftpClient.connect(ftpInfo.getFtpHost(), Integer.valueOf(ftpInfo.getPort()));
         ftpClient.enterLocalPassiveMode();
         boolean success = ftpClient.login(ftpInfo.getUserName(), ftpInfo.getPasswd());
         long endTimes = System.currentTimeMillis();
         String resTimes = endTimes - startTimes + "";
         ftpInfo.setResTimes(Integer.valueOf(resTimes));
         if (!success) {
            ftpInfo.setState("2");
            logger.error("Could not login to the ftp server:" + ftpInfo.getFtpHost());
            return "error";
         } else {
            ftpInfo.setState("1");
            int reply = ftpClient.getReplyCode();
            closeFTPClient(ftpClient);
            return "success";
         }
      } catch (Exception var9) {
         ftpInfo.setResTimes(20000);
         ftpInfo.setState("2");
         logger.error("testFTPClient connect error:" + ftpInfo.getFtpHost(), var9);
         logInfoService.save("测试ftp连通错误:" + ftpInfo.getFtpHost(), var9.toString(), "2");
         return "error";
      }
   }

   public static void closeFTPClient(FTPClient ftp) {
      try {
         ftp.logout();
      } catch (Exception var10) {
         logger.error("FTP关闭失败", var10);
      } finally {
         if (ftp.isConnected()) {
            try {
               ftp.disconnect();
            } catch (IOException var9) {
               logger.error("FTP关闭失败", var9);
            }
         }

      }

   }
}
