package com.wgcloud.entity;

import java.util.Date;

public class LogInfo extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private String infoContent;
   private String state;
   private Date createTime;

   public String getHostname() {
      return this.hostname;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
   }

   public String getInfoContent() {
      return this.infoContent;
   }

   public void setInfoContent(String infoContent) {
      this.infoContent = infoContent;
   }

   public String getState() {
      return this.state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }
}
