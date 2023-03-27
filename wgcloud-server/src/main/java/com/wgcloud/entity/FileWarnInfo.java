package com.wgcloud.entity;

import java.util.Date;

public class FileWarnInfo extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private String filePath;
   private String fileSize;
   private String warnChars;
   private String warnRows;
   private String warnDatas;
   private String remark;
   private String fileNamePrefix;
   private String active;
   private String fileType;
   private Date createTime;
   private String account;

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public String getHostname() {
      return this.hostname;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
   }

   public String getFilePath() {
      return this.filePath;
   }

   public void setFilePath(String filePath) {
      this.filePath = filePath;
   }

   public String getFileSize() {
      return this.fileSize;
   }

   public void setFileSize(String fileSize) {
      this.fileSize = fileSize;
   }

   public String getWarnChars() {
      return this.warnChars;
   }

   public void setWarnChars(String warnChars) {
      this.warnChars = warnChars;
   }

   public String getWarnRows() {
      return this.warnRows;
   }

   public void setWarnRows(String warnRows) {
      this.warnRows = warnRows;
   }

   public String getWarnDatas() {
      return this.warnDatas;
   }

   public void setWarnDatas(String warnDatas) {
      this.warnDatas = warnDatas;
   }

   public String getRemark() {
      return this.remark;
   }

   public void setRemark(String remark) {
      this.remark = remark;
   }

   public String getActive() {
      return this.active;
   }

   public void setActive(String active) {
      this.active = active;
   }

   public String getFileNamePrefix() {
      return this.fileNamePrefix;
   }

   public void setFileNamePrefix(String fileNamePrefix) {
      this.fileNamePrefix = fileNamePrefix;
   }

   public String getAccount() {
      return this.account;
   }

   public void setAccount(String account) {
      this.account = account;
   }

   public String getFileType() {
      return this.fileType;
   }

   public void setFileType(String fileType) {
      this.fileType = fileType;
   }
}
