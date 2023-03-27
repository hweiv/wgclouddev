package com.wgcloud.entity;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class DiskState extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private String fileSystem;
   private String diskSize;
   private String used;
   private String avail;
   private String usePer;
   private Double usePerDouble;
   private String dateStr;
   private Date createTime;

   public String getFileSystem() {
      return this.fileSystem;
   }

   public void setFileSystem(String fileSystem) {
      this.fileSystem = fileSystem;
   }

   public String getDiskSize() {
      return this.diskSize;
   }

   public void setDiskSize(String diskSize) {
      this.diskSize = diskSize;
   }

   public String getUsed() {
      return this.used;
   }

   public void setUsed(String used) {
      this.used = used;
   }

   public String getAvail() {
      return this.avail;
   }

   public void setAvail(String avail) {
      this.avail = avail;
   }

   public String getUsePer() {
      return this.usePer;
   }

   public void setUsePer(String usePer) {
      this.usePer = usePer;
   }

   public Double getUsePerDouble() {
      if (!StringUtils.isEmpty(this.usePer)) {
         this.usePerDouble = Double.valueOf(this.usePer.replace("%", ""));
      }

      return this.usePerDouble;
   }

   public void setUsePerDouble(Double usePerDouble) {
      this.usePerDouble = usePerDouble;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public String getDateStr() {
      return this.dateStr;
   }

   public void setDateStr(String dateStr) {
      this.dateStr = dateStr;
   }

   public String getHostname() {
      return this.hostname;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
   }
}
