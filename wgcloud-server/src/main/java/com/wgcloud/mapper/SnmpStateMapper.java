package com.wgcloud.mapper;

import com.wgcloud.entity.SnmpState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface SnmpStateMapper {
   List<SnmpState> selectAllByParams(Map<String, Object> map) throws Exception;

   List<SnmpState> selectByParams(Map<String, Object> params) throws Exception;

   SnmpState selectById(String id) throws Exception;

   int selectByParamsCount(Map<String, Object> map);

   void save(SnmpState SnmpState) throws Exception;

   void insertList(List<SnmpState> recordList) throws Exception;

   int deleteBySnmpInfoId(String snmpInfoId) throws Exception;

   int deleteByDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;
}
