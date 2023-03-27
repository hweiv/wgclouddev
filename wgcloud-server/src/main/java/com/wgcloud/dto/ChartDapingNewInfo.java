package com.wgcloud.dto;

import com.wgcloud.entity.BaseEntity;

public class ChartDapingNewInfo extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String name;
   private Integer value;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Integer getValue() {
      return this.value;
   }

   public void setValue(Integer value) {
      this.value = value;
   }
}
