package net.laurus.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import net.laurus.thread.LaurusThreadFactory;

@Configuration
public class AppConfig {

    @Bean
    public ExecutorService threadPool() {
        return Executors.newCachedThreadPool(new LaurusThreadFactory("Client-Thread", true));
    }

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder().defaultUseWrapper(false).indentOutput(true);
    }
}
