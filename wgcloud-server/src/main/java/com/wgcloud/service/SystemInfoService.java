package com.wgcloud.service;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.dto.HostWarnDiyDto;
import com.wgcloud.dto.NetIoStateDto;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.MemState;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.mapper.SystemInfoMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class SystemInfoService {
   private static final Logger logger = LoggerFactory.getLogger(SystemInfoService.class);
   @Autowired
   private SystemInfoMapper systemInfoMapper;
   @Autowired
   private CommonConfig commonConfig;
   @Resource
   private DiskStateService diskStateService;
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private HostGroupService hostGroupService;
   @Autowired
   private MailConfig mailConfig;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<SystemInfo> list = this.systemInfoMapper.selectByParams(params);
      PageInfo<SystemInfo> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(SystemInfo SystemInfo) throws Exception {
      SystemInfo.setId(UUIDUtil.getUUID());
      SystemInfo.setCreateTime(DateUtil.getNowTime());
      this.systemInfoMapper.save(SystemInfo);
   }

   public void downByHostName(List<String> recordList) throws Exception {
      if (recordList.size() >= 1) {
         this.systemInfoMapper.downByHostName(recordList);
      }
   }

   public void updateAccountByHostName(Map<String, Object> params) throws Exception {
      this.systemInfoMapper.updateAccountByHostName(params);
   }

   public void updateToTargetAccount(Map<String, Object> params) throws Exception {
      this.systemInfoMapper.updateToTargetAccount(params);
   }

   @Transactional
   public void saveRecord(List<SystemInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            SystemInfo as = (SystemInfo)var2.next();
            as.setId(UUIDUtil.getUUID());
            as.setCreateTime(DateUtil.getNowTime());
         }

         this.systemInfoMapper.insertList(recordList);
      }
   }

   @Transactional
   public void updateRecord(List<SystemInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         this.systemInfoMapper.updateList(recordList);
      }
   }

   @Transactional
   public void updateById(SystemInfo SystemInfo) throws Exception {
      this.systemInfoMapper.updateById(SystemInfo);
   }

   public int deleteById(String[] id) throws Exception {
      return this.systemInfoMapper.deleteById(id);
   }

   public SystemInfo selectById(String id) throws Exception {
      return this.systemInfoMapper.selectById(id);
   }

   public List<SystemInfo> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.systemInfoMapper.selectAllByParams(params);
   }

   public List<SystemInfo> selectAllHostNameByParams(Map<String, Object> params) throws Exception {
      return this.systemInfoMapper.selectAllHostNameByParams(params);
   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.systemInfoMapper.countByParams(params);
   }

   public SystemInfo selectByHostname(String hostname) throws Exception {
      List<SystemInfo> list = this.systemInfoMapper.selectByHostname(hostname);
      return list.size() > 0 ? (SystemInfo)list.get(0) : null;
   }

   public int deleteByAccHname(Map<String, Object> params) throws Exception {
      return this.systemInfoMapper.deleteByAccHname(params);
   }

   public void hostAddVal(PageInfo<SystemInfo> pageInfo, Model model) throws Exception {
      Map<String, Object> paramsAppInfo = new HashMap();
      if ("true".equals(this.commonConfig.getHostGroup())) {
         this.setGroupInList(pageInfo.getList(), model);
      }

      Iterator var4 = pageInfo.getList().iterator();

      while(var4.hasNext()) {
         SystemInfo systemInfo1 = (SystemInfo)var4.next();
         paramsAppInfo.put("hostname", systemInfo1.getHostname());
         systemInfo1.setRxbyt(FormatUtil.kbToM(systemInfo1.getRxbyt()) + "/s");
         systemInfo1.setTxbyt(FormatUtil.kbToM(systemInfo1.getTxbyt()) + "/s");
         HostUtil.setSysImage(systemInfo1);
         List<DiskState> deskStates = this.diskStateService.selectAllByParams(paramsAppInfo);
         HostUtil.setDiskSumPer(deskStates, systemInfo1);
         if ("true".equals(this.commonConfig.getShowWarnCount())) {
            String warnQueryWd = systemInfo1.getHostname();
            this.logInfoService.warnQueryHandle(systemInfo1, warnQueryWd);
         }
      }

   }

   public void hideLeftIp(PageInfo<SystemInfo> pageInfo, HttpServletRequest request) {
      if ("true".equals(this.commonConfig.getDashViewIpHide())) {
         HttpSession session = request.getSession();
         AccountInfo accountInfo = (AccountInfo)session.getAttribute("LOGIN_KEY");
         if (null == accountInfo) {
            Iterator var5 = pageInfo.getList().iterator();

            while(var5.hasNext()) {
               SystemInfo systemInfo = (SystemInfo)var5.next();
               if (!StringUtils.isBlank(systemInfo.getHostname())) {
                  int length = StringUtils.length(systemInfo.getHostname());
                  String resHostName = StringUtils.leftPad(StringUtils.right(systemInfo.getHostname(), length - 5), length, "*");
                  systemInfo.setHostname(resHostName);
               }
            }
         }

      }
   }

   public void setSubtitle(Model model, List<CpuState> cpuStateList, List<MemState> memStateList) {
      Double maxCpu = 0.0D;
      Double avgCpu = 0.0D;
      Double minCpu = 1000.0D;
      Double sumCpu = 0.0D;
      Double maxMem = 0.0D;
      Double minMem = 1000.0D;
      Double avgMem = 0.0D;
      Double sumMem = 0.0D;
      Iterator var12 = cpuStateList.iterator();

      while(var12.hasNext()) {
         CpuState cpuState = (CpuState)var12.next();
         if (null != cpuState.getSys()) {
            if (cpuState.getSys() > maxCpu) {
               maxCpu = cpuState.getSys();
            }

            if (cpuState.getSys() < minCpu) {
               minCpu = cpuState.getSys();
            }

            sumCpu = sumCpu + cpuState.getSys();
         }
      }

      if (cpuStateList.size() > 0) {
         avgCpu = sumCpu / (double)cpuStateList.size();
      } else {
         minCpu = 0.0D;
      }

      SubtitleDto cpuSubtitleDto = new SubtitleDto();
      cpuSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgCpu, 2) + "");
      cpuSubtitleDto.setMaxValue(maxCpu + "");
      cpuSubtitleDto.setMinValue(minCpu + "");
      model.addAttribute("cpuSubtitleDto", cpuSubtitleDto);
      Iterator var16 = memStateList.iterator();

      while(var16.hasNext()) {
         MemState memState = (MemState)var16.next();
         if (null != memState.getUsePer()) {
            if (memState.getUsePer() > maxMem) {
               maxMem = memState.getUsePer();
            }

            if (memState.getUsePer() < minMem) {
               minMem = memState.getUsePer();
            }

            sumMem = sumMem + memState.getUsePer();
         }
      }

      if (memStateList.size() > 0) {
         avgMem = sumMem / (double)memStateList.size();
      } else {
         minMem = 0.0D;
      }

      SubtitleDto memSubtitleDto = new SubtitleDto();
      memSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgMem, 2) + "");
      memSubtitleDto.setMaxValue(maxMem + "");
      memSubtitleDto.setMinValue(minMem + "");
      model.addAttribute("memSubtitleDto", memSubtitleDto);
   }

   public void findLoadMaxVal(List<SysLoadState> sysLoadStateList, Model model) {
      double maxVal = 0.0D;
      Double maxOneLoad = 0.0D;
      Double avgOneLoad = 0.0D;
      Double minOneLoad = 1000.0D;
      Double sumOneLoad = 0.0D;
      Double maxFiveLoad = 0.0D;
      Double avgFiveLoad = 0.0D;
      Double minFiveLoad = 1000.0D;
      Double sumFiveLoad = 0.0D;
      Double maxFifteenLoad = 0.0D;
      Double avgFifteenLoad = 0.0D;
      Double minFifteenLoad = 1000.0D;
      Double sumFifteenLoad = 0.0D;
      if (!CollectionUtil.isEmpty(sysLoadStateList)) {
         Iterator var17 = sysLoadStateList.iterator();

         while(var17.hasNext()) {
            SysLoadState sysLoadState = (SysLoadState)var17.next();
            if (null != sysLoadState.getOneLoad()) {
               if (sysLoadState.getOneLoad() > maxOneLoad) {
                  maxOneLoad = sysLoadState.getOneLoad();
               }

               if (sysLoadState.getOneLoad() < minOneLoad) {
                  minOneLoad = sysLoadState.getOneLoad();
               }

               sumOneLoad = sumOneLoad + sysLoadState.getOneLoad();
            }

            if (null != sysLoadState.getFiveLoad()) {
               if (sysLoadState.getFiveLoad() > maxFiveLoad) {
                  maxFiveLoad = sysLoadState.getFiveLoad();
               }

               if (sysLoadState.getFiveLoad() < minFiveLoad) {
                  minFiveLoad = sysLoadState.getFiveLoad();
               }

               sumFiveLoad = sumFiveLoad + sysLoadState.getFiveLoad();
            }

            if (null != sysLoadState.getFifteenLoad()) {
               if (sysLoadState.getFifteenLoad() > maxFifteenLoad) {
                  maxFifteenLoad = sysLoadState.getFifteenLoad();
               }

               if (sysLoadState.getFifteenLoad() < minFifteenLoad) {
                  minFifteenLoad = sysLoadState.getFifteenLoad();
               }

               sumFifteenLoad = sumFifteenLoad + sysLoadState.getFifteenLoad();
            }
         }
      }

      if (maxOneLoad > maxVal) {
         maxVal = maxOneLoad;
      }

      if (maxFiveLoad > maxVal) {
         maxVal = maxFiveLoad;
      }

      if (maxFifteenLoad > maxVal) {
         maxVal = maxFifteenLoad;
      }

      if (maxVal == 0.0D) {
         maxVal = 1.0D;
      }

      model.addAttribute("sysLoadStateMaxVal", Math.ceil(maxVal));
      if (sysLoadStateList.size() > 0) {
         avgOneLoad = sumOneLoad / (double)sysLoadStateList.size();
         avgFiveLoad = sumFiveLoad / (double)sysLoadStateList.size();
         avgFifteenLoad = sumFifteenLoad / (double)sysLoadStateList.size();
      } else {
         minOneLoad = 0.0D;
         minFiveLoad = 0.0D;
         minFifteenLoad = 0.0D;
      }

      SubtitleDto oneLoadSubtitleDto = new SubtitleDto();
      oneLoadSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgOneLoad, 2) + "");
      oneLoadSubtitleDto.setMaxValue(maxOneLoad + "");
      oneLoadSubtitleDto.setMinValue(minOneLoad + "");
      model.addAttribute("oneLoadSubtitleDto", oneLoadSubtitleDto);
      SubtitleDto fiveLoadSubtitleDto = new SubtitleDto();
      fiveLoadSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgFiveLoad, 2) + "");
      fiveLoadSubtitleDto.setMaxValue(maxFiveLoad + "");
      fiveLoadSubtitleDto.setMinValue(minFiveLoad + "");
      model.addAttribute("fiveLoadSubtitleDto", fiveLoadSubtitleDto);
      SubtitleDto fifteenLoadSubtitleDto = new SubtitleDto();
      fifteenLoadSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgFifteenLoad, 2) + "");
      fifteenLoadSubtitleDto.setMaxValue(maxFifteenLoad + "");
      fifteenLoadSubtitleDto.setMinValue(minFifteenLoad + "");
      model.addAttribute("fifteenLoadSubtitleDto", fifteenLoadSubtitleDto);
   }

   public List<NetIoStateDto> toNetIoStateDto(List<NetIoState> netIoStateList) {
      List<NetIoStateDto> dtoList = new ArrayList();
      Iterator var3 = netIoStateList.iterator();

      while(var3.hasNext()) {
         NetIoState netIoState = (NetIoState)var3.next();
         NetIoStateDto dto = new NetIoStateDto();
         dto.setCreateTime(netIoState.getCreateTime());
         dto.setDateStr(netIoState.getDateStr());
         dto.setHostname(netIoState.getHostname());
         dto.setRxbyt(Double.valueOf(netIoState.getRxbyt()));
         dto.setRxpck(Double.valueOf(netIoState.getRxpck()));
         dto.setTxbyt(Double.valueOf(netIoState.getTxbyt()));
         dto.setTxpck(Double.valueOf(netIoState.getTxpck()));
         dto.setDropin(Double.valueOf(netIoState.getDropin()));
         dto.setDropout(Double.valueOf(netIoState.getDropout()));
         dto.setNetConnections(Integer.valueOf(netIoState.getNetConnections()));
         dtoList.add(dto);
      }

      return dtoList;
   }

   public void findNetIoStateBytMaxVal(List<NetIoStateDto> netIoStateList, Model model) {
      double maxBytesVal = 0.0D;
      double maxPckVal = 0.0D;
      double maxDropVal = 0.0D;
      Double maxRxbyt = 0.0D;
      Double avgRxbyt = 0.0D;
      Double minRxbyt = 99999.0D;
      Double sumRxbyt = 0.0D;
      Double maxTxbyt = 0.0D;
      Double minTxbyt = 99999.0D;
      Double avgTxbyt = 0.0D;
      Double sumTxbyt = 0.0D;
      Double maxRxpck = 0.0D;
      Double avgRxpck = 0.0D;
      Double minRxpck = 99999.0D;
      Double sumRxpck = 0.0D;
      Double maxTxpck = 0.0D;
      Double minTxpck = 99999.0D;
      Double avgTxpck = 0.0D;
      Double sumTxpck = 0.0D;
      Integer maxConns = 0;
      Integer minConns = 99999;
      Double avgConns = 0.0D;
      Integer sumConns = 0;
      if (!CollectionUtil.isEmpty(netIoStateList)) {
         Iterator var29 = netIoStateList.iterator();

         while(var29.hasNext()) {
            NetIoStateDto netIoState = (NetIoStateDto)var29.next();

            try {
               if (null != netIoState.getDropin() && netIoState.getDropin() > maxDropVal) {
                  maxDropVal = netIoState.getDropin();
               }

               if (null != netIoState.getDropout() && netIoState.getDropout() > maxDropVal) {
                  maxDropVal = netIoState.getDropout();
               }

               if (null != netIoState.getRxbyt()) {
                  if (netIoState.getRxbyt() > maxRxbyt) {
                     maxRxbyt = netIoState.getRxbyt();
                  }

                  if (netIoState.getRxbyt() < minRxbyt) {
                     minRxbyt = netIoState.getRxbyt();
                  }

                  sumRxbyt = sumRxbyt + netIoState.getRxbyt();
               }

               if (null != netIoState.getTxbyt()) {
                  if (netIoState.getTxbyt() > maxTxbyt) {
                     maxTxbyt = netIoState.getTxbyt();
                  }

                  if (netIoState.getTxbyt() < minTxbyt) {
                     minTxbyt = netIoState.getTxbyt();
                  }

                  sumTxbyt = sumTxbyt + netIoState.getTxbyt();
               }

               if (null != netIoState.getRxpck()) {
                  if (netIoState.getRxpck() > maxRxpck) {
                     maxRxpck = netIoState.getRxpck();
                  }

                  if (netIoState.getRxpck() < minRxpck) {
                     minRxpck = netIoState.getRxpck();
                  }

                  sumRxpck = sumRxpck + netIoState.getRxpck();
               }

               if (null != netIoState.getTxpck()) {
                  if (netIoState.getTxpck() > maxTxpck) {
                     maxTxpck = netIoState.getTxpck();
                  }

                  if (Double.valueOf(netIoState.getTxpck()) < minTxpck) {
                     minTxpck = netIoState.getTxpck();
                  }

                  sumTxpck = sumTxpck + netIoState.getTxpck();
               }

               if (null != netIoState.getNetConnections()) {
                  if (netIoState.getNetConnections() > maxConns) {
                     maxConns = netIoState.getNetConnections();
                  }

                  if (Double.valueOf((double)netIoState.getNetConnections()) < (double)minConns) {
                     minConns = netIoState.getNetConnections();
                  }

                  sumConns = sumConns + netIoState.getNetConnections();
               }
            } catch (Exception var34) {
               logger.error("设置网络流量图表副标题错误", var34);
            }
         }
      }

      if (maxRxbyt > maxBytesVal) {
         maxBytesVal = maxRxbyt;
      }

      if (maxTxbyt > maxBytesVal) {
         maxBytesVal = maxTxbyt;
      }

      if (maxRxpck > maxPckVal) {
         maxPckVal = maxRxpck;
      }

      if (maxTxpck > maxPckVal) {
         maxPckVal = maxTxpck;
      }

      if (maxBytesVal == 0.0D) {
         maxBytesVal = 1.0D;
      }

      model.addAttribute("netIoStateBytMaxVal", Math.ceil(maxBytesVal));
      if (maxPckVal == 0.0D) {
         maxPckVal = 1.0D;
      }

      model.addAttribute("netIoStatePckMaxVal", Math.ceil(maxPckVal));
      if (maxDropVal == 0.0D) {
         maxDropVal = 1.0D;
      }

      model.addAttribute("netIoStateDropPckMaxVal", Math.ceil(maxDropVal));
      if (netIoStateList.size() > 0) {
         avgRxbyt = sumRxbyt / (double)netIoStateList.size();
         avgTxbyt = sumTxbyt / (double)netIoStateList.size();
         avgRxpck = sumRxpck / (double)netIoStateList.size();
         avgTxpck = sumTxpck / (double)netIoStateList.size();
         avgConns = (double)sumConns / (double)netIoStateList.size();
      } else {
         minRxbyt = 0.0D;
         minTxbyt = 0.0D;
         minRxpck = 0.0D;
         minTxpck = 0.0D;
         minConns = 0;
      }

      SubtitleDto rxbytSubtitleDto = new SubtitleDto();
      rxbytSubtitleDto.setAvgValue(FormatUtil.kbToM(FormatUtil.formatDouble((Double)avgRxbyt, 2) + "") + "/s");
      rxbytSubtitleDto.setMaxValue(FormatUtil.kbToM(maxRxbyt + "") + "/s");
      rxbytSubtitleDto.setMinValue(FormatUtil.kbToM(minRxbyt + "") + "/s");
      model.addAttribute("rxbytSubtitleDto", rxbytSubtitleDto);
      SubtitleDto txbytSubtitleDto = new SubtitleDto();
      txbytSubtitleDto.setAvgValue(FormatUtil.kbToM(FormatUtil.formatDouble((Double)avgTxbyt, 2) + "") + "/s");
      txbytSubtitleDto.setMaxValue(FormatUtil.kbToM(maxTxbyt + "") + "/s");
      txbytSubtitleDto.setMinValue(FormatUtil.kbToM(minTxbyt + "") + "/s");
      model.addAttribute("txbytSubtitleDto", txbytSubtitleDto);
      SubtitleDto rxpckSubtitleDto = new SubtitleDto();
      rxpckSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgRxpck, 2) + "/s");
      rxpckSubtitleDto.setMaxValue(maxRxpck + "/s");
      rxpckSubtitleDto.setMinValue(minRxpck + "/s");
      model.addAttribute("rxpckSubtitleDto", rxpckSubtitleDto);
      SubtitleDto txpckSubtitleDto = new SubtitleDto();
      txpckSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgTxpck, 2) + "/s");
      txpckSubtitleDto.setMaxValue(maxTxpck + "/s");
      txpckSubtitleDto.setMinValue(minTxpck + "/s");
      model.addAttribute("txpckSubtitleDto", txpckSubtitleDto);
      SubtitleDto connsSubtitleDto = new SubtitleDto();
      connsSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgConns, 2) + "");
      connsSubtitleDto.setMaxValue(maxConns + "");
      connsSubtitleDto.setMinValue(minConns + "");
      model.addAttribute("connsSubtitleDto", connsSubtitleDto);
   }

   public void hideLeftIp(SystemInfo systemInfo, HttpServletRequest request) {
      if (!StringUtils.isBlank(systemInfo.getHostname())) {
         if ("true".equals(this.commonConfig.getDashViewIpHide())) {
            HttpSession session = request.getSession();
            AccountInfo accountInfo = (AccountInfo)session.getAttribute("LOGIN_KEY");
            if (null == accountInfo) {
               int length = StringUtils.length(systemInfo.getHostname());
               String resHostName = StringUtils.leftPad(StringUtils.right(systemInfo.getHostname(), length - 5), length, "*");
               systemInfo.setHostname(resHostName);
            }

         }
      }
   }

   public void setChartWarnElement(SystemInfo systemInfo, Model model) {
      try {
         Double memWarnVal = this.mailConfig.getMemWarnVal();
         Double cpuWarnVal = this.mailConfig.getCpuWarnVal();
         Double downSpeedVal = this.mailConfig.getDownSpeedVal();
         Double upSpeedVal = this.mailConfig.getUpSpeedVal();
         Double sysLoadWarnVal = this.mailConfig.getSysLoadWarnVal();
         HostWarnDiyDto hostWarnDiyDto = (HostWarnDiyDto)StaticKeys.HOST_WARN_MAP.get(systemInfo.getHostname());
         if (null != hostWarnDiyDto && null != hostWarnDiyDto.getMemWarnVal()) {
            memWarnVal = hostWarnDiyDto.getMemWarnVal();
            if ("yes".equals(hostWarnDiyDto.getMemWarnMail()) && "true".equals(this.mailConfig.getMemWarnMail())) {
               model.addAttribute("memWarnVal", memWarnVal);
            }
         } else if ("true".equals(this.mailConfig.getMemWarnMail())) {
            model.addAttribute("memWarnVal", memWarnVal);
         }

         if (null != hostWarnDiyDto && null != hostWarnDiyDto.getCpuWarnVal()) {
            cpuWarnVal = hostWarnDiyDto.getCpuWarnVal();
            if ("yes".equals(hostWarnDiyDto.getCpuWarnMail()) && "true".equals(this.mailConfig.getCpuWarnMail())) {
               model.addAttribute("cpuWarnVal", cpuWarnVal);
            }
         } else if ("true".equals(this.mailConfig.getCpuWarnMail())) {
            model.addAttribute("cpuWarnVal", cpuWarnVal);
         }

         if (null != hostWarnDiyDto && null != hostWarnDiyDto.getUpSpeedVal()) {
            upSpeedVal = hostWarnDiyDto.getUpSpeedVal();
            if ("yes".equals(hostWarnDiyDto.getUpSpeedMail()) && "true".equals(this.mailConfig.getUpSpeedMail())) {
               model.addAttribute("upSpeedVal", upSpeedVal);
            }
         } else if ("true".equals(this.mailConfig.getUpSpeedMail())) {
            model.addAttribute("upSpeedVal", upSpeedVal);
         }

         if (null != hostWarnDiyDto && null != hostWarnDiyDto.getDownSpeedVal()) {
            downSpeedVal = hostWarnDiyDto.getDownSpeedVal();
            if ("yes".equals(hostWarnDiyDto.getDownSpeedMail()) && "true".equals(this.mailConfig.getDownSpeedMail())) {
               model.addAttribute("downSpeedVal", downSpeedVal);
            }
         } else if ("true".equals(this.mailConfig.getDownSpeedMail())) {
            model.addAttribute("downSpeedVal", downSpeedVal);
         }

         if (null != hostWarnDiyDto && null != hostWarnDiyDto.getSysLoadWarnVal()) {
            sysLoadWarnVal = hostWarnDiyDto.getSysLoadWarnVal();
            if ("yes".equals(hostWarnDiyDto.getSysLoadWarnMail()) && "true".equals(this.mailConfig.getSysLoadWarnMail())) {
               model.addAttribute("sysLoadWarnVal", sysLoadWarnVal);
            }
         } else if ("true".equals(this.mailConfig.getSysLoadWarnMail())) {
            model.addAttribute("sysLoadWarnVal", sysLoadWarnVal);
         }
      } catch (Exception var9) {
         logger.error("主机图形报表设置告警线错误", var9);
      }

   }

   public void saveGroupId(String ids, String[] groupIdsArr, HttpServletRequest request) throws Exception {
      List<HostGroup> hostGroupList = new ArrayList();
      HashMap ho;
      if (null != groupIdsArr && groupIdsArr.length > 0) {
         ho = new HashMap();
         ho.put("groupIds", groupIdsArr);
         hostGroupList = this.hostGroupService.selectAllByParams(ho);
      }

      if (!StringUtils.isEmpty(ids)) {
         ho = null;
         String[] var6 = ids.split(",");
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String id = var6[var8];
            SystemInfo systemInfo = this.selectById(id);
            if (((List)hostGroupList).size() > 0) {
               systemInfo.setGroupId(StringUtils.join(groupIdsArr, ","));
            } else {
               systemInfo.setGroupId("");
            }

            this.updateById(systemInfo);
            this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "设置主机标签：" + systemInfo.getHostname(), "标签：" + this.hostGroupService.returnGroupNames((List)hostGroupList), "2");
         }
      }

   }

   public List<HostGroup> setGroupInList(List<SystemInfo> recordList, Model model) throws Exception {
      Map<String, Object> params = new HashMap();
      List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params);
      model.addAttribute("hostGroupList", hostGroupList);
      Map<String, String> hostGroupMap = new HashMap();
      Iterator var6 = hostGroupList.iterator();

      while(var6.hasNext()) {
         HostGroup hostGroup = (HostGroup)var6.next();
         hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
      }

      var6 = recordList.iterator();

      while(true) {
         SystemInfo systemInfo;
         do {
            if (!var6.hasNext()) {
               return hostGroupList;
            }

            systemInfo = (SystemInfo)var6.next();
         } while(StringUtils.isEmpty(systemInfo.getGroupId()));

         String groupNames = "";
         String[] var9 = systemInfo.getGroupId().split(",");
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            String groupId = var9[var11];
            if (hostGroupMap.containsKey(groupId)) {
               groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
            }
         }

         systemInfo.setGroupId(groupNames);
      }
   }
}
