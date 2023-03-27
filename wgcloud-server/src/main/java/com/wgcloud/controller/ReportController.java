package com.wgcloud.controller;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.TjbbExcelChartDto;
import com.wgcloud.entity.ReportInfo;
import com.wgcloud.entity.ReportInstance;
import com.wgcloud.service.ReportInfoService;
import com.wgcloud.service.ReportInstanceService;
import com.wgcloud.service.TaskUtilService;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/report"})
public class ReportController {
   private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
   @Autowired
   private TaskUtilService taskUtilService;
   @Resource
   private ReportInfoService reportInfoService;
   @Autowired
   private ReportInstanceService reportInstanceService;
   @Autowired
   private CommonConfig commonConfig;

   private void testThread() {
      Runnable runnable = () -> {
         logger.info("ReportController----------testThread");
      };
      ThreadPoolUtil.executor.execute(runnable);
   }

   @RequestMapping({"list"})
   public String reportInfoList(ReportInfo reportInfo, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         StringBuffer url = new StringBuffer();
         String reportType = request.getParameter("reportType");
         if (!StringUtils.isEmpty(reportInfo.getReportType())) {
            reportType = reportInfo.getReportType();
            params.put("reportType", reportType.trim());
            url.append("&reportType=").append(reportType);
         }

         PageInfo pageInfo = this.reportInfoService.selectByParams(params, reportInfo.getPage(), reportInfo.getPageSize());
         PageUtil.initPageNumber(pageInfo, model);
         model.addAttribute("pageUrl", "/log/list?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
         model.addAttribute("reportInfo", reportInfo);
         if (!StringUtils.isEmpty(request.getParameter(StaticKeys.LICENSE_LICE_FLAGE))) {
            model.addAttribute("msg", "个人版只能查看最近2周报告，请点击页面底部网站，联系我们升级到专业版");
         }
      } catch (Exception var8) {
         logger.error("查询巡检报告列表错误", var8);
      }

      return "report/list";
   }

   @RequestMapping({"view"})
   public String viewReportInfo(Model model, HttpServletRequest request) {
      String id = request.getParameter("id");
      new ReportInfo();

      try {
         ReportInfo reportInfo = this.reportInfoService.selectById(id);
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            Date createTime = reportInfo.getCreateTime();
            long diff = System.currentTimeMillis() - createTime.getTime();
            long diffDays15 = 1296000000L;
            if (diff > diffDays15) {
               return "redirect:/report/list?liceFlage=1";
            }
         }

         model.addAttribute("reportInfo", reportInfo);
         Map<String, Object> params = new HashMap();
         params.put("reportInfoId", id);
         List<ReportInstance> reportInstanceList = this.reportInstanceService.selectAllByParams(params);
         model.addAttribute("reportInstanceList", reportInstanceList);
      } catch (Exception var10) {
         logger.error("查看巡检报告信息错误", var10);
      }

      return "report/view";
   }

   @RequestMapping({"chartExcel"})
   public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
      Object out = null;

      try {
         if (StaticKeys.LICENSE_STATE.equals("1")) {
            String id = request.getParameter("id");
            Map<String, Object> params = new HashMap();
            params.put("reportInfoId", id);
            List<ReportInstance> reportInstanceList = this.reportInstanceService.selectAllByParams(params);
            List<TjbbExcelChartDto> excelList = new ArrayList();

            for(int i = 0; i < reportInstanceList.size(); ++i) {
               ReportInstance reportInstance = (ReportInstance)reportInstanceList.get(i);
               TjbbExcelChartDto excelChartDto = new TjbbExcelChartDto();
               excelChartDto.setInfoKey(reportInstance.getInfoKey());
               excelChartDto.setInfoContent(reportInstance.getInfoContent());
               excelList.add(excelChartDto);
            }

            String fileName = "report_" + id;
            response.setContentType("application/vnd.ms-exce");
            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition", "filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), TjbbExcelChartDto.class).sheet("sheet").doWrite(excelList);
            return;
         }

         response.setContentType("text/html;charset=UTF-8");
         response.getOutputStream().write("The module needs to wgcloud-pro version. Please contact us at www.wgstart.com".getBytes());
      } catch (Exception var21) {
         logger.error("巡检报告导出excel错误", var21);
         return;
      } finally {
         try {
            if (out != null) {
               ((ServletOutputStream)out).close();
            }
         } catch (IOException var20) {
            var20.printStackTrace();
         }

      }

   }
}
