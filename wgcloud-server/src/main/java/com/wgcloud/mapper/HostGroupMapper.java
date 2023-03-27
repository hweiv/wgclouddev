package com.wgcloud.mapper;

import com.wgcloud.entity.HostGroup;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface HostGroupMapper {
   List<HostGroup> selectAllByParams(Map<String, Object> map) throws Exception;

   List<HostGroup> selectByParams(Map<String, Object> params) throws Exception;

   HostGroup selectById(String id) throws Exception;

   void save(HostGroup HostInfo) throws Exception;

   int deleteById(String[] id) throws Exception;

   int updateById(HostGroup HostInfo) throws Exception;

   int countByParams(Map<String, Object> params) throws Exception;
}
