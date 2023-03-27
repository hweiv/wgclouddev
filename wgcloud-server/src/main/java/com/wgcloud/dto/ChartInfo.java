package com.wgcloud.dto;

import com.wgcloud.entity.BaseEntity;

public class ChartInfo extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String item;
   private Integer count;
   private Double percent;
   private Double value;

   public String getItem() {
      return this.item;
   }

   public void setItem(String item) {
      this.item = item;
   }

   public Integer getCount() {
      return this.count;
   }

   public void setCount(Integer count) {
      this.count = count;
   }

   public Double getPercent() {
      return this.percent;
   }

   public void setPercent(Double percent) {
      this.percent = percent;
   }

   public Double getValue() {
      return this.value;
   }

   public void setValue(Double value) {
      this.value = value;
   }
}
