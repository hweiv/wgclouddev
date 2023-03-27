package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.mapper.CpuTemperaturesMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CpuTemperaturesService {
   @Autowired
   private CpuTemperaturesMapper cpuTemperaturesMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<CpuTemperatures> list = this.cpuTemperaturesMapper.selectByParams(params);
      PageInfo<CpuTemperatures> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(CpuTemperatures CpuTemperatures) throws Exception {
      CpuTemperatures.setId(UUIDUtil.getUUID());
      CpuTemperatures.setCreateTime(DateUtil.getNowTime());
      this.cpuTemperaturesMapper.save(CpuTemperatures);
   }

   @Transactional
   public void saveRecord(List<CpuTemperatures> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            CpuTemperatures as = (CpuTemperatures)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.cpuTemperaturesMapper.insertList(recordList);
      }
   }

   public int deleteById(String[] id) throws Exception {
      return this.cpuTemperaturesMapper.deleteById(id);
   }

   public CpuTemperatures selectById(String id) throws Exception {
      return this.cpuTemperaturesMapper.selectById(id);
   }

   public List<CpuTemperatures> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.cpuTemperaturesMapper.selectAllByParams(params);
   }

   public int deleteByAccHname(List<String> recordList) throws Exception {
      return this.cpuTemperaturesMapper.deleteByAccHname(recordList);
   }

   public int deleteByAccountAndDate(Map<String, Object> map) throws Exception {
      return this.cpuTemperaturesMapper.deleteByAccountAndDate(map);
   }
}
