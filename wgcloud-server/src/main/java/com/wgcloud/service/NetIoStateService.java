package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.mapper.NetIoStateMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NetIoStateService {
   @Autowired
   private NetIoStateMapper netIoStateMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<NetIoState> list = this.netIoStateMapper.selectByParams(params);
      PageInfo<NetIoState> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public PageInfo selectTop3(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<NetIoState> list = this.netIoStateMapper.selectTop3(params);
      PageInfo<NetIoState> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(NetIoState NetIoState) throws Exception {
      NetIoState.setId(UUIDUtil.getUUID());
      NetIoState.setCreateTime(DateUtil.getNowTime());
      this.netIoStateMapper.save(NetIoState);
   }

   public void saveRecord(List<NetIoState> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            NetIoState as = (NetIoState)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.netIoStateMapper.insertList(recordList);
      }
   }

   public int deleteById(String[] id) throws Exception {
      return this.netIoStateMapper.deleteById(id);
   }

   public NetIoState selectById(String id) throws Exception {
      return this.netIoStateMapper.selectById(id);
   }

   public List<NetIoState> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.netIoStateMapper.selectAllByParams(params);
   }

   public NetIoState selectMaxByHostname(Map<String, Object> params) throws Exception {
      return this.netIoStateMapper.selectMaxByHostname(params);
   }

   public NetIoState selectAvgByHostname(Map<String, Object> params) throws Exception {
      return this.netIoStateMapper.selectAvgByHostname(params);
   }

   public NetIoState selectMinByHostname(Map<String, Object> params) throws Exception {
      return this.netIoStateMapper.selectMinByHostname(params);
   }

   public NetIoState selectMaxByDate(Map<String, Object> map) throws Exception {
      return this.netIoStateMapper.selectMaxByDate(map);
   }

   public int deleteByAccountAndDate(Map<String, Object> map) throws Exception {
      return this.netIoStateMapper.deleteByAccountAndDate(map);
   }
}
