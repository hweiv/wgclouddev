package com.wgcloud.mapper;

import com.wgcloud.entity.DbTable;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DbTableMapper {
   List<DbTable> selectAllByParams(Map<String, Object> map) throws Exception;

   List<DbTable> selectByParams(Map<String, Object> params) throws Exception;

   DbTable selectById(String id) throws Exception;

   void save(DbTable DbTable) throws Exception;

   int deleteById(String[] id) throws Exception;

   int deleteByDbInfoId(String dbInfoId) throws Exception;

   void updateList(List<DbTable> recordList) throws Exception;

   int countByParams(Map<String, Object> params) throws Exception;

   Long sumByParams(Map<String, Object> params) throws Exception;

   void updateById(DbTable DbTable) throws Exception;
}
