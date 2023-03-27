package com.wgcloud.mapper;

import com.wgcloud.entity.FtpInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface FtpInfoMapper {
   List<FtpInfo> selectAllByParams(Map<String, Object> map) throws Exception;

   List<FtpInfo> selectByParams(Map<String, Object> params) throws Exception;

   FtpInfo selectById(String id) throws Exception;

   void save(FtpInfo FtpInfo) throws Exception;

   void insertList(List<FtpInfo> recordList) throws Exception;

   void updateList(List<FtpInfo> recordList) throws Exception;

   int deleteById(String[] id) throws Exception;

   int countByParams(Map<String, Object> params) throws Exception;

   int updateById(FtpInfo FtpInfo) throws Exception;

   int updateToTargetAccount(Map<String, Object> params) throws Exception;

   int updateActive(Map<String, Object> params) throws Exception;
}
