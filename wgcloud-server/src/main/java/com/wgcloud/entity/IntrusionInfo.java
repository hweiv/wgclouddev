package com.wgcloud.entity;

import java.sql.Timestamp;

public class IntrusionInfo extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private String lsmod;
   private String passwdInfo;
   private String crontab;
   private String promisc;
   private String rpcinfo;
   private Timestamp createTime;

   public Timestamp getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Timestamp createTime) {
      this.createTime = createTime;
   }

   public String getHostname() {
      return this.hostname;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
   }

   public String getLsmod() {
      return this.lsmod;
   }

   public void setLsmod(String lsmod) {
      this.lsmod = lsmod;
   }

   public String getPasswdInfo() {
      return this.passwdInfo;
   }

   public void setPasswdInfo(String passwdInfo) {
      this.passwdInfo = passwdInfo;
   }

   public String getCrontab() {
      return this.crontab;
   }

   public void setCrontab(String crontab) {
      this.crontab = crontab;
   }

   public String getPromisc() {
      return this.promisc;
   }

   public void setPromisc(String promisc) {
      this.promisc = promisc;
   }

   public String getRpcinfo() {
      return this.rpcinfo;
   }

   public void setRpcinfo(String rpcinfo) {
      this.rpcinfo = rpcinfo;
   }
}
