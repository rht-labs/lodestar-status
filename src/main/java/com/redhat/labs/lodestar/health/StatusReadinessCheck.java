package com.redhat.labs.lodestar.health;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.labs.lodestar.config.VersionManifestConfig;
import com.redhat.labs.lodestar.model.version.Version;
import com.redhat.labs.lodestar.model.version.VersionManifest;

public class StatusReadinessCheck implements HealthCheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusReadinessCheck.class);

    private static final String STATUS_READINESS = "STATUS";

    @Inject
    VersionManifestConfig vmConfig;

    @Override
    public HealthCheckResponse call() {

        VersionManifest vm = vmConfig.getVersionData();

        if (null == vm) {
            LOGGER.debug("application misconfigured.  Version Manifest is not found.");
            return HealthCheckResponse.down(STATUS_READINESS);
        }

        Version mainVersion = vm.getMainVersion();
        List<Version> componentVersions = vm.getComponentVersions();

        if (null == mainVersion || componentVersions.size() == 0) {
            LOGGER.debug("status misconfigured. \n\tmain version: {}\n\tcomponentVersions {}", mainVersion,
                    componentVersions);
            return HealthCheckResponse.down(STATUS_READINESS);
        }

        return HealthCheckResponse.up(STATUS_READINESS);

    }

}
