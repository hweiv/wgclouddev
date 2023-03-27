package com.wgcloud.entity;

import com.wgcloud.util.DateUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class HostDiskPer extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private Double diskSumPer;
   private String dateStr;
   private Date createTime;

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public String getDateStr() {
      String s = DateUtil.getDateTimeString(this.getCreateTime());
      return !StringUtils.isEmpty(s) && s.length() > 9 ? s.substring(0, 10) : this.dateStr;
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

   public Double getDiskSumPer() {
      return this.diskSumPer;
   }

   public void setDiskSumPer(Double diskSumPer) {
      this.diskSumPer = diskSumPer;
   }
}
