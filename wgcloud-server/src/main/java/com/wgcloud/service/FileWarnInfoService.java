package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.FileWarnInfo;
import com.wgcloud.mapper.FileWarnInfoMapper;
import com.wgcloud.mapper.FileWarnStateMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FileWarnInfoService {
   @Autowired
   private FileWarnInfoMapper fileWarnInfoMapper;
   @Autowired
   private FileWarnStateMapper fileWarnStateMapper;
   @Autowired
   private LogInfoService logInfoService;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<FileWarnInfo> list = this.fileWarnInfoMapper.selectByParams(params);
      PageInfo<FileWarnInfo> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(FileWarnInfo fileWarnInfo) throws Exception {
      fileWarnInfo.setId(UUIDUtil.getUUID());
      fileWarnInfo.setCreateTime(DateUtil.getNowTime());
      if (!StringUtils.isEmpty(fileWarnInfo.getFilePath())) {
         fileWarnInfo.setFilePath(fileWarnInfo.getFilePath().trim());
      }

      this.fileWarnInfoMapper.save(fileWarnInfo);
   }

   public void saveLog(HttpServletRequest request, String action, FileWarnInfo fileWarnInfo) {
      if (null != fileWarnInfo) {
         this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "日志监控：" + fileWarnInfo.getHostname() + "，" + fileWarnInfo.getFilePath(), "文件路径：" + fileWarnInfo.getFilePath(), "2");
      }
   }

   public int deleteByHostName(Map<String, Object> map) throws Exception {
      return this.fileWarnInfoMapper.deleteByHostName(map);
   }

   @Transactional
   public void saveRecord(List<FileWarnInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            FileWarnInfo as = (FileWarnInfo)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.fileWarnInfoMapper.insertList(recordList);
      }
   }

   public Integer countByParams(Map<String, Object> params) throws Exception {
      return this.fileWarnInfoMapper.countByParams(params);
   }

   @Transactional
   public int deleteById(String[] id) throws Exception {
      return this.fileWarnInfoMapper.deleteById(id);
   }

   @Transactional
   public void updateRecord(List<FileWarnInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         this.fileWarnInfoMapper.updateList(recordList);
      }
   }

   public void updateById(FileWarnInfo FileWarnInfo) throws Exception {
      this.fileWarnInfoMapper.updateById(FileWarnInfo);
   }

   public FileWarnInfo selectById(String id) throws Exception {
      return this.fileWarnInfoMapper.selectById(id);
   }

   public List<FileWarnInfo> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.fileWarnInfoMapper.selectAllByParams(params);
   }
}
