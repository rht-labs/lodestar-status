package com.redhat.labs.lodestar.serdes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;
import javax.json.bind.Jsonb;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.redhat.labs.lodestar.model.version.Version;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class VersionSerdesTest {

    @Inject
    Jsonb jsonb;

    @Test
    public void testVersionSerialization() {

        // given
        String expected = "\n{\n" + "    \"name\": \"somename\",\n" + "    \"value\": \"v1\"\n" + "}";

        Version version = Version.builder().application("somename").version("v1").build();

        // when
        String actual = jsonb.toJson(version);

        // then
        assertEquals(expected, actual);

    }

    @Test
    public void testVersionDeserializationYaml() throws JsonMappingException, JsonProcessingException {

        // given
        String yamlInput = "---\napplication: somename\nversion: v2";
        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        // when
        Version version = om.readValue(yamlInput, Version.class);

        // then
        assertEquals("somename", version.getApplication());
        assertEquals("v2", version.getVersion());

    }

    @Test
    public void testVersionDeserializationJson() {

        // given
        String json = "{\"application\":\"somename\", \"version\":\"v3\"}";

        // when
        Version version = jsonb.fromJson(json, Version.class);

        // then
        assertEquals("somename", version.getApplication());
        assertEquals("v3", version.getVersion());

    }

}
