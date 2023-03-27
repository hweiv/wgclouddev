package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.entity.DeskIo;
import com.wgcloud.entity.DiskSmart;
import com.wgcloud.entity.DiskState;
import com.wgcloud.service.CpuStateService;
import com.wgcloud.service.CpuTemperaturesService;
import com.wgcloud.service.DeskIoService;
import com.wgcloud.service.DiskSmartService;
import com.wgcloud.service.DiskStateService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.MemStateService;
import com.wgcloud.service.NetIoStateService;
import com.wgcloud.service.SysLoadStateService;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnOtherUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/systemInfoOpen"})
public class SystemInfoOpenController {
   private static final Logger logger = LoggerFactory.getLogger(SystemInfoOpenController.class);
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private CpuStateService cpuStateService;
   @Resource
   private DiskStateService diskStateService;
   @Resource
   private DiskSmartService diskSmartService;
   @Resource
   private DeskIoService deskIoService;
   @Resource
   private CpuTemperaturesService cpuTemperaturesService;
   @Resource
   private MemStateService memStateService;
   @Resource
   private NetIoStateService netIoStateService;
   @Resource
   private SysLoadStateService sysLoadStateService;
   @Autowired
   private CommonConfig commonConfig;
   @Autowired
   private TokenUtils tokenUtils;

   @ResponseBody
   @RequestMapping({"agentDiskSmartList"})
   public String agentDiskSmartList(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
      if (!StringUtils.isEmpty(checkResult)) {
         return checkResult;
      } else {
         Map<String, Object> params = new HashMap();
         if (null != agentJsonObject.get("hostname") && !StringUtils.isEmpty(agentJsonObject.get("hostname").toString())) {
            params.put("hostname", agentJsonObject.get("hostname").toString());

            try {
               List<DiskSmart> diskSmarts = this.diskSmartService.selectAllByParams(params);
               return ResDataUtils.resetSuccessJson(diskSmarts);
            } catch (Exception var6) {
               logger.error("agent查询根据ip查询磁盘smar列表错误", var6);
               this.logInfoService.save("agent查询根据ip查询磁盘smar列表错误", var6.toString(), "2");
               return ResDataUtils.resetErrorJson(var6.toString());
            }
         } else {
            return ResDataUtils.resetErrorJson("Missing require parameters");
         }
      }
   }

   @ResponseBody
   @RequestMapping({"agentDeskStatesList"})
   public String agentDeskStatesList(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
      if (!StringUtils.isEmpty(checkResult)) {
         return checkResult;
      } else {
         Map<String, Object> params = new HashMap();
         if (null != agentJsonObject.get("hostname") && !StringUtils.isEmpty(agentJsonObject.get("hostname").toString())) {
            params.put("hostname", agentJsonObject.get("hostname").toString());

            try {
               List<DiskState> deskStates = this.diskStateService.selectAllByParams(params);
               return ResDataUtils.resetSuccessJson(deskStates);
            } catch (Exception var6) {
               logger.error("agent查询根据ip查询磁盘列表错误", var6);
               this.logInfoService.save("agent查询根据ip查询磁盘列表错误", var6.toString(), "2");
               return ResDataUtils.resetErrorJson(var6.toString());
            }
         } else {
            return ResDataUtils.resetErrorJson("Missing require parameters");
         }
      }
   }

   @ResponseBody
   @RequestMapping({"agentDiskIosList"})
   public String agentDiskIosList(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
      if (!StringUtils.isEmpty(checkResult)) {
         return checkResult;
      } else {
         Map<String, Object> params = new HashMap();
         if (null != agentJsonObject.get("hostname") && !StringUtils.isEmpty(agentJsonObject.get("hostname").toString())) {
            params.put("hostname", agentJsonObject.get("hostname").toString());

            try {
               List<DeskIo> deskIos = this.deskIoService.selectAllByParams(params);
               return ResDataUtils.resetSuccessJson(deskIos);
            } catch (Exception var6) {
               logger.error("agent查询根据ip查询磁盘io列表错误", var6);
               this.logInfoService.save("agent查询根据ip查询磁盘io列表错误", var6.toString(), "2");
               return ResDataUtils.resetErrorJson(var6.toString());
            }
         } else {
            return ResDataUtils.resetErrorJson("Missing require parameters");
         }
      }
   }

   @ResponseBody
   @RequestMapping({"agentCpuTemperaturesList"})
   public String agentCpuTemperaturesList(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
      if (!StringUtils.isEmpty(checkResult)) {
         return checkResult;
      } else {
         Map<String, Object> params = new HashMap();
         if (null != agentJsonObject.get("hostname") && !StringUtils.isEmpty(agentJsonObject.get("hostname").toString())) {
            params.put("hostname", agentJsonObject.get("hostname").toString());

            try {
               List<CpuTemperatures> cpuTemperatures = this.cpuTemperaturesService.selectAllByParams(params);
               return ResDataUtils.resetSuccessJson(cpuTemperatures);
            } catch (Exception var6) {
               logger.error("agent查询根据ip查询cpu温度列表错误", var6);
               this.logInfoService.save("agent查询根据ip查询cpu温度列表错误", var6.toString(), "2");
               return ResDataUtils.resetErrorJson(var6.toString());
            }
         } else {
            return ResDataUtils.resetErrorJson("Missing require parameters");
         }
      }
   }

   @ResponseBody
   @RequestMapping({"agentNetIoStatesList"})
   public String agentNetIoStatesList(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
      if (!StringUtils.isEmpty(checkResult)) {
         return checkResult;
      } else {
         Map<String, Object> params = new HashMap();
         if (null != agentJsonObject.get("hostname") && !StringUtils.isEmpty(agentJsonObject.get("hostname").toString())) {
            params.put("hostname", agentJsonObject.get("hostname").toString());
            if (null != agentJsonObject.get("startTime")) {
               params.put("startTime", agentJsonObject.get("startTime").toString());
            }

            if (null != agentJsonObject.get("endTime")) {
               params.put("endTime", agentJsonObject.get("endTime").toString());
            }

            if (!StringUtils.isEmpty(agentJsonObject.getStr("orderBy"))) {
               params.put("orderBy", agentJsonObject.getStr("orderBy"));
               params.put("orderType", agentJsonObject.getStr("orderType"));
            }

            try {
               PageInfo netIoStates = this.netIoStateService.selectByParams(params, agentJsonObject.getInt("page"), agentJsonObject.getInt("pageSize"));
               return ResDataUtils.resetSuccessJson(netIoStates);
            } catch (Exception var6) {
               logger.error("agent查询根据ip查询系统负载列表错误", var6);
               this.logInfoService.save("agent查询根据ip查询系统负载列表错误", var6.toString(), "2");
               return ResDataUtils.resetErrorJson(var6.toString());
            }
         } else {
            return ResDataUtils.resetErrorJson("Missing require parameters");
         }
      }
   }

   @ResponseBody
   @RequestMapping({"agentCpuStatesList"})
   public String agentCpuStatesList(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
      if (!StringUtils.isEmpty(checkResult)) {
         return checkResult;
      } else {
         Map<String, Object> params = new HashMap();
         if (null != agentJsonObject.get("hostname") && !StringUtils.isEmpty(agentJsonObject.get("hostname").toString())) {
            params.put("hostname", agentJsonObject.get("hostname").toString());
            if (null != agentJsonObject.get("startTime")) {
               params.put("startTime", agentJsonObject.get("startTime").toString());
            }

            if (null != agentJsonObject.get("endTime")) {
               params.put("endTime", agentJsonObject.get("endTime").toString());
            }

            if (!StringUtils.isEmpty(agentJsonObject.getStr("orderBy"))) {
               params.put("orderBy", agentJsonObject.getStr("orderBy"));
               params.put("orderType", agentJsonObject.getStr("orderType"));
            }

            try {
               PageInfo cpuStates = this.cpuStateService.selectByParams(params, agentJsonObject.getInt("page"), agentJsonObject.getInt("pageSize"));
               return ResDataUtils.resetSuccessJson(cpuStates);
            } catch (Exception var6) {
               logger.error("agent查询根据ip查询系统cpu监控数据列表错误", var6);
               this.logInfoService.save("agent查询根据ip查询系统cpu监控数据列表错误", var6.toString(), "2");
               return ResDataUtils.resetErrorJson(var6.toString());
            }
         } else {
            return ResDataUtils.resetErrorJson("Missing require parameters");
         }
      }
   }

   @ResponseBody
   @RequestMapping({"agentMemStatesList"})
   public String agentMemStatesList(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
      if (!StringUtils.isEmpty(checkResult)) {
         return checkResult;
      } else {
         Map<String, Object> params = new HashMap();
         if (null != agentJsonObject.get("hostname") && !StringUtils.isEmpty(agentJsonObject.get("hostname").toString())) {
            params.put("hostname", agentJsonObject.get("hostname").toString());
            if (null != agentJsonObject.get("startTime")) {
               params.put("startTime", agentJsonObject.get("startTime").toString());
            }

            if (null != agentJsonObject.get("endTime")) {
               params.put("endTime", agentJsonObject.get("endTime").toString());
            }

            if (!StringUtils.isEmpty(agentJsonObject.getStr("orderBy"))) {
               params.put("orderBy", agentJsonObject.getStr("orderBy"));
               params.put("orderType", agentJsonObject.getStr("orderType"));
            }

            try {
               PageInfo cpuStates = this.memStateService.selectByParams(params, agentJsonObject.getInt("page"), agentJsonObject.getInt("pageSize"));
               return ResDataUtils.resetSuccessJson(cpuStates);
            } catch (Exception var6) {
               logger.error("agent查询根据ip查询系统内存监控数据列表错误", var6);
               this.logInfoService.save("agent查询根据ip查询系统内存监控数据列表错误", var6.toString(), "2");
               return ResDataUtils.resetErrorJson(var6.toString());
            }
         } else {
            return ResDataUtils.resetErrorJson("Missing require parameters");
         }
      }
   }

   @ResponseBody
   @RequestMapping({"agentSysLoadStatesList"})
   public String agentSysLoadStatesList(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
      if (!StringUtils.isEmpty(checkResult)) {
         return checkResult;
      } else {
         Map<String, Object> params = new HashMap();
         if (null != agentJsonObject.get("hostname") && !StringUtils.isEmpty(agentJsonObject.get("hostname").toString())) {
            params.put("hostname", agentJsonObject.get("hostname").toString());
            if (null != agentJsonObject.get("startTime")) {
               params.put("startTime", agentJsonObject.get("startTime").toString());
            }

            if (null != agentJsonObject.get("endTime")) {
               params.put("endTime", agentJsonObject.get("endTime").toString());
            }

            if (!StringUtils.isEmpty(agentJsonObject.getStr("orderBy"))) {
               params.put("orderBy", agentJsonObject.getStr("orderBy"));
               params.put("orderType", agentJsonObject.getStr("orderType"));
            }

            try {
               PageInfo cpuStates = this.sysLoadStateService.selectByParams(params, agentJsonObject.getInt("page"), agentJsonObject.getInt("pageSize"));
               return ResDataUtils.resetSuccessJson(cpuStates);
            } catch (Exception var6) {
               logger.error("aagent查询根据ip查询系统负载监控数据列表错误", var6);
               this.logInfoService.save("agent查询根据ip查询系统负载监控数据列表错误", var6.toString(), "2");
               return ResDataUtils.resetErrorJson(var6.toString());
            }
         } else {
            return ResDataUtils.resetErrorJson("Missing require parameters");
         }
      }
   }

   @ResponseBody
   @RequestMapping({"commonWarnHandle"})
   public String agentCommonWarnHandle(@RequestBody String paramBean) {
      JSONObject agentJsonObject = (JSONObject)JSONUtil.parse(paramBean);
      String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
      if (!StringUtils.isEmpty(checkResult)) {
         return checkResult;
      } else {
         try {
            String title = "公共告警接口通知";
            if (null != agentJsonObject.get("title") && !StringUtils.isEmpty(agentJsonObject.get("title").toString())) {
               title = agentJsonObject.get("title").toString();
            }

            String content = agentJsonObject.get("content").toString();
            WarnOtherUtil.sendUtil(title, content, "", "", false);
            return ResDataUtils.resetSuccessJson("success");
         } catch (Exception var6) {
            logger.error("公共告警接口错误", var6);
            return ResDataUtils.resetErrorJson(var6.toString());
         }
      }
   }
}
