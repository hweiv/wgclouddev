package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.mapper.DbInfoMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DbInfoService {
   @Autowired
   private DbInfoMapper dbInfoMapper;
   @Autowired
   private LogInfoService logInfoService;

   public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
      PageHelper.startPage(currPage, pageSize);
      List<DbInfo> list = this.dbInfoMapper.selectByParams(params);
      PageInfo<DbInfo> pageInfo = new PageInfo(list);
      return pageInfo;
   }

   public void save(DbInfo DbInfo) throws Exception {
      DbInfo.setId(UUIDUtil.getUUID());
      DbInfo.setCreateTime(DateUtil.getNowTime());
      DbInfo.setDbState("1");
      this.dbInfoMapper.save(DbInfo);
   }

   public int countByParams(Map<String, Object> params) throws Exception {
      return this.dbInfoMapper.countByParams(params);
   }

   @Transactional
   public int deleteById(String[] id) throws Exception {
      return this.dbInfoMapper.deleteById(id);
   }

   public int updateById(DbInfo DbInfo) throws Exception {
      return this.dbInfoMapper.updateById(DbInfo);
   }

   public DbInfo selectById(String id) throws Exception {
      return this.dbInfoMapper.selectById(id);
   }

   public List<DbInfo> selectAllByParams(Map<String, Object> params) throws Exception {
      return this.dbInfoMapper.selectAllByParams(params);
   }

   public void saveLog(HttpServletRequest request, String action, DbInfo dbInfo) {
      if (null != dbInfo) {
         this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "数据源监测信息：" + dbInfo.getAliasName(), "数据库类型：" + dbInfo.getDbType(), "2");
      }
   }

   public void dbAddLogo(List<DbInfo> list) throws Exception {
      Iterator var2 = list.iterator();

      while(var2.hasNext()) {
         DbInfo dbInfo = (DbInfo)var2.next();
         if (!StringUtils.isEmpty(dbInfo.getDbType())) {
            if ("mysql".equals(dbInfo.getDbType())) {
               dbInfo.setImage("/wgcloud/static/images/mysql.png");
            } else if ("mariadb".equals(dbInfo.getDbType())) {
               dbInfo.setImage("/wgcloud/static/images/mariadb.png");
            } else if ("postgresql".equals(dbInfo.getDbType())) {
               dbInfo.setImage("/wgcloud/static/images/postgresql.png");
            } else if ("sqlserver".equals(dbInfo.getDbType())) {
               dbInfo.setImage("/wgcloud/static/images/windows.png");
            } else if ("db2".equals(dbInfo.getDbType())) {
               dbInfo.setImage("/wgcloud/static/images/db2.png");
            } else if ("redis".equals(dbInfo.getDbType())) {
               dbInfo.setImage("/wgcloud/static/images/redisDb.png");
            } else if ("mongodb".equals(dbInfo.getDbType())) {
               dbInfo.setImage("/wgcloud/static/images/mongoDb.png");
            } else {
               dbInfo.setImage("/wgcloud/static/images/oracle.png");
            }
         } else {
            dbInfo.setImage("/wgcloud/static/images/mysql.png");
         }
      }

   }

   public void updateToTargetAccount(Map<String, Object> params) throws Exception {
      this.dbInfoMapper.updateToTargetAccount(params);
   }
}
