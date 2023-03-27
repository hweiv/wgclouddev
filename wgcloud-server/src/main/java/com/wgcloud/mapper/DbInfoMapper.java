package com.wgcloud.mapper;

import com.wgcloud.entity.DbInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DbInfoMapper {
   List<DbInfo> selectAllByParams(Map<String, Object> map) throws Exception;

   List<DbInfo> selectByParams(Map<String, Object> params) throws Exception;

   DbInfo selectById(String id) throws Exception;

   void save(DbInfo DbInfo) throws Exception;

   int deleteById(String[] id) throws Exception;

   int countByParams(Map<String, Object> params) throws Exception;

   int updateById(DbInfo DbInfo) throws Exception;

   int updateToTargetAccount(Map<String, Object> params) throws Exception;
}
