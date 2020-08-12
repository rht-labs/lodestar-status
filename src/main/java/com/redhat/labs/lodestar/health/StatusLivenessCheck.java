package com.redhat.labs.lodestar.health;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class StatusLivenessCheck implements HealthCheck {

    private static final String STATUS_LIVENESS = "STATUS LIVENESS";

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up(STATUS_LIVENESS);
    }

}
