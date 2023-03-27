package com.wgcloud.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.HeaderDto;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.entity.HeathState;
import com.wgcloud.mapper.HeathMonitorMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.RestUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.staticvar.BatchData;
import java.util.ArrayList;
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
public class HeathMonitorService {
   private static final Logger logger = LoggerFactory.getLogger(HeathMonitorService.class);
   @Autowired
   private HeathMonitorMapper heathMonitorMapper;
   @Autowired
   private LogInfoService logInfoService;
   @Autowired
   private RestUtil restUtil;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<HeathMonitor> list = this.heathMonitorMapper.selectByParams(params);
      PageInfo<HeathMonitor> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(HeathMonitor heathMonitor) throws Exception {
      heathMonitor.setId(UUIDUtil.getUUID());
      heathMonitor.setCreateTime(DateUtil.getNowTime());
      if (StringUtils.isEmpty(heathMonitor.getResNoKeyword())) {
         heathMonitor.setResNoKeyword("");
      }

      if (StringUtils.isEmpty(heathMonitor.getResKeyword())) {
         heathMonitor.setResKeyword("");
      }

      if (StringUtils.isEmpty(heathMonitor.getHeathUrl())) {
         heathMonitor.setHeathUrl(heathMonitor.getHeathUrl().trim());
      }

      this.setHeaderJson(heathMonitor);
      this.setFormJson(heathMonitor);
      this.heathMonitorMapper.save(heathMonitor);
   }

   @Transactional
   public void saveRecord(List<HeathMonitor> recordList) throws Exception {
      if (recordList.size() >= 1) {
         Iterator var2 = recordList.iterator();

         while(var2.hasNext()) {
            HeathMonitor as = (HeathMonitor)var2.next();
            as.setId(UUIDUtil.getUUID());
         }

         this.heathMonitorMapper.insertList(recordList);
      }
   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.heathMonitorMapper.countByParams(params);
   }

   public int updateActive(Map<String, Object> params) throws Exception {
      return this.heathMonitorMapper.updateActive(params);
   }

   @Transactional
   public int deleteById(String[] id) throws Exception {
      return this.heathMonitorMapper.deleteById(id);
   }

   public void updateById(HeathMonitor heathMonitor) throws Exception {
      if (StringUtils.isEmpty(heathMonitor.getHeathUrl())) {
         heathMonitor.setHeathUrl(heathMonitor.getHeathUrl().trim());
      }

      this.setHeaderJson(heathMonitor);
      this.setFormJson(heathMonitor);
      if (!StringUtils.isEmpty(heathMonitor.getErrorMsg()) && heathMonitor.getErrorMsg().length() > 200) {
         heathMonitor.setErrorMsg(heathMonitor.getErrorMsg().substring(0, 200));
      }

      this.heathMonitorMapper.updateById(heathMonitor);
   }

   public void updateForServerBackup(HeathMonitor heathMonitor) throws Exception {
      this.heathMonitorMapper.updateById(heathMonitor);
   }

   public HeathMonitor selectById(String id) throws Exception {
      return this.heathMonitorMapper.selectById(id);
   }

   @Transactional
   public void updateRecord(List<HeathMonitor> recordList) throws Exception {
      this.heathMonitorMapper.updateList(recordList);
   }

   public List<HeathMonitor> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.heathMonitorMapper.selectAllByParams(params);
   }

   private void setHeaderJson(HeathMonitor heathMonitor) {
      List<HeaderDto> dtoList = new ArrayList();
      HeaderDto dto5;
      if (!StringUtils.isEmpty(heathMonitor.getHeaderKey())) {
         dto5 = new HeaderDto();
         dto5.setSort("1");
         dto5.setKey(heathMonitor.getHeaderKey());
         dto5.setValue(heathMonitor.getHeaderValue());
         dtoList.add(dto5);
      }

      if (!StringUtils.isEmpty(heathMonitor.getHeaderKey2())) {
         dto5 = new HeaderDto();
         dto5.setSort("2");
         dto5.setKey(heathMonitor.getHeaderKey2());
         dto5.setValue(heathMonitor.getHeaderValue2());
         dtoList.add(dto5);
      }

      if (!StringUtils.isEmpty(heathMonitor.getHeaderKey3())) {
         dto5 = new HeaderDto();
         dto5.setSort("3");
         dto5.setKey(heathMonitor.getHeaderKey3());
         dto5.setValue(heathMonitor.getHeaderValue3());
         dtoList.add(dto5);
      }

      if (!StringUtils.isEmpty(heathMonitor.getHeaderKey4())) {
         dto5 = new HeaderDto();
         dto5.setSort("4");
         dto5.setKey(heathMonitor.getHeaderKey4());
         dto5.setValue(heathMonitor.getHeaderValue4());
         dtoList.add(dto5);
      }

      if (!StringUtils.isEmpty(heathMonitor.getHeaderKey5())) {
         dto5 = new HeaderDto();
         dto5.setSort("5");
         dto5.setKey(heathMonitor.getHeaderKey5());
         dto5.setValue(heathMonitor.getHeaderValue5());
         dtoList.add(dto5);
      }

      heathMonitor.setHeaderJson(JSONUtil.toJsonStr(dtoList));
   }

   private void setFormJson(HeathMonitor heathMonitor) {
      List<HeaderDto> dtoList = new ArrayList();
      HeaderDto dto5;
      if (!StringUtils.isEmpty(heathMonitor.getFormKey())) {
         dto5 = new HeaderDto();
         dto5.setSort("1");
         dto5.setKey(heathMonitor.getFormKey());
         dto5.setValue(heathMonitor.getFormValue());
         dtoList.add(dto5);
      }

      if (!StringUtils.isEmpty(heathMonitor.getFormKey2())) {
         dto5 = new HeaderDto();
         dto5.setSort("2");
         dto5.setKey(heathMonitor.getFormKey2());
         dto5.setValue(heathMonitor.getFormValue2());
         dtoList.add(dto5);
      }

      if (!StringUtils.isEmpty(heathMonitor.getFormKey3())) {
         dto5 = new HeaderDto();
         dto5.setSort("3");
         dto5.setKey(heathMonitor.getFormKey3());
         dto5.setValue(heathMonitor.getFormValue3());
         dtoList.add(dto5);
      }

      if (!StringUtils.isEmpty(heathMonitor.getFormKey4())) {
         dto5 = new HeaderDto();
         dto5.setSort("4");
         dto5.setKey(heathMonitor.getFormKey4());
         dto5.setValue(heathMonitor.getFormValue4());
         dtoList.add(dto5);
      }

      if (!StringUtils.isEmpty(heathMonitor.getFormKey5())) {
         dto5 = new HeaderDto();
         dto5.setSort("5");
         dto5.setKey(heathMonitor.getFormKey5());
         dto5.setValue(heathMonitor.getFormValue5());
         dtoList.add(dto5);
      }

      heathMonitor.setFormJson(JSONUtil.toJsonStr(dtoList));
   }

   public HashMap<String, String> displayHeaderJson(HeathMonitor heathMonitor) {
      HashMap headerHashMap = new HashMap();

      try {
         if (!StringUtils.isEmpty(heathMonitor.getHeaderJson())) {
            if (JSONUtil.isJsonObj(heathMonitor.getHeaderJson())) {
               HeaderDto dto = (HeaderDto)JSONUtil.toBean(heathMonitor.getHeaderJson(), HeaderDto.class);
               heathMonitor.setHeaderKey(dto.getKey());
               heathMonitor.setHeaderValue(dto.getValue());
               headerHashMap.put(dto.getKey(), dto.getValue());
            } else {
               JSONArray jsonArray = JSONUtil.parseArray(heathMonitor.getHeaderJson());
               if (jsonArray.size() > 0) {
                  heathMonitor.setHeaderSize(" (" + jsonArray.size() + ")");
               }

               Iterator var4 = jsonArray.iterator();

               while(var4.hasNext()) {
                  Object jsonObject = var4.next();
                  HeaderDto dto = (HeaderDto)JSONUtil.toBean((JSONObject)jsonObject, HeaderDto.class);
                  headerHashMap.put(dto.getKey(), dto.getValue());
                  if ("1".equals(dto.getSort())) {
                     heathMonitor.setHeaderKey(dto.getKey());
                     heathMonitor.setHeaderValue(dto.getValue());
                  }

                  if ("2".equals(dto.getSort())) {
                     heathMonitor.setHeaderKey2(dto.getKey());
                     heathMonitor.setHeaderValue2(dto.getValue());
                  }

                  if ("3".equals(dto.getSort())) {
                     heathMonitor.setHeaderKey3(dto.getKey());
                     heathMonitor.setHeaderValue3(dto.getValue());
                  }

                  if ("4".equals(dto.getSort())) {
                     heathMonitor.setHeaderKey4(dto.getKey());
                     heathMonitor.setHeaderValue4(dto.getValue());
                  }

                  if ("5".equals(dto.getSort())) {
                     heathMonitor.setHeaderKey5(dto.getKey());
                     heathMonitor.setHeaderValue5(dto.getValue());
                  }
               }
            }
         }
      } catch (Exception var7) {
         logger.error("displayHeaderJson错误", var7);
      }

      return headerHashMap;
   }

   public HashMap<String, String> displayFormJson(HeathMonitor heathMonitor) {
      HashMap headerHashMap = new HashMap();

      try {
         if (!StringUtils.isEmpty(heathMonitor.getFormJson())) {
            JSONArray jsonArray = JSONUtil.parseArray(heathMonitor.getFormJson());
            if (jsonArray.size() > 0) {
               heathMonitor.setFormDataSize(" (" + jsonArray.size() + ")");
            }

            Iterator var4 = jsonArray.iterator();

            while(var4.hasNext()) {
               Object jsonObject = var4.next();
               HeaderDto dto = (HeaderDto)JSONUtil.toBean((JSONObject)jsonObject, HeaderDto.class);
               headerHashMap.put(dto.getKey(), dto.getValue());
               if ("1".equals(dto.getSort())) {
                  heathMonitor.setFormKey(dto.getKey());
                  heathMonitor.setFormValue(dto.getValue());
               }

               if ("2".equals(dto.getSort())) {
                  heathMonitor.setFormKey2(dto.getKey());
                  heathMonitor.setFormValue2(dto.getValue());
               }

               if ("3".equals(dto.getSort())) {
                  heathMonitor.setFormKey3(dto.getKey());
                  heathMonitor.setFormValue3(dto.getValue());
               }

               if ("4".equals(dto.getSort())) {
                  heathMonitor.setFormKey4(dto.getKey());
                  heathMonitor.setFormValue4(dto.getValue());
               }

               if ("5".equals(dto.getSort())) {
                  heathMonitor.setFormKey5(dto.getKey());
                  heathMonitor.setFormValue5(dto.getValue());
               }
            }
         }
      } catch (Exception var7) {
         logger.error("displayFormJson错误", var7);
      }

      return headerHashMap;
   }

   public void saveLog(HttpServletRequest request, String action, HeathMonitor heathMonitor) {
      if (null != heathMonitor) {
         this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "服务接口监测信息：" + heathMonitor.getAppName(), "服务接口URL：" + heathMonitor.getHeathUrl(), "2");
      }
   }

   public void taskThreadHandler(HeathMonitor h, Date date) {
//      int status = true;
      new HashMap();
      HashMap<String, String> headerMap = this.displayHeaderJson(h);
      HashMap<String, String> formMap = this.displayFormJson(h);
      Map statusMap;
      if ("POST".equals(h.getMethod())) {
         if (formMap.isEmpty()) {
            statusMap = this.restUtil.post(h, headerMap);
         } else {
            statusMap = this.restUtil.postFormData(h, headerMap, formMap);
         }
      } else {
         statusMap = this.restUtil.get(h, headerMap);
      }

      int status = Integer.valueOf(String.valueOf(statusMap.get("heathStatus")));
      h.setCreateTime(date);
      if (status != 200) {
         h.setCreateTime((Date)null);
      }

      h.setHeathStatus(status + "");
      h.setResTimes(Integer.valueOf(String.valueOf(statusMap.get("resTimes"))));
      if (null != statusMap.get("errorMsg")) {
         h.setErrorMsg(String.valueOf(statusMap.get("errorMsg")));
      }

      try {
         this.updateById(h);
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      HeathState heathState = new HeathState();
      heathState.setResTimes(h.getResTimes());
      heathState.setHeathId(h.getId());
      heathState.setCreateTime(date);
      BatchData.HEATH_STATE_LIST.add(heathState);
      if (!"200".equals(h.getHeathStatus())) {
         WarnOtherUtil.sendHeathInfo(h, true);
      } else if (null != WarnPools.MEM_WARN_MAP.get(h.getId())) {
         WarnOtherUtil.sendHeathInfo(h, false);
      }

   }

   public void updateToTargetAccount(Map<String, Object> params) throws Exception {
      this.heathMonitorMapper.updateToTargetAccount(params);
   }
}
