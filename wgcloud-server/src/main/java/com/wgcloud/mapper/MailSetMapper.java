package com.wgcloud.mapper;

import com.wgcloud.entity.MailSet;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface MailSetMapper {
   List<MailSet> selectAllByParams(Map<String, Object> map) throws Exception;

   void save(MailSet MailSet) throws Exception;

   int deleteById(String[] id) throws Exception;

   int updateById(MailSet MailSet) throws Exception;
}
