package org.ostumpf;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
import com.netflix.client.AbstractLoadBalancerAwareClient;
import com.netflix.client.ClientFactory;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.ConfigurationManager;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        // init Eureka client
        final EurekaInstanceConfig eurekaInstanceConfig = new MyDataCenterInstanceConfig();
        final InstanceInfo instanceInfo = new EurekaConfigBasedInstanceInfoProvider(eurekaInstanceConfig).get();
        final ApplicationInfoManager applicationInfoManager = new ApplicationInfoManager(eurekaInstanceConfig, instanceInfo);

        applicationInfoManager.initComponent(eurekaInstanceConfig);
        applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.UP);

        final EurekaClientConfig eurekaClientConfig = new DefaultEurekaClientConfig();
        final EurekaClient eurekaClient = new DiscoveryClient(applicationInfoManager, eurekaClientConfig);

        // load Ribbon properties
        ConfigurationManager.loadPropertiesFromResources("sample-client.properties");

        // create HTTP client
        final AbstractLoadBalancerAwareClient client = (AbstractLoadBalancerAwareClient) ClientFactory.getNamedClient("sample-client");

        final HttpRequest request = HttpRequest.newBuilder().uri(new URI("/")).build();

        // issue requests
        for (int i = 0; i < 20; i++)  {
            final HttpResponse response = (HttpResponse) client.executeWithLoadBalancer(request);

            logger.info("Status code for " + response.getRequestedURI() + "  :" + response.getStatus());
            Thread.sleep(1000);
        }

        eurekaClient.shutdown();
    }
}
