package com.wgcloud.entity;

import java.util.Date;

public class DockerInfo extends BaseEntity {
   private static final long serialVersionUID = 1L;
   private String hostname;
   private String appType;
   private String dockerId;
   private String dockerName;
   private Double memPer;
   private String dockerState;
   private String userTime;
   private String state;
   private String active;
   private Integer warnCount;
   private String warnQueryWd;
   private String account;
   private Date createTime;
   private String dockerImage;
   private String dockerPort;
   private String dockerCommand;
   private String dockerCreated;
   private String dockerSize;
   private String dockerStatus;
   private String gatherDockerNames;
   private String groupId;

   public Date getCreateTime() {
      return this.createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

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

   public String getState() {
      return this.state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public String getDockerId() {
      return this.dockerId;
   }

   public void setDockerId(String dockerId) {
      this.dockerId = dockerId;
   }

   public String getDockerName() {
      return this.dockerName;
   }

   public void setDockerName(String dockerName) {
      this.dockerName = dockerName;
   }

   public String getDockerState() {
      return this.dockerState;
   }

   public void setDockerState(String dockerState) {
      this.dockerState = dockerState;
   }

   public String getUserTime() {
      return this.userTime;
   }

   public void setUserTime(String userTime) {
      this.userTime = userTime;
   }

   public String getActive() {
      return this.active;
   }

   public void setActive(String active) {
      this.active = active;
   }

   public Integer getWarnCount() {
      return this.warnCount;
   }

   public void setWarnCount(Integer warnCount) {
      this.warnCount = warnCount;
   }

   public String getAppType() {
      return this.appType;
   }

   public void setAppType(String appType) {
      this.appType = appType;
   }

   public String getDockerImage() {
      return this.dockerImage;
   }

   public void setDockerImage(String dockerImage) {
      this.dockerImage = dockerImage;
   }

   public String getDockerPort() {
      return this.dockerPort;
   }

   public void setDockerPort(String dockerPort) {
      this.dockerPort = dockerPort;
   }

   public String getDockerCommand() {
      return this.dockerCommand;
   }

   public void setDockerCommand(String dockerCommand) {
      this.dockerCommand = dockerCommand;
   }

   public String getDockerCreated() {
      return this.dockerCreated;
   }

   public void setDockerCreated(String dockerCreated) {
      this.dockerCreated = dockerCreated;
   }

   public String getDockerSize() {
      return this.dockerSize;
   }

   public void setDockerSize(String dockerSize) {
      this.dockerSize = dockerSize;
   }

   public String getDockerStatus() {
      return this.dockerStatus;
   }

   public void setDockerStatus(String dockerStatus) {
      this.dockerStatus = dockerStatus;
   }

   public String getWarnQueryWd() {
      return this.warnQueryWd;
   }

   public void setWarnQueryWd(String warnQueryWd) {
      this.warnQueryWd = warnQueryWd;
   }

   public String getGatherDockerNames() {
      return this.gatherDockerNames;
   }

   public void setGatherDockerNames(String gatherDockerNames) {
      this.gatherDockerNames = gatherDockerNames;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public String getAccount() {
      return this.account;
   }

   public void setAccount(String account) {
      this.account = account;
   }
}
