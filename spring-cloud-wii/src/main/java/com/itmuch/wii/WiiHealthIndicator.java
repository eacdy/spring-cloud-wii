package com.itmuch.wii;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

/**
 * @author www.itmuch.com
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WiiHealthIndicator extends AbstractHealthIndicator {
    private final WiiProperties wiiProperties;
    private final RestTemplate restTemplate;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        try {
            URI uri = this.wiiProperties.getHealthCheckUrl();
            if (uri == null) {
                builder.up();
                return;
            }

            ResponseEntity<Map<String, Object>> exchange = this.restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    }
            );

            Map<String, Object> map = exchange.getBody();

            if (map == null) {
                this.getWarning(builder);
                return;
            }
            Object status = map.get("status");
            if (status instanceof String) {
                builder.status(status.toString());
            } else {
                this.getWarning(builder);
            }
        } catch (Exception e) {
            builder.down().withDetail("error", e.getMessage());
        }
    }

    private void getWarning(Health.Builder builder) {
        builder.unknown().withDetail("warning", "no status field in response");
    }
}
