package com.wgcloud.common;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.wgcloud.util.staticvar.StaticKeys;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettytHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
   private static final Logger logger = LoggerFactory.getLogger(NettytHandler.class);
   public static final String HANDLE_OPERATE = "handle";
   public static final String HANDLE_VALUE = "value";
   public static final Integer CONNECTION_OUT = 30000;
   public static final String ENTER_VAL = "\r";
   public static final String LINE_NEXT_VAL = "\n";
   public static final String TAB_VAL = "\t";
   public static ChannelGroup channelGroup;
   public static Map<String, String> MAP_CMD;
   public static Map<String, ChannelShell> MAP_SSH_SESSION;

   protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
      String channelId = ctx.channel().id().toString();
      String msgJSonStr = msg.text();
      if (!StringUtils.isEmpty(msgJSonStr)) {
         JSONObject msgJson = JSONUtil.parseObj(msgJSonStr);
         if ("connect".equals(msgJson.getStr("handle"))) {
            try {
               getSSHChannel(channelId, msgJson.getStr("ip"), msgJson.getStr("user"), msgJson.getStr("pwd"), Integer.valueOf(msgJson.getStr("port")), msgJson.getStr("priKeyBasePath"));
               executeCommand(ctx, "\r");
            } catch (JSchException var7) {
               ctx.writeAndFlush(new TextWebSocketFrame("\n" + var7.toString()));
               logger.error("ssh终端连接错误：", var7);
            }
         } else if ("cmd".equals(msgJson.getStr("handle"))) {
            String cmdStr = msgJson.getStr("value");
            if (cmdStr.endsWith("\t")) {
               return;
            }

            if (StringUtils.isEmpty((CharSequence)MAP_CMD.get(channelId))) {
               MAP_CMD.put(channelId, cmdStr);
            } else {
               MAP_CMD.put(channelId, (String)MAP_CMD.get(channelId) + cmdStr);
            }

            if (cmdStr.contains("\r") && !StringUtils.isEmpty((CharSequence)MAP_CMD.get(channelId))) {
               executeCommand(ctx, (String)MAP_CMD.get(channelId));
               MAP_CMD.remove(channelId);
            }
         }

      }
   }

   public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
      channelGroup.add(ctx.channel());
      logger.info(ctx.channel().remoteAddress() + "ssh终端上线了!");
   }

   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
      channelGroup.remove(ctx.channel());
      logger.info(ctx.channel().remoteAddress() + "ssh终端断开连接");
      ChannelShell channelShell = (ChannelShell)MAP_SSH_SESSION.get(ctx.channel().id().toString());
      if (channelShell != null) {
         Session session = channelShell.getSession();
         if (channelShell != null) {
            channelShell.disconnect();
         }

         if (session != null) {
            session.disconnect();
         }

         MAP_SSH_SESSION.remove(ctx.channel().id().toString());
         MAP_CMD.remove(ctx.channel().id().toString());
      }

   }

   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      cause.printStackTrace();
      ctx.channel().close();
   }

   private static ChannelShell getSSHChannel(String channelId, String host, String user, String password, Integer port, String priKeyBasePath) throws JSchException {
      ChannelShell channelShell = (ChannelShell)MAP_SSH_SESSION.get(channelId);
      if (channelShell != null) {
         return channelShell;
      } else {
         JSch jsch = new JSch();
         Session session = jsch.getSession(user, host, port);
         if (!StringUtils.isEmpty(password)) {
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
         } else {
            logger.debug("priKeyBasePath-----------" + StaticKeys.JAR_PATH + "/" + priKeyBasePath);
            jsch.addIdentity(StaticKeys.JAR_PATH + "/" + priKeyBasePath);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
         }

         session.connect(CONNECTION_OUT);
         session.setTimeout(600000);
         channelShell = (ChannelShell)session.openChannel("shell");
         channelShell.connect(CONNECTION_OUT);
         channelShell.setPtyType("dumb");
         channelShell.setPty(true);
         MAP_SSH_SESSION.put(channelId, channelShell);
         return channelShell;
      }
   }

   private static void executeCommand(ChannelHandlerContext ctx, String cmds) {
      ctx.writeAndFlush(new TextWebSocketFrame("\n"));

      try {
         ChannelShell channelShell = (ChannelShell)MAP_SSH_SESSION.get(ctx.channel().id().toString());
         if (null == channelShell) {
            ctx.writeAndFlush(new TextWebSocketFrame("\n\r~$ "));
         } else {
            InputStream inputStream = channelShell.getInputStream();
            OutputStream outputStream = channelShell.getOutputStream();
            outputStream.write(cmds.getBytes());
            outputStream.flush();
            if (!StringUtils.isEmpty(cmds)) {
               logger.info(channelShell.getSession().getHost() + "，" + channelShell.getSession().getUserName() + "，执行ssh指令：" + cmds);
            }

            String showMsg = "";
            Thread.sleep(400L);
            byte[] tmp = new byte[4096];
            int beat = 0;

            while(true) {
               while(true) {
                  if (inputStream.available() > 0) {
                     int i = inputStream.read(tmp);
                     if (i >= 0) {
                        showMsg = new String(tmp, 0, i, "utf-8");
                        ctx.writeAndFlush(new TextWebSocketFrame(showMsg));
                        Thread.sleep(500L);
                        continue;
                     }
                  }

                  Thread.sleep(500L);
                  if (channelShell.isClosed()) {
                     if (inputStream.available() <= 0) {
                        ctx.writeAndFlush(new TextWebSocketFrame("exit-status: " + channelShell.getExitStatus() + "，会话通道已超时请重新连接"));
                        return;
                     }
                  } else {
                     ++beat;
                     if (beat > 2) {
                        return;
                     }
                  }
               }
            }
         }
      } catch (Exception var9) {
         logger.error("shh指令执行错误：", var9);
      }
   }

   public static void clearOldData() {
      MAP_CMD.clear();
      MAP_SSH_SESSION.clear();
   }

   static {
      channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
      MAP_CMD = Collections.synchronizedMap(new HashMap());
      MAP_SSH_SESSION = Collections.synchronizedMap(new HashMap());
   }
}
