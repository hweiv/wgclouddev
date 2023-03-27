package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class TjbbExcelChartDto implements Serializable {
   private static final long serialVersionUID = 1L;
   @ExcelProperty(
      value = {"巡检项"},
      index = 0
   )
   @ColumnWidth(28)
   private String infoKey;
   @ExcelProperty(
      value = {"描述"},
      index = 1
   )
   @ColumnWidth(28)
   private String infoContent;

   public String getInfoKey() {
      return this.infoKey;
   }

   public void setInfoKey(String infoKey) {
      this.infoKey = infoKey;
   }

   public String getInfoContent() {
      return this.infoContent;
   }

   public void setInfoContent(String infoContent) {
      this.infoContent = infoContent;
   }
}
