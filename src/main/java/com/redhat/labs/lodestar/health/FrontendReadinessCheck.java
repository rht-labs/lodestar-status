package com.redhat.labs.lodestar.health;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.labs.lodestar.rest.client.FrontendClient;

@Readiness
@ApplicationScoped
public class FrontendReadinessCheck implements HealthCheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontendReadinessCheck.class);

    private static final String FE = "FRONTEND";

    @Inject
    @RestClient
    FrontendClient client;

    @Override
    public HealthCheckResponse call() {

        try {

            // front end, no status endpoint, using a 200 as operational
            Response response = client.getFrontendStatus();
            int statusCode = response.getStatus();
            response.close();

            LOGGER.debug("frontend response code {}", statusCode);
            return statusCode == 200 ? HealthCheckResponse.up(FE) : HealthCheckResponse.down(FE);

        } catch (Exception e) {

            LOGGER.debug("exception thrown trying to call frontend for check. {}", e.getMessage());
            return HealthCheckResponse.down(FE);

        }

    }

}
