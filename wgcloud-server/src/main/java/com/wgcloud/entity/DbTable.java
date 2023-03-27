package com.wgcloud.entity;

import com.wgcloud.util.DateUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class DbTable extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String dbInfoId;
   private String tableName;
   private String whereVal;
   private String remark;
   private Long tableCount;
   private String resultExp;
   private Long value;
   private String dateStr;
   private String active;
   private String state;
   private Date createTime;
   private String account;
   private String image;

   public String getTableName() {
      return this.tableName;
   }

   public void setTableName(String tableName) {
      this.tableName = tableName;
   }

   public String getWhereVal() {
      return this.whereVal;
   }

   public void setWhereVal(String whereVal) {
      this.whereVal = whereVal;
   }

   public String getRemark() {
      return this.remark;
   }

   public void setRemark(String remark) {
      this.remark = remark;
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

   public Long getValue() {
      return this.tableCount;
   }

   public void setValue(Long value) {
      this.value = value;
   }

   public String getDbInfoId() {
      return this.dbInfoId;
   }

   public void setDbInfoId(String dbInfoId) {
      this.dbInfoId = dbInfoId;
   }

   public String getActive() {
      return this.active;
   }

   public void setActive(String active) {
      this.active = active;
   }

   public String getResultExp() {
      return this.resultExp;
   }

   public void setResultExp(String resultExp) {
      this.resultExp = resultExp;
   }

   public String getAccount() {
      return this.account;
   }

   public void setAccount(String account) {
      this.account = account;
   }

   public String getImage() {
      return this.image;
   }

   public void setImage(String image) {
      this.image = image;
   }

   public String getState() {
      return this.state;
   }

   public void setState(String state) {
      this.state = state;
   }
}
