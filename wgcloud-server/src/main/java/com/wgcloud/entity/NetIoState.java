package com.wgcloud.entity;

import com.wgcloud.util.DateUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class NetIoState extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private String rxpck;
   private String txpck;
   private String rxbyt;
   private String txbyt;
   private String dropin;
   private String dropout;
   private String netConnections;
   private String dateStr;
   private Date createTime;

   public String getRxpck() {
      return this.rxpck;
   }

   public void setRxpck(String rxpck) {
      this.rxpck = rxpck;
   }

   public String getTxpck() {
      return this.txpck;
   }

   public void setTxpck(String txpck) {
      this.txpck = txpck;
   }

   public String getRxbyt() {
      return this.rxbyt;
   }

   public void setRxbyt(String rxbyt) {
      this.rxbyt = rxbyt;
   }

   public String getTxbyt() {
      return this.txbyt;
   }

   public void setTxbyt(String txbyt) {
      this.txbyt = txbyt;
   }

   public String getDropin() {
      if (StringUtils.isEmpty(this.dropin)) {
         this.dropin = "0";
      }

      return this.dropin;
   }

   public void setDropin(String dropin) {
      this.dropin = dropin;
   }

   public String getDropout() {
      if (StringUtils.isEmpty(this.dropout)) {
         this.dropout = "0";
      }

      return this.dropout;
   }

   public void setDropout(String dropout) {
      this.dropout = dropout;
   }

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

   public String getHostname() {
      return this.hostname;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
   }

   public String getNetConnections() {
      if (StringUtils.isEmpty(this.netConnections)) {
         this.netConnections = "0";
      }

      return this.netConnections;
   }

   public void setNetConnections(String netConnections) {
      this.netConnections = netConnections;
   }
}
