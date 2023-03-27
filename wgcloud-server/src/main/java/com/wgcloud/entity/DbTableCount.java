package com.wgcloud.entity;

import com.wgcloud.util.DateUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class DbTableCount extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String dbTableId;
   private Long tableCount;
   private String dateStr;
   private Date createTime;

   public String getDbTableId() {
      return this.dbTableId;
   }

   public void setDbTableId(String dbTableId) {
      this.dbTableId = dbTableId;
   }

   public Long getTableCount() {
      return this.tableCount;
   }

   public void setTableCount(Long tableCount) {
      this.tableCount = tableCount;
   }

   public String getDateStr() {
      String s = DateUtil.getDateTimeString(this.getCreateTime());
      return !StringUtils.isEmpty(s) && s.length() > 16 ? s.substring(5) : this.dateStr;
   }

   public void setDateStr(String dateStr) {
      this.dateStr = dateStr;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }
}
