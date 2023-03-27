package com.wgcloud.util.jdbc;

import cn.hutool.core.collection.CollectionUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.entity.DbTable;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
public class ConnectionUtil {
   private static final Logger logger = LoggerFactory.getLogger(ConnectionUtil.class);
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private DbInfoService dbInfoService;
   @Autowired
   CommonConfig commonConfig;

   public JdbcTemplate getJdbcTemplate(DbInfo dbInfo) throws Exception {
      JdbcTemplate jdbcTemplate = null;
      String driver = "";
      String url = dbInfo.getDbUrl();
      if ("mysql".equals(dbInfo.getDbType())) {
         driver = "com.mysql.jdbc.Driver";
      } else if ("mariadb".equals(dbInfo.getDbType())) {
         driver = "org.mariadb.jdbc.Driver";
      } else if ("postgresql".equals(dbInfo.getDbType())) {
         driver = "org.postgresql.Driver";
      } else if ("sqlserver".equals(dbInfo.getDbType())) {
         driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
      } else if ("db2".equals(dbInfo.getDbType())) {
         driver = "com.ibm.db2.jdbc.app.DB2Driver";
      } else {
         driver = "oracle.jdbc.driver.OracleDriver";
      }

      Runnable runnable;
      try {
         DriverManagerDataSource dataSource = new DriverManagerDataSource();
         dataSource.setDriverClassName(driver);
         dataSource.setUrl(url);
         dataSource.setUsername(dbInfo.getUserName());
         dataSource.setPassword(dbInfo.getPasswd());
         jdbcTemplate = new JdbcTemplate(dataSource);
         jdbcTemplate.setQueryTimeout(120);
         if ("mysql".equals(dbInfo.getDbType())) {
            jdbcTemplate.queryForRowSet("select version()");
         } else if ("mariadb".equals(dbInfo.getDbType())) {
            jdbcTemplate.queryForRowSet("select version()");
         } else if ("postgresql".equals(dbInfo.getDbType())) {
            jdbcTemplate.queryForRowSet("select version()");
         } else if ("sqlserver".equals(dbInfo.getDbType())) {
            jdbcTemplate.queryForRowSet("SELECT @@VERSION");
         } else if ("db2".equals(dbInfo.getDbType())) {
            jdbcTemplate.queryForRowSet("SELECT SERVICE_LEVEL FROM SYSIBMADM.ENV_INST_INFO");
         } else {
            jdbcTemplate.queryForRowSet("select * from v$version");
         }

         dbInfo.setDbState("1");
         if (null != WarnPools.MEM_WARN_MAP && null != WarnPools.MEM_WARN_MAP.get(dbInfo.getId())) {
            runnable = () -> {
               WarnOtherUtil.sendDbDown(dbInfo, false);
            };
            ThreadPoolUtil.executor.execute(runnable);
         }

         dbInfo.setCreateTime(DateUtil.getNowTime());
         this.dbInfoService.updateById(dbInfo);
         return jdbcTemplate;
      } catch (Exception var7) {
         jdbcTemplate = null;
         logger.error("连接数据库错误", var7);
         dbInfo.setDbState("2");
         this.dbInfoService.updateById(dbInfo);
         runnable = () -> {
            WarnOtherUtil.sendDbDown(dbInfo, true);
         };
         ThreadPoolUtil.executor.execute(runnable);
         this.logInfoService.save("连接数据库错误：" + dbInfo.getAliasName(), "数据库别名：" + dbInfo.getAliasName() + "，" + var7.toString(), "2");
         return null;
      }
   }

   public long queryTableCount(DbInfo dbInfo, JdbcTemplate jdbcTemplate, DbTable dbTable) {
      try {
         dbTable.setState("1");
         String sqlinkey = FormatUtil.haveSqlDanger(dbTable.getWhereVal(), this.commonConfig.getSqlInKeys());
         if (!StringUtils.isEmpty(sqlinkey)) {
            String errinfo = "统计SQL语句含有sql敏感字符" + sqlinkey;
            logger.error("统计数据表错误：" + errinfo);
            this.logInfoService.save(errinfo + "：" + dbInfo.getAliasName(), "数据库别名：" + dbInfo.getAliasName() + "，数据表别名：" + dbTable.getRemark() + "，错误信息：" + errinfo, "2");
            return 0L;
         } else if (null == jdbcTemplate) {
            dbTable.setState("2");
            return 0L;
         } else {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(dbTable.getWhereVal());
            if (CollectionUtil.isEmpty(list)) {
               return 0L;
            } else {
               Map<String, Object> mapResult = (Map)list.get(0);
               String resultStr = "0";
               String key;
               if (StringUtils.isEmpty(dbTable.getTableName())) {
                  for(Iterator var8 = mapResult.keySet().iterator(); var8.hasNext(); resultStr = mapResult.get(key).toString()) {
                     key = (String)var8.next();
                  }
               } else {
                  resultStr = mapResult.get(dbTable.getTableName()).toString();
               }

               return resultStr.indexOf(".") > -1 ? Double.valueOf(resultStr).longValue() : Long.valueOf(resultStr);
            }
         }
      } catch (Exception var10) {
         dbTable.setState("2");
         logger.error("统计数据表错误：", var10);
         this.logInfoService.save("统计数据表sql执行错误：" + dbInfo.getAliasName(), "数据库别名：" + dbInfo.getAliasName() + "，数据表别名：" + dbTable.getRemark() + "，错误信息：" + var10.toString(), "2");
         return 0L;
      }
   }
}
