package com.wgcloud.entity;

import java.util.Date;

public class PortInfo extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private String port;
   private String portName;
   private String state;
   private String active;
   private Integer warnCount;
   private String warnQueryWd;
   private String account;
   private Date createTime;
   private String telnetIp;
   private String groupId;

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public String getHostname() {
      return this.hostname;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
   }

   public String getPort() {
      return this.port;
   }

   public void setPort(String port) {
      this.port = port;
   }

   public String getState() {
      return this.state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public String getPortName() {
      return this.portName;
   }

   public void setPortName(String portName) {
      this.portName = portName;
   }

   public String getActive() {
      return this.active;
   }

   public void setActive(String active) {
      this.active = active;
   }

   public Integer getWarnCount() {
      return this.warnCount;
   }

   public void setWarnCount(Integer warnCount) {
      this.warnCount = warnCount;
   }

   public String getTelnetIp() {
      return this.telnetIp;
   }

   public void setTelnetIp(String telnetIp) {
      this.telnetIp = telnetIp;
   }

   public String getWarnQueryWd() {
      return this.warnQueryWd;
   }

   public void setWarnQueryWd(String warnQueryWd) {
      this.warnQueryWd = warnQueryWd;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public String getAccount() {
      return this.account;
   }

   public void setAccount(String account) {
      this.account = account;
   }
}
