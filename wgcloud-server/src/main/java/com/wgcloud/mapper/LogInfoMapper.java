package com.wgcloud.mapper;

import com.wgcloud.entity.LogInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface LogInfoMapper {
   List<LogInfo> selectAllByParams(Map<String, Object> map);

   int countByParams(Map<String, Object> params) throws Exception;

   List<LogInfo> selectByParams(Map<String, Object> params) throws Exception;

   LogInfo selectById(String id) throws Exception;

   void save(LogInfo LogInfo) throws Exception;

   int deleteById(String[] id) throws Exception;

   void insertList(List<LogInfo> recordList) throws Exception;

   int deleteByDate(Map<String, Object> map) throws Exception;
}
