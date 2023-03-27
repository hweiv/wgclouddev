package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.DceState;
import com.wgcloud.mapper.DceStateMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class DceStateService {
   @Autowired
   private DceStateMapper dceStateMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<DceState> list = this.dceStateMapper.selectByParams(params);
      PageInfo<DceState> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(DceState DceState) throws Exception {
      DceState.setId(UUIDUtil.getUUID());
      DceState.setCreateTime(DateUtil.getNowTime());
      this.dceStateMapper.save(DceState);
   }

   public void saveRecord(List<DceState> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            DceState as = (DceState)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.dceStateMapper.insertList(recordList);
      }
   }

   public int deleteById(String[] id) throws Exception {
      return this.dceStateMapper.deleteById(id);
   }

   public DceState selectById(String id) throws Exception {
      return this.dceStateMapper.selectById(id);
   }

   public List<DceState> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.dceStateMapper.selectAllByParams(params);
   }

   public int deleteByDate(Map<String, Object> map) throws Exception {
      return this.dceStateMapper.deleteByDate(map);
   }

   public void setSubtitle(Model model, List<DceState> dceStateList) {
      int maxValue = 0;
      int minValue = 20000;
      Double avgValue = 0.0D;
      long sumValue = 0L;
      Iterator var8 = dceStateList.iterator();

      while(var8.hasNext()) {
         DceState dceState = (DceState)var8.next();
         if (null != dceState.getResTimes()) {
            if (dceState.getResTimes() > maxValue) {
               maxValue = dceState.getResTimes();
            }

            if (dceState.getResTimes() < minValue) {
               minValue = dceState.getResTimes();
            }

            sumValue += (long)dceState.getResTimes();
         }
      }

      if (dceStateList.size() > 0) {
         avgValue = (double)sumValue / (double)dceStateList.size();
      } else {
         minValue = 0;
      }

      SubtitleDto dceStateSubtitleDto = new SubtitleDto();
      dceStateSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgValue, 2) + "");
      dceStateSubtitleDto.setMaxValue(maxValue + "");
      dceStateSubtitleDto.setMinValue(minValue + "");
      model.addAttribute("dceStateSubtitleDto", dceStateSubtitleDto);
   }
}
