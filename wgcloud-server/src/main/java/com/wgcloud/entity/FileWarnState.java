package com.wgcloud.entity;

import java.util.Date;

public class FileWarnState extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private String warContent;
   private String fileWarnId;
   private String filePath;
   private Date createTime;

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

   public String getWarContent() {
      return this.warContent;
   }

   public void setWarContent(String warContent) {
      this.warContent = warContent;
   }

   public String getFileWarnId() {
      return this.fileWarnId;
   }

   public void setFileWarnId(String fileWarnId) {
      this.fileWarnId = fileWarnId;
   }
}
