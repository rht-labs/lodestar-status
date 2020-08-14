package com.redhat.labs.lodestar.health;

import javax.inject.Inject;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.labs.lodestar.model.Health;
import com.redhat.labs.lodestar.rest.client.BackendClient;

public class BackendReadinessCheck implements HealthCheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackendReadinessCheck.class);

    private static final String BACKEND = "BACKEND";

    @Inject
    @RestClient
    BackendClient client;

    @Override
    public HealthCheckResponse call() {

        try {

            Health status = client.getBackendStatus();
            LOGGER.debug("backend readiness health response {}", status);
            return status.getStatus().equals("UP") ? HealthCheckResponse.up(BACKEND)
                    : HealthCheckResponse.down(BACKEND);

        } catch (Exception e) {

            LOGGER.debug("exception thrown trying to call backend readiness check. {}", e.getMessage());
            return HealthCheckResponse.down(BACKEND);

        }

    }

}
