package com.redhat.labs.lodestar.health;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.labs.lodestar.model.HealthStatus;
import com.redhat.labs.lodestar.rest.client.GitApiClient;

@Readiness
@ApplicationScoped
public class GitApiReadinessCheck implements HealthCheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitApiReadinessCheck.class);

    private static final String GIT_API = "GIT API";

    @Inject
    @RestClient
    GitApiClient client;

    @Override
    public HealthCheckResponse call() {

        try {

            HealthStatus status = client.getGitApiStatus();
            LOGGER.debug("git api readiness health response {}", status);
            return status.getStatus().equals("UP") ? HealthCheckResponse.up(GIT_API)
                    : HealthCheckResponse.down(GIT_API);

        } catch (Exception e) {

            LOGGER.debug("exception thrown trying to call git api readiness check. {}", e.getMessage());
            return HealthCheckResponse.down(GIT_API);

        }

    }

}
