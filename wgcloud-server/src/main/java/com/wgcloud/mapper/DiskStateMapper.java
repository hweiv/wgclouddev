package com.wgcloud.mapper;

import com.wgcloud.entity.DiskState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DiskStateMapper {
   List<DiskState> selectAllByParams(Map<String, Object> map) throws Exception;

   List<DiskState> selectByParams(Map<String, Object> params) throws Exception;

   DiskState selectById(String id) throws Exception;

   void save(DiskState DiskState) throws Exception;

   void insertList(List<DiskState> recordList) throws Exception;

   int deleteByAccountAndDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;

   int deleteByAccHname(List<String> recordList) throws Exception;
}
