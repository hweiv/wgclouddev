package com.wgcloud.service;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.mapper.HostGroupMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HostGroupService {
   @Autowired
   private HostGroupMapper hostGroupMapper;
   @Autowired
   private LogInfoService logInfoService;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<HostGroup> list = this.hostGroupMapper.selectByParams(params);
      PageInfo<HostGroup> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(HostGroup HostGroup) throws Exception {
      HostGroup.setId(UUIDUtil.getUUID());
      HostGroup.setCreateTime(DateUtil.getNowTime());
      this.hostGroupMapper.save(HostGroup);
   }

   @Transactional
   public int deleteById(String[] id) throws Exception {
      return this.hostGroupMapper.deleteById(id);
   }

   public void updateById(HostGroup HostGroup) throws Exception {
      this.hostGroupMapper.updateById(HostGroup);
   }

   public HostGroup selectById(String id) throws Exception {
      return this.hostGroupMapper.selectById(id);
   }

   public List<HostGroup> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.hostGroupMapper.selectAllByParams(params);
   }

   public String returnGroupNames(List<HostGroup> hostGroupList) {
      if (CollectionUtil.isEmpty(hostGroupList)) {
         return "";
      } else {
         String names = "";

         for(int i = 0; i < hostGroupList.size(); ++i) {
            names = names + ((HostGroup)hostGroupList.get(i)).getGroupName();
            if (i != hostGroupList.size() - 1) {
               names = names + ",";
            }
         }

         return names;
      }
   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.hostGroupMapper.countByParams(params);
   }

   public void saveLog(HttpServletRequest request, String action, HostGroup hostGroup) {
      if (null != hostGroup) {
         this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "分组信息：" + hostGroup.getGroupName(), "主机分组：" + hostGroup.getRemark(), "2");
      }
   }
}
