package com.wgcloud.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.AppState;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.entity.DockerInfo;
import com.wgcloud.entity.DockerState;
import com.wgcloud.entity.MemState;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.entity.PortInfo;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnMailUtil;
import com.wgcloud.util.staticvar.BatchData;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/agentGo"})
public class AgentGoController {
   private static final Logger logger = LoggerFactory.getLogger(AgentGoController.class);
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private SystemInfoService systemInfoService;
   @Autowired
   private DockerInfoService dockerInfoService;
   @Autowired
   private AppInfoService appInfoService;
   @Autowired
   private PortInfoService portInfoService;
   @Autowired
   private TokenUtils tokenUtils;

   @ResponseBody
   @RequestMapping({"/minTask"})
   public JSONObject minTask(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      logger.debug("agent上报数据-------------" + agentJsonObject.toString());
      JSONObject resultJson = new JSONObject();
      if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
         logger.error("token is error");
         resultJson.set("result", "token is error");
         return resultJson;
      } else {
         String bindIp = agentJsonObject.getStr("bindIp");
         if (this.isExists(bindIp)) {
            logger.error("agent multiple times at the same time：" + bindIp);
            resultJson.set("result", "agent multiple times at the same time：" + bindIp);
            return resultJson;
         } else {
            String hostName = agentJsonObject.getStr("hostName");
            String recvBytes = agentJsonObject.getStr("recvBytes");
            String sentBytes = agentJsonObject.getStr("sentBytes");
            String agentVer = agentJsonObject.getStr("agentVer");
            String cpuCoresNum = agentJsonObject.getStr("cpuCoresNum");
            JSONObject hostInfoJson = agentJsonObject.getJSONObject("hostInfo");
            JSONObject dockersJson = agentJsonObject.getJSONObject("dockers");
            JSONObject portInfosJson = agentJsonObject.getJSONObject("portInfos");
            JSONObject loadJson = agentJsonObject.getJSONObject("load");
            JSONArray temperaturesJsonArray = agentJsonObject.getJSONArray("temperatures");
            String submitSeconds = agentJsonObject.getStr("submitSeconds");
            String netConnections = agentJsonObject.getStr("netConnections");
            JSONArray cpuInfoJson = this.initCpuJson(agentJsonObject);
            Double cpuPercentJson = agentJsonObject.getDouble("cpuPercent");
            JSONObject virtualMemoryJson = agentJsonObject.getJSONObject("virtualMemory");
            JSONObject swapMemoryStatJson = agentJsonObject.getJSONObject("swapMemoryStat");
            JSONObject netIOCountersJson = agentJsonObject.getJSONObject("netIOCounters");
            JSONObject processListJson = agentJsonObject.getJSONObject("processList");
            Double cpuPercentVal = 0.0D;
            Double memTotalVal = 0.0D;
            Double memPercentVal = 0.0D;
            Date nowtime = new Date();
            if (StringUtils.isEmpty(bindIp)) {
               bindIp = hostName;
               logger.error("bindIp is null");
               if (StringUtils.isEmpty(hostName)) {
                  resultJson.set("result", "error：bindIp is null");
                  return resultJson;
               }
            }

            try {
               if (cpuPercentJson != null) {
                  cpuPercentVal = FormatUtil.formatDouble((Double)cpuPercentJson, 2);
                  this.addCpuState(cpuPercentJson, nowtime, bindIp);
               }

               if (virtualMemoryJson != null) {
                  if (virtualMemoryJson.getLong("total") != null) {
                     memTotalVal = FormatUtil.formatDouble((Double)((double)virtualMemoryJson.getLong("total") / 1024.0D / 1024.0D / 1024.0D), 2);
                  } else {
                     memTotalVal = 0.0D;
                  }

                  if (virtualMemoryJson.getDouble("usedPercent") != null) {
                     memPercentVal = FormatUtil.formatDouble((Double)virtualMemoryJson.getDouble("usedPercent"), 2);
                  } else {
                     memPercentVal = 0.0D;
                  }

                  this.addMemState(memPercentVal, nowtime, bindIp);
               }

               SysLoadState sysLoadState = null;
               if (loadJson != null) {
                  sysLoadState = this.addLoadState(loadJson, nowtime, bindIp);
               }

               NetIoState netIoState = null;
               if (netIOCountersJson != null) {
                  netIoState = this.addNetIoState(netIOCountersJson, nowtime, bindIp, netConnections);
               }

               if (processListJson != null) {
                  this.addAppState(processListJson, nowtime, bindIp);
               }

               if (dockersJson != null) {
                  this.addDockerState(dockersJson, nowtime, bindIp);
               }

               if (hostInfoJson != null && cpuInfoJson != null) {
                  this.addSystemInfo(hostInfoJson, cpuInfoJson.getJSONObject(0), cpuPercentVal, memPercentVal, memTotalVal, nowtime, bindIp, submitSeconds, agentVer, recvBytes, sentBytes, netIoState, cpuCoresNum, hostName, sysLoadState, netConnections, swapMemoryStatJson);
               }

               if (portInfosJson != null) {
                  this.addPortInfos(portInfosJson, nowtime, bindIp);
               }

               if (temperaturesJsonArray != null) {
                  this.addCpuTemperatures(temperaturesJsonArray, nowtime, bindIp);
               }

               resultJson.set("result", "success");
            } catch (Exception var29) {
               logger.error("解析上报数据错误", var29);
               resultJson.set("result", "error：" + var29.toString());
            }

            return resultJson;
         }
      }
   }

   private JSONArray initCpuJson(JSONObject agentJsonObject) {
      JSONArray cpuInfoJson = null;

      try {
         cpuInfoJson = agentJsonObject.getJSONArray("cpuInfo");
         if (cpuInfoJson.size() < 1) {
            JSONObject cpuInfoJsonTmp = new JSONObject();
            cpuInfoJsonTmp.set("cores", 0);
            cpuInfoJsonTmp.set("modelName", "?");
            cpuInfoJson = new JSONArray();
            cpuInfoJson.add(cpuInfoJsonTmp);
         }
      } catch (Exception var5) {
         logger.error("cpuInfoJson is error : ", var5);
         JSONObject cpuInfoJsonTmp = new JSONObject();
         cpuInfoJsonTmp.set("cores", 0);
         cpuInfoJsonTmp.set("modelName", "?");
         cpuInfoJson = new JSONArray();
         cpuInfoJson.add(cpuInfoJsonTmp);
      }

      return cpuInfoJson;
   }

   private void addMemState(Double memPercentJson, Date nowtime, String bindIp) {
      try {
         MemState bean = new MemState();
         bean.setUsePer(memPercentJson);
         bean.setCreateTime(nowtime);
         bean.setHostname(bindIp);
         BatchData.MEM_STATE_LIST.add(bean);
         Runnable runnable = () -> {
            WarnMailUtil.sendWarnInfo(bean);
         };
         ThreadPoolUtil.executor.execute(runnable);
      } catch (Exception var6) {
         logger.error("解析内存使用率上报数据错误", var6);
      }

   }

   private void addCpuState(Double cpuPercentJson, Date nowtime, String bindIp) {
      try {
         CpuState bean = new CpuState();
         bean.setHostname(bindIp);
         Double cpuPercentVal = FormatUtil.formatDouble((Double)cpuPercentJson, 2);
         bean.setSys(cpuPercentVal);
         bean.setCreateTime(nowtime);
         BatchData.CPU_STATE_LIST.add(bean);
         Runnable runnable = () -> {
            WarnMailUtil.sendCpuWarnInfo(bean);
         };
         ThreadPoolUtil.executor.execute(runnable);
      } catch (Exception var7) {
         logger.error("解析cpu使用率上报数据错误", var7);
      }

   }

   private SysLoadState addLoadState(JSONObject loadJson, Date nowtime, String bindIp) {
      SysLoadState bean = new SysLoadState();

      try {
         bean.setHostname(bindIp);
         bean.setOneLoad(FormatUtil.formatDouble((Double)loadJson.getDouble("load1"), 2));
         bean.setFiveLoad(FormatUtil.formatDouble((Double)loadJson.getDouble("load5"), 2));
         bean.setFifteenLoad(FormatUtil.formatDouble((Double)loadJson.getDouble("load15"), 2));
         bean.setCreateTime(nowtime);
         BatchData.SYSLOAD_STATE_LIST.add(bean);
         Runnable runnable = () -> {
            WarnMailUtil.sendSysLoadWarnInfo(bean);
         };
         ThreadPoolUtil.executor.execute(runnable);
      } catch (Exception var6) {
         logger.error("解析系统负载上报数据错误", var6);
      }

      return bean;
   }

   private NetIoState addNetIoState(JSONObject netIOCountersJson, Date nowtime, String bindIp, String netConnections) {
      NetIoState bean = new NetIoState();

      try {
         bean.setNetConnections(netConnections);
         if (netIOCountersJson.getDouble("res_recv_bytes") != null) {
            bean.setRxbyt(FormatUtil.formatDouble((Double)netIOCountersJson.getDouble("res_recv_bytes"), 2) + "");
         } else {
            bean.setRxbyt("0");
         }

         if (netIOCountersJson.getDouble("res_sent_bytes") != null) {
            bean.setTxbyt(FormatUtil.formatDouble((Double)netIOCountersJson.getDouble("res_sent_bytes"), 2) + "");
         } else {
            bean.setTxbyt("0");
         }

         if (netIOCountersJson.getDouble("res_sent_packets") != null) {
            bean.setTxpck(FormatUtil.formatDouble((Double)netIOCountersJson.getDouble("res_sent_packets"), 2) + "");
         } else {
            bean.setTxpck("0");
         }

         if (netIOCountersJson.getDouble("res_recv_packets") != null) {
            bean.setRxpck(FormatUtil.formatDouble((Double)netIOCountersJson.getDouble("res_recv_packets"), 2) + "");
         } else {
            bean.setRxpck("0");
         }

         if (netIOCountersJson.getDouble("res_dropin_packets") != null) {
            bean.setDropin(FormatUtil.formatDouble((Double)netIOCountersJson.getDouble("res_dropin_packets"), 2) + "");
         } else {
            bean.setDropin("0");
         }

         if (netIOCountersJson.getDouble("res_dropout_packets") != null) {
            bean.setDropout(FormatUtil.formatDouble((Double)netIOCountersJson.getDouble("res_dropout_packets"), 2) + "");
         } else {
            bean.setDropout("0");
         }

         bean.setCreateTime(nowtime);
         bean.setHostname(bindIp);
         BatchData.NETIO_STATE_LIST.add(bean);
         Runnable runnable = () -> {
            try {
               if (bean != null) {
                  WarnMailUtil.sendDownSpeedWarnInfo(bean);
                  WarnMailUtil.sendUpSpeedWarnInfo(bean);
               }
            } catch (Exception var2) {
               var2.printStackTrace();
            }

         };
         ThreadPoolUtil.executor.execute(runnable);
      } catch (Exception var7) {
         logger.error("解析网络流量上报数据错误", var7);
      }

      return bean;
   }

   private void addAppState(JSONObject processListJson, Date nowtime, String bindIp) {
      List<String> keys = new ArrayList(processListJson.keySet());
      Iterator var5 = keys.iterator();

      while(var5.hasNext()) {
         String proId = (String)var5.next();

         try {
            String[] vals = processListJson.getStr(proId).split(",");
            AppInfo appInfo = new AppInfo();
            appInfo.setHostname(bindIp);
            appInfo.setId(proId);
            appInfo.setCreateTime(nowtime);
            appInfo.setState("1");
            appInfo.setMemPer(FormatUtil.formatDouble((Double)Double.valueOf(vals[1]), 2));
            appInfo.setCpuPer(FormatUtil.formatDouble((Double)Double.valueOf(vals[0]), 2));
            appInfo.setReadBytes(FormatUtil.formatDouble((Double)Double.valueOf(vals[2]), 2) + "");
            appInfo.setWritesBytes(FormatUtil.formatDouble((Double)Double.valueOf(vals[3]), 2) + "");
            appInfo.setThreadsNum(vals[4]);
            appInfo.setGatherPid(vals[5]);
            String appTimes = DateUtil.millisToDate(vals[6], "yyyy-MM-dd HH:mm:ss");
            appInfo.setAppTimes(appTimes);
            if (appInfo.getCpuPer() < 0.0D) {
               appInfo.setState("2");
               appInfo.setCreateTime((Date)null);
               appInfo.setAppTimes((String)null);
               BatchData.APP_INFO_LIST.add(appInfo);
               Runnable runnable = () -> {
                  try {
                     AppInfo appInfoW = this.appInfoService.selectById(proId);
                     if (appInfoW != null) {
                        WarnMailUtil.sendAppDown(appInfoW, true);
                     }
                  } catch (Exception var3) {
                     var3.printStackTrace();
                  }

               };
               ThreadPoolUtil.executor.execute(runnable);
            } else {
               BatchData.APP_INFO_LIST.add(appInfo);
               AppState appState = new AppState();
               appState.setAppInfoId(proId);
               appState.setCreateTime(nowtime);
               appState.setCpuPer(FormatUtil.formatDouble((Double)Double.valueOf(vals[0]), 2));
               appState.setMemPer(FormatUtil.formatDouble((Double)Double.valueOf(vals[1]), 2));
               appState.setThreadsNum(vals[4]);
               BatchData.APP_STATE_LIST.add(appState);
            }
         } catch (Exception var11) {
            logger.error("解析进程上报数据错误", var11);
         }
      }

   }

   private void addDockerState(JSONObject dockersJson, Date nowtime, String bindIp) {
      List<String> keys = new ArrayList(dockersJson.keySet());
      Iterator var5 = keys.iterator();

      while(var5.hasNext()) {
         String dockerId = (String)var5.next();

         try {
            JSONObject valJson = dockersJson.getJSONObject(dockerId);
            DockerInfo dockerInfo = new DockerInfo();
            dockerInfo.setId(dockerId);
            dockerInfo.setCreateTime(nowtime);
            dockerInfo.setState("1");
            dockerInfo.setHostname(bindIp);
            dockerInfo.setMemPer(FormatUtil.formatDouble((Double)valJson.getDouble("memToM"), 2));
            dockerInfo.setDockerCommand(valJson.getStr("command"));
            dockerInfo.setDockerImage(valJson.getStr("image"));
            dockerInfo.setDockerCreated(valJson.getStr("created"));
            dockerInfo.setDockerPort(valJson.getStr("portStr"));
            dockerInfo.setDockerSize(FormatUtil.formatDouble((Double)valJson.getDouble("sizeRootFs"), 2) + "");
            dockerInfo.setDockerStatus(valJson.getStr("status"));
            dockerInfo.setGatherDockerNames(valJson.getStr("names"));
            dockerInfo.setDockerState(valJson.getStr("containerState"));
            if ("exited".equals(FormatUtil.toLowerStr(dockerInfo.getDockerState()))) {
               dockerInfo.setState("2");
               dockerInfo.setCreateTime((Date)null);
               BatchData.DOCKER_INFO_LIST.add(dockerInfo);
               Runnable runnable = () -> {
                  try {
                     DockerInfo dockerInfoW = this.dockerInfoService.selectById(dockerId);
                     if (dockerInfoW != null) {
                        WarnMailUtil.sendDockerDown(dockerInfoW, true);
                     }
                  } catch (Exception var3) {
                     var3.printStackTrace();
                  }

               };
               ThreadPoolUtil.executor.execute(runnable);
            } else {
               BatchData.DOCKER_INFO_LIST.add(dockerInfo);
               DockerState dockerState = new DockerState();
               dockerState.setDockerInfoId(dockerId);
               dockerState.setCreateTime(nowtime);
               dockerState.setMemPer(dockerInfo.getMemPer());
               BatchData.DOCKER_STATE_LIST.add(dockerState);
            }
         } catch (Exception var10) {
            logger.error("解析docker上报数据错误", var10);
         }
      }

   }

   private void addSystemInfo(JSONObject hostInfoJson, JSONObject cpuInfoJson, Double cpuPercentVal, Double memPercentVal, Double memTotalVal, Date nowtime, String bindIp, String submitSeconds, String agentVer, String recvBytes, String sentBytes, NetIoState netIoState, String cpuCoresNum, String hostNameExt, SysLoadState sysLoadState, String netConnections, JSONObject swapMemoryStatJson) throws Exception {
      SystemInfo bean = new SystemInfo();
      bean.setSubmitSeconds(submitSeconds);
      bean.setAgentVer(agentVer);
      bean.setState("1");
      bean.setHostname(bindIp);
      bean.setNetConnections(netConnections);
      bean.setHostnameExt(hostNameExt);
      bean.setCreateTime(nowtime);
      bean.setBootTime(hostInfoJson.getLong("bootTime"));
      bean.setUptime(hostInfoJson.getLong("uptime"));
      bean.setCpuPer(cpuPercentVal);
      bean.setMemPer(memPercentVal);
      bean.setProcs(hostInfoJson.getStr("procs"));
      bean.setCpuCoreNum(cpuCoresNum);
      bean.setCpuXh(cpuInfoJson.getStr("modelName"));
      bean.setPlatForm(hostInfoJson.getStr("platform"));
      bean.setPlatformVersion(hostInfoJson.getStr("platformVersion") + "，kernelArch：" + hostInfoJson.getStr("kernelArch"));
      bean.setTotalMem(memTotalVal + "G");
      if (null == netIoState) {
         netIoState = new NetIoState();
      }

      bean.setRxbyt(netIoState.getRxbyt());
      bean.setTxbyt(netIoState.getTxbyt());
      if (null == sysLoadState) {
         sysLoadState = new SysLoadState();
      }

      bean.setFiveLoad(sysLoadState.getFiveLoad());
      bean.setFifteenLoad(sysLoadState.getFifteenLoad());
      if (!StringUtils.isEmpty(recvBytes)) {
         bean.setBytesRecv(FormatUtil.formatDouble((Double)Double.valueOf(recvBytes), 2) + "");
      } else {
         bean.setBytesRecv("0");
      }

      if (!StringUtils.isEmpty(sentBytes)) {
         bean.setBytesSent(FormatUtil.formatDouble((Double)Double.valueOf(sentBytes), 2) + "");
      } else {
         bean.setBytesSent("0");
      }

      this.setSwapMemInfo(bean, swapMemoryStatJson);
      BatchData.SYSTEM_INFO_LIST.add(bean);
   }

   private void addCpuTemperatures(JSONArray temperaturesJsonArray, Date nowtime, String bindIp) {
      try {
         logger.debug("addCpuTemperatures--------------" + temperaturesJsonArray.toString());
         Iterator var4 = temperaturesJsonArray.iterator();

         while(var4.hasNext()) {
            Object temperaturesObj = var4.next();
            CpuTemperatures cpuTemperatures = new CpuTemperatures();
            cpuTemperatures.setHostname(bindIp);
            cpuTemperatures.setCreateTime(nowtime);
            JSONObject temperaturesJson = JSONUtil.parseObj(temperaturesObj);
            String sensor = temperaturesJson.getStr("sensorKey");
            String sensorTemperature = temperaturesJson.getStr("temperature");
            String sensorHigh = temperaturesJson.getStr("sensorCritical");
            String sensorCritical = temperaturesJson.getStr("sensorCritical");
            cpuTemperatures.setCore_index(sensor);
            cpuTemperatures.setCrit(sensorCritical);
            cpuTemperatures.setInput(sensorTemperature);
            cpuTemperatures.setMax(sensorHigh);
            BatchData.CPU_TEMPERATURES_LIST.add(cpuTemperatures);
            Runnable runnable = () -> {
               WarnMailUtil.sendCpuTemperatures(cpuTemperatures);
            };
            ThreadPoolUtil.executor.execute(runnable);
         }
      } catch (Exception var13) {
         logger.error("解析cpu温度上报数据错误", var13);
      }

   }

   private void addPortInfos(JSONObject portInfoJson, Date nowtime, String bindIp) {
      List<String> keys = new ArrayList(portInfoJson.keySet());
      Iterator var5 = keys.iterator();

      while(var5.hasNext()) {
         String portId = (String)var5.next();

         try {
            String state = portInfoJson.getStr(portId);
            PortInfo portInfo = new PortInfo();
            portInfo.setId(portId);
            portInfo.setCreateTime(nowtime);
            portInfo.setState(state);
            portInfo.setHostname(bindIp);
            if (!"1".equals(state)) {
               portInfo.setState("2");
               portInfo.setCreateTime((Date)null);
               BatchData.PORT_INFO_LIST.add(portInfo);
               Runnable runnable = () -> {
                  try {
                     PortInfo portInfoW = this.portInfoService.selectById(portId);
                     if (portInfoW != null) {
                        WarnMailUtil.sendPortDown(portInfoW, true);
                     }
                  } catch (Exception var3) {
                     var3.printStackTrace();
                  }

               };
               ThreadPoolUtil.executor.execute(runnable);
            } else {
               BatchData.PORT_INFO_LIST.add(portInfo);
            }
         } catch (Exception var10) {
            logger.error("解析端口上报数据错误", var10);
         }
      }

   }

   private void setSwapMemInfo(SystemInfo bean, JSONObject swapMemoryStatJson) {
      try {
         Double memTotalVal = 0.0D;
         Double memPercentVal = 0.0D;
         if (swapMemoryStatJson != null) {
            if (swapMemoryStatJson.getLong("total") != null) {
               memTotalVal = FormatUtil.formatDouble((Double)((double)swapMemoryStatJson.getLong("total") / 1024.0D / 1024.0D / 1024.0D), 2);
            }

            if (swapMemoryStatJson.getDouble("usedPercent") != null) {
               memPercentVal = FormatUtil.formatDouble((Double)swapMemoryStatJson.getDouble("usedPercent"), 2);
            }

            bean.setSwapMemPer(String.valueOf(memPercentVal));
            bean.setTotalSwapMem(memTotalVal + "G");
         }
      } catch (Exception var5) {
         logger.error("解析交换区内存信息数据错误", var5);
      }

   }

   private boolean isExists(String bindIp) {
      if (StringUtils.isEmpty(bindIp)) {
         return true;
      } else {
         Iterator var2 = BatchData.SYSTEM_INFO_LIST.iterator();

         SystemInfo systemInfo;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            systemInfo = (SystemInfo)var2.next();
         } while(!systemInfo.getHostname().equals(bindIp));

         return true;
      }
   }
}
