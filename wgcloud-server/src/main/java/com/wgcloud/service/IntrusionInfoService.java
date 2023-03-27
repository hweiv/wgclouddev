package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.IntrusionInfo;
import com.wgcloud.mapper.IntrusionInfoMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntrusionInfoService {
   @Autowired
   private IntrusionInfoMapper intrusionInfoMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<IntrusionInfo> list = this.intrusionInfoMapper.selectByParams(params);
      PageInfo<IntrusionInfo> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(IntrusionInfo IntrusionInfo) throws Exception {
      IntrusionInfo.setId(UUIDUtil.getUUID());
      IntrusionInfo.setCreateTime(DateUtil.getNowTime());
      this.intrusionInfoMapper.save(IntrusionInfo);
   }

   public void saveRecord(List<IntrusionInfo> recordList) throws Exception {
      Map<String, Object> map = new HashMap();
      Iterator var3 = recordList.iterator();

      while(var3.hasNext()) {
         IntrusionInfo as = (IntrusionInfo)var3.next();
         as.setId(UUIDUtil.getUUID());
         map.put("hostname", as.getHostname());
         this.intrusionInfoMapper.deleteByAccHname(map);
      }

      this.intrusionInfoMapper.insertList(recordList);
   }

   public int deleteById(String[] id) throws Exception {
      return this.intrusionInfoMapper.deleteById(id);
   }

   public IntrusionInfo selectById(String id) throws Exception {
      return this.intrusionInfoMapper.selectById(id);
   }

   public List<IntrusionInfo> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.intrusionInfoMapper.selectAllByParams(params);
   }

   public List<IntrusionInfo> selectByAccountId(String accountId) throws Exception {
      return this.intrusionInfoMapper.selectByAccountId(accountId);
   }
}
