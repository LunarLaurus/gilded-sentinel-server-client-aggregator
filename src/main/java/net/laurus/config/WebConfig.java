package net.laurus.config;

import lombok.extern.java.Log;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@Log
public class WebConfig implements WebMvcConfigurer {

    private static final int[] localPorts = { 3000, 32560 };
    private static final String[] protocols = { "http", "https" };
    private static final String[] localHosts = { "localhost", "127.0.0.1" };

    @Value("${server.port}")
    private int userDefinedSpringPort;
    
    @Value("${system.allowed-ip}")
    private String propertyAllowedIp;

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setAfterMessagePrefix("REQUEST DATA: ");
        return filter;
    }

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        log.info("Using Environment IP for CORS: " + propertyAllowedIp);
        registry
                .addMapping("/**")
                .allowedOrigins(convertSetToArray(getCorsAllowedOrigins()))
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    private static final String[] convertSetToArray(Set<String> set) {
        return set.toArray(new String[0]);
    }

    private Set<String> getCorsAllowedOrigins() {
        final Set<String> corsOriginCache = new HashSet<>();
        int[] localPortsWithCustom = Arrays.copyOf(localPorts, localPorts.length + 1);
        localPortsWithCustom[localPorts.length] = userDefinedSpringPort;
        
        for (String proto : protocols) {
            String localClient = proto + "://" + propertyAllowedIp;
            if (corsOriginCache.add(localClient)) {
                log.info("Mapping " + localClient + " as allowedOrigins for CORS.");
            }
            for (int port : localPortsWithCustom) {
                localClient = proto + "://" + propertyAllowedIp + ":" + port;
                if (corsOriginCache.add(localClient)) {
                    log.info("Mapping " + localClient + " as allowedOrigins for CORS.");
                }
                for (String local : localHosts) {
                    localClient = proto + "://" + local + ":" + port;
                    if (corsOriginCache.add(localClient)) {
                        log.info("Mapping " + localClient + " as allowedOrigins for CORS.");
                    }
                }
            }
        }
        return corsOriginCache;
    }
}
