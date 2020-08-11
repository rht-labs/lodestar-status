package com.redhat.labs.lodestar.model.status.response;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthStatus {

    private String status;
    @Builder.Default
    private List<HealthCheck> checks = new ArrayList();;

}
