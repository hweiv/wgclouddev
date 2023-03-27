package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.Equipment;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.mapper.EquipmentMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class EquipmentService {
   @Autowired
   private EquipmentMapper equipmentMapper;
   @Autowired
   private LogInfoService logInfoService;
   @Resource
   private HostGroupService hostGroupService;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<Equipment> list = this.equipmentMapper.selectByParams(params);
      PageInfo<Equipment> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(Equipment Equipment) throws Exception {
      Equipment.setId(UUIDUtil.getUUID());
      Equipment.setCreateTime(DateUtil.getNowTime());
      this.equipmentMapper.save(Equipment);
   }

   @Transactional
   public int deleteById(String[] id) throws Exception {
      return this.equipmentMapper.deleteById(id);
   }

   public void updateById(Equipment equipment) throws Exception {
      this.equipmentMapper.updateById(equipment);
   }

   public Equipment selectById(String id) throws Exception {
      return this.equipmentMapper.selectById(id);
   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.equipmentMapper.countByParams(params);
   }

   public List<HostGroup> setGroupInList(List<Equipment> recordList, Model model) throws Exception {
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
         Equipment appInfo;
         do {
            if (!var6.hasNext()) {
               return hostGroupList;
            }

            appInfo = (Equipment)var6.next();
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
            Equipment equipment = this.selectById(id);
            if (((List)hostGroupList).size() > 0) {
               equipment.setGroupId(StringUtils.join(groupIdsArr, ","));
            } else {
               equipment.setGroupId("");
            }

            this.updateById(equipment);
            this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "设置资产标签：" + equipment.getName(), "标签：" + this.hostGroupService.returnGroupNames((List)hostGroupList), "2");
         }
      }

   }

   public void saveLog(HttpServletRequest request, String action, Equipment equipment) {
      if (null != equipment) {
         this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "资产信息：" + equipment.getName(), "资产型号：" + equipment.getXinghao(), "2");
      }
   }
}
