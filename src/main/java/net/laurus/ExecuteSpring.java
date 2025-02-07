package net.laurus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import lombok.Getter;

@SpringBootApplication(scanBasePackages = { 
		"net.laurus.app",
		"net.laurus.controller",
		"net.laurus.queue",
		"net.laurus.service"})
@EnableAsync
@Getter
public class ExecuteSpring {
	
	public static void main(String[] args) {
        SpringApplication.run(ExecuteSpring.class, args);
	}

}
