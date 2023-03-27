package com.wgcloud.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.DockerInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.mapper.DockerInfoMapper;
import com.wgcloud.mapper.DockerStateMapper;
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
public class DockerInfoService {
   private static final Logger logger = LoggerFactory.getLogger(DockerInfoService.class);
   @Autowired
   private DockerInfoMapper dockerInfoMapper;
   @Autowired
   private DockerStateMapper dockerStateMapper;
   @Resource
   private HostGroupService hostGroupService;
   @Autowired
   private LogInfoService logInfoService;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<DockerInfo> list = this.dockerInfoMapper.selectByParams(params);
      PageInfo<DockerInfo> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(DockerInfo dockerInfo, HttpServletRequest request) throws Exception {
      dockerInfo.setId(UUIDUtil.getUUID());
      Date nowDate = DateUtil.getNowTime();
      dockerInfo.setCreateTime(nowDate);
      dockerInfo.setDockerSize("0");
      dockerInfo.setMemPer(0.0D);
      dockerInfo.setGatherDockerNames("获取中");
      if (!StringUtils.isEmpty(dockerInfo.getDockerId())) {
         dockerInfo.setDockerId(dockerInfo.getDockerId().trim());
      }

      this.dockerInfoMapper.save(dockerInfo);
      this.addExtDataForm(dockerInfo, request, nowDate);
   }

   private void addExtDataForm(DockerInfo dockerInfo, HttpServletRequest request, Date nowDate) throws Exception {
      String dataFromIndex = request.getParameter("dataFromIndex");
      int rowsLen = 0;
      if (!StringUtils.isEmpty(dataFromIndex)) {
         for(int i = 0; i <= Integer.valueOf(dataFromIndex); ++i) {
            String dockerId = request.getParameter("dockerId_" + i);
            String dockerName = request.getParameter("dockerName_" + i);
            if (!StringUtils.isEmpty(dockerId) && !StringUtils.isEmpty(dockerName)) {
               DockerInfo dockerInfoExt = new DockerInfo();
               BeanUtil.copyProperties(dockerInfo, dockerInfoExt, true);
               dockerInfoExt.setId(UUIDUtil.getUUID());
               dockerInfoExt.setCreateTime(nowDate);
               dockerInfoExt.setDockerId(dockerId);
               dockerInfoExt.setDockerName(dockerName);
               this.dockerInfoMapper.save(dockerInfoExt);
               this.saveLog(request, "添加", dockerInfoExt);
               ++rowsLen;
            }
         }
      }

   }

   public int deleteByHostName(Map<String, Object> map) throws Exception {
      return this.dockerInfoMapper.deleteByHostName(map);
   }

   @Transactional
   public void saveRecord(List<DockerInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            DockerInfo as = (DockerInfo)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.dockerInfoMapper.insertList(recordList);
      }
   }

   public void downByHostName(List<String> recordList) throws Exception {
      if (recordList.size() >= 1) {
         this.dockerInfoMapper.downByHostName(recordList);
      }
   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.dockerInfoMapper.countByParams(params);
   }

   @Transactional
   public int deleteById(String[] id) throws Exception {
      return this.dockerInfoMapper.deleteById(id);
   }

   @Transactional
   public void updateRecord(List<DockerInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         this.dockerInfoMapper.updateList(recordList);
      }
   }

   public void updateById(DockerInfo dockerInfo) throws Exception {
      this.dockerInfoMapper.updateById(dockerInfo);
   }

   public DockerInfo selectById(String id) throws Exception {
      return this.dockerInfoMapper.selectById(id);
   }

   public List<DockerInfo> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.dockerInfoMapper.selectAllByParams(params);
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
            DockerInfo dockerInfo = this.selectById(id);
            if (((List)hostGroupList).size() > 0) {
               dockerInfo.setGroupId(StringUtils.join(groupIdsArr, ","));
            } else {
               dockerInfo.setGroupId("");
            }

            this.updateById(dockerInfo);
            this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "设置DOCKER标签：" + dockerInfo.getHostname(), "标签：" + this.hostGroupService.returnGroupNames((List)hostGroupList), "2");
         }
      }

   }

   public List<HostGroup> setGroupInList(List<DockerInfo> recordList, Model model) throws Exception {
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
         DockerInfo dockerInfo;
         do {
            if (!var6.hasNext()) {
               return hostGroupList;
            }

            dockerInfo = (DockerInfo)var6.next();
         } while(StringUtils.isEmpty(dockerInfo.getGroupId()));

         String groupNames = "";
         String[] var9 = dockerInfo.getGroupId().split(",");
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            String groupId = var9[var11];
            if (hostGroupMap.containsKey(groupId)) {
               groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
            }
         }

         dockerInfo.setGroupId(groupNames);
      }
   }

   public void saveLog(HttpServletRequest request, String action, DockerInfo dockerInfo) {
      if (null != dockerInfo) {
         this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "docker容器监测信息：" + dockerInfo.getHostname() + "，" + dockerInfo.getDockerName(), "docker名称：" + dockerInfo.getDockerName() + "，获取docker方法：" + dockerInfo.getDockerId(), "2");
      }
   }
}
