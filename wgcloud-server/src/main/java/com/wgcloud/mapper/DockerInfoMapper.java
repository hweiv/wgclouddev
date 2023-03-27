package com.wgcloud.mapper;

import com.wgcloud.entity.DockerInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerInfoMapper {
   List<DockerInfo> selectAllByParams(Map<String, Object> map) throws Exception;

   List<DockerInfo> selectByParams(Map<String, Object> params) throws Exception;

   DockerInfo selectById(String id) throws Exception;

   void save(DockerInfo DockerInfo) throws Exception;

   void insertList(List<DockerInfo> recordList) throws Exception;

   void updateList(List<DockerInfo> recordList) throws Exception;

   void downByHostName(List<String> recordList) throws Exception;

   int deleteById(String[] id) throws Exception;

   int deleteByHostName(Map<String, Object> map) throws Exception;

   int countByParams(Map<String, Object> params) throws Exception;

   int updateById(DockerInfo DockerInfo) throws Exception;
}
