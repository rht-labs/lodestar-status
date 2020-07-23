package com.redhat.labs.lodestar.serdes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;
import javax.json.bind.Jsonb;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.redhat.labs.lodestar.model.version.VersionManifest;
import com.redhat.labs.lodestar.utils.ResourceLoader;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class VersionManifestSerdesTest {

    @ConfigProperty(name = "main.version.key")
    String mainVersionKey;

    @Inject
    Jsonb jsonb;

    @Test
    public void testVersionManifestDeserizationFromYaml() throws JsonMappingException, JsonProcessingException {

        // given
        String yamlInput = ResourceLoader.load("data/version/version-manifest.yaml");
        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        // when
        VersionManifest vm = om.readValue(yamlInput, VersionManifest.class);

        // then
        assertNotNull(vm);
        assertEquals(2, vm.getApplications().size());
        assertEquals("ball", vm.getApplications().get(0).getApplication());
        assertEquals("v2.0", vm.getApplications().get(0).getVersion());
        assertEquals("launcher", vm.getApplications().get(1).getApplication());
        assertEquals("v1.1", vm.getApplications().get(1).getVersion());

    }

    @Test
    public void testVersionManifestDeserializationToJson() throws JsonMappingException, JsonProcessingException {

        // given
        String yamlInput = ResourceLoader.load("data/version/version-manifest.yaml");
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        VersionManifest vm = om.readValue(yamlInput, VersionManifest.class);
        vm.setMainVersionKey(mainVersionKey);

        // when
        String actual = jsonb.toJson(vm);

        // then
        String expected = ResourceLoader.load("data/version/get-version-manifest.json");
        assertEquals(expected, actual);

    }

}
