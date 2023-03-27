package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.CpuState;
import com.wgcloud.mapper.CpuStateMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CpuStateService {
   @Autowired
   private CpuStateMapper cpuStateMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<CpuState> list = this.cpuStateMapper.selectByParams(params);
      PageInfo<CpuState> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(CpuState CpuState) throws Exception {
      CpuState.setId(UUIDUtil.getUUID());
      CpuState.setCreateTime(DateUtil.getNowTime());
      this.cpuStateMapper.save(CpuState);
   }

   public void saveRecord(List<CpuState> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            CpuState as = (CpuState)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.cpuStateMapper.insertList(recordList);
      }
   }

   public int deleteById(String[] id) throws Exception {
      return this.cpuStateMapper.deleteById(id);
   }

   public CpuState selectById(String id) throws Exception {
      return this.cpuStateMapper.selectById(id);
   }

   public CpuState selectMaxAvgByHostname(Map<String, Object> map) throws Exception {
      return this.cpuStateMapper.selectMaxAvgByHostname(map);
   }

   public Double selectMaxByDate(Map<String, Object> map) throws Exception {
      return this.cpuStateMapper.selectMaxByDate(map);
   }

   public List<CpuState> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.cpuStateMapper.selectAllByParams(params);
   }

   public int deleteByAccountAndDate(Map<String, Object> params) throws Exception {
      return this.cpuStateMapper.deleteByAccountAndDate(params);
   }
}
