package com.redhat.labs.lodestar.health;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.redhat.labs.lodestar.model.Health;
import com.redhat.labs.lodestar.rest.client.BackendClient;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

//@QuarkusTest
public class BackendReadinessCheckTest {

//    @Inject
//    @Readiness
//    BackendReadinessCheck check;
//
//    @InjectMock
//    @RestClient
//    BackendClient client;
//
//    @Test
//    void testBackendReadinessCheckUp() {
//
//        // given
//        Mockito.when(client.getBackendStatus()).thenReturn(Health.builder().status("UP").build());
//
//        // when
//        HealthCheckResponse response = check.call();
//
//        // then
//        assertNotNull(response);
//        assertEquals("BACKEND", response.getName());
//        assertNotNull(response.getState());
//        assertEquals("UP", response.getState().name());
//
//    }
//
//    @Test
//    void testBackendReadinessCheckDown() {
//
//        // given
//        Mockito.when(client.getBackendStatus()).thenReturn(Health.builder().status("DOWN").build());
//
//        // when
//        HealthCheckResponse response = check.call();
//
//        // then
//        assertNotNull(response);
//        assertEquals("BACKEND", response.getName());
//        assertNotNull(response.getState());
//        assertEquals("DOWN", response.getState().name());
//
//    }
//
//    @Test
//    void testBackendReadinessCheckWebApplicationException() {
//
//        // given
//        Mockito.when(client.getBackendStatus()).thenThrow(new WebApplicationException(500));
//
//        // when
//        HealthCheckResponse response = check.call();
//
//        // then
//        assertNotNull(response);
//        assertEquals("BACKEND", response.getName());
//        assertNotNull(response.getState());
//        assertEquals("DOWN", response.getState().name());
//
//    }
//
//    @Test
//    void testBackendReadinessCheckWebApplicationRuntimeException() {
//
//        // given
//        Mockito.when(client.getBackendStatus()).thenThrow(new RuntimeException("uh oh"));
//
//        // when
//        HealthCheckResponse response = check.call();
//
//        // then
//        assertNotNull(response);
//        assertEquals("BACKEND", response.getName());
//        assertNotNull(response.getState());
//        assertEquals("DOWN", response.getState().name());
//
//    }

}
