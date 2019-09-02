package com.itmuch.wii.nacos;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClientAutoConfiguration;
import com.itmuch.wii.WiiAutoConfiguration;
import com.itmuch.wii.WiiDiscoveryClient;
import com.itmuch.wii.WiiProperties;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author www.itmuch.com
 */
@Configuration
@AutoConfigureBefore({NacosDiscoveryClientAutoConfiguration.class, WiiAutoConfiguration.class})
@ConditionalOnClass(NacosDiscoveryProperties.class)
public class WiiNacosAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public WiiNacosDiscoveryProperties wiiDiscoveryProperties(WiiProperties wiiProperties) {
        return new WiiNacosDiscoveryProperties(wiiProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public WiiDiscoveryClient wiiDiscoveryClient(WiiNacosDiscoveryProperties wiiNacosDiscoveryProperties) {
        return new WiiNacosDiscoveryClient(wiiNacosDiscoveryProperties);
    }
}
