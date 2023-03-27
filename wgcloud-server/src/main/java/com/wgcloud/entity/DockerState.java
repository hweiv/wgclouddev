package com.wgcloud.entity;

import com.wgcloud.util.DateUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class DockerState extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String dockerInfoId;
   private Double cpuPer;
   private Double memPer;
   private String dateStr;
   private Date createTime;

   public Double getCpuPer() {
      return this.cpuPer;
   }

   public void setCpuPer(Double cpuPer) {
      this.cpuPer = cpuPer;
   }

   public String getDockerInfoId() {
      return this.dockerInfoId;
   }

   public void setDockerInfoId(String dockerInfoId) {
      this.dockerInfoId = dockerInfoId;
   }

   public Double getMemPer() {
      return this.memPer;
   }

   public void setMemPer(Double memPer) {
      this.memPer = memPer;
   }

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public String getDateStr() {
      String s = DateUtil.getDateTimeString(this.getCreateTime());
      return !StringUtils.isEmpty(s) && s.length() > 16 ? s.substring(5) : this.dateStr;
   }

   public void setDateStr(String dateStr) {
      this.dateStr = dateStr;
   }
}
