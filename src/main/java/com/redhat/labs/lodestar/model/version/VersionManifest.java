package com.redhat.labs.lodestar.model.version;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VersionManifest {

    @JsonbTransient
    String mainVersionKey;

    @JsonbTransient
    @Builder.Default
    private List<Version> applications = new ArrayList<>();

    @JsonbProperty("main_version")
    public Version getMainVersion() {
        Optional<Version> optional = applications.stream()
                .filter(a -> a.getApplication().equalsIgnoreCase(mainVersionKey)).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }

    @JsonbProperty("component_versions")
    public List<Version> getComponentVersions() {
        return applications.stream().filter(a -> !a.getApplication().equalsIgnoreCase(mainVersionKey))
                .collect(Collectors.toList());
    }

}
