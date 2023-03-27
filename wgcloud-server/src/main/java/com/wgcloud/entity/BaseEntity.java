package com.wgcloud.entity;

import com.wgcloud.util.staticvar.StaticKeys;
import java.io.Serializable;

public class BaseEntity implements Serializable {
   private static final long serialVersionUID = 1L;
   private String id;
   private Integer page;
   private Integer pageSize;
   private String orderBy;
   private String orderType;

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public Integer getPage() {
      if (this.page == null) {
         this.page = 1;
      }

      return this.page;
   }

   public void setPage(Integer page) {
      this.page = page;
   }

   public Integer getPageSize() {
      if (this.pageSize == null) {
         this.pageSize = StaticKeys.PAGE_SIZE;
      }

      return this.pageSize;
   }

   public void setPageSize(Integer pageSize) {
      this.pageSize = pageSize;
   }

   public String getOrderBy() {
      return this.orderBy;
   }

   public void setOrderBy(String orderBy) {
      this.orderBy = orderBy;
   }

   public String getOrderType() {
      return this.orderType;
   }

   public void setOrderType(String orderType) {
      this.orderType = orderType;
   }
}
