package com.wgcloud.mapper;

import com.wgcloud.entity.FileWarnState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface FileWarnStateMapper {
   List<FileWarnState> selectAllByParams(Map<String, Object> map) throws Exception;

   List<FileWarnState> selectByParams(Map<String, Object> params) throws Exception;

   FileWarnState selectById(String id) throws Exception;

   Integer countByParams(Map<String, Object> map);

   void save(FileWarnState FileWarnState) throws Exception;

   void insertList(List<FileWarnState> recordList) throws Exception;

   int deleteByFileWarnId(String fileWarnId) throws Exception;

   int deleteByDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;
}
