package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class FileWarnStateExcelDto implements Serializable {
   private static final long serialVersionUID = 1L;
   @ExcelProperty(
      value = {"时间"},
      index = 0
   )
   @ColumnWidth(20)
   private String datetime;
   @ExcelProperty(
      value = {"告警内容"},
      index = 1
   )
   @ColumnWidth(30)
   private String warContent;

   public String getDatetime() {
      return this.datetime;
   }

   public void setDatetime(String datetime) {
      this.datetime = datetime;
   }

   public String getWarContent() {
      return this.warContent;
   }

   public void setWarContent(String warContent) {
      this.warContent = warContent;
   }
}
