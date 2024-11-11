package net.laurus;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.Getter;

@SpringBootApplication(scanBasePackages = { "net.laurus" })
@EnableScheduling
@EnableAsync
@EnableRabbit
@Getter
public class ExecuteSpring {
	
	public static void main(String[] args) {
        SpringApplication.run(ExecuteSpring.class, args);
	}

}
