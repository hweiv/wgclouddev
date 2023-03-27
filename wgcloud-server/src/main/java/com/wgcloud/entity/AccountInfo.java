package com.wgcloud.entity;

import java.util.Date;

public class AccountInfo extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String account;
   private String role;
   private String email;
   private String passwd;
   private String accountKey;
   private String remark;
   private Date createTime;
   private Integer hostNum;

   public String getAccount() {
      return this.account;
   }

   public void setAccount(String account) {
      this.account = account;
   }

   public String getEmail() {
      return this.email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPasswd() {
      return this.passwd;
   }

   public void setPasswd(String passwd) {
      this.passwd = passwd;
   }

   public String getRole() {
      return this.role;
   }

   public void setRole(String role) {
      this.role = role;
   }

   public String getAccountKey() {
      return this.accountKey;
   }

   public void setAccountKey(String accountKey) {
      this.accountKey = accountKey;
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

   public Integer getHostNum() {
      return this.hostNum;
   }

   public void setHostNum(Integer hostNum) {
      this.hostNum = hostNum;
   }
}
