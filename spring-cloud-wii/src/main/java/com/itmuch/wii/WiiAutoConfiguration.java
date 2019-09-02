package com.itmuch.wii;

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
public class WiiAutoConfiguration {
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
    public WiiChecker wiiCleaner(WiiDiscoveryClient wiiDiscoveryClient, WiiHealthIndicator wiiHealthIndicator, WiiProperties wiiProperties, ConfigurableEnvironment environment) {
        WiiChecker cleaner = new WiiChecker(wiiDiscoveryClient, wiiHealthIndicator, wiiProperties, environment);
        cleaner.check();
        return cleaner;
    }
}