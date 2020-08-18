package com.redhat.labs.lodestar.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.labs.lodestar.k8s.client.KubernetesApiClient;
import com.redhat.labs.lodestar.model.Check;
import com.redhat.labs.lodestar.model.Check.CheckBuilder;
import com.redhat.labs.lodestar.model.Health;

import io.fabric8.kubernetes.api.model.ReplicationControllerStatus;
import lombok.Setter;

@ApplicationScoped
public class ComponentHealthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentHealthService.class);
    
    private final static String UP = "UP";
    private final static String DOWN = "DOWN";
    private static final String CURRENT_REPLICAS = "Current Replicas";
    private static final String DESIRED_REPLICAS = "Desired Replicas";

    @Setter
    @ConfigProperty(name = "component.replica.threshold", defaultValue = "1")
    Integer replicaThreshold;

    @Inject
    KubernetesApiClient client;

    /**
     * Creates a {@link Health} report using all {@link Check} found
     * using the {@link KubernetesApiClient}
     * 
     * @return
     */
    public Health getComponentHealth() {

        // get checks
        List<Check> checks = getChecks();

        LOGGER.trace("found checks {}", checks);

        // find overall status
        String status = getOverallStatus(checks);

        LOGGER.trace("determined overall status {}", status);

        return Health.builder().status(status).checks(checks).build();

    }

    /**
     * Creates and returns a {@link List} of {@link Check} for each
     * {@link ReplicationControllerStatus} found returned from the {@link KubernetesApiClient}.
     * 
     * @return
     */
    private List<Check> getChecks() {

        Map<String, ReplicationControllerStatus> statusMap = client.getComponentStatusMap();

        LOGGER.trace("using status map to create list of checks {}", statusMap);

        // get checks
        return

            statusMap.keySet()
                .stream()
                .map(key -> {

                    ReplicationControllerStatus rcStatus = statusMap.get(key);

                    // builder with component name
                    CheckBuilder builder = Check.builder().name(key);

                    // determine if at least one replica in ready state
                    String status = rcStatus.getReadyReplicas() >= replicaThreshold ? UP : DOWN;
                    builder = builder.status(status);

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

    /**
     * Returns UP if the status of all {@link Check} in the {@link List}
     * is UP.  Otherwise, DOWN is returned.
     * 
     * @param checks
     * @return
     */
    private String getOverallStatus(List<Check> checks) {

        for(Check check : checks) {
            if(DOWN.equals(check.getStatus())) {
                LOGGER.trace("check found DOWN status {}", check);
                return DOWN;
            }
        }

        return UP;

    }

}