package com.wgcloud.dto;

import com.wgcloud.entity.BaseEntity;

public class SubtitleDto extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String maxValue;
   private String avgValue;
   private String minValue;

   public String getMaxValue() {
      return this.maxValue;
   }

   public void setMaxValue(String maxValue) {
      this.maxValue = maxValue;
   }

   public String getAvgValue() {
      return this.avgValue;
   }

   public void setAvgValue(String avgValue) {
      this.avgValue = avgValue;
   }

   public String getMinValue() {
      return this.minValue;
   }

   public void setMinValue(String minValue) {
      this.minValue = minValue;
   }
}
