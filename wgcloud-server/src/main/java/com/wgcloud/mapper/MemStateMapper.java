package com.wgcloud.mapper;

import com.wgcloud.entity.MemState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface MemStateMapper {
   List<MemState> selectAllByParams(Map<String, Object> map) throws Exception;

   List<MemState> selectByParams(Map<String, Object> params) throws Exception;

   MemState selectById(String id) throws Exception;

   MemState selectMaxAvgByHostname(Map<String, Object> map) throws Exception;

   Double selectMaxByDate(Map<String, Object> map) throws Exception;

   void save(MemState MemState) throws Exception;

   void insertList(List<MemState> recordList) throws Exception;

   int deleteByAccountAndDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;
}
