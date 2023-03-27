package com.wgcloud.entity;

import com.wgcloud.util.DateUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class SnmpState extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String snmpInfoId;
   private String recvAvg;
   private Double recvAvgDouble;
   private String sentAvg;
   private Double sentAvgDouble;
   private String cpuPer;
   private Double cpuPerDouble;
   private String memPer;
   private Double memPerDouble;
   private String dateStr;
   private Date createTime;

   public String getSnmpInfoId() {
      return this.snmpInfoId;
   }

   public void setSnmpInfoId(String snmpInfoId) {
      this.snmpInfoId = snmpInfoId;
   }

   public String getRecvAvg() {
      return this.recvAvg;
   }

   public void setRecvAvg(String recvAvg) {
      this.recvAvg = recvAvg;
   }

   public String getSentAvg() {
      return this.sentAvg;
   }

   public void setSentAvg(String sentAvg) {
      this.sentAvg = sentAvg;
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

   public Double getRecvAvgDouble() {
      if (!StringUtils.isEmpty(this.recvAvg)) {
         try {
            this.recvAvgDouble = Double.valueOf(this.recvAvg);
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      } else {
         this.recvAvgDouble = 0.0D;
      }

      return this.recvAvgDouble;
   }

   public void setRecvAvgDouble(Double recvAvgDouble) {
      this.recvAvgDouble = recvAvgDouble;
   }

   public Double getSentAvgDouble() {
      if (!StringUtils.isEmpty(this.sentAvg)) {
         try {
            this.sentAvgDouble = Double.valueOf(this.sentAvg);
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      } else {
         this.sentAvgDouble = 0.0D;
      }

      return this.sentAvgDouble;
   }

   public void setSentAvgDouble(Double sentAvgDouble) {
      this.sentAvgDouble = sentAvgDouble;
   }

   public String getCpuPer() {
      return this.cpuPer;
   }

   public void setCpuPer(String cpuPer) {
      this.cpuPer = cpuPer;
   }

   public Double getCpuPerDouble() {
      if (!StringUtils.isEmpty(this.cpuPer)) {
         try {
            this.cpuPerDouble = Double.valueOf(this.cpuPer);
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      } else {
         this.cpuPerDouble = 0.0D;
      }

      return this.cpuPerDouble;
   }

   public void setCpuPerDouble(Double cpuPerDouble) {
      this.cpuPerDouble = cpuPerDouble;
   }

   public String getMemPer() {
      return this.memPer;
   }

   public void setMemPer(String memPer) {
      this.memPer = memPer;
   }

   public Double getMemPerDouble() {
      if (!StringUtils.isEmpty(this.memPer)) {
         try {
            this.memPerDouble = Double.valueOf(this.memPer);
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      } else {
         this.memPerDouble = 0.0D;
      }

      return this.memPerDouble;
   }

   public void setMemPerDouble(Double memPerDouble) {
      this.memPerDouble = memPerDouble;
   }
}
