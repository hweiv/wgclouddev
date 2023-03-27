package com.wgcloud.mapper;

import com.wgcloud.entity.DockerState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerStateMapper {
   List<DockerState> selectAllByParams(Map<String, Object> map) throws Exception;

   List<DockerState> selectByParams(Map<String, Object> params) throws Exception;

   DockerState selectById(String id) throws Exception;

   int selectByParamsCount(Map<String, Object> map);

   void save(DockerState DockerState) throws Exception;

   void insertList(List<DockerState> recordList) throws Exception;

   int deleteByDockerInfoId(String appInfoId) throws Exception;

   int deleteByDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;
}
