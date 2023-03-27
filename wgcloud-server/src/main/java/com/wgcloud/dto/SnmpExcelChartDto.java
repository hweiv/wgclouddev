package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class SnmpExcelChartDto implements Serializable {
   private static final long serialVersionUID = 1L;
   @ExcelProperty(
      value = {"上报时间"},
      index = 0
   )
   @ColumnWidth(18)
   private String datetime;
   @ExcelProperty(
      value = {"下行速率(MB/s)"},
      index = 1
   )
   @ColumnWidth(18)
   private String recvAvg;
   @ExcelProperty(
      value = {"上行速率(MB/s)"},
      index = 2
   )
   @ColumnWidth(18)
   private String sentAvg;
   @ExcelProperty(
      value = {"CPU使用率%"},
      index = 3
   )
   @ColumnWidth(18)
   private String cpuPer;
   @ExcelProperty(
      value = {"内存使用率%"},
      index = 4
   )
   @ColumnWidth(18)
   private String memPer;

   public String getDatetime() {
      return this.datetime;
   }

   public void setDatetime(String datetime) {
      this.datetime = datetime;
   }

   public String getRecvAvg() {
      return this.recvAvg;
   }

   public void setRecvAvg(String recvAvg) {
      this.recvAvg = recvAvg;
   }

   public String getSentAvg() {
      return this.sentAvg;
   }

   public void setSentAvg(String sentAvg) {
      this.sentAvg = sentAvg;
   }

   public String getCpuPer() {
      return this.cpuPer;
   }

   public void setCpuPer(String cpuPer) {
      this.cpuPer = cpuPer;
   }

   public String getMemPer() {
      return this.memPer;
   }

   public void setMemPer(String memPer) {
      this.memPer = memPer;
   }
}
