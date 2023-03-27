package com.wgcloud.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
   prefix = "mail"
)
public class MailConfig {
   private Double memWarnVal = 98.0D;
   private Double cpuWarnVal = 98.0D;
   private Double upSpeedVal = 10240.0D;
   private Double upSpeedMinVal = 0.0D;
   private Double downSpeedVal = 10240.0D;
   private Double downSpeedMinVal = 0.0D;
   private Double cpuTemperatureWarnVal = 90.0D;
   private Double diskWarnVal = 98.0D;
   private String memWarnMail;
   private String upSpeedMail;
   private String downSpeedMail;
   private String cpuWarnMail;
   private String cpuTemperatureWarnMail;
   private String diskWarnMail;
   private String smartWarnMail;
   private String hostDownWarnMail;
   private String appDownWarnMail;
   private String dockerDownWarnMail;
   private String dbDownWarnMail;
   private String heathWarnMail;
   private String ftpWarnMail;
   private Integer heathWarnCount;
   private String dceWarnMail;
   private Integer dceWarnCount;
   private String snmpWarnMail;
   private String allWarnMail;
   private String warnCronTime;
   private String warnScript;
   private String warnToUnicode;
   private String diskBlock;
   private String fileLogWarnMail;
   private String portWarnMail;
   private String fileSafeWarnMail;
   private String blockIps;
   private String sysLoadWarnMail;
   private Double sysLoadWarnVal = 10.0D;
   private String shellWarnMail;
   private String customInfoWarnMail;
   private String hostLoginWarnMail;
   private String javaXmail;

   public Double getMemWarnVal() {
      return this.memWarnVal == null ? 98.0D : this.memWarnVal;
   }

   public void setMemWarnVal(Double memWarnVal) {
      this.memWarnVal = memWarnVal;
   }

   public Double getCpuWarnVal() {
      return this.cpuWarnVal == null ? 98.0D : this.cpuWarnVal;
   }

   public void setCpuWarnVal(Double cpuWarnVal) {
      this.cpuWarnVal = cpuWarnVal;
   }

   public String getMemWarnMail() {
      return StringUtils.isEmpty(this.memWarnMail) ? "true" : this.memWarnMail;
   }

   public void setMemWarnMail(String memWarnMail) {
      this.memWarnMail = memWarnMail;
   }

   public String getCpuWarnMail() {
      return StringUtils.isEmpty(this.cpuWarnMail) ? "true" : this.cpuWarnMail;
   }

   public void setCpuWarnMail(String cpuWarnMail) {
      this.cpuWarnMail = cpuWarnMail;
   }

   public String getHostDownWarnMail() {
      return StringUtils.isEmpty(this.hostDownWarnMail) ? "true" : this.hostDownWarnMail;
   }

   public void setHostDownWarnMail(String hostDownWarnMail) {
      this.hostDownWarnMail = hostDownWarnMail;
   }

   public String getAppDownWarnMail() {
      return StringUtils.isEmpty(this.appDownWarnMail) ? "true" : this.appDownWarnMail;
   }

   public void setAppDownWarnMail(String appDownWarnMail) {
      this.appDownWarnMail = appDownWarnMail;
   }

   public String getDockerDownWarnMail() {
      return StringUtils.isEmpty(this.dockerDownWarnMail) ? "true" : this.dockerDownWarnMail;
   }

   public void setDockerDownWarnMail(String dockerDownWarnMail) {
      this.dockerDownWarnMail = dockerDownWarnMail;
   }

   public String getHeathWarnMail() {
      return StringUtils.isEmpty(this.heathWarnMail) ? "true" : this.heathWarnMail;
   }

   public void setHeathWarnMail(String heathWarnMail) {
      this.heathWarnMail = heathWarnMail;
   }

   public String getAllWarnMail() {
      return StringUtils.isEmpty(this.allWarnMail) ? "true" : this.allWarnMail;
   }

   public void setAllWarnMail(String allWarnMail) {
      this.allWarnMail = allWarnMail;
   }

   public String getDbDownWarnMail() {
      return StringUtils.isEmpty(this.dbDownWarnMail) ? "true" : this.dbDownWarnMail;
   }

   public void setDbDownWarnMail(String dbDownWarnMail) {
      this.dbDownWarnMail = dbDownWarnMail;
   }

   public String getWarnScript() {
      return this.warnScript;
   }

   public void setWarnScript(String warnScript) {
      this.warnScript = warnScript;
   }

   public String getWarnToUnicode() {
      if (StringUtils.isEmpty(this.warnToUnicode)) {
         this.warnToUnicode = "false";
      }

      return this.warnToUnicode;
   }

   public void setWarnToUnicode(String warnToUnicode) {
      this.warnToUnicode = warnToUnicode;
   }

   public Double getDiskWarnVal() {
      return this.cpuWarnVal == null ? 98.0D : this.diskWarnVal;
   }

   public void setDiskWarnVal(Double diskWarnVal) {
      this.diskWarnVal = diskWarnVal;
   }

   public String getDiskWarnMail() {
      return StringUtils.isEmpty(this.diskWarnMail) ? "true" : this.diskWarnMail;
   }

   public void setDiskWarnMail(String diskWarnMail) {
      this.diskWarnMail = diskWarnMail;
   }

   public String getDiskBlock() {
      return this.diskBlock;
   }

   public void setDiskBlock(String diskBlock) {
      this.diskBlock = diskBlock;
   }

   public String getFileLogWarnMail() {
      return StringUtils.isEmpty(this.fileLogWarnMail) ? "true" : this.fileLogWarnMail;
   }

   public void setFileLogWarnMail(String fileLogWarnMail) {
      this.fileLogWarnMail = fileLogWarnMail;
   }

   public String getPortWarnMail() {
      return StringUtils.isEmpty(this.portWarnMail) ? "true" : this.portWarnMail;
   }

   public void setPortWarnMail(String portWarnMail) {
      this.portWarnMail = portWarnMail;
   }

   public Double getCpuTemperatureWarnVal() {
      return this.cpuTemperatureWarnVal == null ? 90.0D : this.cpuTemperatureWarnVal;
   }

   public void setCpuTemperatureWarnVal(Double cpuTemperatureWarnVal) {
      this.cpuTemperatureWarnVal = cpuTemperatureWarnVal;
   }

   public String getCpuTemperatureWarnMail() {
      return StringUtils.isEmpty(this.cpuTemperatureWarnMail) ? "false" : this.cpuTemperatureWarnMail;
   }

   public void setCpuTemperatureWarnMail(String cpuTemperatureWarnMail) {
      this.cpuTemperatureWarnMail = cpuTemperatureWarnMail;
   }

   public String getBlockIps() {
      return this.blockIps;
   }

   public void setBlockIps(String blockIps) {
      this.blockIps = blockIps;
   }

   public Double getUpSpeedVal() {
      return this.upSpeedVal == null ? 10240.0D : this.upSpeedVal;
   }

   public void setUpSpeedVal(Double upSpeedVal) {
      this.upSpeedVal = upSpeedVal;
   }

   public Double getDownSpeedVal() {
      return this.downSpeedVal == null ? 10240.0D : this.downSpeedVal;
   }

   public void setDownSpeedVal(Double downSpeedVal) {
      this.downSpeedVal = downSpeedVal;
   }

   public String getUpSpeedMail() {
      return StringUtils.isEmpty(this.upSpeedMail) ? "true" : this.upSpeedMail;
   }

   public void setUpSpeedMail(String upSpeedMail) {
      this.upSpeedMail = upSpeedMail;
   }

   public String getDownSpeedMail() {
      return StringUtils.isEmpty(this.downSpeedMail) ? "true" : this.downSpeedMail;
   }

   public void setDownSpeedMail(String downSpeedMail) {
      this.downSpeedMail = downSpeedMail;
   }

   public String getDceWarnMail() {
      return StringUtils.isEmpty(this.dceWarnMail) ? "true" : this.dceWarnMail;
   }

   public void setDceWarnMail(String dceWarnMail) {
      this.dceWarnMail = dceWarnMail;
   }

   public String getSysLoadWarnMail() {
      return StringUtils.isEmpty(this.sysLoadWarnMail) ? "true" : this.sysLoadWarnMail;
   }

   public void setSysLoadWarnMail(String sysLoadWarnMail) {
      this.sysLoadWarnMail = sysLoadWarnMail;
   }

   public Double getSysLoadWarnVal() {
      return this.sysLoadWarnVal == null ? 10.0D : this.sysLoadWarnVal;
   }

   public void setSysLoadWarnVal(Double sysLoadWarnVal) {
      this.sysLoadWarnVal = sysLoadWarnVal;
   }

   public String getSmartWarnMail() {
      return StringUtils.isEmpty(this.smartWarnMail) ? "true" : this.smartWarnMail;
   }

   public void setSmartWarnMail(String smartWarnMail) {
      this.smartWarnMail = smartWarnMail;
   }

   public String getSnmpWarnMail() {
      return StringUtils.isEmpty(this.snmpWarnMail) ? "true" : this.snmpWarnMail;
   }

   public void setSnmpWarnMail(String snmpWarnMail) {
      this.snmpWarnMail = snmpWarnMail;
   }

   public String getWarnCronTime() {
      return this.warnCronTime;
   }

   public void setWarnCronTime(String warnCronTime) {
      this.warnCronTime = warnCronTime;
   }

   public String getShellWarnMail() {
      return StringUtils.isEmpty(this.shellWarnMail) ? "true" : this.shellWarnMail;
   }

   public void setShellWarnMail(String shellWarnMail) {
      this.shellWarnMail = shellWarnMail;
   }

   public String getCustomInfoWarnMail() {
      return StringUtils.isEmpty(this.customInfoWarnMail) ? "true" : this.customInfoWarnMail;
   }

   public void setCustomInfoWarnMail(String customInfoWarnMail) {
      this.customInfoWarnMail = customInfoWarnMail;
   }

   public String getFileSafeWarnMail() {
      return StringUtils.isEmpty(this.fileSafeWarnMail) ? "true" : this.fileSafeWarnMail;
   }

   public void setFileSafeWarnMail(String fileSafeWarnMail) {
      this.fileSafeWarnMail = fileSafeWarnMail;
   }

   public Integer getHeathWarnCount() {
      return this.heathWarnCount;
   }

   public void setHeathWarnCount(Integer heathWarnCount) {
      this.heathWarnCount = heathWarnCount;
   }

   public Integer getDceWarnCount() {
      return this.dceWarnCount;
   }

   public void setDceWarnCount(Integer dceWarnCount) {
      this.dceWarnCount = dceWarnCount;
   }

   public String getJavaXmail() {
      return StringUtils.isEmpty(this.javaXmail) ? "flase" : this.javaXmail;
   }

   public void setJavaXmail(String javaXmail) {
      this.javaXmail = javaXmail;
   }

   public Double getUpSpeedMinVal() {
      return this.upSpeedMinVal;
   }

   public void setUpSpeedMinVal(Double upSpeedMinVal) {
      this.upSpeedMinVal = upSpeedMinVal;
   }

   public Double getDownSpeedMinVal() {
      return this.downSpeedMinVal;
   }

   public void setDownSpeedMinVal(Double downSpeedMinVal) {
      this.downSpeedMinVal = downSpeedMinVal;
   }

   public String getFtpWarnMail() {
      return StringUtils.isEmpty(this.ftpWarnMail) ? "true" : this.ftpWarnMail;
   }

   public void setFtpWarnMail(String ftpWarnMail) {
      this.ftpWarnMail = ftpWarnMail;
   }

   public String getHostLoginWarnMail() {
      return StringUtils.isEmpty(this.hostLoginWarnMail) ? "true" : this.hostLoginWarnMail;
   }

   public void setHostLoginWarnMail(String hostLoginWarnMail) {
      this.hostLoginWarnMail = hostLoginWarnMail;
   }

   public boolean equals(final Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof MailConfig)) {
         return false;
      } else {
         MailConfig other = (MailConfig)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label491: {
               Object this$memWarnVal = this.getMemWarnVal();
               Object other$memWarnVal = other.getMemWarnVal();
               if (this$memWarnVal == null) {
                  if (other$memWarnVal == null) {
                     break label491;
                  }
               } else if (this$memWarnVal.equals(other$memWarnVal)) {
                  break label491;
               }

               return false;
            }

            Object this$cpuWarnVal = this.getCpuWarnVal();
            Object other$cpuWarnVal = other.getCpuWarnVal();
            if (this$cpuWarnVal == null) {
               if (other$cpuWarnVal != null) {
                  return false;
               }
            } else if (!this$cpuWarnVal.equals(other$cpuWarnVal)) {
               return false;
            }

            Object this$upSpeedVal = this.getUpSpeedVal();
            Object other$upSpeedVal = other.getUpSpeedVal();
            if (this$upSpeedVal == null) {
               if (other$upSpeedVal != null) {
                  return false;
               }
            } else if (!this$upSpeedVal.equals(other$upSpeedVal)) {
               return false;
            }

            label470: {
               Object this$upSpeedMinVal = this.getUpSpeedMinVal();
               Object other$upSpeedMinVal = other.getUpSpeedMinVal();
               if (this$upSpeedMinVal == null) {
                  if (other$upSpeedMinVal == null) {
                     break label470;
                  }
               } else if (this$upSpeedMinVal.equals(other$upSpeedMinVal)) {
                  break label470;
               }

               return false;
            }

            label463: {
               Object this$downSpeedVal = this.getDownSpeedVal();
               Object other$downSpeedVal = other.getDownSpeedVal();
               if (this$downSpeedVal == null) {
                  if (other$downSpeedVal == null) {
                     break label463;
                  }
               } else if (this$downSpeedVal.equals(other$downSpeedVal)) {
                  break label463;
               }

               return false;
            }

            label456: {
               Object this$downSpeedMinVal = this.getDownSpeedMinVal();
               Object other$downSpeedMinVal = other.getDownSpeedMinVal();
               if (this$downSpeedMinVal == null) {
                  if (other$downSpeedMinVal == null) {
                     break label456;
                  }
               } else if (this$downSpeedMinVal.equals(other$downSpeedMinVal)) {
                  break label456;
               }

               return false;
            }

            Object this$cpuTemperatureWarnVal = this.getCpuTemperatureWarnVal();
            Object other$cpuTemperatureWarnVal = other.getCpuTemperatureWarnVal();
            if (this$cpuTemperatureWarnVal == null) {
               if (other$cpuTemperatureWarnVal != null) {
                  return false;
               }
            } else if (!this$cpuTemperatureWarnVal.equals(other$cpuTemperatureWarnVal)) {
               return false;
            }

            label442: {
               Object this$diskWarnVal = this.getDiskWarnVal();
               Object other$diskWarnVal = other.getDiskWarnVal();
               if (this$diskWarnVal == null) {
                  if (other$diskWarnVal == null) {
                     break label442;
                  }
               } else if (this$diskWarnVal.equals(other$diskWarnVal)) {
                  break label442;
               }

               return false;
            }

            Object this$heathWarnCount = this.getHeathWarnCount();
            Object other$heathWarnCount = other.getHeathWarnCount();
            if (this$heathWarnCount == null) {
               if (other$heathWarnCount != null) {
                  return false;
               }
            } else if (!this$heathWarnCount.equals(other$heathWarnCount)) {
               return false;
            }

            label428: {
               Object this$dceWarnCount = this.getDceWarnCount();
               Object other$dceWarnCount = other.getDceWarnCount();
               if (this$dceWarnCount == null) {
                  if (other$dceWarnCount == null) {
                     break label428;
                  }
               } else if (this$dceWarnCount.equals(other$dceWarnCount)) {
                  break label428;
               }

               return false;
            }

            Object this$sysLoadWarnVal = this.getSysLoadWarnVal();
            Object other$sysLoadWarnVal = other.getSysLoadWarnVal();
            if (this$sysLoadWarnVal == null) {
               if (other$sysLoadWarnVal != null) {
                  return false;
               }
            } else if (!this$sysLoadWarnVal.equals(other$sysLoadWarnVal)) {
               return false;
            }

            Object this$memWarnMail = this.getMemWarnMail();
            Object other$memWarnMail = other.getMemWarnMail();
            if (this$memWarnMail == null) {
               if (other$memWarnMail != null) {
                  return false;
               }
            } else if (!this$memWarnMail.equals(other$memWarnMail)) {
               return false;
            }

            label407: {
               Object this$upSpeedMail = this.getUpSpeedMail();
               Object other$upSpeedMail = other.getUpSpeedMail();
               if (this$upSpeedMail == null) {
                  if (other$upSpeedMail == null) {
                     break label407;
                  }
               } else if (this$upSpeedMail.equals(other$upSpeedMail)) {
                  break label407;
               }

               return false;
            }

            label400: {
               Object this$downSpeedMail = this.getDownSpeedMail();
               Object other$downSpeedMail = other.getDownSpeedMail();
               if (this$downSpeedMail == null) {
                  if (other$downSpeedMail == null) {
                     break label400;
                  }
               } else if (this$downSpeedMail.equals(other$downSpeedMail)) {
                  break label400;
               }

               return false;
            }

            Object this$cpuWarnMail = this.getCpuWarnMail();
            Object other$cpuWarnMail = other.getCpuWarnMail();
            if (this$cpuWarnMail == null) {
               if (other$cpuWarnMail != null) {
                  return false;
               }
            } else if (!this$cpuWarnMail.equals(other$cpuWarnMail)) {
               return false;
            }

            Object this$cpuTemperatureWarnMail = this.getCpuTemperatureWarnMail();
            Object other$cpuTemperatureWarnMail = other.getCpuTemperatureWarnMail();
            if (this$cpuTemperatureWarnMail == null) {
               if (other$cpuTemperatureWarnMail != null) {
                  return false;
               }
            } else if (!this$cpuTemperatureWarnMail.equals(other$cpuTemperatureWarnMail)) {
               return false;
            }

            label379: {
               Object this$diskWarnMail = this.getDiskWarnMail();
               Object other$diskWarnMail = other.getDiskWarnMail();
               if (this$diskWarnMail == null) {
                  if (other$diskWarnMail == null) {
                     break label379;
                  }
               } else if (this$diskWarnMail.equals(other$diskWarnMail)) {
                  break label379;
               }

               return false;
            }

            Object this$smartWarnMail = this.getSmartWarnMail();
            Object other$smartWarnMail = other.getSmartWarnMail();
            if (this$smartWarnMail == null) {
               if (other$smartWarnMail != null) {
                  return false;
               }
            } else if (!this$smartWarnMail.equals(other$smartWarnMail)) {
               return false;
            }

            Object this$hostDownWarnMail = this.getHostDownWarnMail();
            Object other$hostDownWarnMail = other.getHostDownWarnMail();
            if (this$hostDownWarnMail == null) {
               if (other$hostDownWarnMail != null) {
                  return false;
               }
            } else if (!this$hostDownWarnMail.equals(other$hostDownWarnMail)) {
               return false;
            }

            label358: {
               Object this$appDownWarnMail = this.getAppDownWarnMail();
               Object other$appDownWarnMail = other.getAppDownWarnMail();
               if (this$appDownWarnMail == null) {
                  if (other$appDownWarnMail == null) {
                     break label358;
                  }
               } else if (this$appDownWarnMail.equals(other$appDownWarnMail)) {
                  break label358;
               }

               return false;
            }

            label351: {
               Object this$dockerDownWarnMail = this.getDockerDownWarnMail();
               Object other$dockerDownWarnMail = other.getDockerDownWarnMail();
               if (this$dockerDownWarnMail == null) {
                  if (other$dockerDownWarnMail == null) {
                     break label351;
                  }
               } else if (this$dockerDownWarnMail.equals(other$dockerDownWarnMail)) {
                  break label351;
               }

               return false;
            }

            label344: {
               Object this$dbDownWarnMail = this.getDbDownWarnMail();
               Object other$dbDownWarnMail = other.getDbDownWarnMail();
               if (this$dbDownWarnMail == null) {
                  if (other$dbDownWarnMail == null) {
                     break label344;
                  }
               } else if (this$dbDownWarnMail.equals(other$dbDownWarnMail)) {
                  break label344;
               }

               return false;
            }

            Object this$heathWarnMail = this.getHeathWarnMail();
            Object other$heathWarnMail = other.getHeathWarnMail();
            if (this$heathWarnMail == null) {
               if (other$heathWarnMail != null) {
                  return false;
               }
            } else if (!this$heathWarnMail.equals(other$heathWarnMail)) {
               return false;
            }

            label330: {
               Object this$ftpWarnMail = this.getFtpWarnMail();
               Object other$ftpWarnMail = other.getFtpWarnMail();
               if (this$ftpWarnMail == null) {
                  if (other$ftpWarnMail == null) {
                     break label330;
                  }
               } else if (this$ftpWarnMail.equals(other$ftpWarnMail)) {
                  break label330;
               }

               return false;
            }

            Object this$dceWarnMail = this.getDceWarnMail();
            Object other$dceWarnMail = other.getDceWarnMail();
            if (this$dceWarnMail == null) {
               if (other$dceWarnMail != null) {
                  return false;
               }
            } else if (!this$dceWarnMail.equals(other$dceWarnMail)) {
               return false;
            }

            label316: {
               Object this$snmpWarnMail = this.getSnmpWarnMail();
               Object other$snmpWarnMail = other.getSnmpWarnMail();
               if (this$snmpWarnMail == null) {
                  if (other$snmpWarnMail == null) {
                     break label316;
                  }
               } else if (this$snmpWarnMail.equals(other$snmpWarnMail)) {
                  break label316;
               }

               return false;
            }

            Object this$allWarnMail = this.getAllWarnMail();
            Object other$allWarnMail = other.getAllWarnMail();
            if (this$allWarnMail == null) {
               if (other$allWarnMail != null) {
                  return false;
               }
            } else if (!this$allWarnMail.equals(other$allWarnMail)) {
               return false;
            }

            Object this$warnCronTime = this.getWarnCronTime();
            Object other$warnCronTime = other.getWarnCronTime();
            if (this$warnCronTime == null) {
               if (other$warnCronTime != null) {
                  return false;
               }
            } else if (!this$warnCronTime.equals(other$warnCronTime)) {
               return false;
            }

            label295: {
               Object this$warnScript = this.getWarnScript();
               Object other$warnScript = other.getWarnScript();
               if (this$warnScript == null) {
                  if (other$warnScript == null) {
                     break label295;
                  }
               } else if (this$warnScript.equals(other$warnScript)) {
                  break label295;
               }

               return false;
            }

            label288: {
               Object this$warnToUnicode = this.getWarnToUnicode();
               Object other$warnToUnicode = other.getWarnToUnicode();
               if (this$warnToUnicode == null) {
                  if (other$warnToUnicode == null) {
                     break label288;
                  }
               } else if (this$warnToUnicode.equals(other$warnToUnicode)) {
                  break label288;
               }

               return false;
            }

            Object this$diskBlock = this.getDiskBlock();
            Object other$diskBlock = other.getDiskBlock();
            if (this$diskBlock == null) {
               if (other$diskBlock != null) {
                  return false;
               }
            } else if (!this$diskBlock.equals(other$diskBlock)) {
               return false;
            }

            Object this$fileLogWarnMail = this.getFileLogWarnMail();
            Object other$fileLogWarnMail = other.getFileLogWarnMail();
            if (this$fileLogWarnMail == null) {
               if (other$fileLogWarnMail != null) {
                  return false;
               }
            } else if (!this$fileLogWarnMail.equals(other$fileLogWarnMail)) {
               return false;
            }

            label267: {
               Object this$portWarnMail = this.getPortWarnMail();
               Object other$portWarnMail = other.getPortWarnMail();
               if (this$portWarnMail == null) {
                  if (other$portWarnMail == null) {
                     break label267;
                  }
               } else if (this$portWarnMail.equals(other$portWarnMail)) {
                  break label267;
               }

               return false;
            }

            Object this$fileSafeWarnMail = this.getFileSafeWarnMail();
            Object other$fileSafeWarnMail = other.getFileSafeWarnMail();
            if (this$fileSafeWarnMail == null) {
               if (other$fileSafeWarnMail != null) {
                  return false;
               }
            } else if (!this$fileSafeWarnMail.equals(other$fileSafeWarnMail)) {
               return false;
            }

            Object this$blockIps = this.getBlockIps();
            Object other$blockIps = other.getBlockIps();
            if (this$blockIps == null) {
               if (other$blockIps != null) {
                  return false;
               }
            } else if (!this$blockIps.equals(other$blockIps)) {
               return false;
            }

            label246: {
               Object this$sysLoadWarnMail = this.getSysLoadWarnMail();
               Object other$sysLoadWarnMail = other.getSysLoadWarnMail();
               if (this$sysLoadWarnMail == null) {
                  if (other$sysLoadWarnMail == null) {
                     break label246;
                  }
               } else if (this$sysLoadWarnMail.equals(other$sysLoadWarnMail)) {
                  break label246;
               }

               return false;
            }

            label239: {
               Object this$shellWarnMail = this.getShellWarnMail();
               Object other$shellWarnMail = other.getShellWarnMail();
               if (this$shellWarnMail == null) {
                  if (other$shellWarnMail == null) {
                     break label239;
                  }
               } else if (this$shellWarnMail.equals(other$shellWarnMail)) {
                  break label239;
               }

               return false;
            }

            label232: {
               Object this$customInfoWarnMail = this.getCustomInfoWarnMail();
               Object other$customInfoWarnMail = other.getCustomInfoWarnMail();
               if (this$customInfoWarnMail == null) {
                  if (other$customInfoWarnMail == null) {
                     break label232;
                  }
               } else if (this$customInfoWarnMail.equals(other$customInfoWarnMail)) {
                  break label232;
               }

               return false;
            }

            Object this$hostLoginWarnMail = this.getHostLoginWarnMail();
            Object other$hostLoginWarnMail = other.getHostLoginWarnMail();
            if (this$hostLoginWarnMail == null) {
               if (other$hostLoginWarnMail != null) {
                  return false;
               }
            } else if (!this$hostLoginWarnMail.equals(other$hostLoginWarnMail)) {
               return false;
            }

            Object this$javaXmail = this.getJavaXmail();
            Object other$javaXmail = other.getJavaXmail();
            if (this$javaXmail == null) {
               if (other$javaXmail != null) {
                  return false;
               }
            } else if (!this$javaXmail.equals(other$javaXmail)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(final Object other) {
      return other instanceof MailConfig;
   }

   public int hashCode() {
//      int PRIME = true;
      int result = 1;
      Object $memWarnVal = this.getMemWarnVal();
//      int result = result * 59 + ($memWarnVal == null ? 43 : $memWarnVal.hashCode());
      result = result * 59 + ($memWarnVal == null ? 43 : $memWarnVal.hashCode());
      Object $cpuWarnVal = this.getCpuWarnVal();
      result = result * 59 + ($cpuWarnVal == null ? 43 : $cpuWarnVal.hashCode());
      Object $upSpeedVal = this.getUpSpeedVal();
      result = result * 59 + ($upSpeedVal == null ? 43 : $upSpeedVal.hashCode());
      Object $upSpeedMinVal = this.getUpSpeedMinVal();
      result = result * 59 + ($upSpeedMinVal == null ? 43 : $upSpeedMinVal.hashCode());
      Object $downSpeedVal = this.getDownSpeedVal();
      result = result * 59 + ($downSpeedVal == null ? 43 : $downSpeedVal.hashCode());
      Object $downSpeedMinVal = this.getDownSpeedMinVal();
      result = result * 59 + ($downSpeedMinVal == null ? 43 : $downSpeedMinVal.hashCode());
      Object $cpuTemperatureWarnVal = this.getCpuTemperatureWarnVal();
      result = result * 59 + ($cpuTemperatureWarnVal == null ? 43 : $cpuTemperatureWarnVal.hashCode());
      Object $diskWarnVal = this.getDiskWarnVal();
      result = result * 59 + ($diskWarnVal == null ? 43 : $diskWarnVal.hashCode());
      Object $heathWarnCount = this.getHeathWarnCount();
      result = result * 59 + ($heathWarnCount == null ? 43 : $heathWarnCount.hashCode());
      Object $dceWarnCount = this.getDceWarnCount();
      result = result * 59 + ($dceWarnCount == null ? 43 : $dceWarnCount.hashCode());
      Object $sysLoadWarnVal = this.getSysLoadWarnVal();
      result = result * 59 + ($sysLoadWarnVal == null ? 43 : $sysLoadWarnVal.hashCode());
      Object $memWarnMail = this.getMemWarnMail();
      result = result * 59 + ($memWarnMail == null ? 43 : $memWarnMail.hashCode());
      Object $upSpeedMail = this.getUpSpeedMail();
      result = result * 59 + ($upSpeedMail == null ? 43 : $upSpeedMail.hashCode());
      Object $downSpeedMail = this.getDownSpeedMail();
      result = result * 59 + ($downSpeedMail == null ? 43 : $downSpeedMail.hashCode());
      Object $cpuWarnMail = this.getCpuWarnMail();
      result = result * 59 + ($cpuWarnMail == null ? 43 : $cpuWarnMail.hashCode());
      Object $cpuTemperatureWarnMail = this.getCpuTemperatureWarnMail();
      result = result * 59 + ($cpuTemperatureWarnMail == null ? 43 : $cpuTemperatureWarnMail.hashCode());
      Object $diskWarnMail = this.getDiskWarnMail();
      result = result * 59 + ($diskWarnMail == null ? 43 : $diskWarnMail.hashCode());
      Object $smartWarnMail = this.getSmartWarnMail();
      result = result * 59 + ($smartWarnMail == null ? 43 : $smartWarnMail.hashCode());
      Object $hostDownWarnMail = this.getHostDownWarnMail();
      result = result * 59 + ($hostDownWarnMail == null ? 43 : $hostDownWarnMail.hashCode());
      Object $appDownWarnMail = this.getAppDownWarnMail();
      result = result * 59 + ($appDownWarnMail == null ? 43 : $appDownWarnMail.hashCode());
      Object $dockerDownWarnMail = this.getDockerDownWarnMail();
      result = result * 59 + ($dockerDownWarnMail == null ? 43 : $dockerDownWarnMail.hashCode());
      Object $dbDownWarnMail = this.getDbDownWarnMail();
      result = result * 59 + ($dbDownWarnMail == null ? 43 : $dbDownWarnMail.hashCode());
      Object $heathWarnMail = this.getHeathWarnMail();
      result = result * 59 + ($heathWarnMail == null ? 43 : $heathWarnMail.hashCode());
      Object $ftpWarnMail = this.getFtpWarnMail();
      result = result * 59 + ($ftpWarnMail == null ? 43 : $ftpWarnMail.hashCode());
      Object $dceWarnMail = this.getDceWarnMail();
      result = result * 59 + ($dceWarnMail == null ? 43 : $dceWarnMail.hashCode());
      Object $snmpWarnMail = this.getSnmpWarnMail();
      result = result * 59 + ($snmpWarnMail == null ? 43 : $snmpWarnMail.hashCode());
      Object $allWarnMail = this.getAllWarnMail();
      result = result * 59 + ($allWarnMail == null ? 43 : $allWarnMail.hashCode());
      Object $warnCronTime = this.getWarnCronTime();
      result = result * 59 + ($warnCronTime == null ? 43 : $warnCronTime.hashCode());
      Object $warnScript = this.getWarnScript();
      result = result * 59 + ($warnScript == null ? 43 : $warnScript.hashCode());
      Object $warnToUnicode = this.getWarnToUnicode();
      result = result * 59 + ($warnToUnicode == null ? 43 : $warnToUnicode.hashCode());
      Object $diskBlock = this.getDiskBlock();
      result = result * 59 + ($diskBlock == null ? 43 : $diskBlock.hashCode());
      Object $fileLogWarnMail = this.getFileLogWarnMail();
      result = result * 59 + ($fileLogWarnMail == null ? 43 : $fileLogWarnMail.hashCode());
      Object $portWarnMail = this.getPortWarnMail();
      result = result * 59 + ($portWarnMail == null ? 43 : $portWarnMail.hashCode());
      Object $fileSafeWarnMail = this.getFileSafeWarnMail();
      result = result * 59 + ($fileSafeWarnMail == null ? 43 : $fileSafeWarnMail.hashCode());
      Object $blockIps = this.getBlockIps();
      result = result * 59 + ($blockIps == null ? 43 : $blockIps.hashCode());
      Object $sysLoadWarnMail = this.getSysLoadWarnMail();
      result = result * 59 + ($sysLoadWarnMail == null ? 43 : $sysLoadWarnMail.hashCode());
      Object $shellWarnMail = this.getShellWarnMail();
      result = result * 59 + ($shellWarnMail == null ? 43 : $shellWarnMail.hashCode());
      Object $customInfoWarnMail = this.getCustomInfoWarnMail();
      result = result * 59 + ($customInfoWarnMail == null ? 43 : $customInfoWarnMail.hashCode());
      Object $hostLoginWarnMail = this.getHostLoginWarnMail();
      result = result * 59 + ($hostLoginWarnMail == null ? 43 : $hostLoginWarnMail.hashCode());
      Object $javaXmail = this.getJavaXmail();
      result = result * 59 + ($javaXmail == null ? 43 : $javaXmail.hashCode());
      return result;
   }

   public String toString() {
      return "MailConfig(memWarnVal=" + this.getMemWarnVal() + ", cpuWarnVal=" + this.getCpuWarnVal() + ", upSpeedVal=" + this.getUpSpeedVal() + ", upSpeedMinVal=" + this.getUpSpeedMinVal() + ", downSpeedVal=" + this.getDownSpeedVal() + ", downSpeedMinVal=" + this.getDownSpeedMinVal() + ", cpuTemperatureWarnVal=" + this.getCpuTemperatureWarnVal() + ", diskWarnVal=" + this.getDiskWarnVal() + ", memWarnMail=" + this.getMemWarnMail() + ", upSpeedMail=" + this.getUpSpeedMail() + ", downSpeedMail=" + this.getDownSpeedMail() + ", cpuWarnMail=" + this.getCpuWarnMail() + ", cpuTemperatureWarnMail=" + this.getCpuTemperatureWarnMail() + ", diskWarnMail=" + this.getDiskWarnMail() + ", smartWarnMail=" + this.getSmartWarnMail() + ", hostDownWarnMail=" + this.getHostDownWarnMail() + ", appDownWarnMail=" + this.getAppDownWarnMail() + ", dockerDownWarnMail=" + this.getDockerDownWarnMail() + ", dbDownWarnMail=" + this.getDbDownWarnMail() + ", heathWarnMail=" + this.getHeathWarnMail() + ", ftpWarnMail=" + this.getFtpWarnMail() + ", heathWarnCount=" + this.getHeathWarnCount() + ", dceWarnMail=" + this.getDceWarnMail() + ", dceWarnCount=" + this.getDceWarnCount() + ", snmpWarnMail=" + this.getSnmpWarnMail() + ", allWarnMail=" + this.getAllWarnMail() + ", warnCronTime=" + this.getWarnCronTime() + ", warnScript=" + this.getWarnScript() + ", warnToUnicode=" + this.getWarnToUnicode() + ", diskBlock=" + this.getDiskBlock() + ", fileLogWarnMail=" + this.getFileLogWarnMail() + ", portWarnMail=" + this.getPortWarnMail() + ", fileSafeWarnMail=" + this.getFileSafeWarnMail() + ", blockIps=" + this.getBlockIps() + ", sysLoadWarnMail=" + this.getSysLoadWarnMail() + ", sysLoadWarnVal=" + this.getSysLoadWarnVal() + ", shellWarnMail=" + this.getShellWarnMail() + ", customInfoWarnMail=" + this.getCustomInfoWarnMail() + ", hostLoginWarnMail=" + this.getHostLoginWarnMail() + ", javaXmail=" + this.getJavaXmail() + ")";
   }
}
