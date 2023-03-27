package com.wgcloud.service;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.entity.DbTable;
import com.wgcloud.entity.DbTableCount;
import com.wgcloud.mapper.DbTableMapper;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.MongoDbUtil;
import com.wgcloud.util.RedisUtil;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.jdbc.ConnectionUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.staticvar.BatchData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DbTableService {
   private static final Logger logger = LoggerFactory.getLogger(DbTableService.class);
   @Autowired
   private DbTableMapper dbTableMapper;
   @Autowired
   private DbInfoService dbInfoService;
   @Autowired
   private RedisUtil redisUtil;
   @Autowired
   private MongoDbUtil mongoDbUtil;
   @Autowired
   private ConnectionUtil connectionUtil;
   @Autowired
   private LogInfoService logInfoService;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<DbTable> list = this.dbTableMapper.selectByParams(params);
      PageInfo<DbTable> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(DbTable DbTable) throws Exception {
      DbTable.setId(UUIDUtil.getUUID());
      DbTable.setCreateTime(new Date());
      if (!StringUtils.isEmpty(DbTable.getTableName())) {
         DbTable.setTableName(DbTable.getTableName().trim());
      }

      if (!StringUtils.isEmpty(DbTable.getResultExp())) {
         DbTable.setResultExp(DbTable.getResultExp().trim());
      }

      this.dbTableMapper.save(DbTable);
   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.dbTableMapper.countByParams(params);
   }

   public Long sumByParams(Map<String, Object> params) throws Exception {
      return this.dbTableMapper.sumByParams(params);
   }

   public void warnCheckExp(List<DbTable> dbTableList, List<DbInfo> dbInfos) {
      Map<String, String> dbInfoMap = new HashMap();
      Iterator var4 = dbInfos.iterator();

      while(var4.hasNext()) {
         DbInfo dbInfo = (DbInfo)var4.next();
         dbInfoMap.put(dbInfo.getId(), dbInfo.getAccount());
      }

      var4 = dbTableList.iterator();

      while(var4.hasNext()) {
         DbTable dbTable = (DbTable)var4.next();

         try {
            if (!StringUtils.isEmpty(dbTable.getResultExp()) && null != dbTable.getTableCount()) {
               Boolean result = FormatUtil.validateExpression(dbTable.getResultExp(), dbTable.getTableCount());
               if (result) {
                  dbTable.setState("2");
               }

               Runnable runnable = () -> {
                  WarnOtherUtil.sendDbTableDown(dbTable, (String)dbInfoMap.get(dbTable.getDbInfoId()), result);
               };
               ThreadPoolUtil.executor.execute(runnable);
            }
         } catch (Exception var8) {
            logger.error("数据表监控编译表达式错误", var8);
         }
      }

   }

   @Transactional
   public int deleteById(String[] id) throws Exception {
      return this.dbTableMapper.deleteById(id);
   }

   @Transactional
   public int deleteByDbInfoId(String dbInfoId) throws Exception {
      return StringUtils.isEmpty(dbInfoId) ? 0 : this.dbTableMapper.deleteByDbInfoId(dbInfoId);
   }

   public void updateById(DbTable DbTable) throws Exception {
      if (!StringUtils.isEmpty(DbTable.getTableName())) {
         DbTable.setTableName(DbTable.getTableName().trim());
      }

      if (!StringUtils.isEmpty(DbTable.getResultExp())) {
         DbTable.setResultExp(DbTable.getResultExp().trim());
      }

      this.dbTableMapper.updateById(DbTable);
   }

   @Transactional
   public void updateRecord(List<DbTable> recordList) throws Exception {
      if (recordList.size() >= 1) {
         this.dbTableMapper.updateList(recordList);
      }
   }

   public DbTable selectById(String id) throws Exception {
      return this.dbTableMapper.selectById(id);
   }

   public List<DbTable> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.dbTableMapper.selectAllByParams(params);
   }

   public void taskThreadHandler() {
      Map<String, Object> params = new HashMap();
      List<DbTable> dbTablesUpdate = new ArrayList();
      Date date = new Date();
      Long tableCount = 0L;

      try {
         List<DbInfo> dbInfos = this.dbInfoService.selectAllByParams(params);
         Iterator var6 = dbInfos.iterator();

         while(true) {
            while(var6.hasNext()) {
               DbInfo dbInfo = (DbInfo)var6.next();
               if (ServerBackupUtil.DB_INFO_ID_LIST.contains(dbInfo.getId())) {
                  logger.info("此数据源由wgcloud-server-backup监测:" + dbInfo.getAliasName());
               } else if ("redis".equals(dbInfo.getDbType())) {
                  this.redisUtil.connectRedis(dbInfo);
               } else if ("mongodb".equals(dbInfo.getDbType())) {
                  this.mongoDbUtil.connectMongoDb(dbInfo);
               } else {
                  JdbcTemplate jdbcTemplate = this.connectionUtil.getJdbcTemplate(dbInfo);
                  params.put("dbInfoId", dbInfo.getId());
                  params.put("active", "1");
                  List<DbTable> dbTables = this.selectAllByParams(params);
                  Iterator var10 = dbTables.iterator();

                  while(var10.hasNext()) {
                     DbTable dbTable = (DbTable)var10.next();
                     if (!StringUtils.isEmpty(dbTable.getWhereVal())) {
                        tableCount = this.connectionUtil.queryTableCount(dbInfo, jdbcTemplate, dbTable);
                        DbTableCount dbTableCount = new DbTableCount();
                        dbTableCount.setCreateTime(date);
                        dbTableCount.setDbTableId(dbTable.getId());
                        dbTableCount.setTableCount(tableCount);
                        BatchData.DBTABLE_COUNT_LIST.add(dbTableCount);
                        dbTable.setCreateTime(date);
                        dbTable.setTableCount(tableCount);
                        dbTablesUpdate.add(dbTable);
                     }
                  }

                  if (CollectionUtil.isEmpty(dbTables)) {
                     this.connectionUtil.getJdbcTemplate(dbInfo);
                  }
               }
            }

            if (dbTablesUpdate.size() > 0) {
               this.warnCheckExp(dbTablesUpdate, dbInfos);
               this.updateRecord(dbTablesUpdate);
            }
            break;
         }
      } catch (Exception var13) {
         logger.error("数据表监控任务错误", var13);
         this.logInfoService.save("数据表监控任务错误", var13.toString(), "2");
      }

   }

   public void saveLog(HttpServletRequest request, String action, DbTable dbTable) {
      if (null != dbTable) {
         this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "数据表监测信息：" + dbTable.getRemark(), "数据表：" + dbTable.getRemark(), "2");
      }
   }
}
