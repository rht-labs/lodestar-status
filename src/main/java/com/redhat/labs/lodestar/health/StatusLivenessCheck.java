package com.redhat.labs.lodestar.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

public class StatusLivenessCheck implements HealthCheck {

    private static final String STATUS_LIVENESS = "STATUS LIVENESS";

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up(STATUS_LIVENESS);
    }

}
