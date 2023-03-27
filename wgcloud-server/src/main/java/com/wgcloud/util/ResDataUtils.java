package com.wgcloud.util;

import cn.hutool.json.JSONUtil;
import com.wgcloud.dto.ResDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ResDataUtils {
   private static final Logger logger = LoggerFactory.getLogger(ResDataUtils.class);

   public static String resetErrorJson(String err_Msg) {
      ResDataDto resDataDto = new ResDataDto();
      resDataDto.setCode("1");
      resDataDto.setMsg(err_Msg);
      return JSONUtil.toJsonStr(resDataDto);
   }

   public static String resetSuccessJson(Object data) {
      ResDataDto resDataDto = new ResDataDto();
      resDataDto.setCode("0");
      resDataDto.setMsg("");
      resDataDto.setData(data);
      return JSONUtil.toJsonStr(resDataDto);
   }
}
