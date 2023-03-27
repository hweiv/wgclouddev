package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.ReportInstance;
import com.wgcloud.mapper.ReportInstanceMapper;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportInstanceService {
   private static final Logger logger = LoggerFactory.getLogger(ReportInstanceService.class);
   @Autowired
   private ReportInstanceMapper reportInstanceMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<ReportInstance> list = this.reportInstanceMapper.selectByParams(params);
      PageInfo<ReportInstance> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void saveRecord(List<ReportInstance> recordList, Date date) throws Exception {
      if (recordList.size() >= 1) {
         try {
            Iterator var3 = recordList.iterator();

            while(var3.hasNext()) {
               ReportInstance as = (ReportInstance)var3.next();
               as.setId(UUIDUtil.getUUID());
               as.setCreateTime(date);
            }

            this.reportInstanceMapper.insertList(recordList);
         } catch (Exception var5) {
            logger.error("批量保存巡检报告从表信息异常：", var5);
         }

      }
   }

   public void save(ReportInstance reportInstance) {
      try {
         this.reportInstanceMapper.save(reportInstance);
      } catch (Exception var3) {
         logger.error("保存巡检报告从表信息异常：", var3);
      }

   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.reportInstanceMapper.countByParams(params);
   }

   public int deleteById(String[] id) throws Exception {
      return this.reportInstanceMapper.deleteById(id);
   }

   public ReportInstance selectById(String id) throws Exception {
      return this.reportInstanceMapper.selectById(id);
   }

   public List<ReportInstance> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.reportInstanceMapper.selectAllByParams(params);
   }

   public int deleteByDate(Map<String, Object> map) throws Exception {
      return this.reportInstanceMapper.deleteByDate(map);
   }

   public void mergeReportInsToList(List<ReportInstance> reportInstanceList, String infoKey, String infoContent, String reportInfoId) {
      ReportInstance reportIns = new ReportInstance();
      reportIns.setInfoKey(infoKey);
      reportIns.setInfoContent(infoContent);
      reportIns.setReportInfoId(reportInfoId);
      reportInstanceList.add(reportIns);
   }
}
