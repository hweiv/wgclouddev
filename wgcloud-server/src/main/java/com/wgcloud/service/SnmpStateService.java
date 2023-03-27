package com.wgcloud.service;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.SnmpInfo;
import com.wgcloud.entity.SnmpState;
import com.wgcloud.mapper.SnmpStateMapper;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class SnmpStateService {
   @Autowired
   private SnmpStateMapper snmpStateMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<SnmpState> list = this.snmpStateMapper.selectByParams(params);
      PageInfo<SnmpState> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(SnmpState SnmpState) throws Exception {
      SnmpState.setId(UUIDUtil.getUUID());
      SnmpState.setCreateTime(new Date());
      this.snmpStateMapper.save(SnmpState);
   }

   public void saveRecord(List<SnmpState> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            SnmpState as = (SnmpState)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.snmpStateMapper.insertList(recordList);
      }
   }

   public int deleteById(String[] id) throws Exception {
      return this.snmpStateMapper.deleteById(id);
   }

   public SnmpState selectById(String id) throws Exception {
      return this.snmpStateMapper.selectById(id);
   }

   public List<SnmpState> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.snmpStateMapper.selectAllByParams(params);
   }

   public int deleteByDate(Map<String, Object> map) throws Exception {
      return this.snmpStateMapper.deleteByDate(map);
   }

   public void setSubtitle(Model model, List<SnmpState> snmpStateList, SnmpInfo snmpInfo) {
      double maxval = 0.0D;
      Double maxRxbyt = 0.0D;
      Double avgRxbyt = 0.0D;
      Double minRxbyt = 99999.0D;
      Double sumRxbyt = 0.0D;
      Double maxSent = 0.0D;
      Double minSent = 99999.0D;
      Double avgSent = 0.0D;
      Double sumSent = 0.0D;
      Double maxCpuPer = 0.0D;
      Double minCpuPer = 99999.0D;
      Double avgCpuPer = 0.0D;
      Double sumCpuPer = 0.0D;
      Double maxMemSize = 0.0D;
      Double minMemSize = 99999.0D;
      Double avgMemSize = 0.0D;
      Double sumMemSize = 0.0D;
      Double recvAvgTmp = 0.0D;
      Double sentAvgTmp = 0.0D;
      Double cpuPerTmp = 0.0D;
      Double memSizeTmp = 0.0D;
      if (!CollectionUtil.isEmpty(snmpStateList)) {
         Iterator var26 = snmpStateList.iterator();

         while(var26.hasNext()) {
            SnmpState snmpState = (SnmpState)var26.next();
            recvAvgTmp = snmpState.getRecvAvgDouble();
            sentAvgTmp = snmpState.getSentAvgDouble();
            if ("byte".equals(snmpInfo.getSnmpUnit())) {
               recvAvgTmp = recvAvgTmp / 1024.0D / 1024.0D;
               sentAvgTmp = sentAvgTmp / 1024.0D / 1024.0D;
            } else {
               recvAvgTmp = recvAvgTmp / 1024.0D;
               sentAvgTmp = sentAvgTmp / 1024.0D;
            }

            sentAvgTmp = FormatUtil.formatDouble((Double)sentAvgTmp, 2);
            recvAvgTmp = FormatUtil.formatDouble((Double)recvAvgTmp, 2);
            snmpState.setSentAvg(String.valueOf(sentAvgTmp));
            snmpState.setRecvAvg(String.valueOf(recvAvgTmp));
            if (null != recvAvgTmp) {
               if (recvAvgTmp > maxRxbyt) {
                  maxRxbyt = recvAvgTmp;
               }

               if (recvAvgTmp < minRxbyt) {
                  minRxbyt = recvAvgTmp;
               }

               sumRxbyt = sumRxbyt + recvAvgTmp;
            }

            if (null != sentAvgTmp) {
               if (sentAvgTmp > maxSent) {
                  maxSent = sentAvgTmp;
               }

               if (sentAvgTmp < minSent) {
                  minSent = sentAvgTmp;
               }

               sumSent = sumSent + sentAvgTmp;
            }

            cpuPerTmp = snmpState.getCpuPerDouble();
            memSizeTmp = snmpState.getMemPerDouble();
            if (null != cpuPerTmp) {
               if (cpuPerTmp > maxCpuPer) {
                  maxCpuPer = cpuPerTmp;
               }

               if (cpuPerTmp < minCpuPer) {
                  minCpuPer = cpuPerTmp;
               }

               sumCpuPer = sumCpuPer + cpuPerTmp;
            }

            if (null != memSizeTmp) {
               if (memSizeTmp > maxMemSize) {
                  maxMemSize = memSizeTmp;
               }

               if (memSizeTmp < minMemSize) {
                  minMemSize = memSizeTmp;
               }

               sumMemSize = sumMemSize + memSizeTmp;
            }
         }
      }

      if (maxRxbyt > maxval) {
         maxval = maxRxbyt;
      }

      if (maxSent > maxval) {
         maxval = maxSent;
      }

      if (maxval == 0.0D) {
         maxval = 1.0D;
      }

      model.addAttribute("snmpAvgMax", Math.ceil(maxval));
      if (snmpStateList.size() > 0) {
         avgRxbyt = sumRxbyt / (double)snmpStateList.size();
         avgSent = sumSent / (double)snmpStateList.size();
         avgCpuPer = sumCpuPer / (double)snmpStateList.size();
         avgMemSize = sumMemSize / (double)snmpStateList.size();
      } else {
         minRxbyt = 0.0D;
         minSent = 0.0D;
         minCpuPer = 0.0D;
         minMemSize = 0.0D;
      }

      SubtitleDto rxbytSubtitleDto = new SubtitleDto();
      rxbytSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgRxbyt, 2) + "MB/s");
      rxbytSubtitleDto.setMaxValue(maxRxbyt + "MB/s");
      rxbytSubtitleDto.setMinValue(minRxbyt + "MB/s");
      model.addAttribute("rxbytSubtitleDto", rxbytSubtitleDto);
      SubtitleDto sentSubtitleDto = new SubtitleDto();
      sentSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgSent, 2) + "MB/s");
      sentSubtitleDto.setMaxValue(maxSent + "MB/s");
      sentSubtitleDto.setMinValue(minSent + "MB/s");
      model.addAttribute("sentSubtitleDto", sentSubtitleDto);
      SubtitleDto cpuPerSubtitleDto = new SubtitleDto();
      cpuPerSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgCpuPer, 2) + "%");
      cpuPerSubtitleDto.setMaxValue(maxCpuPer + "%");
      cpuPerSubtitleDto.setMinValue(minCpuPer + "%");
      model.addAttribute("cpuPerSubtitleDto", cpuPerSubtitleDto);
      SubtitleDto memSizeSubtitleDto = new SubtitleDto();
      memSizeSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgMemSize, 2) + "%");
      memSizeSubtitleDto.setMaxValue(maxMemSize + "%");
      memSizeSubtitleDto.setMinValue(minMemSize + "%");
      model.addAttribute("memSizeSubtitleDto", memSizeSubtitleDto);
   }
}
