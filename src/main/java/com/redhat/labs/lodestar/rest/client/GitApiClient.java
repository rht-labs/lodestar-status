package com.redhat.labs.lodestar.rest.client;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.redhat.labs.lodestar.model.Health;

@ApplicationScoped
@RegisterRestClient(configKey = "lodestar.git.api")
public interface GitApiClient {

    @GET
    @Path("/health")
    Health getGitApiStatus();

}
