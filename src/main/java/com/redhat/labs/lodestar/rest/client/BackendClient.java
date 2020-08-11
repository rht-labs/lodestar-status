package com.redhat.labs.lodestar.rest.client;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.redhat.labs.lodestar.model.status.response.HealthStatus;

@ApplicationScoped
@RegisterRestClient(configKey = "lodestar.backend.api")
public interface BackendClient {

    @GET
    @Path("/health")
    HealthStatus getBackendStatus();

}
