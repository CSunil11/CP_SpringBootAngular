package com.ackermans.criticalpath;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
public class CriticalPathServer {

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	public static void main(String[] args) {
		SpringApplication.run(CriticalPathServer.class, args);
	}

	@Bean
	public Module datatypeHibernateModule() {
		return new Hibernate5Module();
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
