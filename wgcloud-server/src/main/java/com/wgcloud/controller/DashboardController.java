package com.wgcloud.controller;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.service.TaskUtilService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.ThreadPoolUtil;
import java.util.HashMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/dash"})
public class DashboardController {
   private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
   @Resource
   private DbTableService dbTableService;
   @Resource
   private SystemInfoService systemInfoService;
   @Resource
   private LogInfoService logInfoService;
   @Autowired
   private TaskUtilService taskUtilService;
   @Resource
   private DashboardService dashboardService;
   @Autowired
   CommonConfig commonConfig;

   private void testThread() {
      Runnable runnable = () -> {
         logger.info("DashboardCotroller----------testThread");
      };
      ThreadPoolUtil.executor.execute(runnable);
   }

   @RequestMapping({"main"})
   public String mainList(Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         HostUtil.addAccountquery(request, params);
         int totalSystemInfoSize = this.systemInfoService.countByParams(params);
         model.addAttribute("totalSystemInfoSize", totalSystemInfoSize);
         params.put("state", "1");
         int hostOnLineSize = this.systemInfoService.countByParams(params);
         model.addAttribute("hostOnLineSize", hostOnLineSize);
         double hostOnLinePer = 0.0D;
         if (totalSystemInfoSize != 0) {
            hostOnLinePer = (double)hostOnLineSize / (double)totalSystemInfoSize;
         }

         model.addAttribute("hostOnLinePer", FormatUtil.formatDouble((Double)(hostOnLinePer * 100.0D), 2));
         this.dashboardService.setPieChart(model, totalSystemInfoSize, request);
         this.dashboardService.setMiddleData(model, request);
         this.dashboardService.setMiddleApplicationData(model);
         params.clear();
         HostUtil.addAccountquery(request, params);
         PageInfo pageInfoDbTableList = this.dbTableService.selectByParams(params, 1, 30);
         model.addAttribute("dbTableList", JSONUtil.parseArray(pageInfoDbTableList.getList()));
         AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
         if (null != accountInfo && "user".equals(accountInfo.getRole())) {
            model.addAttribute("sumDiskSizeCache", this.taskUtilService.sumDiskSizeCache(request));
         }

         this.dashboardService.setDashboardTopData(model, totalSystemInfoSize, request);
      } catch (Exception var10) {
         logger.error("监控概要页面信息异常", var10);
         this.logInfoService.save("监控概要页面信息异常", var10.toString(), "2");
      }

      if (request.getParameter("dashView") != null) {
         model.addAttribute("dashViewAutoData", this.commonConfig.getDashViewAutoData());
         return "dashView/index";
      } else {
         return "index";
      }
   }

   @RequestMapping({"hostDraw"})
   public String hostDraw(Model model, HttpServletRequest request) {
      String id = request.getParameter("id");
      if (StringUtils.isEmpty(id)) {
         return "error/500";
      } else {
         try {
            this.dashboardService.hostDraw(model, id);
         } catch (Exception var5) {
            logger.error("主机画像错误", var5);
            this.logInfoService.save("主机画像错误", var5.toString(), "2");
         }

         return "hostDraw/view";
      }
   }
}
