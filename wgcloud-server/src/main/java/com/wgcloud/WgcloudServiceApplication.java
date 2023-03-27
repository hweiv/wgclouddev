package com.wgcloud;

import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(
   exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class}
)
@MapperScan({"com.wgcloud.mapper"})
@ServletComponentScan({"com.wgcloud.filter"})
@ComponentScan(
   basePackages = {"com.wgcloud"}
)
@EnableCaching
@EnableScheduling
public class WgcloudServiceApplication {
   public static void main(String[] args) {
      SpringApplication.run(WgcloudServiceApplication.class, args);
   }

   @Bean
   public RestTemplate restTemplate() {
      HttpComponentsClientHttpRequestFactory requestFactory = null;

      try {
         requestFactory = generateHttpRequestFactory();
      } catch (NoSuchAlgorithmException var3) {
         var3.printStackTrace();
      } catch (KeyManagementException var4) {
         var4.printStackTrace();
      } catch (KeyStoreException var5) {
         var5.printStackTrace();
      }

      requestFactory.setConnectTimeout(20000);
      requestFactory.setReadTimeout(20000);
      RestTemplate restTemplate = new RestTemplate(requestFactory);
      restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
      return restTemplate;
   }

   public static HttpComponentsClientHttpRequestFactory generateHttpRequestFactory() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
      TrustStrategy acceptingTrustStrategy = (x509Certificates, authType) -> {
         return true;
      };
      SSLContext sslContext = SSLContexts.custom().loadTrustMaterial((KeyStore)null, acceptingTrustStrategy).build();
      SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
      HttpClientBuilder httpClientBuilder = HttpClients.custom();
      httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
      CloseableHttpClient httpClient = httpClientBuilder.build();
      HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
      factory.setHttpClient(httpClient);
      return factory;
   }

   @Bean
   public TaskScheduler taskScheduler() {
      ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
      taskScheduler.setPoolSize(50);
      return taskScheduler;
   }
}
