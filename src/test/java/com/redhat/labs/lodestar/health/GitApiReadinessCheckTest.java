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

import com.redhat.labs.lodestar.model.HealthStatus;
import com.redhat.labs.lodestar.rest.client.GitApiClient;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class GitApiReadinessCheckTest {
    
    @Inject
    @Readiness
    GitApiReadinessCheck check;

    @InjectMock
    @RestClient
    GitApiClient client;

    @Test
    void testGitApiReadinessCheckUp() {

        // given
        Mockito.when(client.getGitApiStatus()).thenReturn(HealthStatus.builder().status("UP").build());

        // when
        HealthCheckResponse response = check.call();

        // then
        assertNotNull(response);
        assertEquals("GIT API", response.getName());
        assertNotNull(response.getState());
        assertEquals("UP", response.getState().name());

    }

    @Test
    void testGitApiReadinessCheckDown() {

        // given
        Mockito.when(client.getGitApiStatus()).thenReturn(HealthStatus.builder().status("DOWN").build());

        // when
        HealthCheckResponse response = check.call();

        // then
        assertNotNull(response);
        assertEquals("GIT API", response.getName());
        assertNotNull(response.getState());
        assertEquals("DOWN", response.getState().name());

    }

    @Test
    void testGitApiReadinessCheckWebApplicationException() {

        // given
        Mockito.when(client.getGitApiStatus()).thenThrow(new WebApplicationException(500));

        // when
        HealthCheckResponse response = check.call();

        // then
        assertNotNull(response);
        assertEquals("GIT API", response.getName());
        assertNotNull(response.getState());
        assertEquals("DOWN", response.getState().name());

    }

    @Test
    void testGitApiReadinessCheckWebApplicationRuntimeException() {

        // given
        Mockito.when(client.getGitApiStatus()).thenThrow(new RuntimeException("uh oh"));

        // when
        HealthCheckResponse response = check.call();

        // then
        assertNotNull(response);
        assertEquals("GIT API", response.getName());
        assertNotNull(response.getState());
        assertEquals("DOWN", response.getState().name());

    }

}
