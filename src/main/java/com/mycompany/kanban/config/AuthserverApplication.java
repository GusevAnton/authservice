package com.mycompany.kanban.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootApplication(scanBasePackages = {
		"com.mycompany.kanban.config",
		"com.mycompany.kanban.endpoint",
		"com.mycompany.kanban.security",
		"com.mycompany.kanban.service",
		"com.mycompany.common.client.fallback",
		"com.mycompany.common.function"
})
@EnableFeignClients(basePackages = "com.mycompany.common.client")
@EnableEurekaClient
@EnableAsync
@Controller
public class AuthserverApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(AuthserverApplication.class, args);
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/oauth/confirm_access").setViewName("authorize");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new StandardPasswordEncoder();
	}

	@Bean
	public Executor executor() {
		return Executors.newCachedThreadPool();
	}

}
