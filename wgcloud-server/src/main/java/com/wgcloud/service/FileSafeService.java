package com.wgcloud.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.FileSafe;
import com.wgcloud.mapper.FileSafeMapper;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FileSafeService {
   @Autowired
   private FileSafeMapper fileSafeMapper;
   @Autowired
   private LogInfoService logInfoService;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<FileSafe> list = this.fileSafeMapper.selectByParams(params);
      PageInfo<FileSafe> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(FileSafe fileSafe, HttpServletRequest request) throws Exception {
      Date nowDate = new Date();
      fileSafe.setId(UUIDUtil.getUUID());
      fileSafe.setCreateTime(nowDate);
      if (!StringUtils.isEmpty(fileSafe.getFileSign())) {
         fileSafe.setFileSign(fileSafe.getFileSign().trim());
      }

      this.fileSafeMapper.save(fileSafe);
      this.addExtDataForm(fileSafe, request, nowDate);
   }

   private void addExtDataForm(FileSafe fileSafe, HttpServletRequest request, Date nowDate) throws Exception {
      String dataFromIndex = request.getParameter("dataFromIndex");
      if (!StringUtils.isEmpty(dataFromIndex)) {
         for(int i = 0; i <= Integer.valueOf(dataFromIndex); ++i) {
            String fileName = request.getParameter("fileName_" + i);
            String filePath = request.getParameter("filePath_" + i);
            String fileSign = request.getParameter("fileSign_" + i);
            if (!StringUtils.isEmpty(fileName) && !StringUtils.isEmpty(filePath)) {
               FileSafe fileSafeExt = new FileSafe();
               BeanUtil.copyProperties(fileSafe, fileSafeExt, true);
               fileSafeExt.setId(UUIDUtil.getUUID());
               fileSafeExt.setCreateTime(nowDate);
               fileSafeExt.setFileName(fileName);
               fileSafeExt.setFilePath(filePath);
               fileSafeExt.setFileSign(fileSign);
               this.fileSafeMapper.save(fileSafeExt);
               this.saveLog(request, "添加", fileSafeExt);
            }
         }
      }

   }

   public int deleteByHostName(Map<String, Object> map) throws Exception {
      return this.fileSafeMapper.deleteByHostName(map);
   }

   @Transactional
   public void saveRecord(List<FileSafe> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            FileSafe as = (FileSafe)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.fileSafeMapper.insertList(recordList);
      }
   }

   public void downByHostName(List<String> recordList) throws Exception {
      if (recordList.size() >= 1) {
         this.fileSafeMapper.downByHostName(recordList);
      }
   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.fileSafeMapper.countByParams(params);
   }

   @Transactional
   public int deleteById(String[] id) throws Exception {
      return this.fileSafeMapper.deleteById(id);
   }

   @Transactional
   public void updateRecord(List<FileSafe> recordList) throws Exception {
      if (recordList.size() >= 1) {
         this.fileSafeMapper.updateList(recordList);
      }
   }

   public void updateById(FileSafe FileSafe) throws Exception {
      this.fileSafeMapper.updateById(FileSafe);
   }

   public FileSafe selectById(String id) throws Exception {
      return this.fileSafeMapper.selectById(id);
   }

   public List<FileSafe> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.fileSafeMapper.selectAllByParams(params);
   }

   public void saveLog(HttpServletRequest request, String action, FileSafe fileSafe) {
      if (null != fileSafe) {
         this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "文件防篡改监测信息：" + fileSafe.getHostname() + "，" + fileSafe.getFileName(), "文件名称：" + fileSafe.getFileName() + "，文件路径：" + fileSafe.getFilePath(), "2");
      }
   }
}
