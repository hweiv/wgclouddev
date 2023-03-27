package com.wgcloud.entity;

import com.wgcloud.util.DateUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class DceState extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String dceId;
   private Integer resTimes;
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
      return !StringUtils.isEmpty(s) && s.length() > 16 ? s.substring(5) : this.dateStr;
   }

   public void setDateStr(String dateStr) {
      this.dateStr = dateStr;
   }

   public Integer getResTimes() {
      return this.resTimes;
   }

   public void setResTimes(Integer resTimes) {
      this.resTimes = resTimes;
   }

   public String getDceId() {
      return this.dceId;
   }

   public void setDceId(String dceId) {
      this.dceId = dceId;
   }
}
