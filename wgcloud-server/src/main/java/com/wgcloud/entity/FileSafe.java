package com.wgcloud.entity;

import java.util.Date;

public class FileSafe extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private String fileName;
   private String filePath;
   private String fileSign;
   private String state;
   private String active;
   private Integer warnCount;
   private String warnQueryWd;
   private String account;
   private String fileModtime;
   private Date createTime;

   public String getHostname() {
      return this.hostname;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
   }

   public String getFileName() {
      return this.fileName;
   }

   public void setFileName(String fileName) {
      this.fileName = fileName;
   }

   public String getFilePath() {
      return this.filePath;
   }

   public void setFilePath(String filePath) {
      this.filePath = filePath;
   }

   public String getFileSign() {
      return this.fileSign;
   }

   public void setFileSign(String fileSign) {
      this.fileSign = fileSign;
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

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
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

   public String getFileModtime() {
      return this.fileModtime;
   }

   public void setFileModtime(String fileModtime) {
      this.fileModtime = fileModtime;
   }
}
