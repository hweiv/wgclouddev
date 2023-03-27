package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.HeathState;
import com.wgcloud.mapper.HeathStateMapper;
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
public class HeathStateService {
   @Autowired
   private HeathStateMapper heathStateMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<HeathState> list = this.heathStateMapper.selectByParams(params);
      PageInfo<HeathState> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(HeathState HeathState) throws Exception {
      HeathState.setId(UUIDUtil.getUUID());
      HeathState.setCreateTime(DateUtil.getNowTime());
      this.heathStateMapper.save(HeathState);
   }

   public void saveRecord(List<HeathState> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            HeathState as = (HeathState)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.heathStateMapper.insertList(recordList);
      }
   }

   public int deleteById(String[] id) throws Exception {
      return this.heathStateMapper.deleteById(id);
   }

   public HeathState selectById(String id) throws Exception {
      return this.heathStateMapper.selectById(id);
   }

   public List<HeathState> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.heathStateMapper.selectAllByParams(params);
   }

   public int deleteByDate(Map<String, Object> map) throws Exception {
      return this.heathStateMapper.deleteByDate(map);
   }

   public void setSubtitle(Model model, List<HeathState> heathStateList) {
      int maxValue = 0;
      int minValue = 20000;
      Double avgValue = 0.0D;
      long sumValue = 0L;
      Iterator var8 = heathStateList.iterator();

      while(var8.hasNext()) {
         HeathState heathState = (HeathState)var8.next();
         if (null != heathState.getResTimes()) {
            if (heathState.getResTimes() > maxValue) {
               maxValue = heathState.getResTimes();
            }

            if (heathState.getResTimes() < minValue) {
               minValue = heathState.getResTimes();
            }

            sumValue += (long)heathState.getResTimes();
         }
      }

      if (heathStateList.size() > 0) {
         avgValue = (double)sumValue / (double)heathStateList.size();
      } else {
         minValue = 0;
      }

      SubtitleDto heathStateSubtitleDto = new SubtitleDto();
      heathStateSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgValue, 2) + "");
      heathStateSubtitleDto.setMaxValue(maxValue + "");
      heathStateSubtitleDto.setMinValue(minValue + "");
      model.addAttribute("heathStateSubtitleDto", heathStateSubtitleDto);
   }
}
