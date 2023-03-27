package com.wgcloud.mapper;

import com.wgcloud.entity.AppInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface AppInfoMapper {
   List<AppInfo> selectAllByParams(Map<String, Object> map) throws Exception;

   List<AppInfo> selectByParams(Map<String, Object> params) throws Exception;

   AppInfo selectById(String id) throws Exception;

   void save(AppInfo AppInfo) throws Exception;

   void insertList(List<AppInfo> recordList) throws Exception;

   void updateList(List<AppInfo> recordList) throws Exception;

   void downByHostName(List<String> recordList) throws Exception;

   int deleteById(String[] id) throws Exception;

   int deleteByHostName(Map<String, Object> map) throws Exception;

   int countByParams(Map<String, Object> params) throws Exception;

   int updateById(AppInfo AppInfo) throws Exception;
}
