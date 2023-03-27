package com.wgcloud.mapper;

import com.wgcloud.entity.HeathMonitor;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface HeathMonitorMapper {
   List<HeathMonitor> selectAllByParams(Map<String, Object> map) throws Exception;

   List<HeathMonitor> selectByParams(Map<String, Object> params) throws Exception;

   HeathMonitor selectById(String id) throws Exception;

   void save(HeathMonitor HeathMonitor) throws Exception;

   void insertList(List<HeathMonitor> recordList) throws Exception;

   int deleteById(String[] id) throws Exception;

   int countByParams(Map<String, Object> params) throws Exception;

   void updateList(List<HeathMonitor> recordList) throws Exception;

   void updateById(HeathMonitor HeathMonitor) throws Exception;

   int updateToTargetAccount(Map<String, Object> params) throws Exception;

   int updateActive(Map<String, Object> params) throws Exception;
}
