package com.wgcloud.util;

import cn.hutool.core.text.UnicodeUtil;
import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.MailConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecUtil {
   private static final Logger logger = LoggerFactory.getLogger(ExecUtil.class);
   private static MailConfig mailConfig = (MailConfig)ApplicationContextHelper.getBean(MailConfig.class);

   public static void runScript(String path, String content, String accountKey) {
      if (!StringUtils.isEmpty(path)) {
         try {
            content = content.replace("</br>", "\\n");
            if ("true".equals(mailConfig.getWarnToUnicode())) {
               content = UnicodeUtil.toUnicode(content, false);
            }

            String execCmdStr = path + " \"" + content + "\"";
            if (!StringUtils.isEmpty(accountKey)) {
               execCmdStr = execCmdStr + " \"" + accountKey + "\"";
            }

            Process var4 = Runtime.getRuntime().exec(execCmdStr);
         } catch (Exception var5) {
            logger.error("执行告警脚本错误：", var5);
         }

      }
   }
}
