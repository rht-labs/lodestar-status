package com.redhat.labs.lodestar.model.version;

import javax.json.bind.annotation.JsonbProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Version {

    private String application;
    private String version;

    @JsonbProperty("name")
    public String getApplication() {
        return application;
    }

    @JsonbProperty("value")
    public String getVersion() {
        return version;
    }

}
