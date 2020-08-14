package com.redhat.labs.lodestar.health;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;

import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.redhat.labs.lodestar.model.Health;
import com.redhat.labs.lodestar.rest.client.BackendClient;
import com.redhat.labs.lodestar.rest.client.FrontendClient;
import com.redhat.labs.lodestar.rest.client.GitApiClient;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;


//@QuarkusTest
public class HealthResourceTest {

//    @InjectMock
//    @RestClient
//    BackendClient backendClient;
//
//    @InjectMock
//    @RestClient
//    FrontendClient frontendClient;
//
//    @InjectMock
//    @RestClient
//    GitApiClient gitApiClient;
//
//    @Test
//    void testStatusHealthUp() {
//
//        // given
//        Mockito.when(backendClient.getBackendStatus()).thenReturn(Health.builder().status("UP").build());
//        Mockito.when(frontendClient.getFrontendStatus()).thenReturn(Response.ok().build());
//        Mockito.when(gitApiClient.getGitApiStatus()).thenReturn(Health.builder().status("UP").build());
//        
//
//        given()
//            .get("/health/ready")
//        .then()
//            .statusCode(200)
//            .body("status", is("UP"))
//            .body("checks", 
//                    hasItem(
//                            allOf(
//                                    Matchers.hasEntry("name", "STATUS"),
//                                    Matchers.hasEntry("status", "UP"))))
//            .body("checks", 
//                    hasItem(
//                            allOf(
//                                    Matchers.hasEntry("name", "BACKEND"),
//                                    Matchers.hasEntry("status", "UP"))))
//            .body("checks", 
//                    hasItem(
//                            allOf(
//                                    Matchers.hasEntry("name", "FRONTEND"),
//                                    Matchers.hasEntry("status", "UP"))))
//            .body("checks", 
//                    hasItem(
//                            allOf(
//                                    Matchers.hasEntry("name", "GIT API"),
//                                    Matchers.hasEntry("status", "UP"))));
//
//    }
//
//    @Test
//    void testStatusHealthDown() {
//
//        // given
//        Mockito.when(backendClient.getBackendStatus()).thenReturn(Health.builder().status("DOWN").build());
//        Mockito.when(frontendClient.getFrontendStatus()).thenReturn(Response.ok().build());
//        Mockito.when(gitApiClient.getGitApiStatus()).thenReturn(Health.builder().status("UP").build());
//        
//
//        given()
//            .get("/health/ready")
//        .then()
//            .statusCode(503)
//            .body("status", is("DOWN"))
//            .body("checks", 
//                    hasItem(
//                            allOf(
//                                    Matchers.hasEntry("name", "STATUS"),
//                                    Matchers.hasEntry("status", "UP"))))
//            .body("checks", 
//                    hasItem(
//                            allOf(
//                                    Matchers.hasEntry("name", "BACKEND"),
//                                    Matchers.hasEntry("status", "DOWN"))))
//            .body("checks", 
//                    hasItem(
//                            allOf(
//                                    Matchers.hasEntry("name", "FRONTEND"),
//                                    Matchers.hasEntry("status", "UP"))))
//            .body("checks", 
//                    hasItem(
//                            allOf(
//                                    Matchers.hasEntry("name", "GIT API"),
//                                    Matchers.hasEntry("status", "UP"))));
//
//    }

}
