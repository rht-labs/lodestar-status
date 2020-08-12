package com.redhat.labs.lodestar.service;

import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.fabric8.kubernetes.api.model.ContainerStatus;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestResource {

    @Inject
    KubernetesService service;
    
    @GET
    @PermitAll
    public Map<String, ContainerStatus> getContainerStatuses() {
        return service.getContainerStatusMap();
    }

}
