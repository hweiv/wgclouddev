package com.wgcloud.entity;

import java.util.Date;

public class HeathMonitor extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String appName;
   private String heathUrl;
   private String heathStatus;
   private Integer resTimes;
   private String active;
   private String resKeyword;
   private String resNoKeyword;
   private String method;
   private String postStr;
   private String headerJson;
   private String headerKey;
   private String headerValue;
   private String headerKey2;
   private String headerValue2;
   private String headerKey3;
   private String headerValue3;
   private String headerKey4;
   private String headerValue4;
   private String headerKey5;
   private String headerValue5;
   private String headerSize;
   private Integer warnCount;
   private String warnQueryWd;
   private Date createTime;
   private String account;
   private String responseBodySize;
   private String formJson;
   private String formKey;
   private String formValue;
   private String formKey2;
   private String formValue2;
   private String formKey3;
   private String formValue3;
   private String formKey4;
   private String formValue4;
   private String formKey5;
   private String formValue5;
   private String formDataSize;
   private String errorMsg;

   public String getAppName() {
      return this.appName;
   }

   public void setAppName(String appName) {
      this.appName = appName;
   }

   public String getHeathUrl() {
      return this.heathUrl;
   }

   public void setHeathUrl(String heathUrl) {
      this.heathUrl = heathUrl;
   }

   public String getHeathStatus() {
      return this.heathStatus;
   }

   public void setHeathStatus(String heathStatus) {
      this.heathStatus = heathStatus;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public Integer getResTimes() {
      return this.resTimes;
   }

   public void setResTimes(Integer resTimes) {
      this.resTimes = resTimes;
   }

   public String getActive() {
      return this.active;
   }

   public void setActive(String active) {
      this.active = active;
   }

   public String getResKeyword() {
      return this.resKeyword;
   }

   public void setResKeyword(String resKeyword) {
      this.resKeyword = resKeyword;
   }

   public String getMethod() {
      return this.method;
   }

   public void setMethod(String method) {
      this.method = method;
   }

   public String getPostStr() {
      return this.postStr;
   }

   public void setPostStr(String postStr) {
      this.postStr = postStr;
   }

   public Integer getWarnCount() {
      return this.warnCount;
   }

   public void setWarnCount(Integer warnCount) {
      this.warnCount = warnCount;
   }

   public String getResNoKeyword() {
      return this.resNoKeyword;
   }

   public void setResNoKeyword(String resNoKeyword) {
      this.resNoKeyword = resNoKeyword;
   }

   public String getWarnQueryWd() {
      return this.warnQueryWd;
   }

   public void setWarnQueryWd(String warnQueryWd) {
      this.warnQueryWd = warnQueryWd;
   }

   public String getHeaderJson() {
      return this.headerJson;
   }

   public void setHeaderJson(String headerJson) {
      this.headerJson = headerJson;
   }

   public String getHeaderKey() {
      return this.headerKey;
   }

   public void setHeaderKey(String headerKey) {
      this.headerKey = headerKey;
   }

   public String getHeaderValue() {
      return this.headerValue;
   }

   public void setHeaderValue(String headerValue) {
      this.headerValue = headerValue;
   }

   public String getHeaderKey2() {
      return this.headerKey2;
   }

   public void setHeaderKey2(String headerKey2) {
      this.headerKey2 = headerKey2;
   }

   public String getHeaderValue2() {
      return this.headerValue2;
   }

   public void setHeaderValue2(String headerValue2) {
      this.headerValue2 = headerValue2;
   }

   public String getHeaderKey3() {
      return this.headerKey3;
   }

   public void setHeaderKey3(String headerKey3) {
      this.headerKey3 = headerKey3;
   }

   public String getHeaderValue3() {
      return this.headerValue3;
   }

   public void setHeaderValue3(String headerValue3) {
      this.headerValue3 = headerValue3;
   }

   public String getHeaderKey4() {
      return this.headerKey4;
   }

   public void setHeaderKey4(String headerKey4) {
      this.headerKey4 = headerKey4;
   }

   public String getHeaderValue4() {
      return this.headerValue4;
   }

   public void setHeaderValue4(String headerValue4) {
      this.headerValue4 = headerValue4;
   }

   public String getHeaderKey5() {
      return this.headerKey5;
   }

   public void setHeaderKey5(String headerKey5) {
      this.headerKey5 = headerKey5;
   }

   public String getHeaderValue5() {
      return this.headerValue5;
   }

   public void setHeaderValue5(String headerValue5) {
      this.headerValue5 = headerValue5;
   }

   public String getAccount() {
      return this.account;
   }

   public void setAccount(String account) {
      this.account = account;
   }

   public String getResponseBodySize() {
      return this.responseBodySize;
   }

   public void setResponseBodySize(String responseBodySize) {
      this.responseBodySize = responseBodySize;
   }

   public String getFormJson() {
      return this.formJson;
   }

   public void setFormJson(String formJson) {
      this.formJson = formJson;
   }

   public String getFormKey() {
      return this.formKey;
   }

   public void setFormKey(String formKey) {
      this.formKey = formKey;
   }

   public String getFormValue() {
      return this.formValue;
   }

   public void setFormValue(String formValue) {
      this.formValue = formValue;
   }

   public String getFormKey2() {
      return this.formKey2;
   }

   public void setFormKey2(String formKey2) {
      this.formKey2 = formKey2;
   }

   public String getFormValue2() {
      return this.formValue2;
   }

   public void setFormValue2(String formValue2) {
      this.formValue2 = formValue2;
   }

   public String getFormKey3() {
      return this.formKey3;
   }

   public void setFormKey3(String formKey3) {
      this.formKey3 = formKey3;
   }

   public String getFormValue3() {
      return this.formValue3;
   }

   public void setFormValue3(String formValue3) {
      this.formValue3 = formValue3;
   }

   public String getFormKey4() {
      return this.formKey4;
   }

   public void setFormKey4(String formKey4) {
      this.formKey4 = formKey4;
   }

   public String getFormValue4() {
      return this.formValue4;
   }

   public void setFormValue4(String formValue4) {
      this.formValue4 = formValue4;
   }

   public String getFormKey5() {
      return this.formKey5;
   }

   public void setFormKey5(String formKey5) {
      this.formKey5 = formKey5;
   }

   public String getFormValue5() {
      return this.formValue5;
   }

   public void setFormValue5(String formValue5) {
      this.formValue5 = formValue5;
   }

   public String getErrorMsg() {
      return this.errorMsg;
   }

   public void setErrorMsg(String errorMsg) {
      this.errorMsg = errorMsg;
   }

   public String getHeaderSize() {
      return this.headerSize;
   }

   public void setHeaderSize(String headerSize) {
      this.headerSize = headerSize;
   }

   public String getFormDataSize() {
      return this.formDataSize;
   }

   public void setFormDataSize(String formDataSize) {
      this.formDataSize = formDataSize;
   }
}
