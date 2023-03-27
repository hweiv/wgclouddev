package com.wgcloud.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyDatabaseIdProvider implements DatabaseIdProvider {
   private static Logger logger = LoggerFactory.getLogger(MyDatabaseIdProvider.class);

   public void setProperties(Properties p) {
      logger.info(p.getProperty("MySQL"));
   }

   public String getDatabaseId(DataSource dataSource) throws SQLException {
      Connection conn = dataSource.getConnection();
      String dbName = conn.getMetaData().getDatabaseProductName();
      String dbAlias = "";
      byte var6 = -1;
      switch(dbName.hashCode()) {
      case -1924994658:
         if (dbName.equals("Oracle")) {
            var6 = 3;
         }
         break;
      case -1791128108:
         if (dbName.equals("MariaDB")) {
            var6 = 2;
         }
         break;
      case -112048300:
         if (dbName.equals("PostgreSQL")) {
            var6 = 1;
         }
         break;
      case 74798178:
         if (dbName.equals("MySQL")) {
            var6 = 0;
         }
      }

      switch(var6) {
      case 0:
         dbAlias = "mysql";
         break;
      case 1:
         dbAlias = "postgresql";
         break;
      case 2:
         dbAlias = "mysql";
         break;
      case 3:
         dbAlias = "oracle";
      }

      return dbAlias;
   }
}
