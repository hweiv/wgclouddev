package com.wgcloud.mapper;

import com.wgcloud.entity.TcpState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface TcpStateMapper {
   List<TcpState> selectAllByParams(Map<String, Object> map) throws Exception;

   List<TcpState> selectByParams(Map<String, Object> params) throws Exception;

   TcpState selectById(String id) throws Exception;

   void save(TcpState TcpState) throws Exception;

   void insertList(List<TcpState> recordList) throws Exception;

   int deleteByAccountAndDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;
}
