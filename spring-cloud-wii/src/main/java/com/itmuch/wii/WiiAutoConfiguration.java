package com.itmuch.wii;

import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClientAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.client.RestTemplate;

/**
 * @author www.itmuch.com
 */
@Configuration
@EnableConfigurationProperties(WiiProperties.class)
@AutoConfigureBefore(NacosDiscoveryClientAutoConfiguration.class)
public class WiiAutoConfiguration {
    @Bean
    public WiiDiscoveryProperties wiiDiscoveryProperties(WiiProperties wiiProperties) {
        return new WiiDiscoveryProperties(wiiProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WiiHealthIndicator wiiHealthIndicator(WiiProperties wiiProperties, RestTemplate restTemplate) {
        return new WiiHealthIndicator(wiiProperties, restTemplate);
    }

    @Bean
    WiiChecker wiiCleaner(WiiDiscoveryProperties wiiDiscoveryProperties, WiiHealthIndicator wiiHealthIndicator, WiiProperties wiiProperties, ConfigurableEnvironment environment) {
        WiiChecker cleaner = new WiiChecker(wiiDiscoveryProperties, wiiHealthIndicator, wiiProperties, environment);
        cleaner.check();
        return cleaner;
    }
}