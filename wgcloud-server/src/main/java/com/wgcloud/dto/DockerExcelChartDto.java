package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class DockerExcelChartDto implements Serializable {
   private static final long serialVersionUID = 1L;
   @ExcelProperty(
      value = {"上报时间"},
      index = 0
   )
   @ColumnWidth(18)
   private String datetime;
   @ExcelProperty(
      value = {"内存使用M"},
      index = 1
   )
   @ColumnWidth(18)
   private Double memPer;

   public String getDatetime() {
      return this.datetime;
   }

   public void setDatetime(String datetime) {
      this.datetime = datetime;
   }

   public Double getMemPer() {
      return this.memPer;
   }

   public void setMemPer(Double memPer) {
      this.memPer = memPer;
   }
}
