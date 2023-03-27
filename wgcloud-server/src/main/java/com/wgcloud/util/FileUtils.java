package com.wgcloud.util;

import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {
   private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

   public static void getFileList(String filePath, List<String> filePathList) {
      File dir = new File(filePath);
      if (dir.exists()) {
         File[] files = dir.listFiles();
         if (files != null) {
            for(int i = 0; i < files.length; ++i) {
               String fileName = files[i].getName();
               if (files[i].isDirectory()) {
                  getFileList(files[i].getAbsolutePath(), filePathList);
               } else {
                  String strFileName = files[i].getAbsolutePath();
                  filePathList.add(files[i].getAbsolutePath());
               }
            }
         }

      }
   }
}
