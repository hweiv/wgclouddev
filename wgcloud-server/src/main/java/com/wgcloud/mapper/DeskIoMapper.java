package com.wgcloud.mapper;

import com.wgcloud.entity.DeskIo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DeskIoMapper {
   List<DeskIo> selectAllByParams(Map<String, Object> map) throws Exception;

   List<DeskIo> selectByParams(Map<String, Object> params) throws Exception;

   DeskIo selectById(String id) throws Exception;

   void save(DeskIo DeskIo) throws Exception;

   void insertList(List<DeskIo> recordList) throws Exception;

   int deleteByAccountAndDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;

   int deleteByAccHname(List<String> recordList) throws Exception;
}
