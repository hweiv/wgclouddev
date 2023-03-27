package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.FileWarnState;
import com.wgcloud.mapper.FileWarnStateMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileWarnStateService {
   private Logger logger = LoggerFactory.getLogger(FileWarnStateService.class);
   @Autowired
   private FileWarnStateMapper fileWarnStateMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<FileWarnState> list = this.fileWarnStateMapper.selectByParams(params);
      PageInfo<FileWarnState> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public Integer countByParams(Map<String, Object> params) throws Exception {
      return this.fileWarnStateMapper.countByParams(params);
   }

   public void save(FileWarnState FileWarnState) throws Exception {
      FileWarnState.setId(UUIDUtil.getUUID());
      FileWarnState.setCreateTime(DateUtil.getNowTime());
      this.fileWarnStateMapper.save(FileWarnState);
   }

   public void saveRecord(List<FileWarnState> recordList) {
      try {
         if (recordList.size() < 1) {
            return;
         }

         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            FileWarnState as = (FileWarnState)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.fileWarnStateMapper.insertList(recordList);
      } catch (Exception var4) {
         this.logger.error("日志文件监控保存错误", var4);
      }

   }

   public int deleteByFileWarnId(String fileWarnId) throws Exception {
      return this.fileWarnStateMapper.deleteByFileWarnId(fileWarnId);
   }

   public int deleteById(String[] id) throws Exception {
      return this.fileWarnStateMapper.deleteById(id);
   }

   public FileWarnState selectById(String id) throws Exception {
      return this.fileWarnStateMapper.selectById(id);
   }

   public List<FileWarnState> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.fileWarnStateMapper.selectAllByParams(params);
   }

   public int deleteByDate(Map<String, Object> map) throws Exception {
      return this.fileWarnStateMapper.deleteByDate(map);
   }
}
