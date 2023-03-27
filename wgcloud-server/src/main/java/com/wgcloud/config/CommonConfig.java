package com.wgcloud.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
   prefix = "base"
)
public class CommonConfig {
   private String account = "admin";
   private String accountPwd = "111111";
   private String guestAccount = "guest";
   private String guestAccountPwd = "111111";
   private String wgToken = "";
   private String dashView;
   private String dashViewIpHide;
   private String dapingView;
   private String openDataAPI;
   private Integer dbTableTimes = 3600;
   private Integer heathTimes = 600;
   private Integer ftpTimes = 600;
   private Integer dceTimes = 900;
   private Integer snmpTimes = 900;
   private Integer warnCacheTimes = 7200;
   private String nodeType = "master";
   private Integer historyDataOut = 10;
   private Integer pageSize = 20;
   private String copyRight;
   private String icoUrl = "";
   private String logoUrl = "";
   private String wgName = "";
   private String wgShortName = "";
   private String webSsh;
   private Integer webSshPort = 9998;
   private String sidebarCollapse;
   private String showWarnCount;
   private String shellToRun;
   private String shellToRunLinuxBlock;
   private String shellToRunWinBlock;
   private String sqlInKeys;
   private String daemonUrl;
   private String mailTitlePrefix;
   private String mailContentSuffix;
   private String hostGroup;
   private String userInfoManage;
   private Integer maxPoolSize;
   private String dashViewAutoData;
   private String dashViewListAutoData;

   public String getAccount() {
      return this.account;
   }

   public void setAccount(String account) {
      this.account = account;
   }

   public String getAccountPwd() {
      return this.accountPwd;
   }

   public void setAccountPwd(String accountPwd) {
      this.accountPwd = accountPwd;
   }

   public String getWgToken() {
      return this.wgToken;
   }

   public void setWgToken(String wgToken) {
      this.wgToken = wgToken;
   }

   public String getDashView() {
      return StringUtils.isEmpty(this.dashView) ? "true" : this.dashView;
   }

   public void setDashView(String dashView) {
      this.dashView = dashView;
   }

   public Integer getDbTableTimes() {
      return this.dbTableTimes == null ? 3600 : this.dbTableTimes;
   }

   public void setDbTableTimes(Integer dbTableTimes) {
      this.dbTableTimes = dbTableTimes;
   }

   public Integer getHeathTimes() {
      return this.heathTimes == null ? 600 : this.heathTimes;
   }

   public void setHeathTimes(Integer heathTimes) {
      this.heathTimes = heathTimes;
   }

   public String getNodeType() {
      return this.nodeType;
   }

   public void setNodeType(String nodeType) {
      this.nodeType = nodeType;
   }

   public Integer getHistoryDataOut() {
      return this.historyDataOut == null ? 10 : this.historyDataOut;
   }

   public void setHistoryDataOut(Integer historyDataOut) {
      this.historyDataOut = historyDataOut;
   }

   public Integer getWarnCacheTimes() {
      return this.warnCacheTimes == null ? 3600 : this.warnCacheTimes;
   }

   public void setWarnCacheTimes(Integer warnCacheTimes) {
      this.warnCacheTimes = warnCacheTimes;
   }

   public String getIcoUrl() {
      return this.icoUrl;
   }

   public void setIcoUrl(String icoUrl) {
      this.icoUrl = icoUrl;
   }

   public String getLogoUrl() {
      return this.logoUrl;
   }

   public void setLogoUrl(String logoUrl) {
      this.logoUrl = logoUrl;
   }

   public String getWgName() {
      return this.wgName;
   }

   public void setWgName(String wgName) {
      this.wgName = wgName;
   }

   public String getWgShortName() {
      return this.wgShortName;
   }

   public void setWgShortName(String wgShortName) {
      this.wgShortName = wgShortName;
   }

   public String getDapingView() {
      return StringUtils.isEmpty(this.dapingView) ? "true" : this.dapingView;
   }

   public void setDapingView(String dapingView) {
      this.dapingView = dapingView;
   }

   public String getWebSsh() {
      return StringUtils.isEmpty(this.webSsh) ? "true" : this.webSsh;
   }

   public void setWebSsh(String webSsh) {
      this.webSsh = webSsh;
   }

   public Integer getWebSshPort() {
      return this.warnCacheTimes == null ? 9998 : this.webSshPort;
   }

   public void setWebSshPort(Integer webSshPort) {
      this.webSshPort = webSshPort;
   }

   public Integer getDceTimes() {
      return this.dceTimes == null ? 900 : this.dceTimes;
   }

   public void setDceTimes(Integer dceTimes) {
      this.dceTimes = dceTimes;
   }

   public String getSidebarCollapse() {
      return !StringUtils.isEmpty(this.sidebarCollapse) && !"true".equals(this.sidebarCollapse) ? "hold-transition sidebar-mini layout-fixed" : "sidebar-mini sidebar-collapse";
   }

   public void setSidebarCollapse(String sidebarCollapse) {
      this.sidebarCollapse = sidebarCollapse;
   }

   public String getShellToRun() {
      return StringUtils.isEmpty(this.shellToRun) ? "false" : this.shellToRun;
   }

   public void setShellToRun(String shellToRun) {
      this.shellToRun = shellToRun;
   }

   public String getShellToRunLinuxBlock() {
      return this.shellToRunLinuxBlock;
   }

   public void setShellToRunLinuxBlock(String shellToRunLinuxBlock) {
      this.shellToRunLinuxBlock = shellToRunLinuxBlock;
   }

   public String getShellToRunWinBlock() {
      return this.shellToRunWinBlock;
   }

   public void setShellToRunWinBlock(String shellToRunWinBlock) {
      this.shellToRunWinBlock = shellToRunWinBlock;
   }

   public String getDaemonUrl() {
      return StringUtils.isEmpty(this.daemonUrl) ? "http://localhost:9997" : this.daemonUrl;
   }

   public void setDaemonUrl(String daemonUrl) {
      this.daemonUrl = daemonUrl;
   }

   public String getGuestAccount() {
      return this.guestAccount;
   }

   public void setGuestAccount(String guestAccount) {
      this.guestAccount = guestAccount;
   }

   public String getGuestAccountPwd() {
      return this.guestAccountPwd;
   }

   public void setGuestAccountPwd(String guestAccountPwd) {
      this.guestAccountPwd = guestAccountPwd;
   }

   public String getMailTitlePrefix() {
      return this.mailTitlePrefix;
   }

   public void setMailTitlePrefix(String mailTitlePrefix) {
      this.mailTitlePrefix = mailTitlePrefix;
   }

   public String getMailContentSuffix() {
      return this.mailContentSuffix;
   }

   public void setMailContentSuffix(String mailContentSuffix) {
      this.mailContentSuffix = mailContentSuffix;
   }

   public String getHostGroup() {
      return StringUtils.isEmpty(this.hostGroup) ? "false" : this.hostGroup;
   }

   public void setHostGroup(String hostGroup) {
      this.hostGroup = hostGroup;
   }

   public String getUserInfoManage() {
      return StringUtils.isEmpty(this.userInfoManage) ? "false" : this.userInfoManage;
   }

   public void setUserInfoManage(String userInfoManage) {
      this.userInfoManage = userInfoManage;
   }

   public String getCopyRight() {
      return StringUtils.isEmpty(this.copyRight) ? "true" : this.copyRight;
   }

   public void setCopyRight(String copyRight) {
      this.copyRight = copyRight;
   }

   public Integer getPageSize() {
      if (this.pageSize < 10) {
         this.pageSize = 10;
      }

      return this.pageSize;
   }

   public void setPageSize(Integer pageSize) {
      this.pageSize = pageSize;
   }

   public Integer getSnmpTimes() {
      return this.snmpTimes == null ? 1200 : this.snmpTimes;
   }

   public void setSnmpTimes(Integer snmpTimes) {
      this.snmpTimes = snmpTimes;
   }

   public String getSqlInKeys() {
      return StringUtils.isEmpty(this.sqlInKeys) ? "execute ,delete ,drop ,alter ,rename ,modify " : this.sqlInKeys;
   }

   public void setSqlInKeys(String sqlInKeys) {
      this.sqlInKeys = sqlInKeys;
   }

   public String getShowWarnCount() {
      return StringUtils.isEmpty(this.showWarnCount) ? "false" : this.showWarnCount;
   }

   public void setShowWarnCount(String showWarnCount) {
      this.showWarnCount = showWarnCount;
   }

   public String getDashViewIpHide() {
      return StringUtils.isEmpty(this.dashViewIpHide) ? "true" : this.dashViewIpHide;
   }

   public void setDashViewIpHide(String dashViewIpHide) {
      this.dashViewIpHide = dashViewIpHide;
   }

   public Integer getFtpTimes() {
      return this.ftpTimes;
   }

   public void setFtpTimes(Integer ftpTimes) {
      this.ftpTimes = ftpTimes;
   }

   public String getOpenDataAPI() {
      return StringUtils.isEmpty(this.openDataAPI) ? "true" : this.openDataAPI;
   }

   public void setOpenDataAPI(String openDataAPI) {
      this.openDataAPI = openDataAPI;
   }

   public Integer getMaxPoolSize() {
      return this.maxPoolSize;
   }

   public void setMaxPoolSize(Integer maxPoolSize) {
      this.maxPoolSize = maxPoolSize;
   }

   public String getDashViewAutoData() {
      return StringUtils.isEmpty(this.dashViewAutoData) ? "false" : this.dashViewAutoData;
   }

   public void setDashViewAutoData(String dashViewAutoData) {
      this.dashViewAutoData = dashViewAutoData;
   }

   public String getDashViewListAutoData() {
      return StringUtils.isEmpty(this.dashViewListAutoData) ? "false" : this.dashViewListAutoData;
   }

   public void setDashViewListAutoData(String dashViewListAutoData) {
      this.dashViewListAutoData = dashViewListAutoData;
   }

   public boolean equals(final Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof CommonConfig)) {
         return false;
      } else {
         CommonConfig other = (CommonConfig)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label479: {
               Object this$dbTableTimes = this.getDbTableTimes();
               Object other$dbTableTimes = other.getDbTableTimes();
               if (this$dbTableTimes == null) {
                  if (other$dbTableTimes == null) {
                     break label479;
                  }
               } else if (this$dbTableTimes.equals(other$dbTableTimes)) {
                  break label479;
               }

               return false;
            }

            Object this$heathTimes = this.getHeathTimes();
            Object other$heathTimes = other.getHeathTimes();
            if (this$heathTimes == null) {
               if (other$heathTimes != null) {
                  return false;
               }
            } else if (!this$heathTimes.equals(other$heathTimes)) {
               return false;
            }

            Object this$ftpTimes = this.getFtpTimes();
            Object other$ftpTimes = other.getFtpTimes();
            if (this$ftpTimes == null) {
               if (other$ftpTimes != null) {
                  return false;
               }
            } else if (!this$ftpTimes.equals(other$ftpTimes)) {
               return false;
            }

            label458: {
               Object this$dceTimes = this.getDceTimes();
               Object other$dceTimes = other.getDceTimes();
               if (this$dceTimes == null) {
                  if (other$dceTimes == null) {
                     break label458;
                  }
               } else if (this$dceTimes.equals(other$dceTimes)) {
                  break label458;
               }

               return false;
            }

            label451: {
               Object this$snmpTimes = this.getSnmpTimes();
               Object other$snmpTimes = other.getSnmpTimes();
               if (this$snmpTimes == null) {
                  if (other$snmpTimes == null) {
                     break label451;
                  }
               } else if (this$snmpTimes.equals(other$snmpTimes)) {
                  break label451;
               }

               return false;
            }

            Object this$warnCacheTimes = this.getWarnCacheTimes();
            Object other$warnCacheTimes = other.getWarnCacheTimes();
            if (this$warnCacheTimes == null) {
               if (other$warnCacheTimes != null) {
                  return false;
               }
            } else if (!this$warnCacheTimes.equals(other$warnCacheTimes)) {
               return false;
            }

            Object this$historyDataOut = this.getHistoryDataOut();
            Object other$historyDataOut = other.getHistoryDataOut();
            if (this$historyDataOut == null) {
               if (other$historyDataOut != null) {
                  return false;
               }
            } else if (!this$historyDataOut.equals(other$historyDataOut)) {
               return false;
            }

            label430: {
               Object this$pageSize = this.getPageSize();
               Object other$pageSize = other.getPageSize();
               if (this$pageSize == null) {
                  if (other$pageSize == null) {
                     break label430;
                  }
               } else if (this$pageSize.equals(other$pageSize)) {
                  break label430;
               }

               return false;
            }

            label423: {
               Object this$webSshPort = this.getWebSshPort();
               Object other$webSshPort = other.getWebSshPort();
               if (this$webSshPort == null) {
                  if (other$webSshPort == null) {
                     break label423;
                  }
               } else if (this$webSshPort.equals(other$webSshPort)) {
                  break label423;
               }

               return false;
            }

            Object this$maxPoolSize = this.getMaxPoolSize();
            Object other$maxPoolSize = other.getMaxPoolSize();
            if (this$maxPoolSize == null) {
               if (other$maxPoolSize != null) {
                  return false;
               }
            } else if (!this$maxPoolSize.equals(other$maxPoolSize)) {
               return false;
            }

            label409: {
               Object this$account = this.getAccount();
               Object other$account = other.getAccount();
               if (this$account == null) {
                  if (other$account == null) {
                     break label409;
                  }
               } else if (this$account.equals(other$account)) {
                  break label409;
               }

               return false;
            }

            Object this$accountPwd = this.getAccountPwd();
            Object other$accountPwd = other.getAccountPwd();
            if (this$accountPwd == null) {
               if (other$accountPwd != null) {
                  return false;
               }
            } else if (!this$accountPwd.equals(other$accountPwd)) {
               return false;
            }

            label395: {
               Object this$guestAccount = this.getGuestAccount();
               Object other$guestAccount = other.getGuestAccount();
               if (this$guestAccount == null) {
                  if (other$guestAccount == null) {
                     break label395;
                  }
               } else if (this$guestAccount.equals(other$guestAccount)) {
                  break label395;
               }

               return false;
            }

            Object this$guestAccountPwd = this.getGuestAccountPwd();
            Object other$guestAccountPwd = other.getGuestAccountPwd();
            if (this$guestAccountPwd == null) {
               if (other$guestAccountPwd != null) {
                  return false;
               }
            } else if (!this$guestAccountPwd.equals(other$guestAccountPwd)) {
               return false;
            }

            Object this$wgToken = this.getWgToken();
            Object other$wgToken = other.getWgToken();
            if (this$wgToken == null) {
               if (other$wgToken != null) {
                  return false;
               }
            } else if (!this$wgToken.equals(other$wgToken)) {
               return false;
            }

            label374: {
               Object this$dashView = this.getDashView();
               Object other$dashView = other.getDashView();
               if (this$dashView == null) {
                  if (other$dashView == null) {
                     break label374;
                  }
               } else if (this$dashView.equals(other$dashView)) {
                  break label374;
               }

               return false;
            }

            label367: {
               Object this$dashViewIpHide = this.getDashViewIpHide();
               Object other$dashViewIpHide = other.getDashViewIpHide();
               if (this$dashViewIpHide == null) {
                  if (other$dashViewIpHide == null) {
                     break label367;
                  }
               } else if (this$dashViewIpHide.equals(other$dashViewIpHide)) {
                  break label367;
               }

               return false;
            }

            Object this$dapingView = this.getDapingView();
            Object other$dapingView = other.getDapingView();
            if (this$dapingView == null) {
               if (other$dapingView != null) {
                  return false;
               }
            } else if (!this$dapingView.equals(other$dapingView)) {
               return false;
            }

            Object this$openDataAPI = this.getOpenDataAPI();
            Object other$openDataAPI = other.getOpenDataAPI();
            if (this$openDataAPI == null) {
               if (other$openDataAPI != null) {
                  return false;
               }
            } else if (!this$openDataAPI.equals(other$openDataAPI)) {
               return false;
            }

            label346: {
               Object this$nodeType = this.getNodeType();
               Object other$nodeType = other.getNodeType();
               if (this$nodeType == null) {
                  if (other$nodeType == null) {
                     break label346;
                  }
               } else if (this$nodeType.equals(other$nodeType)) {
                  break label346;
               }

               return false;
            }

            label339: {
               Object this$copyRight = this.getCopyRight();
               Object other$copyRight = other.getCopyRight();
               if (this$copyRight == null) {
                  if (other$copyRight == null) {
                     break label339;
                  }
               } else if (this$copyRight.equals(other$copyRight)) {
                  break label339;
               }

               return false;
            }

            Object this$icoUrl = this.getIcoUrl();
            Object other$icoUrl = other.getIcoUrl();
            if (this$icoUrl == null) {
               if (other$icoUrl != null) {
                  return false;
               }
            } else if (!this$icoUrl.equals(other$icoUrl)) {
               return false;
            }

            Object this$logoUrl = this.getLogoUrl();
            Object other$logoUrl = other.getLogoUrl();
            if (this$logoUrl == null) {
               if (other$logoUrl != null) {
                  return false;
               }
            } else if (!this$logoUrl.equals(other$logoUrl)) {
               return false;
            }

            label318: {
               Object this$wgName = this.getWgName();
               Object other$wgName = other.getWgName();
               if (this$wgName == null) {
                  if (other$wgName == null) {
                     break label318;
                  }
               } else if (this$wgName.equals(other$wgName)) {
                  break label318;
               }

               return false;
            }

            label311: {
               Object this$wgShortName = this.getWgShortName();
               Object other$wgShortName = other.getWgShortName();
               if (this$wgShortName == null) {
                  if (other$wgShortName == null) {
                     break label311;
                  }
               } else if (this$wgShortName.equals(other$wgShortName)) {
                  break label311;
               }

               return false;
            }

            Object this$webSsh = this.getWebSsh();
            Object other$webSsh = other.getWebSsh();
            if (this$webSsh == null) {
               if (other$webSsh != null) {
                  return false;
               }
            } else if (!this$webSsh.equals(other$webSsh)) {
               return false;
            }

            label297: {
               Object this$sidebarCollapse = this.getSidebarCollapse();
               Object other$sidebarCollapse = other.getSidebarCollapse();
               if (this$sidebarCollapse == null) {
                  if (other$sidebarCollapse == null) {
                     break label297;
                  }
               } else if (this$sidebarCollapse.equals(other$sidebarCollapse)) {
                  break label297;
               }

               return false;
            }

            Object this$showWarnCount = this.getShowWarnCount();
            Object other$showWarnCount = other.getShowWarnCount();
            if (this$showWarnCount == null) {
               if (other$showWarnCount != null) {
                  return false;
               }
            } else if (!this$showWarnCount.equals(other$showWarnCount)) {
               return false;
            }

            label283: {
               Object this$shellToRun = this.getShellToRun();
               Object other$shellToRun = other.getShellToRun();
               if (this$shellToRun == null) {
                  if (other$shellToRun == null) {
                     break label283;
                  }
               } else if (this$shellToRun.equals(other$shellToRun)) {
                  break label283;
               }

               return false;
            }

            Object this$shellToRunLinuxBlock = this.getShellToRunLinuxBlock();
            Object other$shellToRunLinuxBlock = other.getShellToRunLinuxBlock();
            if (this$shellToRunLinuxBlock == null) {
               if (other$shellToRunLinuxBlock != null) {
                  return false;
               }
            } else if (!this$shellToRunLinuxBlock.equals(other$shellToRunLinuxBlock)) {
               return false;
            }

            Object this$shellToRunWinBlock = this.getShellToRunWinBlock();
            Object other$shellToRunWinBlock = other.getShellToRunWinBlock();
            if (this$shellToRunWinBlock == null) {
               if (other$shellToRunWinBlock != null) {
                  return false;
               }
            } else if (!this$shellToRunWinBlock.equals(other$shellToRunWinBlock)) {
               return false;
            }

            label262: {
               Object this$sqlInKeys = this.getSqlInKeys();
               Object other$sqlInKeys = other.getSqlInKeys();
               if (this$sqlInKeys == null) {
                  if (other$sqlInKeys == null) {
                     break label262;
                  }
               } else if (this$sqlInKeys.equals(other$sqlInKeys)) {
                  break label262;
               }

               return false;
            }

            label255: {
               Object this$daemonUrl = this.getDaemonUrl();
               Object other$daemonUrl = other.getDaemonUrl();
               if (this$daemonUrl == null) {
                  if (other$daemonUrl == null) {
                     break label255;
                  }
               } else if (this$daemonUrl.equals(other$daemonUrl)) {
                  break label255;
               }

               return false;
            }

            Object this$mailTitlePrefix = this.getMailTitlePrefix();
            Object other$mailTitlePrefix = other.getMailTitlePrefix();
            if (this$mailTitlePrefix == null) {
               if (other$mailTitlePrefix != null) {
                  return false;
               }
            } else if (!this$mailTitlePrefix.equals(other$mailTitlePrefix)) {
               return false;
            }

            Object this$mailContentSuffix = this.getMailContentSuffix();
            Object other$mailContentSuffix = other.getMailContentSuffix();
            if (this$mailContentSuffix == null) {
               if (other$mailContentSuffix != null) {
                  return false;
               }
            } else if (!this$mailContentSuffix.equals(other$mailContentSuffix)) {
               return false;
            }

            label234: {
               Object this$hostGroup = this.getHostGroup();
               Object other$hostGroup = other.getHostGroup();
               if (this$hostGroup == null) {
                  if (other$hostGroup == null) {
                     break label234;
                  }
               } else if (this$hostGroup.equals(other$hostGroup)) {
                  break label234;
               }

               return false;
            }

            label227: {
               Object this$userInfoManage = this.getUserInfoManage();
               Object other$userInfoManage = other.getUserInfoManage();
               if (this$userInfoManage == null) {
                  if (other$userInfoManage == null) {
                     break label227;
                  }
               } else if (this$userInfoManage.equals(other$userInfoManage)) {
                  break label227;
               }

               return false;
            }

            Object this$dashViewAutoData = this.getDashViewAutoData();
            Object other$dashViewAutoData = other.getDashViewAutoData();
            if (this$dashViewAutoData == null) {
               if (other$dashViewAutoData != null) {
                  return false;
               }
            } else if (!this$dashViewAutoData.equals(other$dashViewAutoData)) {
               return false;
            }

            Object this$dashViewListAutoData = this.getDashViewListAutoData();
            Object other$dashViewListAutoData = other.getDashViewListAutoData();
            if (this$dashViewListAutoData == null) {
               if (other$dashViewListAutoData != null) {
                  return false;
               }
            } else if (!this$dashViewListAutoData.equals(other$dashViewListAutoData)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(final Object other) {
      return other instanceof CommonConfig;
   }

   public int hashCode() {
//      int PRIME = true;
      int result = 1;
      Object $dbTableTimes = this.getDbTableTimes();
//      int result = result * 59 + ($dbTableTimes == null ? 43 : $dbTableTimes.hashCode());
      result = result * 59 + ($dbTableTimes == null ? 43 : $dbTableTimes.hashCode());
      Object $heathTimes = this.getHeathTimes();
      result = result * 59 + ($heathTimes == null ? 43 : $heathTimes.hashCode());
      Object $ftpTimes = this.getFtpTimes();
      result = result * 59 + ($ftpTimes == null ? 43 : $ftpTimes.hashCode());
      Object $dceTimes = this.getDceTimes();
      result = result * 59 + ($dceTimes == null ? 43 : $dceTimes.hashCode());
      Object $snmpTimes = this.getSnmpTimes();
      result = result * 59 + ($snmpTimes == null ? 43 : $snmpTimes.hashCode());
      Object $warnCacheTimes = this.getWarnCacheTimes();
      result = result * 59 + ($warnCacheTimes == null ? 43 : $warnCacheTimes.hashCode());
      Object $historyDataOut = this.getHistoryDataOut();
      result = result * 59 + ($historyDataOut == null ? 43 : $historyDataOut.hashCode());
      Object $pageSize = this.getPageSize();
      result = result * 59 + ($pageSize == null ? 43 : $pageSize.hashCode());
      Object $webSshPort = this.getWebSshPort();
      result = result * 59 + ($webSshPort == null ? 43 : $webSshPort.hashCode());
      Object $maxPoolSize = this.getMaxPoolSize();
      result = result * 59 + ($maxPoolSize == null ? 43 : $maxPoolSize.hashCode());
      Object $account = this.getAccount();
      result = result * 59 + ($account == null ? 43 : $account.hashCode());
      Object $accountPwd = this.getAccountPwd();
      result = result * 59 + ($accountPwd == null ? 43 : $accountPwd.hashCode());
      Object $guestAccount = this.getGuestAccount();
      result = result * 59 + ($guestAccount == null ? 43 : $guestAccount.hashCode());
      Object $guestAccountPwd = this.getGuestAccountPwd();
      result = result * 59 + ($guestAccountPwd == null ? 43 : $guestAccountPwd.hashCode());
      Object $wgToken = this.getWgToken();
      result = result * 59 + ($wgToken == null ? 43 : $wgToken.hashCode());
      Object $dashView = this.getDashView();
      result = result * 59 + ($dashView == null ? 43 : $dashView.hashCode());
      Object $dashViewIpHide = this.getDashViewIpHide();
      result = result * 59 + ($dashViewIpHide == null ? 43 : $dashViewIpHide.hashCode());
      Object $dapingView = this.getDapingView();
      result = result * 59 + ($dapingView == null ? 43 : $dapingView.hashCode());
      Object $openDataAPI = this.getOpenDataAPI();
      result = result * 59 + ($openDataAPI == null ? 43 : $openDataAPI.hashCode());
      Object $nodeType = this.getNodeType();
      result = result * 59 + ($nodeType == null ? 43 : $nodeType.hashCode());
      Object $copyRight = this.getCopyRight();
      result = result * 59 + ($copyRight == null ? 43 : $copyRight.hashCode());
      Object $icoUrl = this.getIcoUrl();
      result = result * 59 + ($icoUrl == null ? 43 : $icoUrl.hashCode());
      Object $logoUrl = this.getLogoUrl();
      result = result * 59 + ($logoUrl == null ? 43 : $logoUrl.hashCode());
      Object $wgName = this.getWgName();
      result = result * 59 + ($wgName == null ? 43 : $wgName.hashCode());
      Object $wgShortName = this.getWgShortName();
      result = result * 59 + ($wgShortName == null ? 43 : $wgShortName.hashCode());
      Object $webSsh = this.getWebSsh();
      result = result * 59 + ($webSsh == null ? 43 : $webSsh.hashCode());
      Object $sidebarCollapse = this.getSidebarCollapse();
      result = result * 59 + ($sidebarCollapse == null ? 43 : $sidebarCollapse.hashCode());
      Object $showWarnCount = this.getShowWarnCount();
      result = result * 59 + ($showWarnCount == null ? 43 : $showWarnCount.hashCode());
      Object $shellToRun = this.getShellToRun();
      result = result * 59 + ($shellToRun == null ? 43 : $shellToRun.hashCode());
      Object $shellToRunLinuxBlock = this.getShellToRunLinuxBlock();
      result = result * 59 + ($shellToRunLinuxBlock == null ? 43 : $shellToRunLinuxBlock.hashCode());
      Object $shellToRunWinBlock = this.getShellToRunWinBlock();
      result = result * 59 + ($shellToRunWinBlock == null ? 43 : $shellToRunWinBlock.hashCode());
      Object $sqlInKeys = this.getSqlInKeys();
      result = result * 59 + ($sqlInKeys == null ? 43 : $sqlInKeys.hashCode());
      Object $daemonUrl = this.getDaemonUrl();
      result = result * 59 + ($daemonUrl == null ? 43 : $daemonUrl.hashCode());
      Object $mailTitlePrefix = this.getMailTitlePrefix();
      result = result * 59 + ($mailTitlePrefix == null ? 43 : $mailTitlePrefix.hashCode());
      Object $mailContentSuffix = this.getMailContentSuffix();
      result = result * 59 + ($mailContentSuffix == null ? 43 : $mailContentSuffix.hashCode());
      Object $hostGroup = this.getHostGroup();
      result = result * 59 + ($hostGroup == null ? 43 : $hostGroup.hashCode());
      Object $userInfoManage = this.getUserInfoManage();
      result = result * 59 + ($userInfoManage == null ? 43 : $userInfoManage.hashCode());
      Object $dashViewAutoData = this.getDashViewAutoData();
      result = result * 59 + ($dashViewAutoData == null ? 43 : $dashViewAutoData.hashCode());
      Object $dashViewListAutoData = this.getDashViewListAutoData();
      result = result * 59 + ($dashViewListAutoData == null ? 43 : $dashViewListAutoData.hashCode());
      return result;
   }

   public String toString() {
      return "CommonConfig(account=" + this.getAccount() + ", accountPwd=" + this.getAccountPwd() + ", guestAccount=" + this.getGuestAccount() + ", guestAccountPwd=" + this.getGuestAccountPwd() + ", wgToken=" + this.getWgToken() + ", dashView=" + this.getDashView() + ", dashViewIpHide=" + this.getDashViewIpHide() + ", dapingView=" + this.getDapingView() + ", openDataAPI=" + this.getOpenDataAPI() + ", dbTableTimes=" + this.getDbTableTimes() + ", heathTimes=" + this.getHeathTimes() + ", ftpTimes=" + this.getFtpTimes() + ", dceTimes=" + this.getDceTimes() + ", snmpTimes=" + this.getSnmpTimes() + ", warnCacheTimes=" + this.getWarnCacheTimes() + ", nodeType=" + this.getNodeType() + ", historyDataOut=" + this.getHistoryDataOut() + ", pageSize=" + this.getPageSize() + ", copyRight=" + this.getCopyRight() + ", icoUrl=" + this.getIcoUrl() + ", logoUrl=" + this.getLogoUrl() + ", wgName=" + this.getWgName() + ", wgShortName=" + this.getWgShortName() + ", webSsh=" + this.getWebSsh() + ", webSshPort=" + this.getWebSshPort() + ", sidebarCollapse=" + this.getSidebarCollapse() + ", showWarnCount=" + this.getShowWarnCount() + ", shellToRun=" + this.getShellToRun() + ", shellToRunLinuxBlock=" + this.getShellToRunLinuxBlock() + ", shellToRunWinBlock=" + this.getShellToRunWinBlock() + ", sqlInKeys=" + this.getSqlInKeys() + ", daemonUrl=" + this.getDaemonUrl() + ", mailTitlePrefix=" + this.getMailTitlePrefix() + ", mailContentSuffix=" + this.getMailContentSuffix() + ", hostGroup=" + this.getHostGroup() + ", userInfoManage=" + this.getUserInfoManage() + ", maxPoolSize=" + this.getMaxPoolSize() + ", dashViewAutoData=" + this.getDashViewAutoData() + ", dashViewListAutoData=" + this.getDashViewListAutoData() + ")";
   }
}
