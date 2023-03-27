package com.wgcloud.mapper;

import com.wgcloud.entity.Equipment;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentMapper {
   List<Equipment> selectAllByParams(Map<String, Object> map) throws Exception;

   List<Equipment> selectByParams(Map<String, Object> params) throws Exception;

   Equipment selectById(String id) throws Exception;

   void save(Equipment Equipment) throws Exception;

   int deleteById(String[] id) throws Exception;

   int countByParams(Map<String, Object> params) throws Exception;

   int updateById(Equipment Equipment) throws Exception;
}
