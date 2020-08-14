package com.redhat.labs.lodestar.health;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;

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

//@QuarkusTest
public class StatusReadinessCheckTest {

//    @Inject
//    @Readiness
//    StatusReadinessCheck check;
//
//    @InjectMock
//    VersionManifestConfig config;
//
//    @Test
//    void testStatusReadinessCheckDownVersionManifestNull() {
//
//        // given
//        Mockito.when(config.getVersionData()).thenReturn(null);
//
//        // when
//        HealthCheckResponse response = check.call();
//
//        // then
//        assertNotNull(response);
//        assertEquals("STATUS", response.getName());
//        assertNotNull(response.getState());
//        assertEquals("DOWN", response.getState().name());
//
//    }
//
//    @Test
//    void testStatusReadinessCheckDownMainVersionMissing() {
//
//        // given
//        Mockito.when(config.getVersionData()).thenReturn(new VersionManifest());
//
//        // when
//        HealthCheckResponse response = check.call();
//
//        // then
//        assertNotNull(response);
//        assertEquals("STATUS", response.getName());
//        assertNotNull(response.getState());
//        assertEquals("DOWN", response.getState().name());
//
//    }
//
//    @Test
//    void testStatusReadinessCheckDownComponentVersionEmpty() {
//
//        // given
//        VersionManifest vm = new VersionManifest("lodestar",
//                Arrays.asList(Version.builder().application("lodestar").build()));
//        Mockito.when(config.getVersionData()).thenReturn(vm);
//
//        // when
//        HealthCheckResponse response = check.call();
//
//        // then
//        assertNotNull(response);
//        assertEquals("STATUS", response.getName());
//        assertNotNull(response.getState());
//        assertEquals("DOWN", response.getState().name());
//
//    }
//
//    @Test
//    void testStatusReadinessCheckUp() {
//
//        // given
//        VersionManifest vm = new VersionManifest("lodestar", Arrays.asList(
//                Version.builder().application("lodestar").build(), Version.builder().application("another").build()));
//        Mockito.when(config.getVersionData()).thenReturn(vm);
//
//        // when
//        HealthCheckResponse response = check.call();
//
//        // then
//        assertNotNull(response);
//        assertEquals("STATUS", response.getName());
//        assertNotNull(response.getState());
//        assertEquals("UP", response.getState().name());
//
//    }

}
