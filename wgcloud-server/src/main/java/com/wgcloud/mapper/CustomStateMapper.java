package com.wgcloud.mapper;

import com.wgcloud.entity.CustomState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomStateMapper {
   List<CustomState> selectAllByParams(Map<String, Object> map) throws Exception;

   List<CustomState> selectByParams(Map<String, Object> params) throws Exception;

   CustomState selectById(String id) throws Exception;

   int selectByParamsCount(Map<String, Object> map);

   void save(CustomState customState) throws Exception;

   void insertList(List<CustomState> recordList) throws Exception;

   int deleteByCustomInfoId(String customInfoId) throws Exception;

   int deleteByDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;
}
