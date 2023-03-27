package com.wgcloud.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.PortInfo;
import com.wgcloud.mapper.PortInfoMapper;
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
public class PortInfoService {
   private static final Logger logger = LoggerFactory.getLogger(PortInfoService.class);
   @Autowired
   private PortInfoMapper portInfoMapper;
   @Resource
   private HostGroupService hostGroupService;
   @Autowired
   private LogInfoService logInfoService;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<PortInfo> list = this.portInfoMapper.selectByParams(params);
      PageInfo<PortInfo> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(PortInfo portInfo, HttpServletRequest request) throws Exception {
      portInfo.setId(UUIDUtil.getUUID());
      Date nowDate = DateUtil.getNowTime();
      portInfo.setCreateTime(nowDate);
      if (!StringUtils.isEmpty(portInfo.getPort())) {
         portInfo.setPort(portInfo.getPort().trim());
      }

      if (!StringUtils.isEmpty(portInfo.getPort())) {
         this.portInfoMapper.save(portInfo);
         this.addExtDataForm(portInfo, request, nowDate);
      }
   }

   private void addExtDataForm(PortInfo portInfo, HttpServletRequest request, Date nowDate) throws Exception {
      String dataFromIndex = request.getParameter("dataFromIndex");
      int rowsLen = 0;
      if (!StringUtils.isEmpty(dataFromIndex)) {
         for(int i = 0; i <= Integer.valueOf(dataFromIndex); ++i) {
            String port = request.getParameter("port_" + i);
            String portName = request.getParameter("portName_" + i);
            if (!StringUtils.isEmpty(port) && !StringUtils.isEmpty(portName)) {
               PortInfo portInfoExt = new PortInfo();
               BeanUtil.copyProperties(portInfo, portInfoExt, true);
               portInfoExt.setId(UUIDUtil.getUUID());
               portInfoExt.setCreateTime(nowDate);
               portInfoExt.setPort(port.trim());
               portInfoExt.setPortName(portName.trim());
               this.portInfoMapper.save(portInfoExt);
               this.saveLog(request, "添加", portInfoExt);
               ++rowsLen;
            }
         }
      }

   }

   public int deleteByHostName(Map<String, Object> map) throws Exception {
      return this.portInfoMapper.deleteByHostName(map);
   }

   @Transactional
   public void saveRecord(List<PortInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            PortInfo as = (PortInfo)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.portInfoMapper.insertList(recordList);
      }
   }

   public void downByHostName(List<String> recordList) throws Exception {
      if (recordList.size() >= 1) {
         this.portInfoMapper.downByHostName(recordList);
      }
   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.portInfoMapper.countByParams(params);
   }

   @Transactional
   public int deleteById(String[] id) throws Exception {
      return this.portInfoMapper.deleteById(id);
   }

   @Transactional
   public void updateRecord(List<PortInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         this.portInfoMapper.updateList(recordList);
      }
   }

   public void updateById(PortInfo portInfo) throws Exception {
      this.portInfoMapper.updateById(portInfo);
   }

   public PortInfo selectById(String id) throws Exception {
      return this.portInfoMapper.selectById(id);
   }

   public List<PortInfo> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.portInfoMapper.selectAllByParams(params);
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
//            PortInfo ho = this.selectById(id);
            PortInfo portInfo = this.selectById(id);
            if (((List)hostGroupList).size() > 0) {
               portInfo.setGroupId(StringUtils.join(groupIdsArr, ","));
            } else {
               portInfo.setGroupId("");
            }

            this.updateById(portInfo);
            this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "设置端口标签：" + portInfo.getHostname(), "标签：" + this.hostGroupService.returnGroupNames((List)hostGroupList), "2");
         }
      }

   }

   public List<HostGroup> setGroupInList(List<PortInfo> recordList, Model model) throws Exception {
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
         PortInfo portInfo;
         do {
            if (!var6.hasNext()) {
               return hostGroupList;
            }

            portInfo = (PortInfo)var6.next();
         } while(StringUtils.isEmpty(portInfo.getGroupId()));

         String groupNames = "";
         String[] var9 = portInfo.getGroupId().split(",");
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            String groupId = var9[var11];
            if (hostGroupMap.containsKey(groupId)) {
               groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
            }
         }

         portInfo.setGroupId(groupNames);
      }
   }

   public void saveLog(HttpServletRequest request, String action, PortInfo portInfo) {
      if (null != portInfo) {
         this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "端口监测信息：" + portInfo.getHostname() + "，" + portInfo.getPortName(), "telnet " + portInfo.getTelnetIp() + " " + portInfo.getPort(), "2");
      }
   }
}
