package com.wgcloud.dto;

import com.wgcloud.entity.BaseEntity;

public class ChartReportInfo extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String name;
   private Integer vote;
   private Double value;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Integer getVote() {
      return this.vote;
   }

   public void setVote(Integer vote) {
      this.vote = vote;
   }

   public Double getValue() {
      return this.value;
   }

   public void setValue(Double value) {
      this.value = value;
   }
}
