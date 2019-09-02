package com.itmuch.wii.consul;

import com.itmuch.wii.WiiDiscoveryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;

/**
 * @author www.itmuch.com
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WiiConsulDiscoveryClient implements WiiDiscoveryClient {
    private final ConsulDiscoveryProperties properties;
    private final ConsulServiceRegistry serviceRegistry;
    private final ConsulAutoRegistration registration;

    @Override
    public void registerInstance(String applicationName, String ip, Integer port) {

        if (!this.properties.isRegister()) {
            log.debug("Registration disabled.");
            return;
        }

        serviceRegistry.register(registration);

    }

    @Override
    public void deregisterInstance(String applicationName, String ip, Integer port) {
        if (!this.properties.isRegister() || !this.properties.isDeregister()) {
            return;
        }
        serviceRegistry.deregister(registration);
    }
}
