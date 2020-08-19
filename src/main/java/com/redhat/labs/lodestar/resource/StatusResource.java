package com.redhat.labs.lodestar.resource;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import com.redhat.labs.lodestar.model.Health;
import com.redhat.labs.lodestar.service.ComponentHealthService;

@RequestScoped
@Path("/api/v1/status")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatusResource {

    @Inject
    ComponentHealthService service;

    @GET
    @PermitAll
    @Timed(name = "statusResourceTimer")
    @Counted(name = "statusResourceCounter")
    public Health getComponentHealth() {
        return service.getHealth();
    }

}
