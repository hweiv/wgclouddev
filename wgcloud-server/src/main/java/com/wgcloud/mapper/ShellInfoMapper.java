package com.wgcloud.mapper;

import com.wgcloud.entity.ShellInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ShellInfoMapper {
   List<ShellInfo> selectAllByParams(Map<String, Object> map) throws Exception;

   List<ShellInfo> selectByParams(Map<String, Object> params) throws Exception;

   ShellInfo selectById(String id) throws Exception;

   void save(ShellInfo ShellInfo) throws Exception;

   void insertList(List<ShellInfo> recordList) throws Exception;

   void updateList(List<ShellInfo> recordList) throws Exception;

   int deleteById(String[] id) throws Exception;

   int countByParams(Map<String, Object> params) throws Exception;

   int updateById(ShellInfo ShellInfo) throws Exception;
}
