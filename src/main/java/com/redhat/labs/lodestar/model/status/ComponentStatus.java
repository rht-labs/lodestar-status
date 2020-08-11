package com.redhat.labs.lodestar.model.status;

import com.redhat.labs.lodestar.model.status.response.HealthStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentStatus {

    private String componentName;
    @Builder.Default
    private boolean operational = false;

    public static ComponentStatus fromHealthStatus(String componentName, HealthStatus status) {
        return ComponentStatus.builder().componentName(componentName).operational("UP".equals(status.getStatus()))
                .build();
    }

}
