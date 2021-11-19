package com.redhat.labs.lodestar.resource;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import com.redhat.labs.lodestar.config.VersionManifestConfig;
import com.redhat.labs.lodestar.model.version.VersionManifest;

@RequestScoped
@Path("/api/v1/version")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VersionResource {

    @Inject
    VersionManifestConfig versionManifestConfig;

    @GET
    @PermitAll
    @Path("/manifest")
    @Timed(name = "versionManifestResourceTimer")
    @Counted(name = "versionManifestResourceCounter")
    public VersionManifest getVersionManifest() {
        return versionManifestConfig.getVersionData();
    }

    @GET
    @PermitAll
    @Path("/manifest/{component}")
    @Timed(name = "versionManifestComponentResourceTimer")
    @Counted(name = "versionManifestComponentResourceCounter")
    public Response getVersionManifestComponent(@PathParam("component") String component) {
        VersionManifest vm = versionManifestConfig.getVersionData();

        return Response.ok(vm.getVersion(component)).build();
    }

}