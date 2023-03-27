package com.wgcloud.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.DceState;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.mapper.DceInfoMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PingUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.staticvar.BatchData;
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
public class DceInfoService {
   private static final Logger logger = LoggerFactory.getLogger(DceInfoService.class);
   @Autowired
   private DceInfoMapper dceInfoMapper;
   @Resource
   private HostGroupService hostGroupService;
   @Autowired
   private LogInfoService logInfoService;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<DceInfo> list = this.dceInfoMapper.selectByParams(params);
      PageInfo<DceInfo> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(DceInfo DceInfo, HttpServletRequest request) throws Exception {
      DceInfo.setId(UUIDUtil.getUUID());
      DceInfo.setResTimes(1);
      Date nowDate = DateUtil.getNowTime();
      DceInfo.setCreateTime(nowDate);
      if (StringUtils.isEmpty(DceInfo.getHostname())) {
         DceInfo.setHostname(DceInfo.getHostname().trim());
      }

      this.dceInfoMapper.save(DceInfo);
      this.addExtDataForm(DceInfo, request, nowDate);
   }

   private void addExtDataForm(DceInfo dceInfo, HttpServletRequest request, Date nowDate) throws Exception {
      String dataFromIndex = request.getParameter("dataFromIndex");
      if (!StringUtils.isEmpty(dataFromIndex)) {
         for(int i = 0; i <= Integer.valueOf(dataFromIndex); ++i) {
            String hostname = request.getParameter("hostname_" + i);
            String remark = request.getParameter("remark_" + i);
            if (!StringUtils.isEmpty(hostname)) {
               DceInfo dceInfoExt = new DceInfo();
               BeanUtil.copyProperties(dceInfo, dceInfoExt, true);
               dceInfoExt.setId(UUIDUtil.getUUID());
               dceInfoExt.setCreateTime(nowDate);
               dceInfoExt.setHostname(hostname);
               dceInfoExt.setRemark(remark);
               this.dceInfoMapper.save(dceInfoExt);
               this.saveLog(request, "添加", dceInfoExt);
            }
         }
      }

   }

   @Transactional
   public void saveRecord(List<DceInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Date now = DateUtil.getNowTime();
         Iterator var3 = recordList.iterator();

         while(var3.hasNext()) {
            DceInfo as = (DceInfo)var3.next();
            as.setResTimes(1);
            as.setId(UUIDUtil.getUUID());
            as.setCreateTime(now);
         }

         this.dceInfoMapper.insertList(recordList);
      }
   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.dceInfoMapper.countByParams(params);
   }

   @Transactional
   public int deleteById(String[] id) throws Exception {
      return this.dceInfoMapper.deleteById(id);
   }

   public void updateById(DceInfo dceInfo) throws Exception {
      if (StringUtils.isEmpty(dceInfo.getHostname())) {
         dceInfo.setHostname(dceInfo.getHostname().trim());
      }

      this.dceInfoMapper.updateById(dceInfo);
   }

   public DceInfo selectById(String id) throws Exception {
      return this.dceInfoMapper.selectById(id);
   }

   public int updateActive(Map<String, Object> params) throws Exception {
      return this.dceInfoMapper.updateActive(params);
   }

   @Transactional
   public void updateRecord(List<DceInfo> recordList) throws Exception {
      this.dceInfoMapper.updateList(recordList);
   }

   public List<DceInfo> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.dceInfoMapper.selectAllByParams(params);
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
            DceInfo dceInfo = this.selectById(id);
            if (((List)hostGroupList).size() > 0) {
               dceInfo.setGroupId(StringUtils.join(groupIdsArr, ","));
            } else {
               dceInfo.setGroupId("");
            }

            this.updateById(dceInfo);
            this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "设置PING标签：" + dceInfo.getHostname(), "标签：" + this.hostGroupService.returnGroupNames((List)hostGroupList), "2");
         }
      }

   }

   public List<HostGroup> setGroupInList(List<DceInfo> recordList, Model model) throws Exception {
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
         DceInfo dceInfo1;
         do {
            if (!var6.hasNext()) {
               return hostGroupList;
            }

            dceInfo1 = (DceInfo)var6.next();
         } while(StringUtils.isEmpty(dceInfo1.getGroupId()));

         String groupNames = "";
         String[] var9 = dceInfo1.getGroupId().split(",");
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            String groupId = var9[var11];
            if (hostGroupMap.containsKey(groupId)) {
               groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
            }
         }

         dceInfo1.setGroupId(groupNames);
      }
   }

   public void saveLog(HttpServletRequest request, String action, DceInfo dceInfo) {
      if (null != dceInfo) {
         this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "数通设备PING监测信息：" + dceInfo.getHostname(), "数通设备：" + dceInfo.getRemark(), "2");
      }
   }

   public void taskThreadHandler(DceInfo h, Date date) {
      long resTimes = PingUtil.ping(h.getHostname(), 1, 3);
      h.setCreateTime(date);
      if (resTimes < 0L && h.getResTimes() < 0) {
         h.setCreateTime((Date)null);
      }

      h.setResTimes(Integer.valueOf(resTimes + ""));

      try {
         this.updateById(h);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      if (resTimes > 0L) {
         DceState dceState = new DceState();
         dceState.setResTimes(h.getResTimes());
         dceState.setDceId(h.getId());
         dceState.setCreateTime(date);
         BatchData.DCE_STATE_LIST.add(dceState);
      }

      if (h.getResTimes() < 0) {
         WarnOtherUtil.sendDceInfo(h, true);
      } else if (null != WarnPools.MEM_WARN_MAP.get(h.getId())) {
         WarnOtherUtil.sendDceInfo(h, false);
      }

   }

   public void updateToTargetAccount(Map<String, Object> params) throws Exception {
      this.dceInfoMapper.updateToTargetAccount(params);
   }
}
