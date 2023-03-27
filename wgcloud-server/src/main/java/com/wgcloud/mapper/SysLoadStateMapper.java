package com.wgcloud.mapper;

import com.wgcloud.entity.SysLoadState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLoadStateMapper {
   List<SysLoadState> selectAllByParams(Map<String, Object> map) throws Exception;

   List<SysLoadState> selectByParams(Map<String, Object> params) throws Exception;

   SysLoadState selectById(String id) throws Exception;

   void save(SysLoadState SysLoadState) throws Exception;

   void insertList(List<SysLoadState> recordList) throws Exception;

   int deleteByAccountAndDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;

   SysLoadState selectMaxByHostname(Map<String, Object> map) throws Exception;

   SysLoadState selectAvgByHostname(Map<String, Object> map) throws Exception;

   SysLoadState selectMinByHostname(Map<String, Object> map) throws Exception;

   SysLoadState selectMaxByDate(Map<String, Object> map) throws Exception;
}
