package com.itmuch.wii;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

import java.net.SocketException;

/**
 * @author itmuch.com
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WiiDiscoveryProperties extends NacosDiscoveryProperties {
    private final WiiProperties wiiProperties;

    @Override
    public void init() throws SocketException {
        super.init();

        String ip = wiiProperties.getIp();
        if (StringUtils.isNotBlank(ip)) {
            this.setIp(ip);
        }

        Integer port = wiiProperties.getPort();
        this.setPort(port);
    }
}
