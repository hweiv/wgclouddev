package com.wgcloud.entity;

import com.wgcloud.util.DateUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class SysLoadState extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private Double oneLoad;
   private Double fiveLoad;
   private Double fifteenLoad;
   private String users;
   private String dateStr;
   private Date createTime;

   public Double getOneLoad() {
      if (null == this.oneLoad) {
         this.oneLoad = 0.0D;
      }

      return this.oneLoad;
   }

   public void setOneLoad(Double oneLoad) {
      this.oneLoad = oneLoad;
   }

   public Double getFiveLoad() {
      if (null == this.fiveLoad) {
         this.fiveLoad = 0.0D;
      }

      return this.fiveLoad;
   }

   public void setFiveLoad(Double fiveLoad) {
      this.fiveLoad = fiveLoad;
   }

   public Double getFifteenLoad() {
      if (null == this.fifteenLoad) {
         this.fifteenLoad = 0.0D;
      }

      return this.fifteenLoad;
   }

   public void setFifteenLoad(Double fifteenLoad) {
      this.fifteenLoad = fifteenLoad;
   }

   public String getUsers() {
      return this.users;
   }

   public void setUsers(String users) {
      this.users = users;
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
}
