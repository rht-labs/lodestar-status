package com.redhat.labs.lodestar.rest.client;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@RegisterRestClient(configKey = "lodestar.frontend.api")
public interface FrontendClient {

    @GET
    @Path("/")
    Response getFrontendStatus();

}
