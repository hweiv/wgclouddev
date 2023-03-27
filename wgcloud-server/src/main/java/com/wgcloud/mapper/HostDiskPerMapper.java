package com.wgcloud.mapper;

import com.wgcloud.entity.HostDiskPer;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface HostDiskPerMapper {
   List<HostDiskPer> selectAllByParams(Map<String, Object> map) throws Exception;

   List<HostDiskPer> selectByParams(Map<String, Object> params) throws Exception;

   HostDiskPer selectById(String id) throws Exception;

   void save(HostDiskPer HostDiskPer) throws Exception;

   void insertList(List<HostDiskPer> recordList) throws Exception;

   int deleteByAccountAndDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;

   int deleteByAccHname(List<String> recordList) throws Exception;
}
