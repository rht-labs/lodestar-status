package com.redhat.labs.lodestar.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.labs.lodestar.k8s.client.KubernetesApiClient;
import com.redhat.labs.lodestar.model.Check;
import com.redhat.labs.lodestar.model.Check.CheckBuilder;
import com.redhat.labs.lodestar.model.Health;

import io.fabric8.kubernetes.api.model.ReplicationControllerStatus;

@ApplicationScoped
public class ComponentHealthService {

    private final static String UP = "UP";
    private final static String DOWN = "DOWN";
    private static final String CURRENT_REPLICAS = "Current Replicas";
    private static final String DESIRED_REPLICAS = "Desired Replicas";

    @Inject
    KubernetesApiClient client;

    public Health getComponentHealth() {

        // get checks
        List<Check> checks = getChecks();

        // find overall status
        String status = getOverallStatus(checks);

        return Health.builder().status(status).checks(checks).build();

    }

    private List<Check> getChecks() {

        Map<String, ReplicationControllerStatus> statusMap = client.getComponentStatusMap();

        // get checks
        return

            statusMap.keySet()
                .stream()
                .map(key -> {

                    ReplicationControllerStatus rcStatus = statusMap.get(key);

                    // builder with component name
                    CheckBuilder builder = Check.builder().name(key);

                    // determine if at least one replica in ready state
                    String status = rcStatus.getReadyReplicas() > 0 ? UP : DOWN;
                    builder = builder.name(status);

                    // add replica data
                    builder.data(Map.of(
                            CURRENT_REPLICAS,
                            rcStatus.getReadyReplicas(),
                            DESIRED_REPLICAS,
                            rcStatus.getReplicas()
                            ));

                    return builder.build();

                })
                .collect(Collectors.toList());

    }

    private String getOverallStatus(List<Check> checks) {

        for(Check check : checks) {
            if(DOWN.equals(check.getStatus())) {
                return DOWN;
            }
        }

        return UP;

    }

}