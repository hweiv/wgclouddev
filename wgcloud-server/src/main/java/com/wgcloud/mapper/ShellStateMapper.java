package com.wgcloud.mapper;

import com.wgcloud.entity.ShellState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ShellStateMapper {
   List<ShellState> selectAllByParams(Map<String, Object> map) throws Exception;

   List<ShellState> selectByParams(Map<String, Object> params) throws Exception;

   ShellState selectById(String id) throws Exception;

   int countByParams(Map<String, Object> map);

   void save(ShellState ShellState) throws Exception;

   void insertList(List<ShellState> recordList) throws Exception;

   int deleteByShellId(String shellId) throws Exception;

   int deleteByDate(Map<String, Object> map) throws Exception;

   int deleteById(String[] id) throws Exception;

   int updateSendByIds(String[] id) throws Exception;

   int cancelByShellId(String shellId) throws Exception;

   int restartByShellId(String shellId, String shellTime) throws Exception;

   int updateById(ShellState ShellState) throws Exception;
}
