package org.ostumpf;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private static EurekaClient eurekaClient;

    public static void main(String[] args) throws Exception {
        loadProperties();

        // initialize client
        final EurekaInstanceConfig eurekaInstanceConfig = new MyDataCenterInstanceConfig();
        final InstanceInfo instanceInfo = new EurekaConfigBasedInstanceInfoProvider(eurekaInstanceConfig).get();
        final ApplicationInfoManager applicationInfoManager = new ApplicationInfoManager(eurekaInstanceConfig, instanceInfo);
        final EurekaClientConfig eurekaClientConfig = new DefaultEurekaClientConfig();

        // let's register to Eureka
        applicationInfoManager.initComponent(eurekaInstanceConfig);
        applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.UP);

        eurekaClient = new DiscoveryClient(applicationInfoManager, eurekaClientConfig);

        // print hostname of every instance of the "eureka" application
        // prints local hostname
        getApplicationInstances("eureka").stream().map(h -> "--->: " + h).forEach(logger::info);

        eurekaClient.shutdown();
    }

    /**
     * Obtains instances of the specified application from Eureka
     * @param applicationName the Eureka application name
     * @return  the (randomly sorted) list of instance hostnames
     */
    private static List<String> getApplicationInstances(final String applicationName) {
        return eurekaClient
                .getApplication(applicationName)
                .getInstances()
                .stream()
                .map(InstanceInfo::getHostName)
                .collect(Collectors.toList());
    }

    private static void loadProperties() throws IOException {
        try (final InputStream propertiesStream = Main.class.getResourceAsStream("/config.properties")) {
            System.getProperties().load(propertiesStream);
        }
    }
}
