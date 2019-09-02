package com.itmuch.wii.consul;

import com.ecwid.consul.v1.agent.model.NewService;
import com.itmuch.wii.WiiProperties;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulManagementRegistrationCustomizer;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistrationCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * @author www.itmuch.com
 */
public class WiiConsulAutoRegistration extends ConsulAutoRegistration {
    public WiiConsulAutoRegistration(NewService service, AutoServiceRegistrationProperties autoServiceRegistrationProperties, ConsulDiscoveryProperties properties, ApplicationContext context, HeartbeatProperties heartbeatProperties, List<ConsulManagementRegistrationCustomizer> managementRegistrationCustomizers) {
        super(service, autoServiceRegistrationProperties, properties, context, heartbeatProperties, managementRegistrationCustomizers);
    }

    public static ConsulAutoRegistration registration(
            AutoServiceRegistrationProperties autoServiceRegistrationProperties,
            ConsulDiscoveryProperties properties, ApplicationContext context,
            List<ConsulRegistrationCustomizer> registrationCustomizers,
            List<ConsulManagementRegistrationCustomizer> managementRegistrationCustomizers,
            HeartbeatProperties heartbeatProperties,
            WiiProperties wiiProperties) {

        NewService service = new NewService();
        String appName = getAppName(properties, context.getEnvironment());
        service.setId(getInstanceId(wiiProperties, context.getEnvironment()));
        if (!properties.isPreferAgentAddress()) {
            service.setAddress(wiiProperties.getIp());
        }
        service.setName(normalizeForDns(appName));
        service.setTags(createTags(properties));

        // set health check, use wii self's port rather than polyglot app's port.
        service.setPort(Integer.valueOf(context.getEnvironment().getProperty("server.port")));
        setCheck(service, autoServiceRegistrationProperties, properties, context,
                heartbeatProperties);

        service.setPort(wiiProperties.getPort());

        ConsulAutoRegistration registration = new ConsulAutoRegistration(service,
                autoServiceRegistrationProperties, properties, context,
                heartbeatProperties, managementRegistrationCustomizers);
        customize(registrationCustomizers, registration);
        return registration;
    }

    public static String getInstanceId(WiiProperties wiiProperties,
                                       Environment environment) {
        return String.format("%s-%s-%s",
                environment.getProperty("spring.application.name"),
                wiiProperties.getIp(),
                wiiProperties.getPort());
    }
}
