package com.wgcloud.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.mapper.AppInfoMapper;
import com.wgcloud.mapper.AppStateMapper;
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
public class AppInfoService {
   private static final Logger logger = LoggerFactory.getLogger(AppInfoService.class);
   @Autowired
   private AppInfoMapper appInfoMapper;
   @Autowired
   private AppStateMapper appStateMapper;
   @Resource
   private HostGroupService hostGroupService;
   @Autowired
   private LogInfoService logInfoService;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<AppInfo> list = this.appInfoMapper.selectByParams(params);
      PageInfo<AppInfo> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(AppInfo appInfo, HttpServletRequest request) throws Exception {
      appInfo.setId(UUIDUtil.getUUID());
      Date nowDate = new Date();
      appInfo.setCreateTime(nowDate);
      appInfo.setThreadsNum("0");
      appInfo.setCpuPer(0.0D);
      appInfo.setMemPer(0.0D);
      appInfo.setGatherPid("获取中");
      appInfo.setReadBytes("0");
      appInfo.setWritesBytes("0");
      appInfo.setAppTimes("获取中");
      if (!StringUtils.isEmpty(appInfo.getAppPid())) {
         appInfo.setAppPid(appInfo.getAppPid().trim());
      }

      this.appInfoMapper.save(appInfo);
      this.addExtDataForm(appInfo, request, nowDate);
   }

   private void addExtDataForm(AppInfo appInfo, HttpServletRequest request, Date nowDate) throws Exception {
      String dataFromIndex = request.getParameter("dataFromIndex");
      int rowsLen = 0;
      if (!StringUtils.isEmpty(dataFromIndex)) {
         for(int i = 0; i <= Integer.valueOf(dataFromIndex); ++i) {
            String appPid = request.getParameter("appPid_" + i);
            String appName = request.getParameter("appName_" + i);
            if (!StringUtils.isEmpty(appPid) && !StringUtils.isEmpty(appName)) {
               AppInfo appInfoExt = new AppInfo();
               BeanUtil.copyProperties(appInfo, appInfoExt, true);
               appInfoExt.setId(UUIDUtil.getUUID());
               appInfoExt.setCreateTime(nowDate);
               appInfoExt.setAppPid(appPid.trim());
               appInfoExt.setAppName(appName.trim());
               this.appInfoMapper.save(appInfoExt);
               this.saveLog(request, "添加", appInfoExt);
               ++rowsLen;
            }
         }
      }

   }

   public int deleteByHostName(Map<String, Object> map) throws Exception {
      return this.appInfoMapper.deleteByHostName(map);
   }

   @Transactional
   public void saveRecord(List<AppInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            AppInfo as = (AppInfo)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.appInfoMapper.insertList(recordList);
      }
   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.appInfoMapper.countByParams(params);
   }

   @Transactional
   public int deleteById(String[] id) throws Exception {
      return this.appInfoMapper.deleteById(id);
   }

   @Transactional
   public void updateRecord(List<AppInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         this.appInfoMapper.updateList(recordList);
      }
   }

   public void downByHostName(List<String> recordList) throws Exception {
      if (recordList.size() >= 1) {
         this.appInfoMapper.downByHostName(recordList);
      }
   }

   public void updateById(AppInfo appInfo) throws Exception {
      this.appInfoMapper.updateById(appInfo);
   }

   public AppInfo selectById(String id) throws Exception {
      return this.appInfoMapper.selectById(id);
   }

   public List<AppInfo> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.appInfoMapper.selectAllByParams(params);
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
            AppInfo appInfo = this.selectById(id);
            if (((List)hostGroupList).size() > 0) {
               appInfo.setGroupId(StringUtils.join(groupIdsArr, ","));
            } else {
               appInfo.setGroupId("");
            }

            this.updateById(appInfo);
            this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "设置进程标签：" + appInfo.getHostname(), "标签：" + this.hostGroupService.returnGroupNames((List)hostGroupList), "2");
         }
      }

   }

   public List<HostGroup> setGroupInList(List<AppInfo> recordList, Model model) throws Exception {
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
         AppInfo appInfo;
         do {
            if (!var6.hasNext()) {
               return hostGroupList;
            }

            appInfo = (AppInfo)var6.next();
         } while(StringUtils.isEmpty(appInfo.getGroupId()));

         String groupNames = "";
         String[] var9 = appInfo.getGroupId().split(",");
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            String groupId = var9[var11];
            if (hostGroupMap.containsKey(groupId)) {
               groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
            }
         }

         appInfo.setGroupId(groupNames);
      }
   }

   public void saveLog(HttpServletRequest request, String action, AppInfo appInfo) {
      if (null != appInfo) {
         this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "进程监测信息：" + appInfo.getHostname() + "，" + appInfo.getAppName(), "获取进程方法：" + appInfo.getAppPid(), "2");
      }
   }
}
