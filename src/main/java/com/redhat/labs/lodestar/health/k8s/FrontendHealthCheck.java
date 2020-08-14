package com.redhat.labs.lodestar.health.k8s;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class FrontendHealthCheck extends K8sHealthCheck {

    @ConfigProperty(name = "frontend.component.name", defaultValue = "omp-frontend")
    String name;

    @Override
    public String getName() {
        return name;
    }

}
