package com.wgcloud.entity;

import java.util.Date;

public class ShellInfo extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String shellName;
   private String shell;
   private String state;
   private Integer state1Count;
   private Integer stateOtherCount;
   private Date createTime;
   private String shellType;
   private String shellDay;
   private String shellTime;
   private String account;

   public String getShell() {
      return this.shell;
   }

   public void setShell(String shell) {
      this.shell = shell;
   }

   public String getState() {
      return this.state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public Integer getState1Count() {
      return this.state1Count;
   }

   public void setState1Count(Integer state1Count) {
      this.state1Count = state1Count;
   }

   public Integer getStateOtherCount() {
      return this.stateOtherCount;
   }

   public void setStateOtherCount(Integer stateOtherCount) {
      this.stateOtherCount = stateOtherCount;
   }

   public String getShellName() {
      return this.shellName;
   }

   public void setShellName(String shellName) {
      this.shellName = shellName;
   }

   public String getShellType() {
      return this.shellType;
   }

   public void setShellType(String shellType) {
      this.shellType = shellType;
   }

   public String getShellTime() {
      return this.shellTime;
   }

   public void setShellTime(String shellTime) {
      this.shellTime = shellTime;
   }

   public String getShellDay() {
      return this.shellDay;
   }

   public void setShellDay(String shellDay) {
      this.shellDay = shellDay;
   }

   public String getAccount() {
      return this.account;
   }

   public void setAccount(String account) {
      this.account = account;
   }
}
