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
public class HealthCheck {

    private String name;
    private String status;
    @Builder.Default
    private Map<String, String> data = new HashMap<String, String>();

}
