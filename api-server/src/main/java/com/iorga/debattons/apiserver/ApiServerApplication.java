package com.iorga.debattons.apiserver;

import com.iorga.debattons.apiserver.security.DebattonsAuthenticationProvider;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.boot.actuate.security.AbstractAuthorizationAuditListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.event.AbstractAuthorizationEvent;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ApiServerApplication {

//   Enabling CORS thanks to https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-cors
//  @Configuration
//  public class MyConfiguration {

  // Enabling CORS thanks to https://spring.io/guides/gs/rest-service-cors/
    @Bean
    public WebMvcConfigurer corsConfigurer() {
      return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
          registry.addMapping("/**").allowedOrigins("http://localhost:4200"); // TODO remove this in production
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

  @Configuration
  @EnableWebSecurity
  public static class ApiServerWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private DebattonsAuthenticationProvider debattonsAuthenticationProvider;

    // Inspired by https://stackoverflow.com/a/35607507/535203
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//      http.authorizeRequests().antMatchers("/**").permitAll();
//      super.configure(http);
      http.httpBasic()
        .and().csrf().disable(); // TODO disable this in production
    }

//    // Inspired by http://www.baeldung.com/spring-security-authentication-provider
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//      auth.authenticationProvider(debattonsAuthenticationProvider);
//    }
  }

  @Configuration
  @EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true)
  public static class ApiServerGlobalMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {
    @Autowired
    private DebattonsAuthenticationProvider debattonsAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.authenticationProvider(debattonsAuthenticationProvider);
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(ApiServerApplication.class, args);
  }

}
