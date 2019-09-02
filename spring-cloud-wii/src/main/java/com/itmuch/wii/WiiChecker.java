package com.itmuch.wii;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.core.env.ConfigurableEnvironment;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author www.itmuch.com
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WiiChecker {
    private final WiiDiscoveryClient wiiDiscoveryClient;
    private final HealthIndicator healthIndicator;
    private final WiiProperties wiiProperties;
    private final ConfigurableEnvironment environment;

    public void check() {
        Schedulers.single()
                .schedulePeriodically(
                        () -> {
                            String ip = wiiProperties.getIp();
                            Integer port = wiiProperties.getPort();

                            Status status = healthIndicator.health().getStatus();
                            String applicationName = environment.getProperty("spring.application.name");

                            if (status.equals(Status.UP)) {
                                this.wiiDiscoveryClient.registerInstance(applicationName, ip, port);
                                log.debug("Health check success. register this instance. applicationName = {}, ip = {}, port = {}, status = {}",
                                        applicationName, ip, port, status
                                );
                            } else {
                                log.warn("Health check failed. unregister this instance. applicationName = {}, ip = {}, port = {}, status = {}",
                                        applicationName, ip, port, status
                                );
                                this.wiiDiscoveryClient.deregisterInstance(applicationName, ip, port);
                            }

                        },
                        0,
                        30,
                        TimeUnit.SECONDS
                );
    }
}
