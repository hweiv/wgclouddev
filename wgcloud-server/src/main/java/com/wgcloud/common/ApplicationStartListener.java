package com.wgcloud.common;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.HostWarnDiyDto;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.staticvar.StaticKeys;
import java.io.File;
import java.nio.charset.Charset;
import javax.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartListener implements ApplicationRunner {
   private Logger logger = LoggerFactory.getLogger(ApplicationStartListener.class);
   @Autowired
   CommonConfig commonConfig;
   @Autowired
   private ServletContext servletContext;

   public void run(ApplicationArguments args) throws Exception {
      this.servletContext.setAttribute("sidebarCollapse", this.commonConfig.getSidebarCollapse());
      this.servletContext.setAttribute("hostGroup", this.commonConfig.getHostGroup());
      this.servletContext.setAttribute("userInfoManage", this.commonConfig.getUserInfoManage());
      this.servletContext.setAttribute("showWarnCount", this.commonConfig.getShowWarnCount());
      this.servletContext.setAttribute("shellToRun", this.commonConfig.getShellToRun());
      StaticKeys.PAGE_SIZE = this.commonConfig.getPageSize();
      ApplicationHome h = new ApplicationHome(this.getClass());
      String jarPath = h.getSource().getParentFile().toString();
      this.logger.debug("jar包路径-----------------" + jarPath);
      StaticKeys.JAR_PATH = jarPath;
      WarnPools.isExpireWarnTime("TEST", this.commonConfig.getWarnCacheTimes());
      WarnPools.initWarnCountMap();
      if ("master".equals(this.commonConfig.getNodeType())) {
         this.servletContext.setAttribute("serverInfoId", UUIDUtil.getUUID());
      }

      if (null != this.commonConfig.getMaxPoolSize()) {
         ThreadPoolUtil.executor.setMaximumPoolSize(this.commonConfig.getMaxPoolSize());
      }

      try {
         String path = System.getProperty("user.dir");
         File file = new File(path + "/config/hostWarnDiy.json");
         if (file.exists()) {
            this.logger.info("发现并解析自定义主机告警配置hostWarnDiy.json");
            JSONArray hostJsonArray = JSONUtil.readJSONArray(file, Charset.forName("utf-8"));
            if (!CollectionUtil.isEmpty(hostJsonArray)) {
               for(int i = 0; i < hostJsonArray.size(); ++i) {
                  JSONObject jsonObject = JSONUtil.parseObj(hostJsonArray.get(i));
                  HostWarnDiyDto hostWarnDto = (HostWarnDiyDto)JSONUtil.toBean(jsonObject, HostWarnDiyDto.class);
                  if (!StringUtils.isEmpty(hostWarnDto.getHostname())) {
                     StaticKeys.HOST_WARN_MAP.put(hostWarnDto.getHostname(), hostWarnDto);
                  }
               }
            }
         }
      } catch (Exception var11) {
         this.logger.error("hostWarnDiy.json解析错误：", var11);
      }

      try {
         this.servletContext.setAttribute("webSsh", this.commonConfig.getWebSsh());
         if ("true".equals(this.commonConfig.getWebSsh())) {
            this.logger.info("NettyServer服务启动，端口：" + this.commonConfig.getWebSshPort());
            NettyServer.start(this.commonConfig.getWebSshPort());
         }
      } catch (Exception var10) {
         this.logger.error("NettyServer服务启动错误：", var10);
      }

   }
}
