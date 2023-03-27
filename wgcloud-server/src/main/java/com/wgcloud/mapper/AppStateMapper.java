package com.wgcloud.mapper;

import com.wgcloud.entity.AppState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface AppStateMapper {
   List<AppState> selectAllByParams(Map<String, Object> map) throws Exception;

   List<AppState> selectByParams(Map<String, Object> params) throws Exception;

   AppState selectById(String id) throws Exception;

   int selectByParamsCount(Map<String, Object> map);

   void save(AppState AppState) throws Exception;

   void insertList(List<AppState> recordList) throws Exception;

   int deleteByAppInfoId(String appInfoId) throws Exception;

   int deleteByDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;
}
