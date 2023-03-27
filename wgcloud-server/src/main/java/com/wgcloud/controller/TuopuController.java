package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.TuopuEdgeDto;
import com.wgcloud.dto.TuopuNodeDto;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.SnmpInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
@RequestMapping({"/tuopu"})
public class TuopuController {
   private static final Logger logger = LoggerFactory.getLogger(TuopuController.class);
   private static final String HOST_ON_IMG = "/wgcloud/static/js/tuopu/img/server.png";
   private static final String HOST_DOWN_IMG = "/wgcloud/static/js/tuopu/img/server2.png";
   private static final String SERVER_IMG = "/wgcloud/static/js/tuopu/img/server_database.png";
   @Resource
   private SystemInfoService systemInfoService;
   @Resource
   private DceInfoService dceInfoService;
   @Resource
   private SnmpInfoService snmpInfoService;
   @Resource
   private LogInfoService logInfoService;
   @Resource
   private HostGroupService hostGroupService;
   @Autowired
   private CommonConfig commonConfig;
   @Resource
   private AccountInfoService accountInfoService;

   private void testThread() {
      Runnable runnable = () -> {
         logger.info("TuopuController----------testThread");
      };
      ThreadPoolUtil.executor.execute(runnable);
   }

   @RequestMapping({"tuopuListHost"})
   public String tuopuListHost(SystemInfo systemInfoParam, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         List<TuopuNodeDto> nodeList = new ArrayList();
         List<TuopuEdgeDto> lineList = new ArrayList();
         String serverNodeId = UUIDUtil.getUUID();
         HostUtil.addAccountquery(request, params);
         if (!StringUtils.isEmpty(systemInfoParam.getState())) {
            params.put("state", systemInfoParam.getState());
         }

         if (!StringUtils.isEmpty(systemInfoParam.getAccount())) {
            params.put("account", systemInfoParam.getAccount());
            model.addAttribute("account", systemInfoParam.getAccount());
         }

         if (!StringUtils.isEmpty(systemInfoParam.getGroupId())) {
            params.put("groupId", systemInfoParam.getGroupId());
            model.addAttribute("groupId", systemInfoParam.getGroupId());
         }

         List<SystemInfo> pageInfo = this.systemInfoService.selectAllByParams(params);
         model.addAttribute("nodeListSize", pageInfo.size());
         if (!StaticKeys.LICENSE_STATE.equals("1") && pageInfo.size() > 10) {
            pageInfo = pageInfo.subList(0, 10);
            model.addAttribute("licenseMsg", "个人版最多监控10项，请点击页面底部网站，联系我们升级到专业版");
         }

         TuopuEdgeDto tuopuEdgeDto;
         for(Iterator var9 = pageInfo.iterator(); var9.hasNext(); lineList.add(tuopuEdgeDto)) {
            SystemInfo systemInfo = (SystemInfo)var9.next();
            TuopuNodeDto dto = new TuopuNodeDto();
            dto.setLabel(systemInfo.getHostname() + HostUtil.addRemark(systemInfo.getHostname()));
            if ("2".equals(systemInfo.getState())) {
               dto.setImg("/wgcloud/static/js/tuopu/img/server2.png");
            } else {
               dto.setImg("/wgcloud/static/js/tuopu/img/server.png");
            }

            dto.setId(systemInfo.getId());
            nodeList.add(dto);
            tuopuEdgeDto = new TuopuEdgeDto();
            tuopuEdgeDto.setSource(dto.getId());
            tuopuEdgeDto.setTarget(serverNodeId);
            if ("2".equals(systemInfo.getState())) {
               JSONObject styleJson = new JSONObject();
               styleJson.set("stroke", "#dc3545");
               tuopuEdgeDto.setStyle(styleJson);
            }
         }

         TuopuNodeDto dtoServer = new TuopuNodeDto();
         dtoServer.setSize(50);
         dtoServer.setLabel("Server");
         dtoServer.setImg("/wgcloud/static/js/tuopu/img/server_database.png");
         dtoServer.setId(serverNodeId);
         nodeList.add(dtoServer);
         JSONObject resultJson = new JSONObject();
         resultJson.set("nodes", nodeList);
         resultJson.set("edges", lineList);
         model.addAttribute("nodeList", resultJson);
         model.addAttribute("pageFlag", "host");
         params.clear();
         List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params);
         model.addAttribute("hostGroupList", hostGroupList);
         if ("true".equals(this.commonConfig.getUserInfoManage())) {
            params.clear();
            List<AccountInfo> accountInfoList = this.accountInfoService.selectAllByParams(params);
            model.addAttribute("accountList", accountInfoList);
         }
      } catch (Exception var14) {
         logger.error("主机拓扑图生成错误", var14);
         this.logInfoService.save("主机拓扑图生成错误", var14.toString(), "2");
      }

      return "tuopu";
   }

   @RequestMapping({"tuopuListSt"})
   public String tuopuListSt(SystemInfo systemInfoParam, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         List<TuopuNodeDto> nodeList = new ArrayList();
         List<TuopuEdgeDto> lineList = new ArrayList();
         String serverNodeId = UUIDUtil.getUUID();
         HostUtil.addAccountquery(request, params);
         if (!StringUtils.isEmpty(systemInfoParam.getState())) {
            if ("2".equals(systemInfoParam.getState())) {
               params.put("resTimes", -1);
            }

            if ("1".equals(systemInfoParam.getState())) {
               params.put("resTimesGt", -1);
            }
         }

         if (!StringUtils.isEmpty(systemInfoParam.getAccount())) {
            params.put("account", systemInfoParam.getAccount());
            model.addAttribute("account", systemInfoParam.getAccount());
         }

         if (!StringUtils.isEmpty(systemInfoParam.getGroupId())) {
            params.put("groupId", systemInfoParam.getGroupId());
            model.addAttribute("groupId", systemInfoParam.getGroupId());
         }

         List<DceInfo> pageInfoDce = this.dceInfoService.selectAllByParams(params);

         TuopuEdgeDto tuopuEdgeDto;
         for(Iterator var9 = pageInfoDce.iterator(); var9.hasNext(); lineList.add(tuopuEdgeDto)) {
            DceInfo dceInfo = (DceInfo)var9.next();
            TuopuNodeDto dto = new TuopuNodeDto();
            if (!StringUtils.isEmpty(dceInfo.getRemark())) {
               dto.setLabel(dceInfo.getHostname() + "(" + dceInfo.getRemark() + ")");
            } else {
               dto.setLabel(dceInfo.getHostname());
            }

            if (dceInfo.getResTimes() < 0) {
               dto.setImg("/wgcloud/static/js/tuopu/img/server2.png");
            } else {
               dto.setImg("/wgcloud/static/js/tuopu/img/server.png");
            }

            dto.setId(dceInfo.getId());
            ((List)nodeList).add(dto);
            tuopuEdgeDto = new TuopuEdgeDto();
            tuopuEdgeDto.setSource(dto.getId());
            tuopuEdgeDto.setTarget(serverNodeId);
            if (dceInfo.getResTimes() < 0) {
               JSONObject styleJson = new JSONObject();
               styleJson.set("stroke", "#dc3545");
               tuopuEdgeDto.setStyle(styleJson);
            }
         }

         if (!StaticKeys.LICENSE_STATE.equals("1") && ((List)nodeList).size() > 10) {
            nodeList = ((List)nodeList).subList(0, 10);
            model.addAttribute("licenseMsg", "个人版最多监控10项，请点击页面底部网站，联系我们升级到专业版");
         }

         TuopuNodeDto dtoServer = new TuopuNodeDto();
         dtoServer.setSize(50);
         dtoServer.setLabel("Server");
         dtoServer.setImg("/wgcloud/static/js/tuopu/img/server_database.png");
         dtoServer.setId(serverNodeId);
         ((List)nodeList).add(dtoServer);
         JSONObject resultJson = new JSONObject();
         resultJson.set("nodes", nodeList);
         resultJson.set("edges", lineList);
         model.addAttribute("nodeList", resultJson);
         model.addAttribute("nodeListSize", pageInfoDce.size());
         model.addAttribute("pageFlag", "ping");
         params.clear();
         List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params);
         model.addAttribute("hostGroupList", hostGroupList);
         if ("true".equals(this.commonConfig.getUserInfoManage())) {
            params.clear();
            List<AccountInfo> accountInfoList = this.accountInfoService.selectAllByParams(params);
            model.addAttribute("accountList", accountInfoList);
         }
      } catch (Exception var14) {
         logger.error("数通拓扑图ping生成错误", var14);
         this.logInfoService.save("数通拓扑图ping生成错误", var14.toString(), "2");
      }

      return "tuopu";
   }

   @RequestMapping({"tuopuListSnmp"})
   public String tuopuListSnmp(SystemInfo systemInfoParam, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         List<TuopuNodeDto> nodeList = new ArrayList();
         List<TuopuEdgeDto> lineList = new ArrayList();
         String serverNodeId = UUIDUtil.getUUID();
         HostUtil.addAccountquery(request, params);
         if (!StringUtils.isEmpty(systemInfoParam.getState())) {
            params.put("state", systemInfoParam.getState());
         }

         List<SnmpInfo> pageInfoSnmp = this.snmpInfoService.selectAllByParams(params);

         TuopuEdgeDto tuopuEdgeDto;
         for(Iterator var9 = pageInfoSnmp.iterator(); var9.hasNext(); lineList.add(tuopuEdgeDto)) {
            SnmpInfo snmpInfo = (SnmpInfo)var9.next();
            TuopuNodeDto dto = new TuopuNodeDto();
            if (!StringUtils.isEmpty(snmpInfo.getRemark())) {
               dto.setLabel(snmpInfo.getHostname() + "(" + snmpInfo.getRemark() + ")");
            } else {
               dto.setLabel(snmpInfo.getHostname());
            }

            if ("2".equals(snmpInfo.getState())) {
               dto.setImg("/wgcloud/static/js/tuopu/img/server2.png");
            } else {
               dto.setImg("/wgcloud/static/js/tuopu/img/server.png");
            }

            dto.setId(snmpInfo.getId());
            ((List)nodeList).add(dto);
            tuopuEdgeDto = new TuopuEdgeDto();
            tuopuEdgeDto.setSource(dto.getId());
            tuopuEdgeDto.setTarget(serverNodeId);
            if ("2".equals(snmpInfo.getState())) {
               JSONObject styleJson = new JSONObject();
               styleJson.set("stroke", "#dc3545");
               tuopuEdgeDto.setStyle(styleJson);
            }
         }

         if (!StaticKeys.LICENSE_STATE.equals("1") && ((List)nodeList).size() > 10) {
            nodeList = ((List)nodeList).subList(0, 10);
            model.addAttribute("licenseMsg", "个人版最多监控10项，请点击页面底部网站，联系我们升级到专业版");
         }

         TuopuNodeDto dtoServer = new TuopuNodeDto();
         dtoServer.setSize(50);
         dtoServer.setLabel("Server");
         dtoServer.setImg("/wgcloud/static/js/tuopu/img/server_database.png");
         dtoServer.setId(serverNodeId);
         ((List)nodeList).add(dtoServer);
         JSONObject resultJson = new JSONObject();
         resultJson.set("nodes", nodeList);
         resultJson.set("edges", lineList);
         model.addAttribute("nodeList", resultJson);
         model.addAttribute("nodeListSize", pageInfoSnmp.size());
         model.addAttribute("pageFlag", "snmp");
         if ("true".equals(this.commonConfig.getUserInfoManage())) {
            params.clear();
            List<AccountInfo> accountInfoList = this.accountInfoService.selectAllByParams(params);
            model.addAttribute("accountList", accountInfoList);
         }
      } catch (Exception var14) {
         logger.error("数通拓扑图snmp生成错误", var14);
         this.logInfoService.save("数通拓扑图snmp生成错误", var14.toString(), "2");
      }

      return "tuopu";
   }
}
