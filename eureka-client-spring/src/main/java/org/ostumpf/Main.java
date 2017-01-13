package org.ostumpf;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableEurekaClient
@EnableAutoConfiguration
@PropertySource({"classpath:config.properties"})
@RestController
public class Main {

    private final EurekaClient eurekaClient;

    @Autowired
    public Main(final EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    @RequestMapping("/")
    public String home() {
        return getApplicationInstances("eureka").stream().collect(Collectors.joining(", "));
    }

    /**
     * Obtains instances of the specified application from Eureka
     * @param applicationName the Eureka application name
     * @return  the (randomly sorted) list of instance hostnames
     */
    private List<String> getApplicationInstances(final String applicationName) {
        return eurekaClient
                .getApplication(applicationName)
                .getInstances()
                .stream()
                .map(InstanceInfo::getHostName)
                .collect(Collectors.toList());
    }

    public static void main(final String[] args) throws IOException {

        // go to http://localhost:8088/ to see the list of hostnames of the Eureka application instances
        SpringApplication.run(Main.class, args);
    }
}
