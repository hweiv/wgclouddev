package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.AppState;
import com.wgcloud.mapper.AppStateMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class AppStateService {
   @Autowired
   private AppStateMapper appStateMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<AppState> list = this.appStateMapper.selectByParams(params);
      PageInfo<AppState> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(AppState AppState) throws Exception {
      AppState.setId(UUIDUtil.getUUID());
      AppState.setCreateTime(DateUtil.getNowTime());
      this.appStateMapper.save(AppState);
   }

   public void saveRecord(List<AppState> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            AppState as = (AppState)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.appStateMapper.insertList(recordList);
      }
   }

   public int deleteByAppInfoId(String appInfoId) throws Exception {
      return this.appStateMapper.deleteByAppInfoId(appInfoId);
   }

   public int deleteById(String[] id) throws Exception {
      return this.appStateMapper.deleteById(id);
   }

   public AppState selectById(String id) throws Exception {
      return this.appStateMapper.selectById(id);
   }

   public List<AppState> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.appStateMapper.selectAllByParams(params);
   }

   public int deleteByDate(Map<String, Object> map) throws Exception {
      return this.appStateMapper.deleteByDate(map);
   }

   public void setSubtitle(Model model, List<AppState> appStateList) {
      Double maxCpu = 0.0D;
      Double avgCpu = 0.0D;
      Double minCpu = 1000.0D;
      Double sumCpu = 0.0D;
      Double maxMem = 0.0D;
      Double minMem = 1000.0D;
      Double avgMem = 0.0D;
      Double sumMem = 0.0D;
      Integer maxThreads = 0;
      Integer minThreads = 1000;
      Double avgThreads = 0.0D;
      Integer sumThreads = 0;
      double cpuPerTmp = 0.0D;
      double memPerTmp = 0.0D;
      Integer threadsTmp = 0;
      Iterator var20 = appStateList.iterator();

      while(var20.hasNext()) {
         AppState appState = (AppState)var20.next();
         if (null != appState.getCpuPer()) {
            cpuPerTmp = Double.valueOf(appState.getCpuPer());
            if (cpuPerTmp > maxCpu) {
               maxCpu = cpuPerTmp;
            }

            if (cpuPerTmp < minCpu) {
               minCpu = cpuPerTmp;
            }

            sumCpu = sumCpu + cpuPerTmp;
         }

         if (null != appState.getMemPer()) {
            memPerTmp = Double.valueOf(appState.getMemPer());
            if (memPerTmp > maxMem) {
               maxMem = memPerTmp;
            }

            if (memPerTmp < minMem) {
               minMem = memPerTmp;
            }

            sumMem = sumMem + memPerTmp;
         }

         if (!StringUtils.isEmpty(appState.getThreadsNum())) {
            threadsTmp = Integer.valueOf(appState.getThreadsNum());
            if (threadsTmp > maxThreads) {
               maxThreads = threadsTmp;
            }

            if (threadsTmp < minThreads) {
               minThreads = threadsTmp;
            }

            sumThreads = sumThreads + threadsTmp;
         }
      }

      if (appStateList.size() > 0) {
         avgCpu = sumCpu / (double)appStateList.size();
         avgMem = sumMem / (double)appStateList.size();
         avgThreads = (double)sumThreads / (double)appStateList.size();
      } else {
         minCpu = 0.0D;
         minMem = 0.0D;
         minThreads = 0;
      }

      SubtitleDto cpuSubtitleDto = new SubtitleDto();
      cpuSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgCpu, 2) + "");
      cpuSubtitleDto.setMaxValue(maxCpu + "");
      cpuSubtitleDto.setMinValue(minCpu + "");
      model.addAttribute("cpuSubtitleDto", cpuSubtitleDto);
      SubtitleDto memSubtitleDto = new SubtitleDto();
      memSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgMem, 2) + "");
      memSubtitleDto.setMaxValue(maxMem + "");
      memSubtitleDto.setMinValue(minMem + "");
      model.addAttribute("memSubtitleDto", memSubtitleDto);
      SubtitleDto threadsSubtitleDto = new SubtitleDto();
      threadsSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgThreads, 2) + "");
      threadsSubtitleDto.setMaxValue(maxThreads + "");
      threadsSubtitleDto.setMinValue(minThreads + "");
      model.addAttribute("threadsSubtitleDto", threadsSubtitleDto);
   }
}
