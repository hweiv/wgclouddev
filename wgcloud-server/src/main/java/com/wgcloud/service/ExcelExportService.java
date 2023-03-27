package com.wgcloud.service;

import com.alibaba.excel.EasyExcel;
import com.wgcloud.dto.AppExcelChartDto;
import com.wgcloud.dto.CustomExcelChartDto;
import com.wgcloud.dto.DbTableExcelChartDto;
import com.wgcloud.dto.DceExcelChartDto;
import com.wgcloud.dto.DockerExcelChartDto;
import com.wgcloud.dto.ExcelChartDto;
import com.wgcloud.dto.FileWarnStateExcelDto;
import com.wgcloud.dto.HeathExcelChartDto;
import com.wgcloud.dto.HostListExcelDto;
import com.wgcloud.dto.SnmpExcelChartDto;
import com.wgcloud.entity.AppState;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.CustomState;
import com.wgcloud.entity.DbTableCount;
import com.wgcloud.entity.DceState;
import com.wgcloud.entity.DockerState;
import com.wgcloud.entity.FileWarnInfo;
import com.wgcloud.entity.FileWarnState;
import com.wgcloud.entity.HeathState;
import com.wgcloud.entity.MemState;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.entity.SnmpState;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcelExportService {
   private static final Logger logger = LoggerFactory.getLogger(ExcelExportService.class);
   @Autowired
   CpuStateService cpuStateService;
   @Autowired
   CustomStateService customStateService;
   @Autowired
   MemStateService memStateService;
   @Autowired
   NetIoStateService netIoStateService;
   @Autowired
   SysLoadStateService sysLoadStateService;
   @Autowired
   AppStateService appStateService;
   @Autowired
   DbTableCountService dbTableCountService;
   @Autowired
   DockerStateService dockerStateService;
   @Autowired
   HeathStateService heathStateService;
   @Autowired
   DceStateService dceStateService;
   @Autowired
   SnmpStateService snmpStateService;
   @Autowired
   FileWarnStateService fileWarnStateService;

   public void exportExcel(Map<String, Object> params, HttpServletResponse response) {
      Object out = null;

      try {
         List<String> datetimeList = new ArrayList();
         List<CpuState> cpuStateList = this.cpuStateService.selectAllByParams(params);
         Map<String, CpuState> cpuStateMap = new HashMap();
         Iterator var7 = cpuStateList.iterator();

         while(var7.hasNext()) {
            CpuState cpuState = (CpuState)var7.next();
            datetimeList.add(cpuState.getDateStr());
            cpuStateMap.put(cpuState.getDateStr(), cpuState);
         }

         List<MemState> memStateList = this.memStateService.selectAllByParams(params);
         Map<String, MemState> memStateMap = new HashMap();
         Iterator var9 = memStateList.iterator();

         while(var9.hasNext()) {
            MemState memState = (MemState)var9.next();
            memStateMap.put(memState.getDateStr(), memState);
         }

         List<SysLoadState> ysLoadSstateList = this.sysLoadStateService.selectAllByParams(params);
         Map<String, SysLoadState> ysLoadSstateMap = new HashMap();
         Iterator var11 = ysLoadSstateList.iterator();

         while(var11.hasNext()) {
            SysLoadState sysLoadState = (SysLoadState)var11.next();
            ysLoadSstateMap.put(sysLoadState.getDateStr(), sysLoadState);
         }

         List<NetIoState> netIoStateList = this.netIoStateService.selectAllByParams(params);
         Map<String, NetIoState> netIoStateMap = new HashMap();
         Iterator var13 = netIoStateList.iterator();

         while(var13.hasNext()) {
            NetIoState netIoState = (NetIoState)var13.next();
            netIoStateMap.put(netIoState.getDateStr(), netIoState);
         }

         List<ExcelChartDto> excelChartList = new ArrayList();

         String datetimeStr;
         ExcelChartDto dto;
         for(Iterator var34 = datetimeList.iterator(); var34.hasNext(); excelChartList.add(dto)) {
            datetimeStr = (String)var34.next();
            dto = new ExcelChartDto();
            dto.setDatetime(datetimeStr);
            if (null != cpuStateMap.get(datetimeStr)) {
               dto.setCpuPer(((CpuState)cpuStateMap.get(datetimeStr)).getSys());
            }

            if (null != memStateMap.get(datetimeStr)) {
               dto.setMemPer(((MemState)memStateMap.get(datetimeStr)).getUsePer());
            }

            if (null != netIoStateMap.get(datetimeStr)) {
               dto.setDropin(((NetIoState)netIoStateMap.get(datetimeStr)).getDropin());
               dto.setDropout(((NetIoState)netIoStateMap.get(datetimeStr)).getDropout());
               dto.setRxbyt(((NetIoState)netIoStateMap.get(datetimeStr)).getRxbyt());
               dto.setTxbyt(((NetIoState)netIoStateMap.get(datetimeStr)).getTxbyt());
               dto.setRxpck(((NetIoState)netIoStateMap.get(datetimeStr)).getRxpck());
               dto.setTxpck(((NetIoState)netIoStateMap.get(datetimeStr)).getTxpck());
               dto.setNetConnections(((NetIoState)netIoStateMap.get(datetimeStr)).getNetConnections());
            }

            if (null != ysLoadSstateMap.get(datetimeStr)) {
               dto.setOneLoad(((SysLoadState)ysLoadSstateMap.get(datetimeStr)).getOneLoad());
               dto.setFiveLoad(((SysLoadState)ysLoadSstateMap.get(datetimeStr)).getFiveLoad());
               dto.setFifteenLoad(((SysLoadState)ysLoadSstateMap.get(datetimeStr)).getFifteenLoad());
            }
         }

         String hostname = params.get("hostname").toString();
         datetimeStr = DateUtil.getCurrentDateTimeNoChar() + "_" + hostname;
         response.setContentType("application/vnd.ms-exce");
         response.setCharacterEncoding("utf-8");
         response.addHeader("Content-Disposition", "filename=" + datetimeStr + ".xlsx");
         EasyExcel.write(response.getOutputStream(), ExcelChartDto.class).sheet("sheet").doWrite(excelChartList);
      } catch (Exception var25) {
         logger.error("主机趋势图导出excel错误", var25);
      } finally {
         try {
            if (out != null) {
               ((ServletOutputStream)out).close();
            }
         } catch (IOException var24) {
            var24.printStackTrace();
         }

      }

   }

   public void exportHostListExcel(List<SystemInfo> systemInfoList, HttpServletResponse response) {
      Object out = null;

      try {
         List<HostListExcelDto> excelList = new ArrayList();
         Iterator var5 = systemInfoList.iterator();

         while(var5.hasNext()) {
            SystemInfo systemInfo = (SystemInfo)var5.next();
            HostListExcelDto dto = new HostListExcelDto();
            dto.setAgentVer(systemInfo.getAgentVer());
            dto.setSubmitSeconds(systemInfo.getSubmitSeconds());
            dto.setBootTimeStr(systemInfo.getBootTimeStr());
            dto.setBytesRecv(systemInfo.getBytesRecv());
            dto.setBytesSent(systemInfo.getBytesSent());
            dto.setFifteenLoad(systemInfo.getFifteenLoad());
            dto.setCpuPer(systemInfo.getCpuPer());
            dto.setMemPer(systemInfo.getMemPer());
            dto.setFiveLoad(systemInfo.getFiveLoad());
            dto.setRxbyt(systemInfo.getRxbyt());
            dto.setTxbyt(systemInfo.getTxbyt());
            dto.setCpuCoreNum(systemInfo.getCpuCoreNum());
            dto.setCpuXh(systemInfo.getCpuXh());
            dto.setDiskPer(systemInfo.getDiskPer());
            dto.setGroupId(systemInfo.getGroupId());
            dto.setHostname(systemInfo.getHostname());
            dto.setHostnameExt(systemInfo.getHostnameExt());
            dto.setRemark(systemInfo.getRemark());
            dto.setNetConnections(systemInfo.getNetConnections());
            dto.setPlatformVersion(systemInfo.getPlatformVersion());
            if ("2".equals(systemInfo.getState())) {
               dto.setState("下线");
            } else {
               dto.setState("在线");
            }

            dto.setPlatForm(systemInfo.getPlatForm());
            dto.setPlatformVersion(systemInfo.getPlatformVersion());
            dto.setProcs(systemInfo.getProcs());
            dto.setWarnCount(systemInfo.getWarnCount());
            dto.setTotalMem(systemInfo.getTotalMem());
            dto.setCreateTime(DateUtil.getDateTimeString(systemInfo.getCreateTime()));
            dto.setUptimeStr(systemInfo.getUptimeStr());
            dto.setTotalSwapMem(systemInfo.getTotalSwapMem());
            dto.setSwapMemPer(systemInfo.getSwapMemPer());
            excelList.add(dto);
         }

         String fileName = DateUtil.getCurrentDateTimeNoChar() + "_hostList";
         response.setContentType("application/vnd.ms-exce");
         response.setCharacterEncoding("utf-8");
         response.addHeader("Content-Disposition", "filename=" + fileName + ".xlsx");
         EasyExcel.write(response.getOutputStream(), HostListExcelDto.class).sheet("sheet").doWrite(excelList);
      } catch (Exception var16) {
         logger.error("主机列表导出excel错误", var16);
      } finally {
         try {
            if (out != null) {
               ((ServletOutputStream)out).close();
            }
         } catch (IOException var15) {
            var15.printStackTrace();
         }

      }

   }

   public void exportAppExcel(Map<String, Object> params, HttpServletResponse response) {
      Object out = null;

      try {
         List<AppState> appStateList = this.appStateService.selectAllByParams(params);
         List<AppExcelChartDto> excelChartList = new ArrayList();
         Iterator var6 = appStateList.iterator();

         while(var6.hasNext()) {
            AppState appState = (AppState)var6.next();
            AppExcelChartDto appExcelChartDto = new AppExcelChartDto();
            appExcelChartDto.setDatetime(appState.getDateStr());
            appExcelChartDto.setCpuPer(appState.getCpuPer());
            appExcelChartDto.setMemPer(appState.getMemPer());
            appExcelChartDto.setThreadsNum(appState.getThreadsNum());
            excelChartList.add(appExcelChartDto);
         }

         String fileName = DateUtil.getCurrentDateTimeNoChar() + "_appState";
         response.setContentType("application/vnd.ms-exce");
         response.setCharacterEncoding("utf-8");
         response.addHeader("Content-Disposition", "filename=" + fileName + ".xlsx");
         EasyExcel.write(response.getOutputStream(), AppExcelChartDto.class).sheet("sheet").doWrite(excelChartList);
      } catch (Exception var17) {
         logger.error("进程趋势图导出excel错误", var17);
      } finally {
         try {
            if (out != null) {
               ((ServletOutputStream)out).close();
            }
         } catch (IOException var16) {
            var16.printStackTrace();
         }

      }

   }

   public void exportHeathExcel(Map<String, Object> params, HttpServletResponse response) {
      Object out = null;

      try {
         List<HeathState> appStateList = this.heathStateService.selectAllByParams(params);
         List<HeathExcelChartDto> excelChartList = new ArrayList();
         Iterator var6 = appStateList.iterator();

         while(var6.hasNext()) {
            HeathState heathState = (HeathState)var6.next();
            HeathExcelChartDto heathExcelChartDto = new HeathExcelChartDto();
            heathExcelChartDto.setDatetime(heathState.getDateStr());
            heathExcelChartDto.setResTimes(heathState.getResTimes());
            excelChartList.add(heathExcelChartDto);
         }

         String fileName = DateUtil.getCurrentDateTimeNoChar() + "_heathState";
         response.setContentType("application/vnd.ms-exce");
         response.setCharacterEncoding("utf-8");
         response.addHeader("Content-Disposition", "filename=" + fileName + ".xlsx");
         EasyExcel.write(response.getOutputStream(), HeathExcelChartDto.class).sheet("sheet").doWrite(excelChartList);
      } catch (Exception var17) {
         logger.error("服务接口趋势图导出excel错误", var17);
      } finally {
         try {
            if (out != null) {
               ((ServletOutputStream)out).close();
            }
         } catch (IOException var16) {
            var16.printStackTrace();
         }

      }

   }

   public void exportDceExcel(Map<String, Object> params, HttpServletResponse response) {
      Object out = null;

      try {
         List<DceState> appStateList = this.dceStateService.selectAllByParams(params);
         List<DceExcelChartDto> excelChartList = new ArrayList();
         Iterator var6 = appStateList.iterator();

         while(var6.hasNext()) {
            DceState dceState = (DceState)var6.next();
            DceExcelChartDto dceExcelChartDto = new DceExcelChartDto();
            dceExcelChartDto.setDatetime(dceState.getDateStr());
            dceExcelChartDto.setResTimes(dceState.getResTimes());
            excelChartList.add(dceExcelChartDto);
         }

         String fileName = DateUtil.getCurrentDateTimeNoChar() + "_pingState";
         response.setContentType("application/vnd.ms-exce");
         response.setCharacterEncoding("utf-8");
         response.addHeader("Content-Disposition", "filename=" + fileName + ".xlsx");
         EasyExcel.write(response.getOutputStream(), DceExcelChartDto.class).sheet("sheet").doWrite(excelChartList);
      } catch (Exception var17) {
         logger.error("ping设备趋势图导出excel错误", var17);
      } finally {
         try {
            if (out != null) {
               ((ServletOutputStream)out).close();
            }
         } catch (IOException var16) {
            var16.printStackTrace();
         }

      }

   }

   public void exportSnmpExcel(Map<String, Object> params, HttpServletResponse response) {
      Object out = null;

      try {
         List<SnmpState> appStateList = this.snmpStateService.selectAllByParams(params);
         List<SnmpExcelChartDto> excelChartList = new ArrayList();
         Double recvAvgTmp = 0.0D;
         Double sentAvgTmp = 0.0D;
         Iterator var8 = appStateList.iterator();

         while(var8.hasNext()) {
            SnmpState snmpState = (SnmpState)var8.next();
            SnmpExcelChartDto snmpExcelChartDto = new SnmpExcelChartDto();
            recvAvgTmp = snmpState.getRecvAvgDouble();
            sentAvgTmp = snmpState.getSentAvgDouble();
            recvAvgTmp = recvAvgTmp / 1024.0D / 1024.0D;
            sentAvgTmp = sentAvgTmp / 1024.0D / 1024.0D;
            sentAvgTmp = FormatUtil.formatDouble((Double)sentAvgTmp, 2);
            recvAvgTmp = FormatUtil.formatDouble((Double)recvAvgTmp, 2);
            snmpExcelChartDto.setSentAvg(String.valueOf(sentAvgTmp));
            snmpExcelChartDto.setRecvAvg(String.valueOf(recvAvgTmp));
            snmpExcelChartDto.setMemPer(snmpState.getMemPer());
            snmpExcelChartDto.setCpuPer(snmpState.getCpuPer());
            snmpExcelChartDto.setDatetime(snmpState.getDateStr());
            excelChartList.add(snmpExcelChartDto);
         }

         String fileName = DateUtil.getCurrentDateTimeNoChar() + "_snmpState";
         response.setContentType("application/vnd.ms-exce");
         response.setCharacterEncoding("utf-8");
         response.addHeader("Content-Disposition", "filename=" + fileName + ".xlsx");
         EasyExcel.write(response.getOutputStream(), SnmpExcelChartDto.class).sheet("sheet").doWrite(excelChartList);
      } catch (Exception var19) {
         logger.error("snmp设备趋势图导出excel错误", var19);
      } finally {
         try {
            if (out != null) {
               ((ServletOutputStream)out).close();
            }
         } catch (IOException var18) {
            var18.printStackTrace();
         }

      }

   }

   public void exportCustomExcel(Map<String, Object> params, HttpServletResponse response) {
      Object out = null;

      try {
         List<CustomState> customStateList = this.customStateService.selectAllByParams(params);
         List<CustomExcelChartDto> excelChartList = new ArrayList();
         Iterator var6 = customStateList.iterator();

         while(var6.hasNext()) {
            CustomState customState = (CustomState)var6.next();
            CustomExcelChartDto customExcelChartDto = new CustomExcelChartDto();
            customExcelChartDto.setDatetime(customState.getDateStr());
            customExcelChartDto.setCustomValue(customState.getCustomValue());
            excelChartList.add(customExcelChartDto);
         }

         String fileName = DateUtil.getCurrentDateTimeNoChar() + "_customState";
         response.setContentType("application/vnd.ms-exce");
         response.setCharacterEncoding("utf-8");
         response.addHeader("Content-Disposition", "filename=" + fileName + ".xlsx");
         EasyExcel.write(response.getOutputStream(), CustomExcelChartDto.class).sheet("sheet").doWrite(excelChartList);
      } catch (Exception var17) {
         logger.error("自定义监控项值趋势图导出excel错误", var17);
      } finally {
         try {
            if (out != null) {
               ((ServletOutputStream)out).close();
            }
         } catch (IOException var16) {
            var16.printStackTrace();
         }

      }

   }

   public void exportDockerExcel(Map<String, Object> params, HttpServletResponse response) {
      Object out = null;

      try {
         List<DockerState> dockerStateList = this.dockerStateService.selectAllByParams(params);
         List<DockerExcelChartDto> excelChartList = new ArrayList();
         Iterator var6 = dockerStateList.iterator();

         while(var6.hasNext()) {
            DockerState dockerState = (DockerState)var6.next();
            DockerExcelChartDto dockerExcelChartDto = new DockerExcelChartDto();
            dockerExcelChartDto.setDatetime(dockerState.getDateStr());
            dockerExcelChartDto.setMemPer(dockerState.getMemPer());
            excelChartList.add(dockerExcelChartDto);
         }

         String fileName = DateUtil.getCurrentDateTimeNoChar() + "_dockerState";
         response.setContentType("application/vnd.ms-exce");
         response.setCharacterEncoding("utf-8");
         response.addHeader("Content-Disposition", "filename=" + fileName + ".xlsx");
         EasyExcel.write(response.getOutputStream(), DockerExcelChartDto.class).sheet("sheet").doWrite(excelChartList);
      } catch (Exception var17) {
         logger.error("docker趋势图导出excel错误", var17);
      } finally {
         try {
            if (out != null) {
               ((ServletOutputStream)out).close();
            }
         } catch (IOException var16) {
            var16.printStackTrace();
         }

      }

   }

   public void exportDbTableExcel(Map<String, Object> params, HttpServletResponse response) {
      Object out = null;

      try {
         List<DbTableCount> dbTableCountList = this.dbTableCountService.selectAllByParams(params);
         List<DbTableExcelChartDto> excelChartList = new ArrayList();
         Iterator var6 = dbTableCountList.iterator();

         while(var6.hasNext()) {
            DbTableCount dbTableCount = (DbTableCount)var6.next();
            DbTableExcelChartDto dbTableExcelChartDto = new DbTableExcelChartDto();
            if (null != dbTableCount.getCreateTime()) {
               dbTableExcelChartDto.setDatetime(DateUtil.getDateTimeString(dbTableCount.getCreateTime()));
            }

            dbTableExcelChartDto.setTableCount(dbTableCount.getTableCount());
            excelChartList.add(dbTableExcelChartDto);
         }

         String fileName = DateUtil.getCurrentDateTimeNoChar() + "_dbTableCount";
         response.setContentType("application/vnd.ms-exce");
         response.setCharacterEncoding("utf-8");
         response.addHeader("Content-Disposition", "filename=" + fileName + ".xlsx");
         EasyExcel.write(response.getOutputStream(), DbTableExcelChartDto.class).sheet("sheet").doWrite(excelChartList);
      } catch (Exception var17) {
         logger.error("数据表趋势图导出excel错误", var17);
      } finally {
         try {
            if (out != null) {
               ((ServletOutputStream)out).close();
            }
         } catch (IOException var16) {
            var16.printStackTrace();
         }

      }

   }

   public void exportFileWarnStateExcel(FileWarnInfo fileWarnInfo, Map<String, Object> params, HttpServletResponse response) {
      Object out = null;

      try {
         List<FileWarnState> fileWarnStateList = this.fileWarnStateService.selectAllByParams(params);
         List<FileWarnStateExcelDto> excelChartList = new ArrayList();
         Iterator var7 = fileWarnStateList.iterator();

         while(var7.hasNext()) {
            FileWarnState fileWarnState = (FileWarnState)var7.next();
            FileWarnStateExcelDto fileWarnStateExcelDto = new FileWarnStateExcelDto();
            if (null != fileWarnState.getCreateTime()) {
               fileWarnStateExcelDto.setDatetime(DateUtil.getDateTimeString(fileWarnState.getCreateTime()));
            }

            fileWarnStateExcelDto.setWarContent(fileWarnState.getWarContent());
            excelChartList.add(fileWarnStateExcelDto);
         }

         String fileName = fileWarnInfo.getHostname() + "_fileWarnInfo";
         response.setContentType("application/vnd.ms-exce");
         response.setCharacterEncoding("utf-8");
         response.addHeader("Content-Disposition", "filename=" + fileName + ".xlsx");
         EasyExcel.write(response.getOutputStream(), FileWarnStateExcelDto.class).sheet("sheet").doWrite(excelChartList);
      } catch (Exception var18) {
         logger.error("日志监控列表导出excel错误", var18);
      } finally {
         try {
            if (out != null) {
               ((ServletOutputStream)out).close();
            }
         } catch (IOException var17) {
            var17.printStackTrace();
         }

      }

   }
}
