package com.wgcloud.entity;

import java.util.Date;

public class Equipment extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String name;
   private String xinghao;
   private String person;
   private String dept;
   private String code;
   private Double price;
   private String gongyingshang;
   private String caigouDate;
   private String remark;
   private String weibaoDate;
   private Date createTime;
   private String account;
   private String groupId;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getXinghao() {
      return this.xinghao;
   }

   public void setXinghao(String xinghao) {
      this.xinghao = xinghao;
   }

   public String getPerson() {
      return this.person;
   }

   public void setPerson(String person) {
      this.person = person;
   }

   public String getDept() {
      return this.dept;
   }

   public void setDept(String dept) {
      this.dept = dept;
   }

   public String getCode() {
      return this.code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public Double getPrice() {
      return this.price;
   }

   public void setPrice(Double price) {
      this.price = price;
   }

   public String getGongyingshang() {
      return this.gongyingshang;
   }

   public void setGongyingshang(String gongyingshang) {
      this.gongyingshang = gongyingshang;
   }

   public String getCaigouDate() {
      return this.caigouDate;
   }

   public void setCaigouDate(String caigouDate) {
      this.caigouDate = caigouDate;
   }

   public String getRemark() {
      return this.remark;
   }

   public void setRemark(String remark) {
      this.remark = remark;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public String getAccount() {
      return this.account;
   }

   public void setAccount(String account) {
      this.account = account;
   }

   public String getWeibaoDate() {
      return this.weibaoDate;
   }

   public void setWeibaoDate(String weibaoDate) {
      this.weibaoDate = weibaoDate;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }
}
