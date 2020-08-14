package com.redhat.labs.lodestar.health;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.redhat.labs.lodestar.rest.client.FrontendClient;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

//@QuarkusTest
public class FrontendReadinessCheckTest {

//    @Inject
//    @Readiness
//    FrontendReadinessCheck check;
//
//    @InjectMock
//    @RestClient
//    FrontendClient client;
//
//    @Test
//    void testFrontendReadinessCheckUp() {
//
//        // given
//        Mockito.when(client.getFrontendStatus()).thenReturn(Response.ok().build());
//
//        // when
//        HealthCheckResponse response = check.call();
//
//        // then
//        assertNotNull(response);
//        assertEquals("FRONTEND", response.getName());
//        assertNotNull(response.getState());
//        assertEquals("UP", response.getState().name());
//
//    }
//
//    @Test
//    void testFrontendReadinessCheckDown() {
//
//        // given
//        Mockito.when(client.getFrontendStatus()).thenReturn(Response.serverError().build());
//
//        // when
//        HealthCheckResponse response = check.call();
//
//        // then
//        assertNotNull(response);
//        assertEquals("FRONTEND", response.getName());
//        assertNotNull(response.getState());
//        assertEquals("DOWN", response.getState().name());
//
//    }
//
//    @Test
//    void testFrontendReadinessCheckWebApplicationException() {
//
//        // given
//        Mockito.when(client.getFrontendStatus()).thenThrow(new WebApplicationException(500));
//
//        // when
//        HealthCheckResponse response = check.call();
//
//        // then
//        assertNotNull(response);
//        assertEquals("FRONTEND", response.getName());
//        assertNotNull(response.getState());
//        assertEquals("DOWN", response.getState().name());
//
//    }
//
//    @Test
//    void testFrontendReadinessCheckRuntimeException() {
//
//        // given
//        Mockito.when(client.getFrontendStatus()).thenThrow(new RuntimeException("uh oh"));
//
//        // when
//        HealthCheckResponse response = check.call();
//
//        // then
//        assertNotNull(response);
//        assertEquals("FRONTEND", response.getName());
//        assertNotNull(response.getState());
//        assertEquals("DOWN", response.getState().name());
//
//    }

}
