package com.wgcloud.mapper;

import com.wgcloud.entity.ReportInstance;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportInstanceMapper {
   List<ReportInstance> selectAllByParams(Map<String, Object> map);

   int countByParams(Map<String, Object> params) throws Exception;

   List<ReportInstance> selectByParams(Map<String, Object> params) throws Exception;

   ReportInstance selectById(String id) throws Exception;

   void save(ReportInstance ReportInstance) throws Exception;

   int deleteById(String[] id) throws Exception;

   void insertList(List<ReportInstance> recordList) throws Exception;

   int deleteByDate(Map<String, Object> map) throws Exception;
}
