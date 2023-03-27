package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.DiskSmart;
import com.wgcloud.mapper.DiskSmartMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiskSmartService {
   @Autowired
   private DiskSmartMapper diskSmartMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<DiskSmart> list = this.diskSmartMapper.selectByParams(params);
      PageInfo<DiskSmart> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(DiskSmart DiskSmart) throws Exception {
      DiskSmart.setId(UUIDUtil.getUUID());
      DiskSmart.setCreateTime(DateUtil.getNowTime());
      this.diskSmartMapper.save(DiskSmart);
   }

   @Transactional
   public void saveRecord(List<DiskSmart> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            DiskSmart as = (DiskSmart)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.diskSmartMapper.insertList(recordList);
      }
   }

   public int deleteById(String[] id) throws Exception {
      return this.diskSmartMapper.deleteById(id);
   }

   public DiskSmart selectById(String id) throws Exception {
      return this.diskSmartMapper.selectById(id);
   }

   public List<DiskSmart> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.diskSmartMapper.selectAllByParams(params);
   }

   public int deleteByAccHname(List<String> recordList) throws Exception {
      return this.diskSmartMapper.deleteByAccHname(recordList);
   }

   public int deleteByAccountAndDate(Map<String, Object> map) throws Exception {
      return this.diskSmartMapper.deleteByAccountAndDate(map);
   }
}
