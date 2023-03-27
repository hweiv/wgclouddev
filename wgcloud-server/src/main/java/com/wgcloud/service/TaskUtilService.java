package com.wgcloud.service;

import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import java.math.BigDecimal;
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
public class TaskUtilService {
   private Logger logger = LoggerFactory.getLogger(TaskUtilService.class);
   @Autowired
   SystemInfoService systemInfoService;
   @Autowired
   AppInfoService appInfoService;
   @Autowired
   DockerInfoService dockerInfoService;
   @Autowired
   PortInfoService portInfoService;
   @Autowired
   DiskStateService diskStateService;

   @Transactional
   public void refreshCommitDate() throws Exception {
      Date nowDate = new Date();
      this.logger.info("刷新监控数据更新时间：" + nowDate);
      Map<String, Object> params = new HashMap();
      params.put("state", "1");
      List<SystemInfo> systemInfolist = this.systemInfoService.selectAllByParams(params);
      Iterator var4 = systemInfolist.iterator();

      while(var4.hasNext()) {
         SystemInfo systemInfo = (SystemInfo)var4.next();
         systemInfo.setCreateTime(nowDate);
      }

      this.systemInfoService.updateRecord(systemInfolist);
   }

   public String sumDiskSizeCache(HttpServletRequest request) throws Exception {
      Map<String, Object> params = new HashMap();
      HostUtil.addAccountquery(request, params);
      List<DiskState> deskStateList = this.diskStateService.selectAllByParams(params);
      BigDecimal sumSize = new BigDecimal(0);
      Iterator var5 = deskStateList.iterator();

      while(var5.hasNext()) {
         DiskState deskState = (DiskState)var5.next();
         if (!StringUtils.isEmpty(deskState.getDiskSize())) {
            try {
               sumSize = sumSize.add(new BigDecimal(deskState.getDiskSize().replace("G", "")));
            } catch (Exception var8) {
               this.logger.error("double类型转换错误：", var8);
            }
         }
      }

      String sumSizeStr = String.valueOf(sumSize);
      if (sumSizeStr.indexOf(".") > 0) {
         sumSizeStr = sumSizeStr.substring(0, sumSizeStr.lastIndexOf("."));
      }

      return FormatUtil.gToT(sumSizeStr);
   }
}
