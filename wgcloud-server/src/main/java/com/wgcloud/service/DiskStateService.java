package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.DiskState;
import com.wgcloud.mapper.DiskStateMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiskStateService {
   @Autowired
   private DiskStateMapper diskStateMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<DiskState> list = this.diskStateMapper.selectByParams(params);
      PageInfo<DiskState> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(DiskState DiskState) throws Exception {
      DiskState.setId(UUIDUtil.getUUID());
      DiskState.setCreateTime(DateUtil.getNowTime());
      this.diskStateMapper.save(DiskState);
   }

   @Transactional
   public void saveRecord(List<DiskState> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            DiskState as = (DiskState)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.diskStateMapper.insertList(recordList);
      }
   }

   public int deleteById(String[] id) throws Exception {
      return this.diskStateMapper.deleteById(id);
   }

   public DiskState selectById(String id) throws Exception {
      return this.diskStateMapper.selectById(id);
   }

   public List<DiskState> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.diskStateMapper.selectAllByParams(params);
   }

   public int deleteByAccHname(List<String> recordList) throws Exception {
      return this.diskStateMapper.deleteByAccHname(recordList);
   }

   public int deleteByAccountAndDate(Map<String, Object> map) throws Exception {
      return this.diskStateMapper.deleteByAccountAndDate(map);
   }
}
