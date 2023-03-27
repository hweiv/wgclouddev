package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.entity.DbTable;
import com.wgcloud.entity.DbTableCount;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.DbTableCountService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DESUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"/dbTable"})
public class DbTableController {
   private static final Logger logger = LoggerFactory.getLogger(DbTableController.class);
   @Resource
   private DbInfoService dbInfoService;
   @Resource
   private DbTableService dbTableService;
   @Resource
   private DbTableCountService dbTableCountService;
   @Resource
   private LogInfoService logInfoService;
   @Autowired
   private TokenUtils tokenUtils;
   @Resource
   private ExcelExportService excelExportService;
   @Resource
   private DashboardService dashboardService;
   @Autowired
   CommonConfig commonConfig;

   @ResponseBody
   @RequestMapping({"agentList"})
   public String agentList(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
      if (!StringUtils.isEmpty(checkResult)) {
         return checkResult;
      } else {
         HashMap params = new HashMap();

         try {
            params.put("active", "1");
            if (null != agentJsonObject.get("dbInfoIds") && !StringUtils.isEmpty(agentJsonObject.get("dbInfoIds").toString())) {
               params.put("dbInfoIds", agentJsonObject.get("dbInfoIds").toString().split(","));
            }

            List<DbTable> dbTableList = this.dbTableService.selectAllByParams(params);
            List<DbTable> dbTableListResult = new ArrayList();
            Iterator var7 = dbTableList.iterator();

            while(var7.hasNext()) {
               DbTable dbTable = (DbTable)var7.next();
               dbTable.setWhereVal(DESUtil.encryption(dbTable.getWhereVal()));
               String sqlinkey = FormatUtil.haveSqlDanger(dbTable.getWhereVal(), this.commonConfig.getSqlInKeys());
               if (!StringUtils.isEmpty(sqlinkey)) {
                  logger.error("统计SQL语句含有sql敏感字符" + sqlinkey);
               } else {
                  dbTableListResult.add(dbTable);
               }
            }

            return ResDataUtils.resetSuccessJson(dbTableListResult);
         } catch (Exception var10) {
            logger.error("agent获取监控数据表信息错误", var10);
            this.logInfoService.save("agent获取监控数据表信息错误", var10.toString(), "2");
            return ResDataUtils.resetErrorJson(var10.toString());
         }
      }
   }

   @RequestMapping({"list"})
   public String dbTableList(DbTable DbTable, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         LicenseUtil.maxLicense_10(model, request, DbTable);
         StringBuffer url = new StringBuffer();
         String dbInfoId = null;
         if (!StringUtils.isEmpty(DbTable.getDbInfoId())) {
            dbInfoId = DbTable.getDbInfoId();
            params.put("dbInfoId", dbInfoId.trim());
            url.append("&dbInfoId=").append(dbInfoId);
         }

         if (!StringUtils.isEmpty(DbTable.getAccount())) {
            params.put("account", DbTable.getAccount());
            url.append("&account=").append(DbTable.getAccount());
         }

         if (!StringUtils.isEmpty(DbTable.getRemark())) {
            params.put("remark", DbTable.getRemark());
            url.append("&remark=").append(DbTable.getRemark());
         }

         if (!StringUtils.isEmpty(DbTable.getOrderBy())) {
            params.put("orderBy", DbTable.getOrderBy());
            params.put("orderType", DbTable.getOrderType());
            url.append("&orderBy=").append(DbTable.getOrderBy());
            url.append("&orderType=").append(DbTable.getOrderType());
         }

         HostUtil.addAccountquery(request, params);
         PageInfo<DbTable> pageInfo = this.dbTableService.selectByParams(params, DbTable.getPage(), DbTable.getPageSize());
         PageUtil.initPageNumber(pageInfo, model);
         params.clear();
         List<DbInfo> dbInfoList = this.dbInfoService.selectAllByParams(params);
         this.dbInfoService.dbAddLogo(dbInfoList);
         Iterator var9 = pageInfo.getList().iterator();

         while(var9.hasNext()) {
            DbTable dbTable = (DbTable)var9.next();
            Iterator var11 = dbInfoList.iterator();

            while(var11.hasNext()) {
               DbInfo dbInfo = (DbInfo)var11.next();
               if (dbInfo.getId().equals(dbTable.getDbInfoId())) {
                  dbTable.setTableName(dbInfo.getAliasName());
                  dbTable.setImage(dbInfo.getImage());
                  if ("true".equals(this.commonConfig.getUserInfoManage())) {
                     dbTable.setAccount(dbInfo.getAccount());
                  }
               }
            }
         }

         HostUtil.addAccountListModel(model);
         model.addAttribute("pageUrl", "/dbTable/list?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
         model.addAttribute("dbTable", DbTable);
         model.addAttribute("dbInfoList", dbInfoList);
      } catch (Exception var13) {
         logger.error("查询数据表信息错误", var13);
         this.logInfoService.save("查询数据表信息错误", var13.toString(), "2");
      }

      return "mysql/tableList";
   }

   @RequestMapping({"save"})
   public String saveDbTable(DbTable DbTable, Model model, HttpServletRequest request) {
      try {
         String sqlinkey = FormatUtil.haveSqlDanger(DbTable.getWhereVal(), this.commonConfig.getSqlInKeys());
         if (!StringUtils.isEmpty(sqlinkey)) {
            model.addAttribute("dbTable", DbTable);
            List<DbInfo> dbInfoList = this.dbInfoService.selectAllByParams(new HashMap());
            model.addAttribute("dbInfoList", dbInfoList);
            model.addAttribute("sqlInKeys", this.commonConfig.getSqlInKeys());
            model.addAttribute("msg", "统计SQL语句含有sql敏感字符" + sqlinkey + "，请检查");
            return "mysql/addTable";
         }

         if (StringUtils.isEmpty(DbTable.getId())) {
            this.dbTableService.save(DbTable);
            this.dbTableService.saveLog(request, "添加", DbTable);
         } else {
            this.dbTableService.updateById(DbTable);
            this.dbTableService.saveLog(request, "修改", DbTable);
         }
      } catch (Exception var6) {
         logger.error("保存数据表错误", var6);
         this.logInfoService.save("保存数据表错误", var6.toString(), "2");
      }

      return "redirect:/dbTable/list";
   }

   @RequestMapping({"edit"})
   public String editDbTable(DbTable DbTable, Model model, HttpServletRequest request) {
      try {
         String id = request.getParameter("id");
         DbTable dbTableInfo = new DbTable();
         if (!StringUtils.isEmpty(id)) {
            dbTableInfo = this.dbTableService.selectById(id);
         }

         if (!this.isAddContinue()) {
            return "redirect:/dbTable/list?liceFlage=1";
         }

         Map<String, Object> params = new HashMap();
         HostUtil.addAccountquery(request, params);
         List<DbInfo> dbInfoList = this.dbInfoService.selectAllByParams(params);
         List<DbInfo> dbInfoListResult = new ArrayList();
         Iterator var9 = dbInfoList.iterator();

         while(var9.hasNext()) {
            DbInfo dbInfo = (DbInfo)var9.next();
            if (!"redis".equals(dbInfo.getDbType()) && !"mongodb".equals(dbInfo.getDbType())) {
               dbInfoListResult.add(dbInfo);
            }
         }

         model.addAttribute("dbInfoList", dbInfoListResult);
         model.addAttribute("dbTable", dbTableInfo);
         model.addAttribute("sqlInKeys", this.commonConfig.getSqlInKeys());
      } catch (Exception var11) {
         logger.error("查看数据表错误", var11);
         this.logInfoService.save("查看数据表错误", var11.toString(), "2");
      }

      return "mysql/addTable";
   }

   @RequestMapping({"viewChart"})
   public String viewChartDbTable(DbTable DbTable, Model model, HttpServletRequest request) {
      try {
         String id = request.getParameter("id");
         String startTime = request.getParameter("startTime");
         String endTime = request.getParameter("endTime");
         String am = request.getParameter("am");
         if (!StringUtils.isEmpty(id)) {
            DbTable dbTableInfo = this.dbTableService.selectById(id);
            Map<String, Object> params = new HashMap();
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            model.addAttribute("amList", this.dashboardService.getAmList());
            params.put("dbTableId", id);
            List<DbTableCount> dbTableCounts = this.dbTableCountService.selectAllByParams(params);
            model.addAttribute("dbTableCounts", JSONUtil.parseArray(dbTableCounts));
            this.dbTableCountService.setSubtitle(model, dbTableCounts);
            model.addAttribute("dbTable", dbTableInfo);
         }
      } catch (Exception var11) {
         logger.error("查看数据表图表统计错误", var11);
         this.logInfoService.save("查看数据表图表统计错误", var11.toString(), "2");
      }

      return "mysql/tableView";
   }

   @RequestMapping({"chartExcel"})
   public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
      String errorMsg = "数据表统计图导出excel错误";
      String id = request.getParameter("id");
      String startTime = request.getParameter("startTime");
      String endTime = request.getParameter("endTime");
      String am = request.getParameter("am");

      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            response.setContentType("text/html;charset=UTF-8");
            response.getOutputStream().write("The module needs to wgcloud-pro version. Please contact us at www.wgstart.com".getBytes());
            return;
         }

         if (StringUtils.isEmpty(id)) {
            response.setContentType("text/html;charset=UTF-8");
            response.getOutputStream().write("Missing require parameters".getBytes());
            return;
         }

         Map<String, Object> params = new HashMap();
         this.dashboardService.setDateParam(am, startTime, endTime, params, model);
         params.put("dbTableId", id);
         this.excelExportService.exportDbTableExcel(params, response);
      } catch (Exception var10) {
         logger.error(errorMsg, var10);
         this.logInfoService.save(errorMsg, var10.toString(), "2");
      }

   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "删除数据表信息错误";

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            String[] ids = request.getParameter("id").split(",");
            String[] var6 = ids;
            int var7 = ids.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String id = var6[var8];
               DbTable dbTable = this.dbTableService.selectById(id);
               this.dbTableService.saveLog(request, "删除", dbTable);
            }

            this.dbTableService.deleteById(ids);
         }
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "redirect:/dbTable/list";
   }

   private boolean isAddContinue() {
      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            Map<String, Object> params = new HashMap();
            int dbSize = this.dbTableService.countByParams(params);
            if (dbSize >= 10) {
               return false;
            }
         }
      } catch (Exception var3) {
         logger.error("isAddContinue error", var3);
      }

      return true;
   }
}
