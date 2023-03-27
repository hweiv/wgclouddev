package com.wgcloud.mapper;

import com.wgcloud.entity.DbTableCount;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DbTableCountMapper {
   List<DbTableCount> selectAllByParams(Map<String, Object> map) throws Exception;

   List<DbTableCount> selectByParams(Map<String, Object> params) throws Exception;

   DbTableCount selectById(String id) throws Exception;

   void save(DbTableCount DbTableCount) throws Exception;

   int deleteById(String[] id) throws Exception;

   void insertList(List<DbTableCount> recordList) throws Exception;

   int countByParams(Map<String, Object> params) throws Exception;

   int updateById(DbTableCount DbTableCount) throws Exception;

   int deleteByDate(Map<String, Object> map) throws Exception;
}
