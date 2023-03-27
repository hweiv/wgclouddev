package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.MessageDto;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DESUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.MongoDbUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.RedisUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.jdbc.ConnectionUtil;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"/dbInfo"})
public class DbInfoController {
   private static final Logger logger = LoggerFactory.getLogger(DbInfoController.class);
   @Resource
   private DbInfoService dbInfoService;
   @Resource
   private DbTableService dbTableService;
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private ConnectionUtil connectionUtil;
   @Resource
   private RedisUtil redisUtil;
   @Resource
   private MongoDbUtil mongoDbUtil;
   @Autowired
   private CommonConfig commonConfig;
   @Autowired
   private TokenUtils tokenUtils;

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
            if (null != agentJsonObject.get("dbInfoNames") && !StringUtils.isEmpty(agentJsonObject.get("dbInfoNames").toString())) {
               params.put("dbInfoNames", agentJsonObject.get("dbInfoNames").toString().split(","));
            }

            List<DbInfo> dbInfoList = this.dbInfoService.selectAllByParams(params);
            Iterator var6 = dbInfoList.iterator();

            while(var6.hasNext()) {
               DbInfo dbInfo = (DbInfo)var6.next();
               dbInfo.setUserName(DESUtil.encryption(dbInfo.getUserName()));
               dbInfo.setDbUrl(DESUtil.encryption(dbInfo.getDbUrl()));
               dbInfo.setPasswd(DESUtil.encryption(dbInfo.getPasswd()));
            }

            ServerBackupUtil.cacheSaveDbInfoId(dbInfoList);
            return ResDataUtils.resetSuccessJson(dbInfoList);
         } catch (Exception var8) {
            logger.error("agent获取监控数据源信息错误", var8);
            this.logInfoService.save("agent获取监控数据源信息错误", var8.toString(), "2");
            return ResDataUtils.resetErrorJson(var8.toString());
         }
      }
   }

   @ResponseBody
   @RequestMapping({"validate"})
   public String valdateDbInfo(DbInfo DbInfo, Model model, HttpServletRequest request) {
      MessageDto messageDto = new MessageDto();

      try {
         if ("redis".equals(DbInfo.getDbType())) {
            String pong = this.redisUtil.connectRedis(DbInfo);
            if (StringUtils.isEmpty(pong)) {
               messageDto.setCode("1");
               messageDto.setMsg("连接Redis错误，请检查参数是否正确。请在系统信息里查看日志");
            } else {
               messageDto.setCode("0");
               messageDto.setMsg("连接Redis成功");
            }
         } else if ("mongodb".equals(DbInfo.getDbType())) {
            this.mongoDbUtil.connectMongoDb(DbInfo);
            if ("2".equals(DbInfo.getDbState())) {
               messageDto.setCode("1");
               messageDto.setMsg("连接MongoDb错误，请检查参数是否正确。请在系统信息里查看日志");
            } else {
               messageDto.setCode("0");
               messageDto.setMsg("连接MongoDb成功");
            }
         } else {
            JdbcTemplate JdbcTemplate = this.connectionUtil.getJdbcTemplate(DbInfo);
            if (JdbcTemplate == null) {
               messageDto.setCode("1");
               messageDto.setMsg("连接数据库错误，请检查参数是否正确。请在系统信息里查看日志");
            } else {
               messageDto.setCode("0");
               messageDto.setMsg("连接数据库成功");
            }
         }
      } catch (Exception var6) {
         logger.error("测试数据源信息错误", var6);
         this.logInfoService.save("测试数据源信息错误", var6.toString(), "2");
      }

      return JSONUtil.toJsonStr(messageDto);
   }

   @RequestMapping({"list"})
   public String dbInfoList(DbInfo DbInfo, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         LicenseUtil.maxLicense_10(model, request, DbInfo);
         StringBuffer url = new StringBuffer();
         if (!StringUtils.isEmpty(DbInfo.getAccount())) {
            params.put("account", DbInfo.getAccount());
            url.append("&account=").append(DbInfo.getAccount());
         }

         HostUtil.addAccountquery(request, params);
         PageInfo<DbInfo> pageInfo = this.dbInfoService.selectByParams(params, DbInfo.getPage(), DbInfo.getPageSize());
         if ("true".equals(this.commonConfig.getShowWarnCount())) {
            Iterator var7 = pageInfo.getList().iterator();

            while(var7.hasNext()) {
               DbInfo dbInfo = (DbInfo)var7.next();
               String warnQueryWd = "数据源连接失败告警：" + dbInfo.getAliasName();
               this.logInfoService.warnQueryHandle(dbInfo, warnQueryWd);
            }
         }

         this.dbInfoService.dbAddLogo(pageInfo.getList());
         PageUtil.initPageNumber(pageInfo, model);
         HostUtil.addAccountListModel(model);
         model.addAttribute("pageUrl", "/dbInfo/list?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
      } catch (Exception var10) {
         logger.error("查询数据源信息错误", var10);
         this.logInfoService.save("查询数据源信息错误", var10.toString(), "2");
      }

      return "mysql/dbList";
   }

   @RequestMapping({"edit"})
   public String edit(Model model, HttpServletRequest request) {
      String errorMsg = "添加数据源错误";
      String id = request.getParameter("id");
      DbInfo dbInfo = new DbInfo();

      try {
         if (!this.isAddContinue()) {
            return "redirect:/dbInfo/list?liceFlage=1";
         }

         if (StringUtils.isEmpty(id)) {
            model.addAttribute("dbInfo", dbInfo);
            return "mysql/addDb";
         }

         dbInfo = this.dbInfoService.selectById(id);
         model.addAttribute("dbInfo", dbInfo);
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "mysql/addDb";
   }

   @RequestMapping({"save"})
   public String saveDbInfo(DbInfo DbInfo, Model model, HttpServletRequest request) {
      try {
         if (StringUtils.isEmpty(DbInfo.getId())) {
            AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
            if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
               DbInfo.setAccount(accountInfo.getAccount());
            }

            this.dbInfoService.save(DbInfo);
            this.dbInfoService.saveLog(request, "添加", DbInfo);
         } else {
            this.dbInfoService.updateById(DbInfo);
            this.dbInfoService.saveLog(request, "修改", DbInfo);
         }
      } catch (Exception var5) {
         logger.error("保存数据源错误", var5);
         this.logInfoService.save("保存数据源错误", var5.toString(), "2");
      }

      return "redirect:/dbInfo/list?msg=save";
   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "删除数据源信息错误";
      new DbInfo();

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            String[] ids = request.getParameter("id").split(",");
            String[] var7 = ids;
            int var8 = ids.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String id = var7[var9];
               DbInfo DbInfo = this.dbInfoService.selectById(id);
               this.dbInfoService.saveLog(request, "删除", DbInfo);
               this.dbTableService.deleteByDbInfoId(DbInfo.getId());
            }

            this.dbInfoService.deleteById(ids);
         }
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "redirect:/dbInfo/list?msg=del";
   }

   private boolean isAddContinue() {
      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            Map<String, Object> params = new HashMap();
            int dbSize = this.dbInfoService.countByParams(params);
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
