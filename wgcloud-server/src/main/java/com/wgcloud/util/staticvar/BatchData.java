package com.wgcloud.util.staticvar;

import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.AppState;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.entity.CustomInfo;
import com.wgcloud.entity.CustomState;
import com.wgcloud.entity.DbTableCount;
import com.wgcloud.entity.DceState;
import com.wgcloud.entity.DeskIo;
import com.wgcloud.entity.DiskSmart;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.DockerInfo;
import com.wgcloud.entity.DockerState;
import com.wgcloud.entity.FileSafe;
import com.wgcloud.entity.FileWarnInfo;
import com.wgcloud.entity.FileWarnState;
import com.wgcloud.entity.HeathState;
import com.wgcloud.entity.LogInfo;
import com.wgcloud.entity.MemState;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.entity.PortInfo;
import com.wgcloud.entity.SnmpState;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.entity.TcpState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BatchData {
   public static List<SystemInfo> SYSTEM_INFO_LIST = Collections.synchronizedList(new ArrayList());
   public static List<AppInfo> APP_INFO_LIST = Collections.synchronizedList(new ArrayList());
   public static List<AppState> APP_STATE_LIST = Collections.synchronizedList(new ArrayList());
   public static List<FileWarnInfo> FILEWARN_INFO_LIST = Collections.synchronizedList(new ArrayList());
   public static List<FileWarnState> FILEWARN_STATE_LIST = Collections.synchronizedList(new ArrayList());
   public static List<CustomInfo> CUSTOM_INFO_LIST = Collections.synchronizedList(new ArrayList());
   public static List<CustomState> CUSTOM_STATE_LIST = Collections.synchronizedList(new ArrayList());
   public static List<DockerInfo> DOCKER_INFO_LIST = Collections.synchronizedList(new ArrayList());
   public static List<PortInfo> PORT_INFO_LIST = Collections.synchronizedList(new ArrayList());
   public static List<FileSafe> FILE_SAFE_LIST = Collections.synchronizedList(new ArrayList());
   public static List<DockerState> DOCKER_STATE_LIST = Collections.synchronizedList(new ArrayList());
   public static List<CpuState> CPU_STATE_LIST = Collections.synchronizedList(new ArrayList());
   public static List<MemState> MEM_STATE_LIST = Collections.synchronizedList(new ArrayList());
   public static List<NetIoState> NETIO_STATE_LIST = Collections.synchronizedList(new ArrayList());
   public static List<DiskState> DISK_STATE_LIST = Collections.synchronizedList(new ArrayList());
   public static List<DeskIo> DESK_IO_LIST = Collections.synchronizedList(new ArrayList());
   public static List<DiskSmart> DISK_SMART_LIST = Collections.synchronizedList(new ArrayList());
   public static List<CpuTemperatures> CPU_TEMPERATURES_LIST = Collections.synchronizedList(new ArrayList());
   public static List<SysLoadState> SYSLOAD_STATE_LIST = Collections.synchronizedList(new ArrayList());
   public static List<TcpState> TCP_STATE_LIST = Collections.synchronizedList(new ArrayList());
   public static List<LogInfo> LOG_INFO_LIST = Collections.synchronizedList(new ArrayList());
   public static List<HeathState> HEATH_STATE_LIST = Collections.synchronizedList(new ArrayList());
   public static List<DceState> DCE_STATE_LIST = Collections.synchronizedList(new ArrayList());
   public static List<SnmpState> SNMP_STATE_LIST = Collections.synchronizedList(new ArrayList());
   public static List<DbTableCount> DBTABLE_COUNT_LIST = Collections.synchronizedList(new ArrayList());

   public static void clearAll() {
      SYSTEM_INFO_LIST.clear();
      APP_INFO_LIST.clear();
      FILEWARN_INFO_LIST.clear();
      FILEWARN_STATE_LIST.clear();
      DOCKER_INFO_LIST.clear();
      PORT_INFO_LIST.clear();
      CPU_TEMPERATURES_LIST.clear();
   }
}
