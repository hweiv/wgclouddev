package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.CustomState;
import com.wgcloud.mapper.CustomStateMapper;
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
public class CustomStateService {
   @Autowired
   private CustomStateMapper customStateMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<CustomState> list = this.customStateMapper.selectByParams(params);
      PageInfo<CustomState> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(CustomState customState) throws Exception {
      customState.setId(UUIDUtil.getUUID());
      customState.setCreateTime(DateUtil.getNowTime());
      this.customStateMapper.save(customState);
   }

   public void saveRecord(List<CustomState> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            CustomState as = (CustomState)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.customStateMapper.insertList(recordList);
      }
   }

   public int deleteByCustomInfoId(String customInfoId) throws Exception {
      return this.customStateMapper.deleteByCustomInfoId(customInfoId);
   }

   public int deleteById(String[] id) throws Exception {
      return this.customStateMapper.deleteById(id);
   }

   public CustomState selectById(String id) throws Exception {
      return this.customStateMapper.selectById(id);
   }

   public List<CustomState> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.customStateMapper.selectAllByParams(params);
   }

   public int deleteByDate(Map<String, Object> map) throws Exception {
      return this.customStateMapper.deleteByDate(map);
   }

   public void setSubtitle(Model model, List<CustomState> customStateList) {
      Double maxValue = 0.0D;
      Double avgValue = 0.0D;
      Double minValue = 1000000.0D;
      Double sumValue = 0.0D;
      double valueTmp = 0.0D;
      Iterator var9 = customStateList.iterator();

      while(var9.hasNext()) {
         CustomState customState = (CustomState)var9.next();
         if (null != customState.getCustomValue()) {
            valueTmp = Double.valueOf(customState.getCustomValue());
            if (valueTmp > maxValue) {
               maxValue = valueTmp;
            }

            if (valueTmp < minValue) {
               minValue = valueTmp;
            }

            sumValue = sumValue + valueTmp;
         }
      }

      if (customStateList.size() > 0) {
         avgValue = sumValue / (double)customStateList.size();
      } else {
         avgValue = 0.0D;
         minValue = 0.0D;
      }

      SubtitleDto subtitleDto = new SubtitleDto();
      subtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgValue, 2) + "");
      subtitleDto.setMaxValue(maxValue + "");
      subtitleDto.setMinValue(minValue + "");
      model.addAttribute("subtitleDto", subtitleDto);
   }
}
