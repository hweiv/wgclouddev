package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.FtpInfo;
import com.wgcloud.mapper.FtpInfoMapper;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.JschUtil;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FtpInfoService {
   private static final Logger logger = LoggerFactory.getLogger(FtpInfoService.class);
   @Autowired
   private FtpInfoMapper ftpInfoMapper;
   @Autowired
   private LogInfoService logInfoService;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<FtpInfo> list = this.ftpInfoMapper.selectByParams(params);
      PageInfo<FtpInfo> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(FtpInfo ftpInfo) throws Exception {
      ftpInfo.setId(UUIDUtil.getUUID());
      ftpInfo.setCreateTime(new Date());
      if (!StringUtils.isEmpty(ftpInfo.getFtpHost())) {
         ftpInfo.setFtpHost(ftpInfo.getFtpHost().trim());
      }

      if (!StringUtils.isEmpty(ftpInfo.getPort())) {
         ftpInfo.setPort(ftpInfo.getPort().trim());
      }

      if (StringUtils.isEmpty(ftpInfo.getUserName())) {
         ftpInfo.setUserName(ftpInfo.getUserName().trim());
      }

      if (StringUtils.isEmpty(ftpInfo.getPasswd())) {
         ftpInfo.setPasswd(ftpInfo.getPasswd().trim());
      }

      this.ftpInfoMapper.save(ftpInfo);
   }

   @Transactional
   public void saveRecord(List<FtpInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            FtpInfo as = (FtpInfo)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.ftpInfoMapper.insertList(recordList);
      }
   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.ftpInfoMapper.countByParams(params);
   }

   @Transactional
   public int deleteById(String[] id) throws Exception {
      return this.ftpInfoMapper.deleteById(id);
   }

   public int updateActive(Map<String, Object> params) throws Exception {
      return this.ftpInfoMapper.updateActive(params);
   }

   @Transactional
   public void updateRecord(List<FtpInfo> recordList) throws Exception {
      if (recordList.size() >= 1) {
         this.ftpInfoMapper.updateList(recordList);
      }
   }

   public void updateById(FtpInfo ftpInfo) throws Exception {
      this.ftpInfoMapper.updateById(ftpInfo);
   }

   public FtpInfo selectById(String id) throws Exception {
      return this.ftpInfoMapper.selectById(id);
   }

   public List<FtpInfo> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.ftpInfoMapper.selectAllByParams(params);
   }

   public void taskThreadHandler() {
      Map<String, Object> params = new HashMap();
      Date date = new Date();

      try {
         params.put("active", "1");
         List<FtpInfo> ftpInfos = this.ftpInfoMapper.selectAllByParams(params);
         Iterator var4 = ftpInfos.iterator();

         while(var4.hasNext()) {
            FtpInfo ftpInfo = (FtpInfo)var4.next();
            if (ServerBackupUtil.FTP_ID_LIST.contains(ftpInfo.getId())) {
               logger.info("此ftp/sftp由wgcloud-server-backup监测:" + ftpInfo.getFtpHost());
            } else {
               Runnable runnable = () -> {
                  if ("SFTP".equals(ftpInfo.getFtpType())) {
                     JschUtil.testSFtpSession(ftpInfo);
                  } else {
                     JschUtil.testFTPClient(ftpInfo);
                  }

                  if ("2".equals(ftpInfo.getState())) {
                     ftpInfo.setCreateTime((Date)null);
                  } else {
                     ftpInfo.setCreateTime(date);
                  }

                  try {
                     this.ftpInfoMapper.updateById(ftpInfo);
                  } catch (Exception e) {
                     e.printStackTrace();
                  }

                  if ("2".equals(ftpInfo.getState())) {
                     WarnOtherUtil.sendFtpInfo(ftpInfo, true);
                  } else if (null != WarnPools.MEM_WARN_MAP.get(ftpInfo.getId())) {
                     WarnOtherUtil.sendFtpInfo(ftpInfo, false);
                  }

               };
               ThreadPoolUtil.executor.execute(runnable);
            }
         }
      } catch (Exception var7) {
         logger.error("FTP监控任务错误", var7);
         this.logInfoService.save("FTP监控任务错误", var7.toString(), "2");
      }

   }

   public void saveLog(HttpServletRequest request, String action, FtpInfo ftpInfo) {
      if (null != ftpInfo) {
         this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "ftp监测信息：" + ftpInfo.getFtpName(), "主机：" + ftpInfo.getFtpHost() + "，端口：" + ftpInfo.getPort(), "2");
      }
   }

   public void updateToTargetAccount(Map<String, Object> params) throws Exception {
      this.ftpInfoMapper.updateToTargetAccount(params);
   }
}
