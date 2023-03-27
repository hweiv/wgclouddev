package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.DbTableCount;
import com.wgcloud.mapper.DbTableCountMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class DbTableCountService {
   @Autowired
   private DbTableCountMapper dbTableCountMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<DbTableCount> list = this.dbTableCountMapper.selectByParams(params);
      PageInfo<DbTableCount> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(DbTableCount DbTableCount) throws Exception {
      DbTableCount.setId(UUIDUtil.getUUID());
      DbTableCount.setCreateTime(DateUtil.getNowTime());
      this.dbTableCountMapper.save(DbTableCount);
   }

   public void saveRecord(List<DbTableCount> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            DbTableCount as = (DbTableCount)var2.next();
            as.setId(UUIDUtil.getUUID());
            as.setDateStr(DateUtil.getDateTimeString(as.getCreateTime()));
         }

         this.dbTableCountMapper.insertList(recordList);
      }
   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.dbTableCountMapper.countByParams(params);
   }

   @Transactional
   public int deleteById(String[] id) throws Exception {
      return this.dbTableCountMapper.deleteById(id);
   }

   public void updateById(DbTableCount DbTableCount) throws Exception {
      this.dbTableCountMapper.updateById(DbTableCount);
   }

   public DbTableCount selectById(String id) throws Exception {
      return this.dbTableCountMapper.selectById(id);
   }

   public List<DbTableCount> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.dbTableCountMapper.selectAllByParams(params);
   }

   public int deleteByDate(Map<String, Object> map) throws Exception {
      return this.dbTableCountMapper.deleteByDate(map);
   }

   public void setSubtitle(Model model, List<DbTableCount> dbTableCountList) {
      long maxValue = 0L;
      long minValue = 1000000L;
      Double avgValue = 0.0D;
      long sumValue = 0L;
      Iterator var10 = dbTableCountList.iterator();

      while(var10.hasNext()) {
         DbTableCount dbTableCount = (DbTableCount)var10.next();
         if (null != dbTableCount.getTableCount()) {
            if (dbTableCount.getTableCount() > maxValue) {
               maxValue = dbTableCount.getTableCount();
            }

            if (dbTableCount.getTableCount() < minValue) {
               minValue = dbTableCount.getTableCount();
            }

            sumValue += dbTableCount.getTableCount();
         }
      }

      if (dbTableCountList.size() > 0) {
         avgValue = (double)sumValue / (double)dbTableCountList.size();
      } else {
         minValue = 0L;
      }

      SubtitleDto countSubtitleDto = new SubtitleDto();
      countSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgValue, 2) + "");
      countSubtitleDto.setMaxValue(maxValue + "");
      countSubtitleDto.setMinValue(minValue + "");
      model.addAttribute("countSubtitleDto", countSubtitleDto);
   }
}
