package com.wgcloud.entity;

import java.util.Date;

public class DeskIo extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private String fileSystem;
   private String readCount;
   private String writeCount;
   private String readBytes;
   private String writeBytes;
   private String readTime;
   private String writeTime;
   private Date createTime;

   public String getHostname() {
      return this.hostname;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
   }

   public String getFileSystem() {
      return this.fileSystem;
   }

   public void setFileSystem(String fileSystem) {
      this.fileSystem = fileSystem;
   }

   public String getReadCount() {
      return this.readCount;
   }

   public void setReadCount(String readCount) {
      this.readCount = readCount;
   }

   public String getWriteCount() {
      return this.writeCount;
   }

   public void setWriteCount(String writeCount) {
      this.writeCount = writeCount;
   }

   public String getReadBytes() {
      return this.readBytes;
   }

   public void setReadBytes(String readBytes) {
      this.readBytes = readBytes;
   }

   public String getWriteBytes() {
      return this.writeBytes;
   }

   public void setWriteBytes(String writeBytes) {
      this.writeBytes = writeBytes;
   }

   public String getReadTime() {
      return this.readTime;
   }

   public void setReadTime(String readTime) {
      this.readTime = readTime;
   }

   public String getWriteTime() {
      return this.writeTime;
   }

   public void setWriteTime(String writeTime) {
      this.writeTime = writeTime;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }
}
