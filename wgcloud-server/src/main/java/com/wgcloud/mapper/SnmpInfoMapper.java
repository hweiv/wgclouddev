package com.wgcloud.mapper;

import com.wgcloud.entity.SnmpInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface SnmpInfoMapper {
   List<SnmpInfo> selectAllByParams(Map<String, Object> map) throws Exception;

   List<SnmpInfo> selectByParams(Map<String, Object> params) throws Exception;

   SnmpInfo selectById(String id) throws Exception;

   void save(SnmpInfo SnmpInfo) throws Exception;

   void updateList(List<SnmpInfo> recordList) throws Exception;

   int deleteById(String[] id) throws Exception;

   int countByParams(Map<String, Object> params) throws Exception;

   int updateById(SnmpInfo SnmpInfo) throws Exception;

   int updateToTargetAccount(Map<String, Object> params) throws Exception;

   int updateActive(Map<String, Object> params) throws Exception;
}
