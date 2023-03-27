/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : wgcloud

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2021-12-18 21:44:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for app_info
-- ----------------------------
DROP TABLE IF EXISTS `APP_INFO`;
CREATE TABLE `APP_INFO` (
  `ID` char(32) NOT NULL,
  `HOST_NAME` char(50) DEFAULT NULL,
  `APP_PID` char(200) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `APP_NAME` varchar(50) DEFAULT NULL,
  `CPU_PER` double(8,2) DEFAULT NULL,
  `MEM_PER` double(10,2) DEFAULT NULL,
  `APP_TYPE` char(1) DEFAULT NULL,
  `STATE` char(1) DEFAULT NULL,
  `ACTIVE` char(1) DEFAULT NULL,
  `READ_BYTES` char(20) DEFAULT NULL,
  `WRITES_BYTES` char(20) DEFAULT NULL,
  `THREADS_NUM` varchar(20) DEFAULT NULL,
  `GATHER_PID` varchar(20) DEFAULT NULL,
  `GROUP_ID` varchar(500) DEFAULT NULL,
  `APP_TIMES` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for app_state
-- ----------------------------
DROP TABLE IF EXISTS `APP_STATE`;
CREATE TABLE `APP_STATE` (
  `ID` char(32) NOT NULL,
  `APP_INFO_ID` char(32) DEFAULT NULL,
  `CPU_PER` double(8,2) DEFAULT NULL,
  `MEM_PER` double(10,2) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `THREADS_NUM` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `APP_STAT_INDEX` (`APP_INFO_ID`,`CREATE_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for cpu_state
-- ----------------------------
DROP TABLE IF EXISTS `CPU_STATE`;
CREATE TABLE `CPU_STATE` (
  `ID` char(32) NOT NULL,
  `HOST_NAME` char(50) DEFAULT NULL,
  `USER_PER` char(30) DEFAULT NULL,
  `SYS` double(8,2) DEFAULT NULL,
  `IDLE` double(8,2) DEFAULT NULL,
  `IOWAIT` double(8,2) DEFAULT NULL,
  `IRQ` char(30) DEFAULT NULL,
  `SOFT` char(30) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `CPU_ACC_HOST_INDEX` (`HOST_NAME`,`CREATE_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for cpu_temperatures
-- ----------------------------
DROP TABLE IF EXISTS `CPU_TEMPERATURES`;
CREATE TABLE `CPU_TEMPERATURES` (
  `ID` char(32) NOT NULL,
  `HOST_NAME` char(50) DEFAULT NULL,
  `CORE_INDEX` varchar(50) DEFAULT NULL,
  `CRIT` char(10) DEFAULT NULL,
  `INPUT` char(10) DEFAULT NULL,
  `MAX` char(10) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for db_info
-- ----------------------------
DROP TABLE IF EXISTS `DB_INFO`;
CREATE TABLE `DB_INFO` (
  `ID` char(32) NOT NULL,
  `DBTYPE` char(32) DEFAULT NULL,
  `USER_NAME` varchar(50) DEFAULT NULL,
  `PASSWD` varchar(50) DEFAULT NULL,
  `DBURL` varchar(500) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `DB_STATE` char(1) DEFAULT NULL,
  `ALIAS_NAME` varchar(50) DEFAULT NULL,
  `ACCOUNT` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for db_table
-- ----------------------------
DROP TABLE IF EXISTS `DB_TABLE`;
CREATE TABLE `DB_TABLE` (
  `ID` char(32) NOT NULL,
  `TABLE_NAME` varchar(50) DEFAULT NULL,
  `WHERE_VAL` varchar(2000) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `REMARK` varchar(50) DEFAULT NULL,
  `TABLE_COUNT` bigint(20) DEFAULT NULL,
  `DBINFO_ID` char(32) DEFAULT NULL,
  `ACTIVE` char(1) DEFAULT NULL,
  `STATE` varchar(1) DEFAULT NULL,
  `RESULT_EXP` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for db_table_count
-- ----------------------------
DROP TABLE IF EXISTS `DB_TABLE_COUNT`;
CREATE TABLE `DB_TABLE_COUNT` (
  `ID` char(32) NOT NULL,
  `DB_TABLE_ID` char(32) DEFAULT NULL,
  `TABLE_COUNT` bigint(20) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `DBTABLE_ID_CREATE_TIME` (`DB_TABLE_ID`,`CREATE_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dce_info
-- ----------------------------
DROP TABLE IF EXISTS `DCE_INFO`;
CREATE TABLE `DCE_INFO` (
  `ID` char(32) NOT NULL,
  `HOST_NAME` char(50) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `ACTIVE` char(1) DEFAULT NULL,
  `RES_TIMES` int(11) DEFAULT NULL,
  `REMARK` char(50) DEFAULT NULL,
  `GROUP_ID` varchar(500) DEFAULT NULL,
  `ACCOUNT` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `DCE_INFO_HOSTNAME_INDEX` (`HOST_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dce_state
-- ----------------------------
DROP TABLE IF EXISTS `DCE_STATE`;
CREATE TABLE `DCE_STATE` (
  `ID` char(32) NOT NULL,
  `DCE_ID` char(32) DEFAULT NULL,
  `RES_TIMES` int(11) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `DCE_STATE_DCEID_INDEX` (`DCE_ID`,`CREATE_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for desk_io
-- ----------------------------
DROP TABLE IF EXISTS `DESK_IO`;
CREATE TABLE `DESK_IO` (
  `ID` char(32) NOT NULL,
  `FILE_STSTEM` varchar(200) DEFAULT NULL,
  `READ_COUNT` char(20) DEFAULT NULL,
  `WRITE_OUNT` char(20) DEFAULT NULL,
  `READ_BYTES` char(20) DEFAULT NULL,
  `WRITE_BYTES` char(20) DEFAULT NULL,
  `READ_TIME` char(20) DEFAULT NULL,
  `WRITE_TIME` char(20) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `HOST_NAME` char(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for DISK_STATE
-- ----------------------------
DROP TABLE IF EXISTS `DISK_STATE`;
CREATE TABLE `DISK_STATE` (
  `ID` char(32) NOT NULL,
  `HOST_NAME` char(50) DEFAULT NULL,
  `FILE_STSTEM` char(200) DEFAULT NULL,
  `DISK_SIZE` char(30) DEFAULT NULL,
  `USED` char(30) DEFAULT NULL,
  `AVAIL` char(30) DEFAULT NULL,
  `USE_PER` char(10) DEFAULT NULL,
  `DATE_STR` char(30) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for disk_smart
-- ----------------------------
DROP TABLE IF EXISTS `DISK_SMART`;
CREATE TABLE `DISK_SMART` (
  `ID` char(32) COLLATE utf8_bin NOT NULL,
  `HOST_NAME` char(50) DEFAULT NULL,
  `FILE_STSTEM` varchar(255) DEFAULT NULL,
  `DISK_STATE` char(50) DEFAULT NULL,
  `POWER_HOURS` char(50) DEFAULT NULL,
  `POWER_COUNT` char(50) DEFAULT NULL,
  `TEMPERATURE` char(50) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for docker_info
-- ----------------------------
DROP TABLE IF EXISTS `DOCKER_INFO`;
CREATE TABLE `DOCKER_INFO` (
  `ID` char(32) NOT NULL,
  `HOST_NAME` char(50) DEFAULT NULL,
  `DOCKER_ID` varchar(100) DEFAULT NULL,
  `DOCKER_NAME` varchar(100) DEFAULT NULL,
  `DOCKER_STATE` varchar(50) DEFAULT NULL,
  `MEM_PER` double(8,0) DEFAULT NULL,
  `STATE` char(1) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `USER_TIME` char(20) DEFAULT NULL,
  `ACTIVE` char(1) DEFAULT NULL,
  `APP_TYPE` char(1) DEFAULT NULL,
  `DOCKER_IMAGE` varchar(100) DEFAULT NULL,
  `DOCKER_PORT` varchar(200) DEFAULT NULL,
  `DOCKER_COMMAND` varchar(500) DEFAULT NULL,
  `DOCKER_CREATED` varchar(50) DEFAULT NULL,
  `DOCKER_SIZE` varchar(20) DEFAULT NULL,
  `DOCKER_STATUS` varchar(100) DEFAULT NULL,
  `GATHER_DOCKER_NAMES` varchar(100) DEFAULT NULL,
  `GROUP_ID` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for docker_state
-- ----------------------------
DROP TABLE IF EXISTS `DOCKER_STATE`;
CREATE TABLE `DOCKER_STATE` (
  `ID` char(32) NOT NULL,
  `DOCKER_INFO_ID` char(32) DEFAULT NULL,
  `CPU_PER` double(8,0) DEFAULT NULL,
  `MEM_PER` double(8,0) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `DOCKER_STATE_INDEX` (`DOCKER_INFO_ID`,`CREATE_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for equipment
-- ----------------------------
DROP TABLE IF EXISTS `EQUIPMENT`;
CREATE TABLE `EQUIPMENT` (
  `ID` char(32) NOT NULL,
  `NAME` char(50) DEFAULT NULL,
  `XINGHAO` char(50) DEFAULT NULL,
  `PERSON` char(50) DEFAULT NULL,
  `DEPT` char(50) DEFAULT NULL,
  `CODE` char(50) DEFAULT NULL,
  `PRICE` double(10,2) DEFAULT NULL,
  `GONGYINGSHANG` varchar(50) DEFAULT NULL,
  `CAIGOU_DATE` varchar(50) DEFAULT NULL,
  `WEIBAO_DATE` varchar(50) DEFAULT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `ACCOUNT` varchar(50) DEFAULT NULL,
  `GROUP_ID` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for file_safe
-- ----------------------------
DROP TABLE IF EXISTS `FILE_SAFE`;
CREATE TABLE `FILE_SAFE` (
  `ID` char(32) NOT NULL,
  `HOST_NAME` char(50) DEFAULT NULL,
  `FILE_NAME` varchar(50) DEFAULT NULL,
  `STATE` char(1) DEFAULT NULL,
  `FILE_PATH` varchar(255) DEFAULT NULL,
  `ACTIVE` char(1) DEFAULT NULL,
  `FILE_SIGN` char(50) DEFAULT NULL,
  `FILE_MODTIME` char(50) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for file_warn
-- ----------------------------
DROP TABLE IF EXISTS `FILE_WARN`;
CREATE TABLE `FILE_WARN` (
  `ID` char(32) NOT NULL,
  `FILE_PATH` varchar(255) DEFAULT NULL,
  `FILE_SIZE` char(32) DEFAULT NULL,
  `WARN_CHARS` varchar(500) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `WARN_ROWS` char(20) DEFAULT NULL,
  `HOST_NAME` char(50) DEFAULT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  `ACTIVE` char(1) DEFAULT NULL,
  `FILE_NAME_PREFIX` varchar(50) DEFAULT NULL,
  `FILE_TYPE` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for file_warn_state
-- ----------------------------
DROP TABLE IF EXISTS `FILE_WARN_STATE`;
CREATE TABLE `FILE_WARN_STATE` (
  `ID` char(32) NOT NULL,
  `WAR_CONTENT` text,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `FILE_WARN_ID` char(32) DEFAULT NULL,
  `FILE_PATH` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FILE_WARN_ID_INDEX` (`FILE_WARN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for heath_monitor
-- ----------------------------
DROP TABLE IF EXISTS `HEATH_MONITOR`;
CREATE TABLE `HEATH_MONITOR` (
  `ID` char(32) NOT NULL,
  `APP_NAME` char(50) DEFAULT NULL,
  `HEATH_URL` varchar(255) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `HEATH_STATUS` char(10) DEFAULT NULL,
  `RES_TIMES` int(11) DEFAULT NULL,
  `ACTIVE` char(1) DEFAULT NULL,
  `RES_KEYWORD` varchar(255) DEFAULT NULL,
  `METHOD` char(5) DEFAULT NULL,
  `POST_STR` varchar(2000) DEFAULT NULL,
  `RES_NO_KEYWORD` varchar(255) DEFAULT NULL,
  `HEADER_JSON` varchar(1500) DEFAULT NULL,
  `ACCOUNT` varchar(50) DEFAULT NULL,
  `RESPONSE_BODY_SIZE` varchar(20) DEFAULT NULL,
  `FORM_JSON` varchar(1500) DEFAULT NULL,
  `ERROR_MSG` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for heath_state
-- ----------------------------
DROP TABLE IF EXISTS `HEATH_STATE`;
CREATE TABLE `HEATH_STATE` (
  `ID` char(32) NOT NULL,
  `HEATH_ID` char(32) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `RES_TIMES` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `HEATH_ID_CREATE_TIME` (`HEATH_ID`,`CREATE_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for host_disk_per
-- ----------------------------
DROP TABLE IF EXISTS `HOST_DISK_PER`;
CREATE TABLE `HOST_DISK_PER` (
  `ID` char(32) NOT NULL,
  `HOST_NAME` char(50) DEFAULT NULL,
  `DISK_SUM_PER` double DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for host_group
-- ----------------------------
DROP TABLE IF EXISTS `HOST_GROUP`;
CREATE TABLE `HOST_GROUP` (
  `ID` char(32) NOT NULL,
  `GROUP_NAME` char(30) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `REMARK` varchar(255) DEFAULT NULL,
  `GROUP_TYPE` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for intrusion_info
-- ----------------------------
DROP TABLE IF EXISTS `INTRUSION_INFO`;
CREATE TABLE `INTRUSION_INFO` (
  `ID` char(32) NOT NULL,
  `HOST_NAME` char(30) DEFAULT NULL,
  `LSMOD` text,
  `PASSWD_INFO` varchar(100) DEFAULT NULL,
  `CRONTAB` text,
  `PROMISC` varchar(100) DEFAULT NULL,
  `RPCINFO` text,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for log_info
-- ----------------------------
DROP TABLE IF EXISTS `LOG_INFO`;
CREATE TABLE `LOG_INFO` (
  `ID` char(32) NOT NULL,
  `HOST_NAME` char(200) DEFAULT NULL,
  `INFO_CONTENT` text,
  `STATE` char(1) DEFAULT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mail_set
-- ----------------------------
DROP TABLE IF EXISTS `MAIL_SET`;
CREATE TABLE `MAIL_SET` (
  `ID` char(32) COLLATE utf8_unicode_ci NOT NULL,
  `SEND_MAIL` char(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FROM_MAIL_NAME` char(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FROM_PWD` char(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SMTP_HOST` char(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SMTP_PORT` char(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SMTP_SSL` char(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TO_MAIL` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CPU_PER` char(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `MEM_PER` char(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `HEATH_PER` char(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for mem_state
-- ----------------------------
DROP TABLE IF EXISTS `MEM_STATE`;
CREATE TABLE `MEM_STATE` (
  `ID` char(32) NOT NULL,
  `HOST_NAME` char(50) DEFAULT NULL,
  `TOTAL` char(30) DEFAULT NULL,
  `USED` char(30) DEFAULT NULL,
  `FREE` char(30) DEFAULT NULL,
  `USE_PER` double(8,2) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `MEM_ACC_HOST_INDEX` (`HOST_NAME`,`CREATE_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for netio_state
-- ----------------------------
DROP TABLE IF EXISTS `NETIO_STATE`;
CREATE TABLE `NETIO_STATE` (
  `ID` char(32) NOT NULL,
  `HOST_NAME` char(50) DEFAULT NULL,
  `RXPCK` char(30) DEFAULT NULL,
  `TXPCK` char(30) DEFAULT NULL,
  `RXBYT` char(30) DEFAULT NULL,
  `TXBYT` char(30) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `DROPIN` char(30) DEFAULT NULL,
  `DROPOUT` char(30) DEFAULT NULL,
  `NET_CONNECTIONS` char(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `NETIO_ACC_HOST_INDEX` (`HOST_NAME`,`CREATE_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for port_info
-- ----------------------------
DROP TABLE IF EXISTS `PORT_INFO`;
CREATE TABLE `PORT_INFO` (
  `ID` char(32) NOT NULL,
  `PORT` char(30) DEFAULT NULL,
  `PORT_NAME` varchar(30) DEFAULT NULL,
  `STATE` char(1) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `HOST_NAME` char(50) DEFAULT NULL,
  `ACTIVE` char(1) DEFAULT NULL,
  `TELNET_IP` varchar(300) DEFAULT NULL,
  `GROUP_ID` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `PORT_HOST_NAME_INDEX` (`HOST_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for shell_info
-- ----------------------------
DROP TABLE IF EXISTS `SHELL_INFO`;
CREATE TABLE `SHELL_INFO` (
  `ID` char(32) NOT NULL,
  `SHELL` varchar(2000) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `STATE` char(1) DEFAULT NULL,
  `SHELL_NAME` varchar(50) DEFAULT NULL,
  `SHELL_TYPE` varchar(5) DEFAULT NULL,
  `SHELL_TIME` varchar(20) DEFAULT NULL,
  `SHELL_DAY` varchar(5) DEFAULT NULL,
  `ACCOUNT` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for shell_state
-- ----------------------------
DROP TABLE IF EXISTS `SHELL_STATE`;
CREATE TABLE `SHELL_STATE` (
  `ID` char(32) NOT NULL,
  `SHELL_ID` char(32) DEFAULT NULL,
  `HOST_NAME` char(50) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `STATE` varchar(500) DEFAULT NULL,
  `SHELL` varchar(2000) DEFAULT NULL,
  `SHELL_TIME` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `SHELL_STATE_INDEX2` (`SHELL_ID`) USING BTREE,
  KEY `SHELL_STATE_INDEX1` (`HOST_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_info
-- ----------------------------
DROP TABLE IF EXISTS `SYSTEM_INFO`;
CREATE TABLE `SYSTEM_INFO` (
  `ID` char(32) NOT NULL,
  `HOST_NAME` char(50) DEFAULT NULL,
  `PLATFORM` char(200) DEFAULT NULL,
  `CPU_PER` double(8,2) DEFAULT NULL,
  `MEM_PER` double(8,2) DEFAULT NULL,
  `CPU_CORE_NUM` char(10) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `CPU_XH` char(150) DEFAULT NULL,
  `STATE` char(1) DEFAULT NULL,
  `BOOT_TIME` bigint(20) DEFAULT NULL,
  `PROCS` char(11) DEFAULT NULL,
  `PLATFORM_VERSION` char(100) DEFAULT NULL,
  `UPTIME` bigint(20) DEFAULT NULL,
  `GROUP_ID` varchar(500) DEFAULT NULL,
  `REMARK` varchar(100) DEFAULT NULL,
  `TOTAL_MEM` char(50) DEFAULT NULL,
  `SUBMIT_SECONDS` char(10) DEFAULT NULL,
  `AGENT_VER` char(50) DEFAULT NULL,
  `BYTES_RECV` char(20) DEFAULT NULL,
  `BYTES_SENT` char(20) DEFAULT NULL,
  `RXBYT` char(30) DEFAULT NULL,
  `TXBYT` char(30) DEFAULT NULL,
  `WIN_CONSOLE` varchar(255) DEFAULT NULL,
  `HOST_NAME_EXT` varchar(100) DEFAULT NULL,
  `FIVE_LOAD` double(8,2) DEFAULT NULL,
  `FIFTEEN_LOAD` double(8,2) DEFAULT NULL,
  `NET_CONNECTIONS` char(20) DEFAULT NULL,
  `SWAP_MEM_PER` varchar(20) DEFAULT NULL,
  `TOTAL_SWAP_MEM` varchar(50) DEFAULT NULL,
  `ACCOUNT` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_load_state
-- ----------------------------
DROP TABLE IF EXISTS `SYS_LOAD_STATE`;
CREATE TABLE `SYS_LOAD_STATE` (
  `ID` char(32) NOT NULL,
  `HOST_NAME` char(50) DEFAULT NULL,
  `ONE_LOAD` double(8,2) DEFAULT NULL,
  `FIVE_LOAD` double(8,2) DEFAULT NULL,
  `FIFTEEN_LOAD` double(8,2) DEFAULT NULL,
  `USERS` char(10) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `LOAD_ACC_HOST_INDEX` (`HOST_NAME`,`CREATE_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tcp_state
-- ----------------------------
DROP TABLE IF EXISTS `TCP_STATE`;
CREATE TABLE `TCP_STATE` (
  `ID` char(32) NOT NULL,
  `HOST_NAME` char(30) DEFAULT NULL,
  `ACTIVE` char(30) DEFAULT NULL,
  `PASSIVE` char(30) DEFAULT NULL,
  `RETRANS` char(30) DEFAULT NULL,
  `DATE_STR` char(30) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `TCP_ACC_HOST_INDEX` (`HOST_NAME`,`CREATE_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `SNMP_INFO`;
CREATE TABLE `SNMP_INFO` (
  `ID` char(32) NOT NULL,
  `HOST_NAME` varchar(50) DEFAULT NULL,
  `BYTES_RECV` varchar(30) DEFAULT NULL,
  `BYTES_SENT` varchar(30) DEFAULT NULL,
  `RECV_AVG` varchar(20) DEFAULT NULL,
  `SENT_AVG` varchar(20) DEFAULT NULL,
  `STATE` char(1) DEFAULT NULL,
  `ACTIVE` char(1) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `SNMP_UNIT` varchar(20) DEFAULT NULL,
  `REMARK` varchar(50) DEFAULT NULL,
  `RECV_OID` varchar(2000) DEFAULT NULL,
  `SENT_OID` varchar(2000) DEFAULT NULL,
  `SNMP_COMMUNITY` varchar(50) DEFAULT NULL,
  `SNMP_PORT` varchar(10) DEFAULT NULL,
  `SNMP_VERSION` varchar(10) DEFAULT NULL,
  `ACCOUNT` varchar(50) DEFAULT NULL,
  `CPU_PER_OID` varchar(100) DEFAULT NULL,
  `MEM_SIZE_OID` varchar(100) DEFAULT NULL,
  `MEM_TOTAL_SIZE_OID` varchar(100) DEFAULT NULL,
  `CPU_PER` varchar(10) DEFAULT NULL,
  `MEM_PER` varchar(10) DEFAULT NULL,
  `DISK_PER_OID` varchar(100) DEFAULT NULL,
  `DISK_PER` varchar(10) DEFAULT NULL,
  `RECV_AVG_OID` varchar(100) DEFAULT NULL,
  `SENT_AVG_OID` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `SNMP_STATE`;
CREATE TABLE `SNMP_STATE` (
  `ID` char(32) NOT NULL,
  `SNMP_INFO_ID` char(32) DEFAULT NULL,
  `RECV_AVG` varchar(20) DEFAULT NULL,
  `SENT_AVG` varchar(20) DEFAULT NULL,
  `CPU_PER` varchar(10) DEFAULT NULL,
  `MEM_PER` varchar(10) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `SNMP_ID_CREATE_TIME` (`SNMP_INFO_ID`,`CREATE_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `ACCOUNT_INFO`;
CREATE TABLE `ACCOUNT_INFO` (
  `ID` char(32) NOT NULL,
  `ACCOUNT` varchar(50) DEFAULT NULL,
  `EMAIL` varchar(50) DEFAULT NULL,
  `PASSWD` varchar(50) DEFAULT NULL,
  `ACCOUNT_KEY` varchar(50) DEFAULT NULL,
  `REMARK` varchar(50) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for custom_info
-- ----------------------------
DROP TABLE IF EXISTS `CUSTOM_INFO`;
CREATE TABLE `CUSTOM_INFO` (
  `ID` char(32) NOT NULL,
  `CUSTOM_NAME` varchar(50) DEFAULT NULL,
  `CUSTOM_SHELL` varchar(2000) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `HOST_NAME` char(50) DEFAULT NULL,
  `ACTIVE` char(1) DEFAULT NULL,
  `STATE` char(1) DEFAULT NULL,
  `RESULT_EXP` varchar(100) DEFAULT NULL,
  `GROUP_ID` varchar(500) DEFAULT NULL,
  `CUSTOM_VALUE` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for custom_state
-- ----------------------------
DROP TABLE IF EXISTS `CUSTOM_STATE`;
CREATE TABLE `CUSTOM_STATE` (
  `ID` char(32) NOT NULL,
  `CUSTOM_INFO_ID` char(32) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `CUSTOM_VALUE` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `CUSTOM_STAT_INDEX` (`CUSTOM_INFO_ID`,`CREATE_TIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `FTP_INFO`;
CREATE TABLE `FTP_INFO` (
  `ID` varchar(32) NOT NULL,
  `FTP_HOST` varchar(100) DEFAULT NULL,
  `USER_NAME` varchar(50) DEFAULT NULL,
  `PASSWD` varchar(50) DEFAULT NULL,
  `PORT` varchar(50) DEFAULT NULL,
  `ACTIVE` char(1) DEFAULT NULL,
  `WARN_TYPE` varchar(10) DEFAULT NULL,
  `STATE` char(1) DEFAULT NULL,
  `FTP_TYPE` varchar(10) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `ACCOUNT` varchar(50) DEFAULT NULL,
  `FTP_NAME` varchar(50) DEFAULT NULL,
  `RES_TIMES` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `REPORT_INFO`;
CREATE TABLE `REPORT_INFO` (
  `ID` char(32) NOT NULL,
  `TIME_PART` varchar(100) DEFAULT NULL,
  `REPORT_TYPE` varchar(1) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `REPORT_INSTANCE`;
CREATE TABLE `REPORT_INSTANCE` (
  `ID` char(32) NOT NULL,
  `INFO_KEY` varchar(50) DEFAULT NULL,
  `INFO_CONTENT` varchar(200) DEFAULT NULL,
  `REPORT_INFO_ID` char(32) DEFAULT NULL,
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
