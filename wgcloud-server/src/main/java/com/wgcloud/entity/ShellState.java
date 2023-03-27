package com.wgcloud.entity;

import java.util.Date;

public class ShellState extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String shellId;
   private String hostname;
   private String shell;
   private String state;
   private Date createTime;
   private String shellTime;

   public String getShellId() {
      return this.shellId;
   }

   public void setShellId(String shellId) {
      this.shellId = shellId;
   }

   public String getHostname() {
      return this.hostname;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
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

   public String getShell() {
      return this.shell;
   }

   public void setShell(String shell) {
      this.shell = shell;
   }

   public String getShellTime() {
      return this.shellTime;
   }

   public void setShellTime(String shellTime) {
      this.shellTime = shellTime;
   }
}
