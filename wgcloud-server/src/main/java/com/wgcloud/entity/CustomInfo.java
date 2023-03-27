package com.wgcloud.entity;

import java.util.Date;

public class CustomInfo extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private String customName;
   private String customShell;
   private String state;
   private String active;
   private String resultExp;
   private String customValue;
   private String groupId;
   private Integer warnCount;
   private String warnQueryWd;
   private String account;
   private Date createTime;

   public String getHostname() {
      return this.hostname;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
   }

   public String getCustomName() {
      return this.customName;
   }

   public void setCustomName(String customName) {
      this.customName = customName;
   }

   public String getCustomShell() {
      return this.customShell;
   }

   public void setCustomShell(String customShell) {
      this.customShell = customShell;
   }

   public String getState() {
      return this.state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public String getActive() {
      return this.active;
   }

   public void setActive(String active) {
      this.active = active;
   }

   public String getResultExp() {
      return this.resultExp;
   }

   public void setResultExp(String resultExp) {
      this.resultExp = resultExp;
   }

   public Integer getWarnCount() {
      return this.warnCount;
   }

   public void setWarnCount(Integer warnCount) {
      this.warnCount = warnCount;
   }

   public String getWarnQueryWd() {
      return this.warnQueryWd;
   }

   public void setWarnQueryWd(String warnQueryWd) {
      this.warnQueryWd = warnQueryWd;
   }

   public String getAccount() {
      return this.account;
   }

   public void setAccount(String account) {
      this.account = account;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public String getCustomValue() {
      return this.customValue;
   }

   public void setCustomValue(String customValue) {
      this.customValue = customValue;
   }
}
