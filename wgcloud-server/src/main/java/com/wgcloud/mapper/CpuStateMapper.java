package com.wgcloud.mapper;

import com.wgcloud.entity.CpuState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CpuStateMapper {
   List<CpuState> selectAllByParams(Map<String, Object> map) throws Exception;

   List<CpuState> selectByParams(Map<String, Object> params) throws Exception;

   CpuState selectById(String id) throws Exception;

   CpuState selectMaxAvgByHostname(Map<String, Object> map) throws Exception;

   Double selectMaxByDate(Map<String, Object> map) throws Exception;

   int selectByParamsCount(Map<String, Object> map);

   void save(CpuState CpuState) throws Exception;

   void insertList(List<CpuState> recordList) throws Exception;

   int deleteByAccountAndDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;
}
