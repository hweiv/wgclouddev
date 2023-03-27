package com.wgcloud.entity;

import java.util.Date;

public class ReportInstance extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String infoKey;
   private String infoContent;
   private String reportInfoId;
   private Date createTime;

   public String getInfoKey() {
      return this.infoKey;
   }

   public void setInfoKey(String infoKey) {
      this.infoKey = infoKey;
   }

   public String getInfoContent() {
      return this.infoContent;
   }

   public void setInfoContent(String infoContent) {
      this.infoContent = infoContent;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public String getReportInfoId() {
      return this.reportInfoId;
   }

   public void setReportInfoId(String reportInfoId) {
      this.reportInfoId = reportInfoId;
   }
}
