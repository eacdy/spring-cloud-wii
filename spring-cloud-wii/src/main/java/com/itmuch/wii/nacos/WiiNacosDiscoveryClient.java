package com.itmuch.wii.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.itmuch.wii.WiiDiscoveryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author www.itmuch.com
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WiiNacosDiscoveryClient implements WiiDiscoveryClient {
    private final WiiNacosDiscoveryProperties wiiNacosDiscoveryProperties;

    @Override
    public void registerInstance(String applicationName, String ip, Integer port) {
        try {
            this.wiiNacosDiscoveryProperties.namingServiceInstance()
                    .registerInstance(applicationName, ip, port);
        } catch (NacosException e) {
            log.warn("nacos exception happens", e);
        }
    }

    @Override
    public void deregisterInstance(String applicationName, String ip, Integer port) {
        try {
            this.wiiNacosDiscoveryProperties.namingServiceInstance()
                    .deregisterInstance(applicationName, ip, port);
        } catch (NacosException e) {
            log.warn("nacos exception happens", e);
        }
    }
}
