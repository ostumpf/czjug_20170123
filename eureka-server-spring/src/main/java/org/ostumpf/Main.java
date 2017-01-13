package org.ostumpf;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableEurekaServer
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
public class Main {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Main.class).web(true).run(args);
    }

}
