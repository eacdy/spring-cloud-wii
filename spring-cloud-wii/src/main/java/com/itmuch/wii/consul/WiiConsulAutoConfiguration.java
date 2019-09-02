package com.itmuch.wii.consul;

import com.itmuch.wii.WiiAutoConfiguration;
import com.itmuch.wii.WiiDiscoveryClient;
import com.itmuch.wii.WiiProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.serviceregistry.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author www.itmuch.com
 */
@Configuration
@ConditionalOnClass(ConsulServiceRegistryAutoConfiguration.class)
@AutoConfigureBefore({ConsulAutoServiceRegistrationAutoConfiguration.class, WiiAutoConfiguration.class})
public class WiiConsulAutoConfiguration {
    @Bean
    public ConsulAutoRegistration consulRegistration(
            AutoServiceRegistrationProperties autoServiceRegistrationProperties,
            ConsulDiscoveryProperties properties,
            ApplicationContext applicationContext,
            ObjectProvider<List<ConsulRegistrationCustomizer>> registrationCustomizers,
            ObjectProvider<List<ConsulManagementRegistrationCustomizer>> managementRegistrationCustomizers,
            HeartbeatProperties heartbeatProperties,
            WiiProperties wiiProperties) {
        return WiiConsulAutoRegistration.registration(autoServiceRegistrationProperties,
                properties, applicationContext, registrationCustomizers.getIfAvailable(),
                managementRegistrationCustomizers.getIfAvailable(), heartbeatProperties, wiiProperties);
    }

    @Bean
    public WiiDiscoveryClient wiiDiscoveryClient(
            ConsulDiscoveryProperties properties,
            ConsulServiceRegistry serviceRegistry,
            ConsulAutoRegistration registration) {
        return new WiiConsulDiscoveryClient(properties, serviceRegistry, registration);
    }
}
