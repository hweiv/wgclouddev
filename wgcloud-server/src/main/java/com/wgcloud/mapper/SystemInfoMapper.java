package com.wgcloud.mapper;

import com.wgcloud.entity.SystemInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemInfoMapper {
   List<SystemInfo> selectAllHostNameByParams(Map<String, Object> map) throws Exception;

   List<SystemInfo> selectAllByParams(Map<String, Object> map) throws Exception;

   List<SystemInfo> selectByHostname(String hostname) throws Exception;

   List<SystemInfo> selectByParams(Map<String, Object> params);

   void insertList(List<SystemInfo> recordList) throws Exception;

   void updateList(List<SystemInfo> recordList) throws Exception;

   SystemInfo selectById(String id) throws Exception;

   int updateById(SystemInfo SystemInfo) throws Exception;

   int countByParams(Map<String, Object> params) throws Exception;

   void save(SystemInfo SystemInfo) throws Exception;

   int deleteById(String[] id) throws Exception;

   void downByHostName(List<String> recordList) throws Exception;

   int deleteByAccHname(Map<String, Object> map) throws Exception;

   int updateAccountByHostName(Map<String, Object> params) throws Exception;

   int updateToTargetAccount(Map<String, Object> params) throws Exception;
}
