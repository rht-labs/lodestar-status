package com.redhat.labs.lodestar.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.redhat.labs.lodestar.model.version.VersionManifest;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class VersionManifestConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionManifestConfig.class);

    @ConfigProperty(name = "version.yml")
    String versionJsonFile;

    @ConfigProperty(name = "main.version.key")
    String mainVersionKey;

    private VersionManifest versionData = new VersionManifest();
    private long lastModifiedTime;

    void onStart(@Observes StartupEvent event) {
        loadVersionData();
    }

    @Scheduled(every = "10s")
    void loadVersionData() {

        Path path = Paths.get(versionJsonFile);

        if (Files.isReadable(path)) {
            if (isModified(path)) {
                LOGGER.debug("Loading versions from {}", versionJsonFile);
                try {
                    String fileContents = new String(Files.readAllBytes(path));

                    ObjectMapper om = new ObjectMapper(new YAMLFactory());
                    versionData = om.readValue(fileContents, VersionManifest.class);
                    versionData.setMainVersionKey(mainVersionKey);
                    LOGGER.debug(versionData.toString());
                } catch (IOException e) {
                    LOGGER.error(String.format("Found but unable to read file %s", versionJsonFile), e);
                }
            }
        } else {
            LOGGER.warn("Unable to locate version manifest file at {}. ok in dev mode.", versionJsonFile);
        }
    }

    /**
     * Returns the current state from {@link VersionManifest}
     * 
     * @return
     */
    public VersionManifest getVersionData() {
        return versionData;
    }

    private boolean isModified(Path file) {
        LOGGER.trace("Checking mod for version manifest config");
        FileTime fileTime;
        try {
            fileTime = Files.getLastModifiedTime(file);
            if (fileTime.toMillis() > lastModifiedTime) {
                LOGGER.info("New version data detected for {}", versionJsonFile);
                lastModifiedTime = fileTime.toMillis();
                return true;
            }
        } catch (IOException e) {
            LOGGER.error("Unable to locate read file timestamp {}", versionJsonFile);
        }

        return false;
    }
}
