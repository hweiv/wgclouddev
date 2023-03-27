package com.wgcloud.util.jdbc;

import org.springframework.stereotype.Component;

@Component
public class RDSConnection {
   public static final String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
   public static final String URL_ORACLE = "jdbc:oracle:thin:@{ip}:{port}:{dbname}";
   public static final String DRIVER_POSTGRESQL = "org.postgresql.Driver";
   public static final String URL_POSTGRESQL = "jdbc:postgresql://{ip}:{port}/{dbname}";
   public static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
   public static final String URL_MYSQL = "jdbc:mysql://{ip}:{port}/{dbname}?characterEncoding=utf-8&characterSetResults=utf8&autoReconnect=true&useSSL=false";
   public static final String DRIVER_MARIADB = "org.mariadb.jdbc.Driver";
   public static final String DRIVER_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
   public static final String URL_SQLSERVER = "jdbc:sqlserver://{ip}:{port};DatabaseName={dbname}";
   public static final String DRIVER_DB2 = "com.ibm.db2.jdbc.app.DB2Driver";
   public static final String URL_DB2 = "jdbc:db2://{ip}:{port}/{dbname}";
   public static final String MYSQL_VERSION = "select version()";
   public static final String ORACLE_VERSION = "select * from v$version";
   public static final String SQLSERVER_VERSION = "SELECT @@VERSION";
   public static final String DB2_VERSION = "SELECT SERVICE_LEVEL FROM SYSIBMADM.ENV_INST_INFO";
   public static final String PROCESS_LIST = "SHOW FULL PROCESSLIST";
   public static final String GLOBAL_VAR = "SHOW GLOBAL VARIABLES";
   public static final String MAX_CONN = "show variables like '%max_connections%'";
   public static final String MAX_USED_CONN = "show global status like 'Max_used_connections'";
   public static final String KEY_MYSQL = "mysql";
   public static final String KEY_MARIADB = "mariadb";
   public static final String KEY_POSTGRESQL = "postgresql";
   public static final String KEY_SQLSERVER = "sqlserver";
   public static final String KEY_DB2 = "db2";
   public static final String KEY_ORACLE = "oracle";
   public static final String KEY_REDIS = "redis";
   public static final String KEY_MONGODB = "mongodb";
}
