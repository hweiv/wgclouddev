package com.wgcloud.mapper;

import com.wgcloud.entity.DiskSmart;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DiskSmartMapper {
   List<DiskSmart> selectAllByParams(Map<String, Object> map) throws Exception;

   List<DiskSmart> selectByParams(Map<String, Object> params) throws Exception;

   DiskSmart selectById(String id) throws Exception;

   void save(DiskSmart DiskSmart) throws Exception;

   void insertList(List<DiskSmart> recordList) throws Exception;

   int deleteByAccountAndDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;

   int deleteByAccHname(List<String> recordList) throws Exception;
}
