package com.redhat.labs.lodestar.health.k8s;

import java.util.Optional;

import javax.inject.Inject;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

import com.redhat.labs.lodestar.k8s.client.KubernetesApiClient;

import io.fabric8.kubernetes.api.model.ReplicationControllerStatus;

public abstract class K8sHealthCheck implements HealthCheck {

    private static final String CURRENT_REPLICAS = "CURRENT REPLICAS";
    private static final String DESIRED_REPLICAS = "DESIRED REPLICAS";

    @Inject
    KubernetesApiClient client;

    public abstract String getName();

    
    @Override
    public HealthCheckResponse call() {

        Optional<HealthCheckResponse> optionalResponse = getStatusFromKubernetesApi();

        if(optionalResponse.isPresent()) {
            return optionalResponse.get();
        }

        return HealthCheckResponse.down(getName());

    }

    private Optional<HealthCheckResponse> getStatusFromKubernetesApi() {

        HealthCheckResponse response = null;
        Optional<ReplicationControllerStatus> optionalStatus = client.getReplicationControllerStatusByName(getName());

        if(optionalStatus.isPresent()) {

            ReplicationControllerStatus status = optionalStatus.get();

            HealthCheckResponseBuilder builder = HealthCheckResponse.builder().name(getName());
            builder = status.getReadyReplicas() > 0 ? builder.up() : builder.down();
            builder
                .withData(CURRENT_REPLICAS, status.getReadyReplicas())
                .withData(DESIRED_REPLICAS, status.getReplicas());

            response = builder.build();

        }

        return Optional.ofNullable(response);

    }

}
