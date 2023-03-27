package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.LogInfo;
import com.wgcloud.mapper.LogInfoMapper;
import com.wgcloud.util.UUIDUtil;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogInfoService {
   private static final Logger logger = LoggerFactory.getLogger(LogInfoService.class);
   @Autowired
   private LogInfoMapper logInfoMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<LogInfo> list = this.logInfoMapper.selectByParams(params);
      PageInfo<LogInfo> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void saveRecord(List<LogInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Date date = new Date();

         try {
            Iterator var3 = recordList.iterator();

            while(var3.hasNext()) {
               LogInfo as = (LogInfo)var3.next();
               as.setId(UUIDUtil.getUUID());
               as.setCreateTime(date);
            }

            this.logInfoMapper.insertList(recordList);
         } catch (Exception var5) {
            logger.error("批量保存日志信息异常：", var5);
         }

      }
   }

   public void save(String hostname, String infoContent, String state) {
      LogInfo logInfo = new LogInfo();
      logInfo.setHostname(hostname);
      logInfo.setInfoContent(infoContent);
      logInfo.setState(state);
      logInfo.setId(UUIDUtil.getUUID());
      logInfo.setCreateTime(new Date());

      try {
         this.logInfoMapper.save(logInfo);
      } catch (Exception var6) {
         logger.error("保存日志信息异常：", var6);
      }

   }

   public void warnQueryHandle(Object object, String warnQueryWd) {
      try {
         Map<String, Object> paramsLogInfo = new HashMap();
         paramsLogInfo.put("hostname", warnQueryWd);
         paramsLogInfo.put("hostnameNe", "已恢复");
         Integer resultCount = this.countByParams(paramsLogInfo);
         Class superClazz = object.getClass();
         Method warnCountSetMethod = superClazz.getDeclaredMethod("setWarnCount", Integer.class);
         Method warnQueryWdSetMethod = superClazz.getDeclaredMethod("setWarnQueryWd", String.class);
         warnCountSetMethod.invoke(object, resultCount);
         warnQueryWdSetMethod.invoke(object, warnQueryWd);
      } catch (Exception var8) {
         logger.error("warnQueryHandle", var8);
      }

   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.logInfoMapper.countByParams(params);
   }

   public int deleteById(String[] id) throws Exception {
      return this.logInfoMapper.deleteById(id);
   }

   public LogInfo selectById(String id) throws Exception {
      return this.logInfoMapper.selectById(id);
   }

   public List<LogInfo> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.logInfoMapper.selectAllByParams(params);
   }

   public int deleteByDate(Map<String, Object> map) throws Exception {
      return this.logInfoMapper.deleteByDate(map);
   }
}
