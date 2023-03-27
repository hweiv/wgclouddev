package com.wgcloud.mapper;

import com.wgcloud.entity.CustomInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomInfoMapper {
   List<CustomInfo> selectAllByParams(Map<String, Object> map) throws Exception;

   List<CustomInfo> selectByParams(Map<String, Object> params) throws Exception;

   CustomInfo selectById(String id) throws Exception;

   void save(CustomInfo customInfo) throws Exception;

   void insertList(List<CustomInfo> recordList) throws Exception;

   void updateList(List<CustomInfo> recordList) throws Exception;

   void downByHostName(List<String> recordList) throws Exception;

   int deleteById(String[] id) throws Exception;

   int deleteByHostName(Map<String, Object> map) throws Exception;

   int countByParams(Map<String, Object> params) throws Exception;

   int updateById(CustomInfo customInfo) throws Exception;
}
