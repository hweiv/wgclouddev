package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.MemState;
import com.wgcloud.mapper.MemStateMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemStateService {
   @Autowired
   private MemStateMapper memStateMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<MemState> list = this.memStateMapper.selectByParams(params);
      PageInfo<MemState> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(MemState MemState) throws Exception {
      MemState.setId(UUIDUtil.getUUID());
      MemState.setCreateTime(DateUtil.getNowTime());
      this.memStateMapper.save(MemState);
   }

   public void saveRecord(List<MemState> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            MemState as = (MemState)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.memStateMapper.insertList(recordList);
      }
   }

   public int deleteById(String[] id) throws Exception {
      return this.memStateMapper.deleteById(id);
   }

   public MemState selectById(String id) throws Exception {
      return this.memStateMapper.selectById(id);
   }

   public MemState selectMaxAvgByHostname(Map<String, Object> map) throws Exception {
      return this.memStateMapper.selectMaxAvgByHostname(map);
   }

   public Double selectMaxByDate(Map<String, Object> map) throws Exception {
      return this.memStateMapper.selectMaxByDate(map);
   }

   public List<MemState> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.memStateMapper.selectAllByParams(params);
   }

   public int deleteByAccountAndDate(Map<String, Object> map) throws Exception {
      return this.memStateMapper.deleteByAccountAndDate(map);
   }
}
