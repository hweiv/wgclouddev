package com.wgcloud.entity;

import java.util.Date;

public class HostGroup extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String groupName;
   private String remark;
   private String groupType;
   private Date createTime;

   public String getGroupName() {
      return this.groupName;
   }

   public void setGroupName(String groupName) {
      this.groupName = groupName;
   }

   public String getRemark() {
      return this.remark;
   }

   public void setRemark(String remark) {
      this.remark = remark;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public String getGroupType() {
      return this.groupType;
   }

   public void setGroupType(String groupType) {
      this.groupType = groupType;
   }
}
