package com.redhat.labs.lodestar.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import com.redhat.labs.lodestar.utils.ResourceLoader;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class VersionResourceTest {

    @Test
    public void testGetVersionManifest() {

        String expected = ResourceLoader.load("data/version/get-version-manifest.json");

        given()
          .when().get("/api/v1/version/manifest")
          .then()
             .statusCode(200)
             .body(is(expected));
    }

    @Test
    public void testGetVersionComponentManifest() {

        String expected = ResourceLoader.load("data/version/get-version-manifest.json");

        given()
                .when().get("/api/v1/version/manifest/launcher")
                .then()
                .statusCode(200)
                .body("name", equalTo("launcher"))
                .body("value", equalTo("v1.1"));
    }

    @Test
    public void testGetVersionComponentManifestEmpty() {

        String expected = ResourceLoader.load("data/version/get-version-manifest.json");

        given()
                .when().get("/api/v1/version/manifest/nada")
                .then()
                .statusCode(200)
                .body("name", equalTo(null))
                .body("value", equalTo(null));
    }

}