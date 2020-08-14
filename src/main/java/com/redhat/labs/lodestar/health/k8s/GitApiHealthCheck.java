package com.redhat.labs.lodestar.health.k8s;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class GitApiHealthCheck extends K8sHealthCheck {

    @ConfigProperty(name = "git.api.component.name", defaultValue = "omp-git-api")
    String name;

    @Override
    public String getName() {
        return name;
    }

}
