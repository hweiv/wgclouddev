package com.wgcloud.util;

import com.wgcloud.dto.TuopuNodeDto;
import java.util.List;

public class TuopuUtil {
   public static void initList(List<TuopuNodeDto> list) {
      int centerX = 760;
      int centerY = 520;
      int radius = getRadius(list.size());
      int count = list.size();

      for(int i = 0; i < list.size(); ++i) {
         int x = centerX + (int)((double)radius * Math.cos(6.283185307179586D / (double)count * (double)i));
         int y = centerY + (int)((double)radius * Math.sin(6.283185307179586D / (double)count * (double)i));
         ((TuopuNodeDto)list.get(i)).setX(x);
         ((TuopuNodeDto)list.get(i)).setY(y);
      }

   }

   public static int getRadius(int count) {
      if (count == 1) {
         count = 2;
      }

      double disitem = 360.0D / (double)count;
      double minr = 5.0D / Math.sin(0.5D * disitem * 3.141592653589793D / 180.0D);
      double noder = minr * 1.5D + 15.0D + 80.0D;
      if (noder < 260.0D) {
         noder = 260.0D;
      }

      return Integer.valueOf(String.valueOf(noder).substring(0, String.valueOf(noder).indexOf(".")));
   }
}
