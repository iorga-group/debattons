package com.iorga.debattons.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class ApiServerApplication {

//   Enabling CORS thanks to https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-cors
//  @Configuration
//  public class MyConfiguration {

  // Enabling CORS thanks to https://spring.io/guides/gs/rest-service-cors/
    @Bean
    public WebMvcConfigurer corsConfigurer() {
      return new WebMvcConfigurerAdapter() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
          registry.addMapping("/**").allowedOrigins("http://localhost:4200");
        }
      };
    }
//  }

//  // Enabling CORS thanks to https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/cors.html
//  @EnableWebSecurity
//  public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//      http
//        // by default uses a Bean by the name of corsConfigurationSource
//        .cors();
//    }
//
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//      CorsConfiguration configuration = new CorsConfiguration();
//      configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
//      configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
//      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//      source.registerCorsConfiguration("/**", configuration);
//      return source;
//    }
//  }

//  // Enabling CORS thanks to https://sandstorm.de/de/blog/post/cors-headers-for-spring-boot-kotlin-webflux-reactor-project.html
//  @Component
//  public class CorsFilter implements WebFilter {
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//      if (exchange != null) {
//        ServerHttpResponse response = exchange.getResponse();
//        HttpHeaders headers = response.getHeaders();
//        headers.add("Access-Control-Allow-Origin", "http://localhost:8080");
//        headers.add("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
//        headers.add("Access-Control-Allow-Headers", "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range");
//        if (exchange.getRequest().getMethod() == OPTIONS) {
//          headers.add("Access-Control-Max-Age", "1728000");
//          response.setStatusCode(NO_CONTENT);
//          return Mono.empty();
//        } else {
//          headers.add("Access-Control-Expose-Headers", "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Content-Range,Range");
//          return continueFilter(exchange, chain);
//        }
//      } else {
//        return continueFilter(exchange, chain);
//      }
//    }
//
//    private Mono<Void> continueFilter(ServerWebExchange exchange, WebFilterChain chain) {
//      if (chain != null) {
//        Mono<Void> filterResult = chain.filter(exchange);
//        if (filterResult != null) {
//          return filterResult;
//        }
//      }
//      return Mono.empty();
//    }
//  }

  public static void main(String[] args) {
    SpringApplication.run(ApiServerApplication.class, args);
  }

}
