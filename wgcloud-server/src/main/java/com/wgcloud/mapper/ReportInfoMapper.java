package com.wgcloud.mapper;

import com.wgcloud.entity.ReportInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportInfoMapper {
   List<ReportInfo> selectAllByParams(Map<String, Object> map);

   int countByParams(Map<String, Object> params) throws Exception;

   List<ReportInfo> selectByParams(Map<String, Object> params) throws Exception;

   ReportInfo selectById(String id) throws Exception;

   void save(ReportInfo ReportInfo) throws Exception;

   int deleteById(String[] id) throws Exception;

   int deleteByDate(Map<String, Object> map) throws Exception;
}
