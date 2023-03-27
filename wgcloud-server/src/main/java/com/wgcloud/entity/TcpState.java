package com.wgcloud.entity;

import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class TcpState extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private String active;
   private String passive;
   private String retrans;
   private String dateStr;
   private Date createTime;

   public String getActive() {
      return this.active;
   }

   public void setActive(String active) {
      this.active = active;
   }

   public String getPassive() {
      return this.passive;
   }

   public void setPassive(String passive) {
      this.passive = passive;
   }

   public String getRetrans() {
      return this.retrans;
   }

   public void setRetrans(String retrans) {
      this.retrans = retrans;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public String getDateStr() {
      return !StringUtils.isEmpty(this.dateStr) && this.dateStr.length() > 16 ? this.dateStr.substring(5) : this.dateStr;
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
