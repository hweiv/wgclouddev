package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.entity.DeskIo;
import com.wgcloud.entity.DiskSmart;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.FileSafe;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.FileSafeService;
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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/agentDiskGo"})
public class AgentDiskController {
   private static final Logger logger = LoggerFactory.getLogger(AgentDiskController.class);
   @Autowired
   private DockerInfoService dockerInfoService;
   @Autowired
   private AppInfoService appInfoService;
   @Autowired
   private FileSafeService fileSafeService;
   @Autowired
   private TokenUtils tokenUtils;

   @ResponseBody
   @RequestMapping({"/minTask"})
   public JSONObject minTask(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      logger.debug("agent上报磁盘数据-------------" + agentJsonObject.toString());
      JSONObject resultJson = new JSONObject();
      if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
         logger.error("token is error");
         resultJson.set("result", "token is error");
         return resultJson;
      } else {
         String bindIp = agentJsonObject.getStr("bindIp");
         if (this.isExists(bindIp)) {
            logger.error("agentDisk multiple times at the same time：" + bindIp);
            resultJson.set("result", "agentDisk multiple times at the same time：" + bindIp);
            return resultJson;
         } else {
            JSONObject diskInfoListJson = agentJsonObject.getJSONObject("diskInfoList");
            JSONObject diskIoJson = agentJsonObject.getJSONObject("diskIo");
            JSONObject diskSmartJson = agentJsonObject.getJSONObject("diskSmart");
            JSONObject fileSafesJson = agentJsonObject.getJSONObject("fileSafes");
            String hostName = agentJsonObject.getStr("hostName");
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
               if (diskInfoListJson != null) {
                  this.addDeskState(diskInfoListJson, nowtime, bindIp);
               }

               if (diskIoJson != null) {
                  this.addDeskIo(diskIoJson, nowtime, bindIp);
               }

               if (diskSmartJson != null) {
                  this.addDiskSmart(diskSmartJson, nowtime, bindIp);
               }

               if (fileSafesJson != null) {
                  this.addFileSafes(fileSafesJson, nowtime, bindIp);
               }

               resultJson.set("result", "success");
            } catch (Exception var12) {
               logger.error("解析磁盘上报数据错误", var12);
               resultJson.set("result", "error：" + var12.toString());
            }

            return resultJson;
         }
      }
   }

   private void addDeskState(JSONObject diskInfoListJson, Date nowtime, String bindIp) {
      try {
         List<String> keys = new ArrayList(diskInfoListJson.keySet());
         Iterator var5 = keys.iterator();

         while(var5.hasNext()) {
            String diskname = (String)var5.next();
            JSONObject diskJson = diskInfoListJson.getJSONObject(diskname);
            DiskState bean = new DiskState();
            bean.setFileSystem(diskname);
            bean.setHostname(bindIp);
            bean.setCreateTime(nowtime);
            bean.setUsed(FormatUtil.formatDouble((Double)((double)diskJson.getLong("used") / 1024.0D / 1024.0D / 1024.0D), 2) + "G");
            bean.setAvail(FormatUtil.formatDouble((Double)((double)diskJson.getLong("free") / 1024.0D / 1024.0D / 1024.0D), 2) + "G");
            bean.setDiskSize(FormatUtil.formatDouble((Double)((double)diskJson.getLong("total") / 1024.0D / 1024.0D / 1024.0D), 2) + "G");
            bean.setUsePer(FormatUtil.formatDouble((Double)diskJson.getDouble("usedPercent"), 2) + "%");
            BatchData.DISK_STATE_LIST.add(bean);
            Runnable runnable = () -> {
               WarnMailUtil.sendDiskWarnInfo(bean);
            };
            ThreadPoolUtil.executor.execute(runnable);
         }
      } catch (Exception var10) {
         logger.error("解析磁盘上报数据错误", var10);
      }

   }

   private void addDeskIo(JSONObject diskIoListJson, Date nowtime, String bindIp) {
      try {
         List<String> keys = new ArrayList(diskIoListJson.keySet());
         Iterator var5 = keys.iterator();

         while(var5.hasNext()) {
            String diskname = (String)var5.next();
            DeskIo ioBean = new DeskIo();
            JSONObject deskIoJsonObj = diskIoListJson.getJSONObject(diskname);
            if (null != deskIoJsonObj) {
               ioBean.setFileSystem(diskname);
               ioBean.setHostname(bindIp);
               ioBean.setCreateTime(nowtime);
               ioBean.setReadCount(deskIoJsonObj.getLong("readCount") + "");
               ioBean.setWriteCount(deskIoJsonObj.getLong("writeCount") + "");
               ioBean.setReadBytes(deskIoJsonObj.getLong("readBytes") / 1024L / 1024L / 1024L + "G");
               ioBean.setWriteBytes(deskIoJsonObj.getLong("writeBytes") / 1024L / 1024L / 1024L + "G");
               ioBean.setReadTime(deskIoJsonObj.getLong("readTime") + "");
               ioBean.setWriteTime(deskIoJsonObj.getLong("writeTime") + "");
               BatchData.DESK_IO_LIST.add(ioBean);
            }
         }
      } catch (Exception var9) {
         logger.error("解析磁盘IO数据错误", var9);
      }

   }

   private void addDiskSmart(JSONObject diskSmartListJson, Date nowtime, String bindIp) {
      List<String> keys = new ArrayList(diskSmartListJson.keySet());
      Iterator var5 = keys.iterator();

      while(var5.hasNext()) {
         String diskname = (String)var5.next();

         try {
            DiskSmart smartBean = new DiskSmart();
            smartBean.setFileSystem(diskname);
            smartBean.setHostname(bindIp);
            smartBean.setCreateTime(nowtime);
            String[] smartStrs = diskSmartListJson.getStr(diskname).split(",");
            smartBean.setDiskState(smartStrs[0]);
            smartBean.setPowerHours(smartStrs[1]);
            smartBean.setPowerCount(smartStrs[2]);
            smartBean.setTemperature(smartStrs[3]);
            BatchData.DISK_SMART_LIST.add(smartBean);
            Runnable runnable = () -> {
               WarnMailUtil.sendDiskSmartWarnInfo(smartBean);
            };
            ThreadPoolUtil.executor.execute(runnable);
         } catch (Exception var10) {
            logger.error("解析磁盘smart数据错误", var10);
         }
      }

   }

   private void addFileSafes(JSONObject fileSafeJson, Date nowtime, String bindIp) {
      List<String> keys = new ArrayList(fileSafeJson.keySet());
      Iterator var5 = keys.iterator();

      while(var5.hasNext()) {
         String fileSafeId = (String)var5.next();

         try {
            String fileState = fileSafeJson.getStr(fileSafeId);
            String state = fileState.split(",")[0];
            String fileModtime = fileState.split(",")[1];
            FileSafe fileSafe = new FileSafe();
            fileSafe.setId(fileSafeId);
            fileSafe.setCreateTime(nowtime);
            fileSafe.setState(state);
            fileSafe.setFileModtime(DateUtil.secondToDate(Long.valueOf(fileModtime), "yyyy-MM-dd HH:mm:ss"));
            fileSafe.setHostname(bindIp);
            if (!"1".equals(state)) {
               fileSafe.setState("2");
               fileSafe.setCreateTime((Date)null);
               BatchData.FILE_SAFE_LIST.add(fileSafe);
               Runnable runnable = () -> {
                  try {
                     FileSafe portInfoW = this.fileSafeService.selectById(fileSafeId);
                     if (portInfoW != null) {
                        WarnMailUtil.sendFileSafeDown(portInfoW, true);
                     }
                  } catch (Exception var3) {
                     var3.printStackTrace();
                  }

               };
               ThreadPoolUtil.executor.execute(runnable);
            } else {
               BatchData.FILE_SAFE_LIST.add(fileSafe);
            }
         } catch (Exception var12) {
            logger.error("解析文件防篡改监测信息上报数据错误", var12);
         }
      }

   }

   private boolean isExists(String bindIp) {
      if (StringUtils.isEmpty(bindIp)) {
         return true;
      } else {
         Iterator var2 = BatchData.DISK_STATE_LIST.iterator();

         DiskState deskState;
         do {
            if (!var2.hasNext()) {
               var2 = BatchData.DESK_IO_LIST.iterator();

               DeskIo deskIo;
               do {
                  if (!var2.hasNext()) {
                     var2 = BatchData.DISK_SMART_LIST.iterator();

                     DiskSmart diskSmart;
                     do {
                        if (!var2.hasNext()) {
                           return false;
                        }

                        diskSmart = (DiskSmart)var2.next();
                     } while(!diskSmart.getHostname().equals(bindIp));

                     return true;
                  }

                  deskIo = (DeskIo)var2.next();
               } while(!deskIo.getHostname().equals(bindIp));

               return true;
            }

            deskState = (DiskState)var2.next();
         } while(!deskState.getHostname().equals(bindIp));

         return true;
      }
   }
}
