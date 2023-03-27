package com.wgcloud.dto;

import com.wgcloud.entity.BaseEntity;

public class HostWarnDiyDto extends BaseEntity {
   private static final long serialVersionUID = -2913111613773445949L;
   private String hostname;
   private Double memWarnVal;
   private Double cpuWarnVal;
   private Double upSpeedVal;
   private Double upSpeedMinVal;
   private Double downSpeedVal;
   private Double downSpeedMinVal;
   private Double cpuTemperatureWarnVal;
   private String memWarnMail;
   private String upSpeedMail;
   private String downSpeedMail;
   private String cpuWarnMail;
   private String cpuTemperatureWarnMail;
   private String diskWarnMail;
   private String smartWarnMail;
   private String diskBlock;
   private Double diskWarnVal;
   private String sysLoadWarnMail;
   private Double sysLoadWarnVal;
   private String hostDownWarnMail;

   public String getHostname() {
      return this.hostname;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
   }

   public Double getMemWarnVal() {
      return this.memWarnVal;
   }

   public void setMemWarnVal(Double memWarnVal) {
      this.memWarnVal = memWarnVal;
   }

   public Double getCpuWarnVal() {
      return this.cpuWarnVal;
   }

   public void setCpuWarnVal(Double cpuWarnVal) {
      this.cpuWarnVal = cpuWarnVal;
   }

   public Double getUpSpeedVal() {
      return this.upSpeedVal;
   }

   public void setUpSpeedVal(Double upSpeedVal) {
      this.upSpeedVal = upSpeedVal;
   }

   public Double getDownSpeedVal() {
      return this.downSpeedVal;
   }

   public void setDownSpeedVal(Double downSpeedVal) {
      this.downSpeedVal = downSpeedVal;
   }

   public Double getCpuTemperatureWarnVal() {
      return this.cpuTemperatureWarnVal;
   }

   public void setCpuTemperatureWarnVal(Double cpuTemperatureWarnVal) {
      this.cpuTemperatureWarnVal = cpuTemperatureWarnVal;
   }

   public String getMemWarnMail() {
      return this.memWarnMail;
   }

   public void setMemWarnMail(String memWarnMail) {
      this.memWarnMail = memWarnMail;
   }

   public String getUpSpeedMail() {
      return this.upSpeedMail;
   }

   public void setUpSpeedMail(String upSpeedMail) {
      this.upSpeedMail = upSpeedMail;
   }

   public String getDownSpeedMail() {
      return this.downSpeedMail;
   }

   public void setDownSpeedMail(String downSpeedMail) {
      this.downSpeedMail = downSpeedMail;
   }

   public String getCpuWarnMail() {
      return this.cpuWarnMail;
   }

   public void setCpuWarnMail(String cpuWarnMail) {
      this.cpuWarnMail = cpuWarnMail;
   }

   public String getCpuTemperatureWarnMail() {
      return this.cpuTemperatureWarnMail;
   }

   public void setCpuTemperatureWarnMail(String cpuTemperatureWarnMail) {
      this.cpuTemperatureWarnMail = cpuTemperatureWarnMail;
   }

   public String getDiskWarnMail() {
      return this.diskWarnMail;
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

   public Double getDiskWarnVal() {
      return this.diskWarnVal;
   }

   public void setDiskWarnVal(Double diskWarnVal) {
      this.diskWarnVal = diskWarnVal;
   }

   public String getSysLoadWarnMail() {
      return this.sysLoadWarnMail;
   }

   public void setSysLoadWarnMail(String sysLoadWarnMail) {
      this.sysLoadWarnMail = sysLoadWarnMail;
   }

   public Double getSysLoadWarnVal() {
      return this.sysLoadWarnVal;
   }

   public void setSysLoadWarnVal(Double sysLoadWarnVal) {
      this.sysLoadWarnVal = sysLoadWarnVal;
   }

   public String getSmartWarnMail() {
      return this.smartWarnMail;
   }

   public void setSmartWarnMail(String smartWarnMail) {
      this.smartWarnMail = smartWarnMail;
   }

   public String getHostDownWarnMail() {
      return this.hostDownWarnMail;
   }

   public void setHostDownWarnMail(String hostDownWarnMail) {
      this.hostDownWarnMail = hostDownWarnMail;
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
}
