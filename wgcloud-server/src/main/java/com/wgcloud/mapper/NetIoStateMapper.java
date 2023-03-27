package com.wgcloud.mapper;

import com.wgcloud.entity.NetIoState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface NetIoStateMapper {
   List<NetIoState> selectAllByParams(Map<String, Object> map) throws Exception;

   List<NetIoState> selectByParams(Map<String, Object> params);

   List<NetIoState> selectTop3(Map<String, Object> params);

   NetIoState selectById(String id) throws Exception;

   void save(NetIoState NetIoState) throws Exception;

   void insertList(List<NetIoState> recordList) throws Exception;

   int deleteByAccountAndDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;

   NetIoState selectMaxByHostname(Map<String, Object> map) throws Exception;

   NetIoState selectAvgByHostname(Map<String, Object> map) throws Exception;

   NetIoState selectMinByHostname(Map<String, Object> map) throws Exception;

   NetIoState selectMaxByDate(Map<String, Object> map) throws Exception;
}
