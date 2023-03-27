package com.wgcloud.dto;

import java.io.Serializable;

public class ResDataDto implements Serializable {
   private static final long serialVersionUID = -2913111613773445949L;
   private String code;
   private String msg;
   private Object data;

   public String getCode() {
      return this.code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public String getMsg() {
      return this.msg;
   }

   public void setMsg(String msg) {
      this.msg = msg;
   }

   public Object getData() {
      return this.data;
   }

   public void setData(Object data) {
      this.data = data;
   }
}
