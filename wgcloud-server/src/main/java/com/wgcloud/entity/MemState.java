package com.wgcloud.entity;

import com.wgcloud.util.DateUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class MemState extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private String total;
   private String used;
   private String free;
   private Double usePer;
   private String dateStr;
   private Date createTime;

   public String getTotal() {
      return this.total;
   }

   public void setTotal(String total) {
      this.total = total;
   }

   public String getUsed() {
      return this.used;
   }

   public void setUsed(String used) {
      this.used = used;
   }

   public String getFree() {
      return this.free;
   }

   public void setFree(String free) {
      this.free = free;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public String getDateStr() {
      String s = DateUtil.getDateTimeString(this.getCreateTime());
      return !StringUtils.isEmpty(s) && s.length() > 16 ? s.substring(5) : this.dateStr;
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

   public Double getUsePer() {
      return this.usePer;
   }

   public void setUsePer(Double usePer) {
      this.usePer = usePer;
   }
}
