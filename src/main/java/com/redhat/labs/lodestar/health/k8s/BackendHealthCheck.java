package com.redhat.labs.lodestar.health.k8s;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class BackendHealthCheck extends K8sHealthCheck {

    @ConfigProperty(name = "backend.component.name", defaultValue = "omp-backend")
    String name;

    @Override
    public String getName() {
        return name;
    }

}
