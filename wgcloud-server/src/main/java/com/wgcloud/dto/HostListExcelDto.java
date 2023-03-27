package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class HostListExcelDto implements Serializable {
   private static final long serialVersionUID = 1L;
   @ExcelProperty(
      value = {"主机IP"},
      index = 0
   )
   @ColumnWidth(18)
   private String hostname;
   @ExcelProperty(
      value = {"主机名"},
      index = 1
   )
   @ColumnWidth(18)
   private String hostnameExt;
   @ExcelProperty(
      value = {"备注"},
      index = 2
   )
   @ColumnWidth(18)
   private String remark;
   @ExcelProperty(
      value = {"内存使用率%"},
      index = 3
   )
   @ColumnWidth(18)
   private Double memPer;
   @ExcelProperty(
      value = {"cpu使用率%"},
      index = 4
   )
   @ColumnWidth(18)
   private Double cpuPer;
   @ExcelProperty(
      value = {"磁盘总使用率%"},
      index = 5
   )
   @ColumnWidth(18)
   private Double diskPer;
   @ExcelProperty(
      value = {"cpu核数"},
      index = 6
   )
   @ColumnWidth(18)
   private String cpuCoreNum;
   @ExcelProperty(
      value = {"内存总大小"},
      index = 7
   )
   @ColumnWidth(18)
   private String totalMem;
   @ExcelProperty(
      value = {"下行传输速率"},
      index = 8
   )
   @ColumnWidth(18)
   private String rxbyt;
   @ExcelProperty(
      value = {"上行传输速率"},
      index = 9
   )
   @ColumnWidth(18)
   private String txbyt;
   @ExcelProperty(
      value = {"5分钟系统负载"},
      index = 10
   )
   @ColumnWidth(18)
   private Double fiveLoad;
   @ExcelProperty(
      value = {"15分钟系统负载"},
      index = 11
   )
   @ColumnWidth(20)
   private Double fifteenLoad;
   @ExcelProperty(
      value = {"连接数量"},
      index = 12
   )
   @ColumnWidth(18)
   private String netConnections;
   @ExcelProperty(
      value = {"标签"},
      index = 13
   )
   @ColumnWidth(18)
   private String groupId;
   @ExcelProperty(
      value = {"告警次数"},
      index = 14
   )
   @ColumnWidth(18)
   private Integer warnCount;
   @ExcelProperty(
      value = {"上报数据频率（秒）"},
      index = 15
   )
   @ColumnWidth(22)
   private String submitSeconds;
   @ExcelProperty(
      value = {"累计接收流量G"},
      index = 16
   )
   @ColumnWidth(18)
   private String bytesRecv;
   @ExcelProperty(
      value = {"累计发送流量G"},
      index = 17
   )
   @ColumnWidth(18)
   private String bytesSent;
   @ExcelProperty(
      value = {"系统描述"},
      index = 18
   )
   @ColumnWidth(18)
   private String platForm;
   @ExcelProperty(
      value = {"系统版本"},
      index = 19
   )
   @ColumnWidth(18)
   private String platformVersion;
   @ExcelProperty(
      value = {"运行时间"},
      index = 20
   )
   @ColumnWidth(18)
   private String uptimeStr;
   @ExcelProperty(
      value = {"启动时间"},
      index = 21
   )
   @ColumnWidth(18)
   private String bootTimeStr;
   @ExcelProperty(
      value = {"运行进程数量"},
      index = 22
   )
   @ColumnWidth(18)
   private String procs;
   @ExcelProperty(
      value = {"CPU型号信息"},
      index = 23
   )
   @ColumnWidth(18)
   private String cpuXh;
   @ExcelProperty(
      value = {"主机状态"},
      index = 24
   )
   @ColumnWidth(18)
   private String state;
   @ExcelProperty(
      value = {"agent版本"},
      index = 25
   )
   @ColumnWidth(18)
   private String agentVer;
   @ExcelProperty(
      value = {"交换区内存总大小"},
      index = 26
   )
   @ColumnWidth(18)
   private String totalSwapMem;
   @ExcelProperty(
      value = {"交换区内存使用率%"},
      index = 27
   )
   @ColumnWidth(18)
   private String swapMemPer;
   @ExcelProperty(
      value = {"更新时间"},
      index = 28
   )
   @ColumnWidth(20)
   private String createTime;

   public String getHostname() {
      return this.hostname;
   }

   public void setHostname(String hostname) {
      this.hostname = hostname;
   }

   public Double getMemPer() {
      return this.memPer;
   }

   public void setMemPer(Double memPer) {
      this.memPer = memPer;
   }

   public Double getCpuPer() {
      return this.cpuPer;
   }

   public void setCpuPer(Double cpuPer) {
      this.cpuPer = cpuPer;
   }

   public Double getDiskPer() {
      return this.diskPer;
   }

   public void setDiskPer(Double diskPer) {
      this.diskPer = diskPer;
   }

   public String getCpuCoreNum() {
      return this.cpuCoreNum;
   }

   public void setCpuCoreNum(String cpuCoreNum) {
      this.cpuCoreNum = cpuCoreNum;
   }

   public String getTotalMem() {
      return this.totalMem;
   }

   public void setTotalMem(String totalMem) {
      this.totalMem = totalMem;
   }

   public String getSubmitSeconds() {
      return this.submitSeconds;
   }

   public void setSubmitSeconds(String submitSeconds) {
      this.submitSeconds = submitSeconds;
   }

   public String getBytesRecv() {
      return this.bytesRecv;
   }

   public void setBytesRecv(String bytesRecv) {
      this.bytesRecv = bytesRecv;
   }

   public String getBytesSent() {
      return this.bytesSent;
   }

   public void setBytesSent(String bytesSent) {
      this.bytesSent = bytesSent;
   }

   public String getRxbyt() {
      return this.rxbyt;
   }

   public void setRxbyt(String rxbyt) {
      this.rxbyt = rxbyt;
   }

   public String getTxbyt() {
      return this.txbyt;
   }

   public void setTxbyt(String txbyt) {
      this.txbyt = txbyt;
   }

   public String getHostnameExt() {
      return this.hostnameExt;
   }

   public void setHostnameExt(String hostnameExt) {
      this.hostnameExt = hostnameExt;
   }

   public Double getFiveLoad() {
      return this.fiveLoad;
   }

   public void setFiveLoad(Double fiveLoad) {
      this.fiveLoad = fiveLoad;
   }

   public Double getFifteenLoad() {
      return this.fifteenLoad;
   }

   public void setFifteenLoad(Double fifteenLoad) {
      this.fifteenLoad = fifteenLoad;
   }

   public String getNetConnections() {
      return this.netConnections;
   }

   public void setNetConnections(String netConnections) {
      this.netConnections = netConnections;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public Integer getWarnCount() {
      return this.warnCount;
   }

   public void setWarnCount(Integer warnCount) {
      this.warnCount = warnCount;
   }

   public String getPlatForm() {
      return this.platForm;
   }

   public void setPlatForm(String platForm) {
      this.platForm = platForm;
   }

   public String getPlatformVersion() {
      return this.platformVersion;
   }

   public void setPlatformVersion(String platformVersion) {
      this.platformVersion = platformVersion;
   }

   public String getUptimeStr() {
      return this.uptimeStr;
   }

   public void setUptimeStr(String uptimeStr) {
      this.uptimeStr = uptimeStr;
   }

   public String getBootTimeStr() {
      return this.bootTimeStr;
   }

   public void setBootTimeStr(String bootTimeStr) {
      this.bootTimeStr = bootTimeStr;
   }

   public String getProcs() {
      return this.procs;
   }

   public void setProcs(String procs) {
      this.procs = procs;
   }

   public String getCpuXh() {
      return this.cpuXh;
   }

   public void setCpuXh(String cpuXh) {
      this.cpuXh = cpuXh;
   }

   public String getState() {
      return this.state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public String getAgentVer() {
      return this.agentVer;
   }

   public void setAgentVer(String agentVer) {
      this.agentVer = agentVer;
   }

   public String getRemark() {
      return this.remark;
   }

   public void setRemark(String remark) {
      this.remark = remark;
   }

   public String getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(String createTime) {
      this.createTime = createTime;
   }

   public String getTotalSwapMem() {
      return this.totalSwapMem;
   }

   public void setTotalSwapMem(String totalSwapMem) {
      this.totalSwapMem = totalSwapMem;
   }

   public String getSwapMemPer() {
      return this.swapMemPer;
   }

   public void setSwapMemPer(String swapMemPer) {
      this.swapMemPer = swapMemPer;
   }
}
