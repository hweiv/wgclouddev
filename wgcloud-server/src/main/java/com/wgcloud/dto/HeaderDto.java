package com.wgcloud.dto;

import java.io.Serializable;

public class HeaderDto implements Serializable {
   private static final long serialVersionUID = 1L;
   private String sort;
   private String key;
   private String value;

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getSort() {
      return this.sort;
   }

   public void setSort(String sort) {
      this.sort = sort;
   }
}
