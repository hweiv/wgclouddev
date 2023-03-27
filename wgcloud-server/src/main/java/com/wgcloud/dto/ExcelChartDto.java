package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class ExcelChartDto implements Serializable {
   private static final long serialVersionUID = 1L;
   @ExcelProperty(
      value = {"上报时间"},
      index = 0
   )
   @ColumnWidth(18)
   private String datetime;
   @ExcelProperty(
      value = {"cpu使用率%"},
      index = 1
   )
   @ColumnWidth(18)
   private Double cpuPer;
   @ExcelProperty(
      value = {"内存使用率%"},
      index = 2
   )
   @ColumnWidth(18)
   private Double memPer;
   @ExcelProperty(
      value = {"接收数据包/秒"},
      index = 3
   )
   @ColumnWidth(20)
   private String rxpck;
   @ExcelProperty(
      value = {"发送数据包/秒"},
      index = 4
   )
   @ColumnWidth(20)
   private String txpck;
   @ExcelProperty(
      value = {"下行速率KB/s"},
      index = 5
   )
   @ColumnWidth(18)
   private String rxbyt;
   @ExcelProperty(
      value = {"上行速率KB/s"},
      index = 6
   )
   @ColumnWidth(18)
   private String txbyt;
   @ExcelProperty(
      value = {"丢弃传入数据包/秒"},
      index = 7
   )
   @ColumnWidth(24)
   private String dropin;
   @ExcelProperty(
      value = {"丢弃传出数据包/秒"},
      index = 8
   )
   @ColumnWidth(24)
   private String dropout;
   @ExcelProperty(
      value = {"1分钟负载"},
      index = 9
   )
   @ColumnWidth(18)
   private Double oneLoad;
   @ExcelProperty(
      value = {"5分钟负载"},
      index = 10
   )
   @ColumnWidth(18)
   private Double fiveLoad;
   @ExcelProperty(
      value = {"15分钟负载"},
      index = 11
   )
   @ColumnWidth(18)
   private Double fifteenLoad;
   @ExcelProperty(
      value = {"主机连接数"},
      index = 12
   )
   @ColumnWidth(18)
   private String netConnections;

   public String getDatetime() {
      return this.datetime;
   }

   public void setDatetime(String datetime) {
      this.datetime = datetime;
   }

   public Double getCpuPer() {
      return this.cpuPer;
   }

   public void setCpuPer(Double cpuPer) {
      this.cpuPer = cpuPer;
   }

   public Double getMemPer() {
      return this.memPer;
   }

   public void setMemPer(Double memPer) {
      this.memPer = memPer;
   }

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
      return this.dropin;
   }

   public void setDropin(String dropin) {
      this.dropin = dropin;
   }

   public String getDropout() {
      return this.dropout;
   }

   public void setDropout(String dropout) {
      this.dropout = dropout;
   }

   public Double getOneLoad() {
      return this.oneLoad;
   }

   public void setOneLoad(Double oneLoad) {
      this.oneLoad = oneLoad;
   }

   public Double getFiveLoad() {
      return this.fiveLoad;
   }

   public void setFiveLoad(Double fiveLoad) {
      this.fiveLoad = fiveLoad;
   }

   public Double getFifteenLoad() {
      return this.fifteenLoad;
   }

   public void setFifteenLoad(Double fifteenLoad) {
      this.fifteenLoad = fifteenLoad;
   }

   public String getNetConnections() {
      return this.netConnections;
   }

   public void setNetConnections(String netConnections) {
      this.netConnections = netConnections;
   }
}
