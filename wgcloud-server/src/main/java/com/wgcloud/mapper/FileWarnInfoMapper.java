package com.wgcloud.mapper;

import com.wgcloud.entity.FileWarnInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface FileWarnInfoMapper {
   List<FileWarnInfo> selectAllByParams(Map<String, Object> map) throws Exception;

   List<FileWarnInfo> selectByParams(Map<String, Object> params) throws Exception;

   FileWarnInfo selectById(String id) throws Exception;

   void save(FileWarnInfo FileWarnInfo) throws Exception;

   void insertList(List<FileWarnInfo> recordList) throws Exception;

   void updateList(List<FileWarnInfo> recordList) throws Exception;

   int deleteById(String[] id) throws Exception;

   int deleteByHostName(Map<String, Object> map) throws Exception;

   Integer countByParams(Map<String, Object> params) throws Exception;

   int updateById(FileWarnInfo FileWarnInfo) throws Exception;
}
