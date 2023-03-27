package com.wgcloud.mapper;

import com.wgcloud.entity.FileSafe;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface FileSafeMapper {
   List<FileSafe> selectAllByParams(Map<String, Object> map) throws Exception;

   List<FileSafe> selectByParams(Map<String, Object> params) throws Exception;

   FileSafe selectById(String id) throws Exception;

   void save(FileSafe FileSafe) throws Exception;

   void insertList(List<FileSafe> recordList) throws Exception;

   void updateList(List<FileSafe> recordList) throws Exception;

   void downByHostName(List<String> recordList) throws Exception;

   int deleteById(String[] id) throws Exception;

   int deleteByHostName(Map<String, Object> map) throws Exception;

   int countByParams(Map<String, Object> params) throws Exception;

   int updateById(FileSafe FileSafe) throws Exception;
}
