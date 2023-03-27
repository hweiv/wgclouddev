package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.mapper.SysLoadStateMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLoadStateService {
   @Autowired
   private SysLoadStateMapper sysLoadStateMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<SysLoadState> list = this.sysLoadStateMapper.selectByParams(params);
      PageInfo<SysLoadState> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(SysLoadState SysLoadState) throws Exception {
      SysLoadState.setId(UUIDUtil.getUUID());
      SysLoadState.setCreateTime(DateUtil.getNowTime());
      this.sysLoadStateMapper.save(SysLoadState);
   }

   public void saveRecord(List<SysLoadState> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            SysLoadState as = (SysLoadState)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.sysLoadStateMapper.insertList(recordList);
      }
   }

   public int deleteById(String[] id) throws Exception {
      return this.sysLoadStateMapper.deleteById(id);
   }

   public SysLoadState selectById(String id) throws Exception {
      return this.sysLoadStateMapper.selectById(id);
   }

   public List<SysLoadState> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.sysLoadStateMapper.selectAllByParams(params);
   }

   public SysLoadState selectMaxByHostname(Map<String, Object> map) throws Exception {
      return this.sysLoadStateMapper.selectMaxByHostname(map);
   }

   public SysLoadState selectAvgByHostname(Map<String, Object> map) throws Exception {
      return this.sysLoadStateMapper.selectAvgByHostname(map);
   }

   public SysLoadState selectMinByHostname(Map<String, Object> map) throws Exception {
      return this.sysLoadStateMapper.selectMinByHostname(map);
   }

   public SysLoadState selectMaxByDate(Map<String, Object> map) throws Exception {
      return this.sysLoadStateMapper.selectMaxByDate(map);
   }

   public int deleteByAccountAndDate(Map<String, Object> map) throws Exception {
      return this.sysLoadStateMapper.deleteByAccountAndDate(map);
   }
}
