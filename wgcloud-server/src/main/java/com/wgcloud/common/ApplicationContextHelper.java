package com.wgcloud.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHelper implements ApplicationContextAware {
   private static ApplicationContext applicationContext;

   public void setApplicationContext(ApplicationContext applicationContext1) throws BeansException {
      applicationContext = applicationContext1;
   }

   public static ApplicationContext getApplicationContext() {
      return applicationContext;
   }

   public static <T> T getBean(Class<T> clazz) {
      return applicationContext.getBean(clazz);
   }
}
