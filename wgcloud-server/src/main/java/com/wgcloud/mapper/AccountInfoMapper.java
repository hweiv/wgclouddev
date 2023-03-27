package com.wgcloud.mapper;

import com.wgcloud.entity.AccountInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountInfoMapper {
   List<AccountInfo> selectAllByParams(Map<String, Object> map) throws Exception;

   List<AccountInfo> selectByParams(Map<String, Object> params) throws Exception;

   AccountInfo selectById(String id) throws Exception;

   void save(AccountInfo HostInfo) throws Exception;

   int deleteById(String[] id) throws Exception;

   int updateById(AccountInfo HostInfo) throws Exception;

   int countByParams(Map<String, Object> params) throws Exception;
}
