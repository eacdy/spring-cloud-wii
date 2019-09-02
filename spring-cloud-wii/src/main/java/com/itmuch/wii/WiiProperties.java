package com.itmuch.wii;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.net.URI;

/**
 * @author www.itmuch.com
 */
@ConfigurationProperties("wii")
@Data
@Validated
public class WiiProperties {
    /**
     * polyglot service's ip
     */
    private String ip;

    /**
     * polyglot service's port
     */
    @NotNull
    @Max(65535)
    @Min(1)
    private Integer port;

    /**
     * polyglot service's health check url.
     * this endpoint must return json and the format must follow spring boot actuator's health endpoint.
     * eg. {"status": "UP"}
     */
    private URI healthCheckUrl;
}
