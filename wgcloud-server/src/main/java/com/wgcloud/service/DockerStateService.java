package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.DockerState;
import com.wgcloud.mapper.DockerStateMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class DockerStateService {
   @Autowired
   private DockerStateMapper dockerStateMapper;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<DockerState> list = this.dockerStateMapper.selectByParams(params);
      PageInfo<DockerState> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(DockerState DockerState) throws Exception {
      DockerState.setId(UUIDUtil.getUUID());
      DockerState.setCreateTime(DateUtil.getNowTime());
      DockerState.setDateStr(DateUtil.getDateTimeString(DockerState.getCreateTime()));
      this.dockerStateMapper.save(DockerState);
   }

   public void saveRecord(List<DockerState> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            DockerState as = (DockerState)var2.next();
            as.setId(UUIDUtil.getUUID());
            as.setDateStr(DateUtil.getDateTimeString(as.getCreateTime()));
         }

         this.dockerStateMapper.insertList(recordList);
      }
   }

   public int deleteByAppInfoId(String appInfoId) throws Exception {
      return this.dockerStateMapper.deleteByDockerInfoId(appInfoId);
   }

   public int deleteById(String[] id) throws Exception {
      return this.dockerStateMapper.deleteById(id);
   }

   public DockerState selectById(String id) throws Exception {
      return this.dockerStateMapper.selectById(id);
   }

   public List<DockerState> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.dockerStateMapper.selectAllByParams(params);
   }

   public int deleteByDate(Map<String, Object> map) throws Exception {
      return this.dockerStateMapper.deleteByDate(map);
   }

   public void setSubtitle(Model model, List<DockerState> dockerStateList) {
      Double maxMem = 0.0D;
      Double minMem = 100000.0D;
      Double avgMem = 0.0D;
      Double sumMem = 0.0D;
      Iterator var7 = dockerStateList.iterator();

      while(var7.hasNext()) {
         DockerState dockerState = (DockerState)var7.next();
         if (null != dockerState.getMemPer()) {
            if (dockerState.getMemPer() > maxMem) {
               maxMem = dockerState.getMemPer();
            }

            if (dockerState.getMemPer() < minMem) {
               minMem = dockerState.getMemPer();
            }

            sumMem = sumMem + dockerState.getMemPer();
         }
      }

      if (dockerStateList.size() > 0) {
         avgMem = sumMem / (double)dockerStateList.size();
      } else {
         minMem = 0.0D;
      }

      SubtitleDto memSubtitleDto = new SubtitleDto();
      memSubtitleDto.setAvgValue(FormatUtil.formatDouble((Double)avgMem, 2) + "");
      memSubtitleDto.setMaxValue(maxMem + "");
      memSubtitleDto.setMinValue(minMem + "");
      model.addAttribute("memSubtitleDto", memSubtitleDto);
   }
}
