package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.ShellInfo;
import com.wgcloud.entity.ShellState;
import com.wgcloud.mapper.ShellInfoMapper;
import com.wgcloud.mapper.ShellStateMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class ShellInfoService {
   @Autowired
   private ShellInfoMapper shellInfoMapper;
   @Autowired
   private ShellStateMapper shellStateMapper;
   @Autowired
   private ShellStateService shellStateService;
   @Autowired
   private CommonConfig commonConfig;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<ShellInfo> list = this.shellInfoMapper.selectByParams(params);
      PageInfo<ShellInfo> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(ShellInfo shellInfo, String[] hostnames) throws Exception {
      shellInfo.setId(UUIDUtil.getUUID());
      shellInfo.setState("1");
      shellInfo.setCreateTime(DateUtil.getNowTime());
      if (!StringUtils.isEmpty(shellInfo.getShell())) {
         shellInfo.setShell(shellInfo.getShell().trim());
      }

      List<ShellState> shellStateList = new ArrayList();
      String[] var4 = hostnames;
      int var5 = hostnames.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String hostname = var4[var6];
         ShellState shellState = new ShellState();
         shellState.setShellId(shellInfo.getId());
         shellState.setHostname(hostname);
         shellState.setCreateTime(shellInfo.getCreateTime());
         shellState.setShell(shellInfo.getShell());
         shellState.setState("1");
         if ("2".equals(shellInfo.getShellType())) {
            shellState.setShellTime(DateUtil.getDate(shellInfo.getShellTime()).getTime() + "");
         } else {
            shellState.setShellTime("0");
         }

         shellStateList.add(shellState);
      }

      this.shellInfoMapper.save(shellInfo);
      this.shellStateService.saveRecord(shellStateList);
   }

   @Transactional
   public void saveRecord(List<ShellInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            ShellInfo as = (ShellInfo)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.shellInfoMapper.insertList(recordList);
      }
   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.shellInfoMapper.countByParams(params);
   }

   @Transactional
   public int deleteById(String[] id) throws Exception {
      String[] var2 = id;
      int var3 = id.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String shellInfoId = var2[var4];
         this.shellStateMapper.cancelByShellId(shellInfoId);
      }

      return this.shellInfoMapper.deleteById(id);
   }

   @Transactional
   public void updateRecord(List<ShellInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         this.shellInfoMapper.updateList(recordList);
      }
   }

   public void updateById(ShellInfo ShellInfo) throws Exception {
      this.shellInfoMapper.updateById(ShellInfo);
   }

   @Transactional
   public void cancelShell(String id) throws Exception {
      ShellInfo shellInfo = this.shellInfoMapper.selectById(id);
      shellInfo.setState("2");
      this.shellInfoMapper.updateById(shellInfo);
      this.shellStateMapper.cancelByShellId(id);
   }

   @Transactional
   public void restart(String id) throws Exception {
      ShellInfo shellInfo = this.shellInfoMapper.selectById(id);
      shellInfo.setState("1");
      shellInfo.setCreateTime(new Date());
      this.shellInfoMapper.updateById(shellInfo);
      String shellTime = "0";
      if ("2".equals(shellInfo.getShellType())) {
         shellTime = DateUtil.getDate(shellInfo.getShellTime()).getTime() + "";
      }

      this.shellStateMapper.restartByShellId(id, shellTime);
   }

   public ShellInfo selectById(String id) throws Exception {
      return this.shellInfoMapper.selectById(id);
   }

   public List<ShellInfo> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.shellInfoMapper.selectAllByParams(params);
   }

   public void getBlockStr(Model model) {
      String blockStr = "";
      if (!StringUtils.isEmpty(this.commonConfig.getShellToRunLinuxBlock())) {
         blockStr = "[Linux不能包含敏感字符:" + this.commonConfig.getShellToRunLinuxBlock() + "]";
      }

      if (!StringUtils.isEmpty(this.commonConfig.getShellToRunLinuxBlock())) {
         blockStr = blockStr + "[Windows不能包含敏感字符:" + this.commonConfig.getShellToRunWinBlock() + "]";
      }

      model.addAttribute("blockStr", blockStr);
   }
}
