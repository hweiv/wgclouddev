package com.wgcloud.service;

import com.wgcloud.entity.MailSet;
import com.wgcloud.mapper.MailSetMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailSetService {
   @Autowired
   private MailSetMapper mailSetMapper;
   @Autowired
   private LogInfoService logInfoService;

   public void save(MailSet MailSet) throws Exception {
      MailSet.setId(UUIDUtil.getUUID());
      MailSet.setCreateTime(DateUtil.getNowTime());
      MailSet.setFromMailName(MailSet.getFromMailName().trim());
      MailSet.setFromPwd(MailSet.getFromPwd().trim());
      MailSet.setToMail(MailSet.getToMail().trim());
      MailSet.setSmtpHost(MailSet.getSmtpHost().trim());
      this.mailSetMapper.save(MailSet);
   }

   public void saveLog(HttpServletRequest request, String action, MailSet mailSet) {
      if (null != mailSet) {
         this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "邮件设置信息", "接受邮件：" + mailSet.getToMail(), "2");
      }
   }

   public int deleteById(String[] id) throws Exception {
      return this.mailSetMapper.deleteById(id);
   }

   public List<MailSet> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.mailSetMapper.selectAllByParams(params);
   }

   public int updateById(MailSet MailSet) throws Exception {
      return this.mailSetMapper.updateById(MailSet);
   }
}
