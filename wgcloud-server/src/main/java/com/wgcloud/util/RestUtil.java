package com.wgcloud.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.entity.HeathMonitor;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestUtil {
   private Logger logger = LoggerFactory.getLogger(RestUtil.class);
   @Autowired
   private RestTemplate restTemplate;

   public Map<String, Object> post(HeathMonitor heathMonitor, HashMap<String, String> headerMap) {
      HashMap responseMap = new HashMap();

      try {
         responseMap.put("resTimes", 20000);
         long startTimes = System.currentTimeMillis();
         HttpHeaders headers = new HttpHeaders();
         headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
         headers.add("Accept", "*/*");
         headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
         if (null != headerMap && headerMap.size() > 0) {
            headers.setAll(headerMap);
         }

         HttpEntity<String> httpEntity = new HttpEntity(heathMonitor.getPostStr(), headers);
         ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(heathMonitor.getHeathUrl(), httpEntity, String.class, new Object[0]);
         long endTimes = System.currentTimeMillis();
         String resTimes = endTimes - startTimes + "";
         responseMap.put("resTimes", Integer.valueOf(resTimes));
         responseMap.put("heathStatus", responseEntity.getStatusCodeValue());
         this.logger.debug((String)responseEntity.getBody());
         Integer bodyBytes = ((String)responseEntity.getBody()).getBytes(StandardCharsets.UTF_8).length;
         String bodySize = FormatUtil.bytesFormatUnit(String.valueOf(bodyBytes), "byte");
         heathMonitor.setResponseBodySize(bodySize);
         this.responseBodyHandle(responseEntity, heathMonitor, responseMap);
         return responseMap;
      } catch (HttpClientErrorException var14) {
         this.logger.error("服务接口检测任务错误", var14);
         responseMap.put("heathStatus", var14.getRawStatusCode());
         heathMonitor.setResponseBodySize("0B");
         return responseMap;
      } catch (Exception var15) {
         this.logger.error("服务接口检测任务错误", var15);
         responseMap.put("heathStatus", 500);
         heathMonitor.setResponseBodySize("0B");
         return responseMap;
      }
   }

   public Map<String, Object> postFormData(HeathMonitor heathMonitor, HashMap<String, String> headerMap, HashMap<String, String> formMap) {
      HashMap responseMap = new HashMap();

      try {
         responseMap.put("resTimes", 20000);
         long startTimes = System.currentTimeMillis();
         HttpHeaders headers = new HttpHeaders();
         headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
         headers.add("Accept", "*/*");
         headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
         if (null != headerMap && headerMap.size() > 0) {
            headers.setAll(headerMap);
         }

         MultiValueMap<String, String> params = new LinkedMultiValueMap();
         if (null != formMap && formMap.size() > 0) {
            params.setAll(formMap);
         }

         HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(params, headers);
         ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(heathMonitor.getHeathUrl(), httpEntity, String.class, new Object[0]);
         long endTimes = System.currentTimeMillis();
         String resTimes = endTimes - startTimes + "";
         responseMap.put("resTimes", Integer.valueOf(resTimes));
         responseMap.put("heathStatus", responseEntity.getStatusCodeValue());
         this.logger.debug((String)responseEntity.getBody());
         Integer bodyBytes = ((String)responseEntity.getBody()).getBytes(StandardCharsets.UTF_8).length;
         String bodySize = FormatUtil.bytesFormatUnit(String.valueOf(bodyBytes), "byte");
         heathMonitor.setResponseBodySize(bodySize);
         this.responseBodyHandle(responseEntity, heathMonitor, responseMap);
         return responseMap;
      } catch (HttpClientErrorException var16) {
         this.logger.error("服务接口检测任务错误", var16);
         responseMap.put("heathStatus", var16.getRawStatusCode());
         heathMonitor.setResponseBodySize("0B");
         responseMap.put("errorMsg", var16.toString());
         return responseMap;
      } catch (Exception var17) {
         this.logger.error("服务接口检测任务错误", var17);
         responseMap.put("heathStatus", 500);
         heathMonitor.setResponseBodySize("0B");
         responseMap.put("errorMsg", var17.toString());
         return responseMap;
      }
   }

   public JSONObject post(String url, JSONObject jsonObject) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
      headers.add("Accept", MediaType.APPLICATION_JSON_UTF8.toString());
      HttpEntity<String> httpEntity = new HttpEntity(JSONUtil.parse(jsonObject).toString(), headers);
      ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(url, httpEntity, String.class, new Object[0]);
      return JSONUtil.parseObj((String)responseEntity.getBody());
   }

   public JSONObject post(String url) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
      headers.add("Accept", MediaType.APPLICATION_JSON_UTF8.toString());
      HttpEntity<String> httpEntity = new HttpEntity("", headers);
      ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(url, httpEntity, String.class, new Object[0]);
      return JSONUtil.parseObj((String)responseEntity.getBody());
   }

   public Map<String, Object> get(HeathMonitor heathMonitor, HashMap<String, String> headerMap) {
      HashMap responseMap = new HashMap();

      try {
         responseMap.put("resTimes", 20000);
         long startTimes = System.currentTimeMillis();
         ResponseEntity<String> responseEntity = null;
         HttpHeaders headers = new HttpHeaders();
         headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
         headers.add("Accept", "*/*");
         headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
         if (null != headerMap && headerMap.size() > 0) {
            headers.setAll(headerMap);
         }

         HttpEntity<String> httpEntity = new HttpEntity("", headers);
         responseEntity = this.restTemplate.exchange(heathMonitor.getHeathUrl(), HttpMethod.GET, httpEntity, String.class, new Object[0]);
         long endTimes = System.currentTimeMillis();
         String resTimes = endTimes - startTimes + "";
         responseMap.put("resTimes", Integer.valueOf(resTimes));
         responseMap.put("heathStatus", responseEntity.getStatusCodeValue());
         this.logger.debug((String)responseEntity.getBody());
         Integer bodyBytes = ((String)responseEntity.getBody()).getBytes(StandardCharsets.UTF_8).length;
         String bodySize = FormatUtil.bytesFormatUnit(String.valueOf(bodyBytes), "byte");
         heathMonitor.setResponseBodySize(bodySize);
         this.responseBodyHandle(responseEntity, heathMonitor, responseMap);
         return responseMap;
      } catch (HttpClientErrorException var14) {
         this.logger.error("服务接口检测任务错误", var14);
         responseMap.put("heathStatus", var14.getRawStatusCode());
         heathMonitor.setResponseBodySize("0B");
         responseMap.put("errorMsg", var14.toString());
         return responseMap;
      } catch (Exception var15) {
         this.logger.error("服务接口检测任务错误", var15);
         responseMap.put("heathStatus", 500);
         heathMonitor.setResponseBodySize("0B");
         responseMap.put("errorMsg", var15.toString());
         return responseMap;
      }
   }

   public String get(String url) {
      try {
         ResponseEntity<String> responseEntity = null;
         if (url.startsWith("https")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headers.add("Accept", MediaType.APPLICATION_JSON_UTF8.toString());
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
            HttpEntity<String> httpEntity = new HttpEntity("", headers);
            responseEntity = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, new Object[0]);
         } else {
            responseEntity = this.restTemplate.getForEntity(url, String.class, new Object[0]);
         }

         return (String)responseEntity.getBody();
      } catch (HttpClientErrorException var5) {
         this.logger.error("请求守护进程信息错误", var5);
         return "";
      } catch (Exception var6) {
         this.logger.error("请求守护进程信息错误", var6);
         return "";
      }
   }

   private void responseBodyHandle(ResponseEntity<String> responseEntity, HeathMonitor heathMonitor, Map<String, Object> responseMap) {
      if (!StringUtils.isEmpty(heathMonitor.getResKeyword()) && !((String)responseEntity.getBody()).contains(heathMonitor.getResKeyword())) {
         this.logger.error(heathMonitor.getHeathUrl() + "----响应内容没有含标识-------" + heathMonitor.getResKeyword());
         responseMap.put("heathStatus", 500);
         responseMap.put("errorMsg", "响应内容没有含标识：" + heathMonitor.getResKeyword());
      }

      if (!StringUtils.isEmpty(heathMonitor.getResNoKeyword())) {
         String[] noKeyWords = heathMonitor.getResNoKeyword().split(",");
         String[] var5 = noKeyWords;
         int var6 = noKeyWords.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String noKeyWordChar = var5[var7];
            if (((String)responseEntity.getBody()).contains(noKeyWordChar)) {
               this.logger.error(heathMonitor.getHeathUrl() + "----响应内容包含(设置不能包含的关键字)标识-------" + noKeyWordChar);
               responseMap.put("heathStatus", 500);
               responseMap.put("errorMsg", "响应内容包含(设置不能包含的关键字)标识：" + noKeyWordChar);
               break;
            }
         }
      }

   }
}
