package com.wgcloud.dto;

import java.io.Serializable;

public class TuopuNodeDto implements Serializable {
   private static final long serialVersionUID = 1L;
   private String id;
   private int x;
   private int y;
   private String label;
   private String img;
   private Integer size;

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public String getLabel() {
      return this.label;
   }

   public void setLabel(String label) {
      this.label = label;
   }

   public String getImg() {
      return this.img;
   }

   public void setImg(String img) {
      this.img = img;
   }

   public Integer getSize() {
      return this.size;
   }

   public void setSize(Integer size) {
      this.size = size;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }
}
