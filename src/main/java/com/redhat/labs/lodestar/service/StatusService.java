package com.redhat.labs.lodestar.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.labs.lodestar.model.status.ComponentStatus;
import com.redhat.labs.lodestar.model.status.response.HealthStatus;
import com.redhat.labs.lodestar.rest.client.BackendClient;
import com.redhat.labs.lodestar.rest.client.FrontendClient;
import com.redhat.labs.lodestar.rest.client.GitApiClient;

@ApplicationScoped
public class StatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusService.class);

    private static final String BACKEND = "BACKEND";
    private static final String GIT_API = "GIT_API";
    private static final String FE = "FE";

    private static final List<String> COMPONENTS = Arrays.asList(BACKEND, GIT_API, FE);

    @Inject
    @RestClient
    BackendClient backendClient;

    @Inject
    @RestClient
    GitApiClient gitApiClient;

    @Inject
    @RestClient
    FrontendClient frontendClient;

    public List<ComponentStatus> getComponentStatus() {

        return COMPONENTS.parallelStream().map(component -> {
            Optional<ComponentStatus> optional = getComponentStatus(component);
            return optional.isPresent() ? optional.get()
                    : ComponentStatus.builder().componentName(component).operational(false).build();
        }).collect(Collectors.toList());

    }

    private Optional<ComponentStatus> getComponentStatus(String component) {

        try {

            if (BACKEND.equals(component)) {

                // quarkus app, returns health status type
                HealthStatus status = backendClient.getBackendStatus();
                return Optional.ofNullable(ComponentStatus.fromHealthStatus(BACKEND, status));

            } else if (GIT_API.equals(component)) {

                // quarkus app, returns health status type
                HealthStatus status = gitApiClient.getGitApiStatus();
                return Optional.ofNullable(ComponentStatus.fromHealthStatus(GIT_API, status));

            } else if (FE.equals(component)) {

                // front end, no status endpoint, using a 200 as operational
                Response response = frontendClient.getFrontendStatus();
                boolean operational = response.getStatus() == 200;
                response.close();

                return Optional
                        .ofNullable(ComponentStatus.builder().componentName(FE).operational(operational).build());

            } else {
                return Optional.empty();
            }

        } catch (Exception e) {

            LOGGER.warn("call to component {} endpoint failed with message {}", component, e.getMessage());
            // rest call failed, assuming service is not responding
            return Optional.empty();

        }

    }

}
