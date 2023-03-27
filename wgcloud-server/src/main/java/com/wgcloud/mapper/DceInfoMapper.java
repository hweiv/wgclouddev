package com.wgcloud.mapper;

import com.wgcloud.entity.DceInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DceInfoMapper {
   List<DceInfo> selectAllByParams(Map<String, Object> map) throws Exception;

   List<DceInfo> selectByParams(Map<String, Object> params) throws Exception;

   DceInfo selectById(String id) throws Exception;

   void save(DceInfo DceInfo) throws Exception;

   void insertList(List<DceInfo> recordList) throws Exception;

   int deleteById(String[] id) throws Exception;

   int countByParams(Map<String, Object> params) throws Exception;

   void updateList(List<DceInfo> recordList) throws Exception;

   void updateById(DceInfo DceInfo) throws Exception;

   int updateToTargetAccount(Map<String, Object> params) throws Exception;

   int updateActive(Map<String, Object> params) throws Exception;
}
