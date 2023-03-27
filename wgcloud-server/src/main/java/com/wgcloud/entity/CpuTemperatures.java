package com.wgcloud.entity;

import java.util.Date;

public class CpuTemperatures extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private String core_index;
   private String crit;
   private String input;
   private String max;
   private Date createTime;

   public String getHostname() {
      return this.hostname;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
   }

   public String getCore_index() {
      return this.core_index;
   }

   public void setCore_index(String core_index) {
      this.core_index = core_index;
   }

   public String getCrit() {
      return this.crit;
   }

   public void setCrit(String crit) {
      this.crit = crit;
   }

   public String getInput() {
      return this.input;
   }

   public void setInput(String input) {
      this.input = input;
   }

   public String getMax() {
      return this.max;
   }

   public void setMax(String max) {
      this.max = max;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }
}
