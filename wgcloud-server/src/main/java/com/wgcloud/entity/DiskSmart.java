package com.wgcloud.entity;

import java.util.Date;

public class DiskSmart extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private String fileSystem;
   private String diskState;
   private String powerHours;
   private String powerCount;
   private String temperature;
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

   public String getDiskState() {
      return this.diskState;
   }

   public void setDiskState(String diskState) {
      this.diskState = diskState;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public String getPowerHours() {
      return this.powerHours;
   }

   public void setPowerHours(String powerHours) {
      this.powerHours = powerHours;
   }

   public String getPowerCount() {
      return this.powerCount;
   }

   public void setPowerCount(String powerCount) {
      this.powerCount = powerCount;
   }

   public String getTemperature() {
      return this.temperature;
   }

   public void setTemperature(String temperature) {
      this.temperature = temperature;
   }
}
