package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.mapper.AccountInfoMapper;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountInfoService {
   @Autowired
   private AccountInfoMapper accountInfoMapper;
   @Autowired
   private LogInfoService logInfoService;
   @Resource
   private SystemInfoService systemInfoService;
   @Resource
   private DbInfoService dbInfoService;
   @Resource
   private HeathMonitorService heathMonitorService;
   @Resource
   private SnmpInfoService snmpInfoService;
   @Resource
   private DceInfoService dceInfoService;
   @Resource
   private FtpInfoService ftpInfoService;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<AccountInfo> list = this.accountInfoMapper.selectByParams(params);
      PageInfo<AccountInfo> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(AccountInfo accountInfo) throws Exception {
      accountInfo.setId(UUIDUtil.getUUID());
      accountInfo.setCreateTime(new Date());
      this.accountInfoMapper.save(accountInfo);
   }

   public void updateHostAccount(AccountInfo accountInfo, String[] hostnames) throws Exception {
      Map<String, Object> params = new HashMap();
      params.put("account", accountInfo.getAccount());
      params.put("accountTarget", "");
      this.systemInfoService.updateToTargetAccount(params);
      if (null != hostnames && hostnames.length != 0) {
         params.put("hostnames", hostnames);
         this.systemInfoService.updateAccountByHostName(params);
      }
   }

   @Transactional
   public int deleteById(String[] id) throws Exception {
      return this.accountInfoMapper.deleteById(id);
   }

   public void updateById(AccountInfo accountInfo) throws Exception {
      this.accountInfoMapper.updateById(accountInfo);
   }

   public AccountInfo selectById(String id) throws Exception {
      return this.accountInfoMapper.selectById(id);
   }

   public List<AccountInfo> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.accountInfoMapper.selectAllByParams(params);
   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.accountInfoMapper.countByParams(params);
   }

   public void clearOthersAccount(Map<String, Object> params) throws Exception {
      params.put("accountTarget", "");
      this.systemInfoService.updateToTargetAccount(params);
      this.dbInfoService.updateToTargetAccount(params);
      this.heathMonitorService.updateToTargetAccount(params);
      this.snmpInfoService.updateToTargetAccount(params);
      this.dceInfoService.updateToTargetAccount(params);
      this.ftpInfoService.updateToTargetAccount(params);
   }

   public void moveToAccount(String accountTarget, String account) throws Exception {
      Map<String, Object> params = new HashMap();
      params.put("accountTarget", accountTarget);
      params.put("account", account);
      this.systemInfoService.updateToTargetAccount(params);
      this.dbInfoService.updateToTargetAccount(params);
      this.heathMonitorService.updateToTargetAccount(params);
      this.snmpInfoService.updateToTargetAccount(params);
      this.dceInfoService.updateToTargetAccount(params);
      this.ftpInfoService.updateToTargetAccount(params);
   }

   public void saveLog(HttpServletRequest request, String action, AccountInfo accountInfo) {
      if (null != accountInfo) {
         this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "成员账号：" + accountInfo.getAccount(), "成员账号：" + accountInfo.getRemark(), "2");
      }
   }
}
