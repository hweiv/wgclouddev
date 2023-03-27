package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.CustomInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.mapper.CustomInfoMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class CustomInfoService {
   private static final Logger logger = LoggerFactory.getLogger(CustomInfoService.class);
   @Autowired
   private CustomInfoMapper customInfoMapper;
   @Resource
   private HostGroupService hostGroupService;
   @Autowired
   private LogInfoService logInfoService;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<CustomInfo> list = this.customInfoMapper.selectByParams(params);
      PageInfo<CustomInfo> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(CustomInfo customInfo, HttpServletRequest request) throws Exception {
      customInfo.setId(UUIDUtil.getUUID());
      Date nowDate = DateUtil.getNowTime();
      customInfo.setCreateTime(nowDate);
      customInfo.setState("1");
      if (!StringUtils.isEmpty(customInfo.getCustomShell())) {
         customInfo.setCustomShell(customInfo.getCustomShell().trim());
      }

      this.customInfoMapper.save(customInfo);
   }

   public int deleteByHostName(Map<String, Object> map) throws Exception {
      return this.customInfoMapper.deleteByHostName(map);
   }

   @Transactional
   public void saveRecord(List<CustomInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            CustomInfo as = (CustomInfo)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.customInfoMapper.insertList(recordList);
      }
   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.customInfoMapper.countByParams(params);
   }

   @Transactional
   public int deleteById(String[] id) throws Exception {
      return this.customInfoMapper.deleteById(id);
   }

   @Transactional
   public void updateRecord(List<CustomInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         this.customInfoMapper.updateList(recordList);
      }
   }

   public void downByHostName(List<String> recordList) throws Exception {
      if (recordList.size() >= 1) {
         this.customInfoMapper.downByHostName(recordList);
      }
   }

   public void updateById(CustomInfo CustomInfo) throws Exception {
      this.customInfoMapper.updateById(CustomInfo);
   }

   public CustomInfo selectById(String id) throws Exception {
      return this.customInfoMapper.selectById(id);
   }

   public List<CustomInfo> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.customInfoMapper.selectAllByParams(params);
   }

   public void saveGroupId(String ids, String[] groupIdsArr, HttpServletRequest request) throws Exception {
      List<HostGroup> hostGroupList = new ArrayList();
      HashMap ho;
      if (null != groupIdsArr && groupIdsArr.length > 0) {
         ho = new HashMap();
         ho.put("groupIds", groupIdsArr);
         hostGroupList = this.hostGroupService.selectAllByParams(ho);
      }

      if (!StringUtils.isEmpty(ids)) {
         ho = null;
         String[] var6 = ids.split(",");
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String id = var6[var8];
            CustomInfo customInfo = this.selectById(id);
            if (((List)hostGroupList).size() > 0) {
               customInfo.setGroupId(StringUtils.join(groupIdsArr, ","));
            } else {
               customInfo.setGroupId("");
            }

            this.updateById(customInfo);
            this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "设置自定义监控项标签：" + customInfo.getHostname(), "标签：" + this.hostGroupService.returnGroupNames((List)hostGroupList), "2");
         }
      }

   }

   public List<HostGroup> setGroupInList(List<CustomInfo> recordList, Model model) throws Exception {
      Map<String, Object> params = new HashMap();
      List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params);
      model.addAttribute("hostGroupList", hostGroupList);
      Map<String, String> hostGroupMap = new HashMap();
      Iterator var6 = hostGroupList.iterator();

      while(var6.hasNext()) {
         HostGroup hostGroup = (HostGroup)var6.next();
         hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
      }

      var6 = recordList.iterator();

      while(true) {
         CustomInfo customInfo;
         do {
            if (!var6.hasNext()) {
               return hostGroupList;
            }

            customInfo = (CustomInfo)var6.next();
         } while(StringUtils.isEmpty(customInfo.getGroupId()));

         String groupNames = "";
         String[] var9 = customInfo.getGroupId().split(",");
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            String groupId = var9[var11];
            if (hostGroupMap.containsKey(groupId)) {
               groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
            }
         }

         customInfo.setGroupId(groupNames);
      }
   }

   public void saveLog(HttpServletRequest request, String action, CustomInfo customInfo) {
      if (null != customInfo) {
         this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "自定义监控项信息：" + customInfo.getHostname() + "，" + customInfo.getCustomName(), "自定义监控项信息：" + customInfo.getCustomName(), "2");
      }
   }
}
