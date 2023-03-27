package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class AppExcelChartDto implements Serializable {
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
      value = {"线程数"},
      index = 3
   )
   @ColumnWidth(18)
   private String threadsNum;

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

   public String getThreadsNum() {
      return this.threadsNum;
   }

   public void setThreadsNum(String threadsNum) {
      this.threadsNum = threadsNum;
   }
}
