package com.wgcloud.util;

import com.github.pagehelper.PageInfo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.ui.Model;

public class PageUtil {
   public static void initPageNumber(PageInfo pageInfo, Model model) {
      List<String> pageNumbers = new ArrayList();
      int i;
      if (pageInfo.getPages() >= 10 && pageInfo.getPageNum() <= 5) {
         for(i = 0; i < 10; ++i) {
            pageNumbers.add(1 + i + "");
         }
      } else {
         for(i = 5; i > 0; --i) {
            if (pageInfo.getPageNum() - i > 0) {
               pageNumbers.add(pageInfo.getPageNum() - i + "");
            }
         }

         for(i = 0; i < 5; ++i) {
            if (pageInfo.getPageNum() + i <= pageInfo.getPages()) {
               pageNumbers.add(pageInfo.getPageNum() + i + "");
            }
         }
      }

      model.addAttribute("pageNumbers", pageNumbers);
   }
}
