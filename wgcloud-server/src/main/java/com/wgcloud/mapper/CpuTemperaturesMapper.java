package com.wgcloud.mapper;

import com.wgcloud.entity.CpuTemperatures;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CpuTemperaturesMapper {
   List<CpuTemperatures> selectAllByParams(Map<String, Object> map) throws Exception;

   List<CpuTemperatures> selectByParams(Map<String, Object> params) throws Exception;

   CpuTemperatures selectById(String id) throws Exception;

   void save(CpuTemperatures CpuTemperatures) throws Exception;

   void insertList(List<CpuTemperatures> recordList) throws Exception;

   int deleteByAccountAndDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;

   int deleteByAccHname(List<String> recordList) throws Exception;
}
