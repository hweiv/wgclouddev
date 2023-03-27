package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.TcpState;
import com.wgcloud.mapper.TcpStateMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TcpStateService {
   @Autowired
   private TcpStateMapper tcpStateMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<TcpState> list = this.tcpStateMapper.selectByParams(params);
      PageInfo<TcpState> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(TcpState TcpState) throws Exception {
      TcpState.setId(UUIDUtil.getUUID());
      TcpState.setCreateTime(DateUtil.getNowTime());
      TcpState.setDateStr(DateUtil.getDateTimeString(TcpState.getCreateTime()));
      this.tcpStateMapper.save(TcpState);
   }

   public void saveRecord(List<TcpState> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            TcpState as = (TcpState)var2.next();
            as.setId(UUIDUtil.getUUID());
            as.setDateStr(DateUtil.getDateTimeString(as.getCreateTime()));
         }

         this.tcpStateMapper.insertList(recordList);
      }
   }

   public int deleteById(String[] id) throws Exception {
      return this.tcpStateMapper.deleteById(id);
   }

   public TcpState selectById(String id) throws Exception {
      return this.tcpStateMapper.selectById(id);
   }

   public List<TcpState> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.tcpStateMapper.selectAllByParams(params);
   }
}
