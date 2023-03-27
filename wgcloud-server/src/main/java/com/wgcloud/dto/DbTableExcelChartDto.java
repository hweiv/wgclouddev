package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class DbTableExcelChartDto implements Serializable {
   private static final long serialVersionUID = 1L;
   @ExcelProperty(
      value = {"时间"},
      index = 0
   )
   @ColumnWidth(20)
   private String datetime;
   @ExcelProperty(
      value = {"数据量"},
      index = 1
   )
   @ColumnWidth(18)
   private Long tableCount;

   public String getDatetime() {
      return this.datetime;
   }

   public void setDatetime(String datetime) {
      this.datetime = datetime;
   }

   public Long getTableCount() {
      return this.tableCount;
   }

   public void setTableCount(Long tableCount) {
      this.tableCount = tableCount;
   }
}
