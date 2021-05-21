package com.redhat.labs.lodestar.model;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Check {

    private String name;
    private String status;
    private String version;
    @Builder.Default
    private Map<String, Object> data = new HashMap<>();

}
