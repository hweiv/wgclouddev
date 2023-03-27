package com.wgcloud.entity;

import java.util.Date;

public class DbInfo extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String aliasName;
   private String dbType;
   private String userName;
   private String passwd;
   private String dbUrl;
   private String dbState;
   private Integer warnCount;
   private String warnQueryWd;
   private String image;
   private Date createTime;
   private String account;

   public String getDbType() {
      return this.dbType;
   }

   public void setDbType(String dbType) {
      this.dbType = dbType;
   }

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public String getPasswd() {
      return this.passwd;
   }

   public void setPasswd(String passwd) {
      this.passwd = passwd;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public String getDbState() {
      return this.dbState;
   }

   public void setDbState(String dbState) {
      this.dbState = dbState;
   }

   public String getAliasName() {
      return this.aliasName;
   }

   public void setAliasName(String aliasName) {
      this.aliasName = aliasName;
   }

   public String getDbUrl() {
      return this.dbUrl;
   }

   public void setDbUrl(String dbUrl) {
      this.dbUrl = dbUrl;
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

   public String getImage() {
      return this.image;
   }

   public void setImage(String image) {
      this.image = image;
   }
}
