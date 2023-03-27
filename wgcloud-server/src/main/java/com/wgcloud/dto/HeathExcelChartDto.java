package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class HeathExcelChartDto implements Serializable {
   private static final long serialVersionUID = 1L;
   @ExcelProperty(
      value = {"上报时间"},
      index = 0
   )
   @ColumnWidth(18)
   private String datetime;
   @ExcelProperty(
      value = {"响应时间(ms)"},
      index = 1
   )
   @ColumnWidth(18)
   private Integer resTimes;

   public String getDatetime() {
      return this.datetime;
   }

   public void setDatetime(String datetime) {
      this.datetime = datetime;
   }

   public Integer getResTimes() {
      return this.resTimes;
   }

   public void setResTimes(Integer resTimes) {
      this.resTimes = resTimes;
   }
}
