package com.wgcloud.controller;

import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"/hostGroup"})
public class HostGroupController {
   private static final Logger logger = LoggerFactory.getLogger(HostGroupController.class);
   @Resource
   private HostGroupService hostGroupService;
   @Resource
   private LogInfoService logInfoService;

   @RequestMapping({"list"})
   public String hostGroupList(HostGroup hostGroup, Model model, HttpServletRequest request) {
      String errorMsg = "查询标签信息错误";
      LicenseUtil.maxLicense_10(model, request, hostGroup);
      HashMap params = new HashMap();

      try {
         StringBuffer url = new StringBuffer();
         if (!StringUtils.isEmpty(hostGroup.getGroupName())) {
            params.put("groupName", hostGroup.getGroupName());
            url.append("&groupName=").append(hostGroup.getGroupName());
         }

         PageInfo pageInfo = this.hostGroupService.selectByParams(params, Integer.valueOf(hostGroup.getPage()), Integer.valueOf(hostGroup.getPageSize()));
         PageUtil.initPageNumber(pageInfo, model);
         model.addAttribute("page", pageInfo);
         model.addAttribute("pageUrl", "/hostGroup/list?1=1" + url.toString());
         model.addAttribute("hostGroup", hostGroup);
      } catch (Exception var8) {
         logger.error(errorMsg, var8);
         this.logInfoService.save(errorMsg, var8.toString(), "2");
      }

      return "hostGroup/list";
   }

   @RequestMapping({"edit"})
   public String edit(Model model, HttpServletRequest request) {
      String errorMsg = "添加标签信息错误";
      String id = request.getParameter("id");
      HostGroup hostGroup = new HostGroup();

      try {
         if (StringUtils.isEmpty(id)) {
            model.addAttribute("hostGroup", hostGroup);
            if (!this.isAddContinue()) {
               return "redirect:/hostGroup/list?liceFlage=1";
            }

            return "hostGroup/add";
         }

         hostGroup = this.hostGroupService.selectById(id);
         model.addAttribute("hostGroup", hostGroup);
      } catch (Exception var7) {
         logger.error(errorMsg, var7);
         this.logInfoService.save(errorMsg, var7.toString(), "2");
      }

      return "hostGroup/add";
   }

   @RequestMapping({"save"})
   public String saveHostGroup(HostGroup hostGroup, Model model, HttpServletRequest request) {
      try {
         if (StringUtils.isEmpty(hostGroup.getId())) {
            this.hostGroupService.save(hostGroup);
            this.hostGroupService.saveLog(request, "添加", hostGroup);
         } else {
            this.hostGroupService.updateById(hostGroup);
            this.hostGroupService.saveLog(request, "修改", hostGroup);
         }
      } catch (Exception var5) {
         logger.error("保存标签错误：", var5);
         this.logInfoService.save("保存标签错误", var5.toString(), "2");
      }

      return "redirect:/hostGroup/list";
   }

   @RequestMapping({"del"})
   public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
      String errorMsg = "删除分组错误";

      try {
         if (!StringUtils.isEmpty(request.getParameter("id"))) {
            String[] ids = request.getParameter("id").split(",");
            String[] var6 = ids;
            int var7 = ids.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               String id = var6[var8];
               HostGroup hostGroup = this.hostGroupService.selectById(id);
               this.hostGroupService.deleteById(request.getParameter("id").split(","));
               this.hostGroupService.saveLog(request, "删除", hostGroup);
            }
         }
      } catch (Exception var11) {
         logger.error(errorMsg, var11);
         this.logInfoService.save(errorMsg, var11.toString(), "2");
      }

      return "redirect:/hostGroup/list";
   }

   private boolean isAddContinue() {
      try {
         if (!StaticKeys.LICENSE_STATE.equals("1")) {
            Map<String, Object> params = new HashMap();
            int Size = this.hostGroupService.countByParams(params);
            if (Size >= 10) {
               return false;
            }
         }
      } catch (Exception var3) {
         logger.error("isAddContinue error", var3);
      }

      return true;
   }
}
