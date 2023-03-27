package com.wgcloud.controller;

import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.Equipment;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.EquipmentService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"/equipment"})
public class EquipmentController {
   private static final Logger logger = LoggerFactory.getLogger(EquipmentController.class);
   @Resource
   private EquipmentService equipmentService;
   @Resource
   private AccountInfoService accountInfoService;
   @Resource
   private LogInfoService logInfoService;
   @Autowired
   private CommonConfig commonConfig;

   @RequestMapping({"list"})
   public String equipmentList(Equipment equipment, Model model, HttpServletRequest request) {
      HashMap params = new HashMap();

      try {
         LicenseUtil.maxLicense_10(model, request, equipment);
         StringBuffer url = new StringBuffer();
         String hostname = null;
         if (!StringUtils.isEmpty(equipment.getName())) {
            hostname = equipment.getName();
            params.put("name", hostname.trim());
            url.append("&name=").append(hostname);
         }

         if (!StringUtils.isEmpty(equipment.getAccount())) {
            params.put("account", equipment.getAccount());
            url.append("&account=").append(equipment.getAccount());
         }

         HostUtil.addAccountquery(request, params);
         PageInfo pageInfo = this.equipmentService.selectByParams(params, equipment.getPage(), equipment.getPageSize());
         PageUtil.initPageNumber(pageInfo, model);
         if ("true".equals(this.commonConfig.getHostGroup())) {
            this.equipmentService.setGroupInList(pageInfo.getList(), model);
         }

         HostUtil.addAccountListModel(model);
         model.addAttribute("pageUrl", "/equipment/list?1=1" + url.toString());
         model.addAttribute("page", pageInfo);
         model.addAttribute("equipment", equipment);
      } catch (Exception var8) {
         logger.error("查询资产信息错误", var8);
         this.logInfoService.save("查询资产信息错误", var8.toString(), "2");
      }

      return "equipment/list";
   }

   @RequestMapping({"save"})
   public String saveEquipment(Equipment Equipment, Model model, HttpServletRequest request) {
      try {
         if (StringUtils.isEmpty(Equipment.getId())) {
            AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
            if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
               Equipment.setAccount(accountInfo.getAccount());
            }

            this.equipmentService.save(Equipment);
            this.equipmentService.saveLog(request, "添加", Equipment);
         } else {
            this.equipmentService.updateById(Equipment);
            this.equipmentService.saveLog(request, "修改", Equipment);
         }
      } catch (Exception var5) {
         logger.error("保存资产错误", var5);
         this.logInfoService.save("保存资产错误", var5.toString(), "2");
      }

      return "redirect:/equipment/list";
   }

   @RequestMapping({"edit"})
   public String edit(Model model, HttpServletRequest request) {
      String errorMsg = "添加资产";
      String id = request.getParameter("id");
      Equipment Equipment = new Equipment();

      try {
         if (StringUtils.isEmpty(id)) {
            model.addAttribute("equipment", Equipment);
            if (!this.isAddContinue()) {
               return "redirect:/equipment/list?liceFlage=1";
            }

            return "equipment/add";
         }

         Equipment = this.equipmentService.selectById(id);
         model.addAttribute("equipment", Equipment);
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "equipment/add";
   }

   @ResponseBody
   @RequestMapping({"saveGroupId"})
   public String saveGroupId(Model model, HttpServletRequest request) {
      try {
         String ids = request.getParameter("ids");
         String[] groupIdsArr = request.getParameterValues("groupId");
         this.equipmentService.saveGroupId(ids, groupIdsArr, request);
      } catch (Exception var5) {
         logger.error("保存进程标签信息错误", var5);
         this.logInfoService.save("保存进程标签信息错误", var5.toString(), "2");
      }

      return "redirect:/appInfo/list";
   }

   @RequestMapping({"view"})
   public String viewChart(Model model, HttpServletRequest request) {
      String errorMsg = "查看资产信息错误";
      String id = request.getParameter("id");
      new Equipment();

      try {
         Equipment Equipment = this.equipmentService.selectById(id);
         model.addAttribute("equipment", Equipment);
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "equipment/view";
   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "删除资产信息错误";
      new Equipment();

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            String[] ids = request.getParameter("id").split(",");
            String[] var7 = ids;
            int var8 = ids.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String id = var7[var9];
               Equipment Equipment = this.equipmentService.selectById(id);
               this.equipmentService.saveLog(request, "删除", Equipment);
            }

            this.equipmentService.deleteById(ids);
         }
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "redirect:/equipment/list";
   }

   private boolean isAddContinue() {
      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            Map<String, Object> params = new HashMap();
            int dbSize = this.equipmentService.countByParams(params);
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
