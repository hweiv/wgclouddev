package com.wgcloud.mapper;

import com.wgcloud.entity.DceState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DceStateMapper {
   List<DceState> selectAllByParams(Map<String, Object> map) throws Exception;

   List<DceState> selectByParams(Map<String, Object> params) throws Exception;

   DceState selectById(String id) throws Exception;

   void save(DceState DceState) throws Exception;

   void insertList(List<DceState> recordList) throws Exception;

   int deleteByDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;
}
