package com.redhat.labs.lodestar.health.k8s;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class StatusHealthCheck extends K8sHealthCheck {

    @ConfigProperty(name = "status.component.name", defaultValue = "lodestar-status")
    String name;

    @Override
    public String getName() {
        return name;
    }
    
}
