package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.DeskIo;
import com.wgcloud.mapper.DeskIoMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeskIoService {
   @Autowired
   private DeskIoMapper deskIoMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<DeskIo> list = this.deskIoMapper.selectByParams(params);
      PageInfo<DeskIo> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(DeskIo DeskIo) throws Exception {
      DeskIo.setId(UUIDUtil.getUUID());
      DeskIo.setCreateTime(DateUtil.getNowTime());
      this.deskIoMapper.save(DeskIo);
   }

   @Transactional
   public void saveRecord(List<DeskIo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            DeskIo as = (DeskIo)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.deskIoMapper.insertList(recordList);
      }
   }

   public int deleteById(String[] id) throws Exception {
      return this.deskIoMapper.deleteById(id);
   }

   public DeskIo selectById(String id) throws Exception {
      return this.deskIoMapper.selectById(id);
   }

   public List<DeskIo> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.deskIoMapper.selectAllByParams(params);
   }

   public int deleteByAccHname(List<String> recordList) throws Exception {
      return this.deskIoMapper.deleteByAccHname(recordList);
   }

   public int deleteByAccountAndDate(Map<String, Object> map) throws Exception {
      return this.deskIoMapper.deleteByAccountAndDate(map);
   }
}
