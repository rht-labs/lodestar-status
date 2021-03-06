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

}