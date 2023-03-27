package com.wgcloud.entity;

public class DashboardView extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private String version;
   private String yxDays;
   private double memPer;
   private String dateStr;

   public String getHostname() {
      return this.hostname;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String getYxDays() {
      return this.yxDays;
   }

   public void setYxDays(String yxDays) {
      this.yxDays = yxDays;
   }

   public double getMemPer() {
      return this.memPer;
   }

   public void setMemPer(double memPer) {
      this.memPer = memPer;
   }

   public String getDateStr() {
      return this.dateStr;
   }

   public void setDateStr(String dateStr) {
      this.dateStr = dateStr;
   }
}
