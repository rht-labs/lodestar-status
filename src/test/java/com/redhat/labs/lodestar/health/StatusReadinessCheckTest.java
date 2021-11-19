package com.redhat.labs.lodestar.health;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.redhat.labs.lodestar.config.VersionManifestConfig;
import com.redhat.labs.lodestar.model.version.Version;
import com.redhat.labs.lodestar.model.version.VersionManifest;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class StatusReadinessCheckTest {

    @Inject
    @Readiness
    StatusReadinessCheck check;

    @InjectMock
    VersionManifestConfig config;

    @Test
    void testStatusReadinessCheckDownVersionManifestNull() {

        // given
        Mockito.when(config.getVersionData()).thenReturn(null);

        // when
        HealthCheckResponse response = check.call();

        // then
        assertNotNull(response);
        assertEquals("STATUS READINESS", response.getName());
        assertNotNull(response.getStatus());
        assertEquals("DOWN", response.getStatus().name());

    }

    @Test
    void testStatusReadinessCheckDownMainVersionMissing() {

        // given
        Mockito.when(config.getVersionData()).thenReturn(new VersionManifest());

        // when
        HealthCheckResponse response = check.call();

        // then
        assertNotNull(response);
        assertEquals("STATUS READINESS", response.getName());
        assertNotNull(response.getStatus());
        assertEquals("DOWN", response.getStatus().name());

    }

    @Test
    void testStatusReadinessCheckDownComponentVersionEmpty() {

        // given
        VersionManifest vm = VersionManifest.builder().mainVersionKey("lodestar")
                .applications(List.of(Version.builder().application("lodestar").build()))
                .build();
        Mockito.when(config.getVersionData()).thenReturn(vm);

        // when
        HealthCheckResponse response = check.call();

        // then
        assertNotNull(response);
        assertEquals("STATUS READINESS", response.getName());
        assertNotNull(response.getStatus());
        assertEquals("DOWN", response.getStatus().name());

    }

    @Test
    void testStatusReadinessCheckUp() {

        // given
        VersionManifest vm = VersionManifest.builder().mainVersionKey("lodestar")
                .applications(Arrays.asList(Version.builder().application("lodestar").build(), Version.builder().application("another").build()))
                .build();
        Mockito.when(config.getVersionData()).thenReturn(vm);

        // when
        HealthCheckResponse response = check.call();

        // then
        assertNotNull(response);
        assertEquals("STATUS READINESS", response.getName());
        assertNotNull(response.getStatus());
        assertEquals("UP", response.getStatus().name());

    }

}
