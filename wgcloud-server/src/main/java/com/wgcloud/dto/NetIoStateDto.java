package com.wgcloud.dto;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class NetIoStateDto extends BaseEntity {
   private static final long serialVersionUID = -8314012397341825158L;
   private String hostname;
   private Double rxpck;
   private Double txpck;
   private Double rxbyt;
   private Double txbyt;
   private Double dropin;
   private Double dropout;
   private Integer netConnections;
   private String dateStr;
   private Date createTime;

   public Double getRxpck() {
      return this.rxpck;
   }

   public void setRxpck(Double rxpck) {
      this.rxpck = rxpck;
   }

   public Double getTxpck() {
      return this.txpck;
   }

   public void setTxpck(Double txpck) {
      this.txpck = txpck;
   }

   public Double getRxbyt() {
      return this.rxbyt;
   }

   public void setRxbyt(Double rxbyt) {
      this.rxbyt = rxbyt;
   }

   public Double getTxbyt() {
      return this.txbyt;
   }

   public void setTxbyt(Double txbyt) {
      this.txbyt = txbyt;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public String getDateStr() {
      return !StringUtils.isEmpty(this.dateStr) && this.dateStr.length() > 16 ? this.dateStr.substring(5) : this.dateStr;
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

   public Double getDropin() {
      return this.dropin;
   }

   public void setDropin(Double dropin) {
      this.dropin = dropin;
   }

   public Double getDropout() {
      return this.dropout;
   }

   public void setDropout(Double dropout) {
      this.dropout = dropout;
   }

   public Integer getNetConnections() {
      return this.netConnections;
   }

   public void setNetConnections(Integer netConnections) {
      this.netConnections = netConnections;
   }
}
