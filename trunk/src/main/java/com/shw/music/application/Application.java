package com.shw.music.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.shw.music.config.BeetlProperties;


@Configuration
@ComponentScan(basePackages = {"com.shw.music.controller", "com.shw.music.application"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, ThymeleafAutoConfiguration.class})
@EnableConfigurationProperties({BeetlProperties.class})
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate(clientHttpRequestFactory());
	}
	
	@Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory();
    }
	
}
