package com.wgcloud.mapper;

import com.wgcloud.entity.PortInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface PortInfoMapper {
   List<PortInfo> selectAllByParams(Map<String, Object> map) throws Exception;

   List<PortInfo> selectByParams(Map<String, Object> params) throws Exception;

   PortInfo selectById(String id) throws Exception;

   void save(PortInfo PortInfo) throws Exception;

   void insertList(List<PortInfo> recordList) throws Exception;

   void updateList(List<PortInfo> recordList) throws Exception;

   void downByHostName(List<String> recordList) throws Exception;

   int deleteById(String[] id) throws Exception;

   int deleteByHostName(Map<String, Object> map) throws Exception;

   int countByParams(Map<String, Object> params) throws Exception;

   int updateById(PortInfo PortInfo) throws Exception;
}
