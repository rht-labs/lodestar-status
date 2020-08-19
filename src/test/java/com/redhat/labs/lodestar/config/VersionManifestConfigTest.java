package com.redhat.labs.lodestar.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.redhat.labs.lodestar.model.version.VersionManifest;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class VersionManifestConfigTest {

    @Inject
    VersionManifestConfig vmc;

    @Test
    void testYaml() throws JsonProcessingException {

        VersionManifest vm = vmc.getVersionData();

        assertEquals(2, vm.getApplications().size());
       
        assertNotNull(vm.getMainVersion());
        assertEquals("ball", vm.getMainVersion().getApplication());
        assertEquals("v2.0", vm.getMainVersion().getVersion());
        assertNotNull(vm.getComponentVersions());
        assertEquals(1, vm.getComponentVersions().size());
        assertEquals("launcher", vm.getComponentVersions().get(0).getApplication());
        assertEquals("v1.1", vm.getComponentVersions().get(0).getVersion());

    }

}
