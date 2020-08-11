package com.redhat.labs.lodestar.resource;

import java.util.List;

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

import com.redhat.labs.lodestar.model.status.ComponentStatus;
import com.redhat.labs.lodestar.service.StatusService;

@RequestScoped
@Path("/api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatusResource {

    @Inject
    StatusService statusService;

    @GET
    @PermitAll
    @Path("/status")
    @Timed(name = "versionStatusResourceTimer")
    @Counted(name = "versionStatusResourceCounter")
    public List<ComponentStatus> getStatusOfComponents() {
        return statusService.getComponentStatus();
    }

}
