package com.wgcloud.mapper;

import com.wgcloud.entity.HeathState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface HeathStateMapper {
   List<HeathState> selectAllByParams(Map<String, Object> map) throws Exception;

   List<HeathState> selectByParams(Map<String, Object> params) throws Exception;

   HeathState selectById(String id) throws Exception;

   void save(HeathState HeathState) throws Exception;

   void insertList(List<HeathState> recordList) throws Exception;

   int deleteByDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;
}
