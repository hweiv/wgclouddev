package com.wgcloud.filter;

import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@WebFilter(
   filterName = "authRestFilter",
   urlPatterns = {"/*"}
)
public class AuthRestFilter implements Filter {
   static Logger log = LoggerFactory.getLogger(AuthRestFilter.class);
   @Autowired
   CommonConfig commonConfig;
   String[] static_resource = new String[]{"/fileSafe/agentList", "/shellInfo/agentList", "/shellInfo/shellCallback", "/fileWarnInfo/agentStateListList", "/fileWarnInfo/agentList", "/heathMonitor/agentList", "/dbTable/agentList", "/systemInfoOpen/", "/systemInfo/agentList", "/agentLogGo/minTask", "/agentGo/minTask", "/agentDiskGo/minTask", "/dceInfo/agentList", "/login/toLogin", "/login/login", "/appInfo/agentList", "/dockerInfo/agentList", "/portInfo/agentList", "/license/", "/static/", "/resources/", "/log/agentList", "/customInfo/agentList", "/agentCustomGo/minTask", "/dbInfo/agentList", "/agentDbTableGo/minTask", "/agentHeathMonitorGo/minTask", "/agentDceInfoGo/minTask", "/agentSnmpInfoGo/minTask", "/snmpInfo/agentList", "/ftpInfo/agentList", "/agentFtpInfoGo/minTask", "/agentLastlogInfoGo/minTask"};
   String[] dash_views = new String[]{"/dash/main", "/systemInfo/systemInfoList", "/systemInfo/systemInfoListAjax", "/systemInfo/detail", "/systemInfo/chart", "/warnInfo/warnCountAjax", "/warnInfo/warnCountAjaxHandle"};
   String[] daping_views = new String[]{"/daping/index", "/dapingNew/index"};
   String[] guest_no_views = new String[]{"/del", "/save", "/edit", "/editBatch"};

   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
      HttpServletResponse response = (HttpServletResponse)servletResponse;
      HttpServletRequest request = (HttpServletRequest)servletRequest;
      HttpSession session = request.getSession();
      AccountInfo accountInfo = (AccountInfo)session.getAttribute("LOGIN_KEY");
      String uri = request.getRequestURI();
      log.debug("uri----" + uri);
      String servletPath = request.getServletPath();
      log.debug("servletPath----" + servletPath);
      this.menuActive(session, uri);
      if (accountInfo == null || !servletPath.startsWith("/login/toLogin") && !uri.endsWith("wgcloud/") && !uri.endsWith("wgcloud")) {
         String[] var10;
         int var11;
         int var12;
         String ss;
         if (accountInfo != null && "guest".equals(accountInfo.getRole())) {
            var10 = this.guest_no_views;
            var11 = var10.length;

            for(var12 = 0; var12 < var11; ++var12) {
               ss = var10[var12];
               if (servletPath.endsWith(ss)) {
                  response.sendRedirect("/wgcloud/common/error/guestError");
                  return;
               }
            }
         }

         var10 = this.static_resource;
         var11 = var10.length;

         for(var12 = 0; var12 < var11; ++var12) {
            ss = var10[var12];
            if (servletPath.startsWith(ss)) {
               filterChain.doFilter(servletRequest, servletResponse);
               return;
            }
         }

         if (accountInfo == null) {
            var10 = this.dash_views;
            var11 = var10.length;

            for(var12 = 0; var12 < var11; ++var12) {
               ss = var10[var12];
               if (servletPath.startsWith(ss) && "true".equals(this.commonConfig.getDashView()) && request.getParameter("dashView") != null) {
                  filterChain.doFilter(servletRequest, servletResponse);
                  return;
               }
            }
         }

         if (accountInfo == null) {
            var10 = this.daping_views;
            var11 = var10.length;

            for(var12 = 0; var12 < var11; ++var12) {
               ss = var10[var12];
               if (servletPath.startsWith(ss) && "true".equals(this.commonConfig.getDapingView())) {
                  filterChain.doFilter(servletRequest, servletResponse);
                  return;
               }
            }
         }

         if (accountInfo == null) {
            response.sendRedirect("/wgcloud/login/toLogin");
         } else {
            filterChain.doFilter(servletRequest, servletResponse);
         }
      } else {
         response.sendRedirect("/wgcloud/dash/main");
      }
   }

   public void menuActive(HttpSession session, String uri) {
      if (uri.indexOf("/log/") > -1) {
         session.setAttribute("menuActive", "21");
      } else if (uri.indexOf("/dash/main") > -1) {
         session.setAttribute("menuActive", "01");
      } else if (uri.indexOf("/systemInfo/systemInfoList") <= -1 && uri.indexOf("/systemInfo/detail") <= -1 && uri.indexOf("/systemInfo/chart") <= -1 && uri.indexOf("/dash/hostDraw") <= -1) {
         if (uri.indexOf("/appInfo") > -1) {
            session.setAttribute("menuActive", "13");
         } else if (uri.indexOf("/dockerInfo") > -1) {
            session.setAttribute("menuActive", "14");
         } else if (uri.indexOf("/portInfo") > -1) {
            session.setAttribute("menuActive", "15");
         } else if (uri.indexOf("/fileWarnInfo") > -1) {
            session.setAttribute("menuActive", "16");
         } else if (uri.indexOf("/fileSafe") > -1) {
            session.setAttribute("menuActive", "17");
         } else if (uri.indexOf("/customInfo") > -1) {
            session.setAttribute("menuActive", "18");
         } else if (uri.indexOf("/mailset") > -1) {
            session.setAttribute("menuActive", "22");
         } else if (uri.indexOf("/shellInfo") > -1) {
            session.setAttribute("menuActive", "23");
         } else if (uri.indexOf("/hostGroup") > -1) {
            session.setAttribute("menuActive", "24");
         } else if (uri.indexOf("/accountInfo") > -1) {
            session.setAttribute("menuActive", "25");
         } else if (uri.indexOf("/dbInfo") > -1) {
            session.setAttribute("menuActive", "41");
         } else if (uri.indexOf("/dbTable") > -1) {
            session.setAttribute("menuActive", "42");
         } else if (uri.indexOf("/heathMonitor") > -1) {
            session.setAttribute("menuActive", "51");
         } else if (uri.indexOf("/dceInfo") > -1) {
            session.setAttribute("menuActive", "61");
         } else if (uri.indexOf("/snmpInfo") > -1) {
            session.setAttribute("menuActive", "62");
         } else if (uri.indexOf("/tuopu/tuopuListHost") > -1) {
            session.setAttribute("menuActive", "71");
         } else if (uri.indexOf("/tuopu/tuopuListSt") > -1) {
            session.setAttribute("menuActive", "72");
         } else if (uri.indexOf("/tuopu/tuopuListSnmp") > -1) {
            session.setAttribute("menuActive", "73");
         } else if (uri.indexOf("/equipment") > -1) {
            session.setAttribute("menuActive", "81");
         } else if (uri.indexOf("/report") > -1) {
            session.setAttribute("menuActive", "91");
         } else if (uri.indexOf("/ftpInfo") > -1) {
            session.setAttribute("menuActive", "a1");
         } else {
            session.setAttribute("menuActive", "01");
         }
      } else {
         session.setAttribute("menuActive", "12");
      }
   }
}
