package com.rishi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DnsPrototype2Application {
	
	
	 protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	        return application.sources(DnsPrototype2Application.class);
	    }

	public static void main(String[] args) {
		SpringApplication.run(DnsPrototype2Application.class, args);
	}
	

}
