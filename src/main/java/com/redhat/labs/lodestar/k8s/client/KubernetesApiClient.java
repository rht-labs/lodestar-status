package com.redhat.labs.lodestar.k8s.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.ReplicationControllerStatus;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import lombok.Getter;
import lombok.Setter;

@ApplicationScoped
public class KubernetesApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(KubernetesApiClient.class);
    private static final String NOT_SET = "not.set";

    @Setter
    @ConfigProperty(name = "component.namespaces", defaultValue = NOT_SET)
    List<String> componentNamespaces;

    @Setter
    @ConfigProperty(name = "component.names", defaultValue = NOT_SET)
    List<String> componentNames;

    @Inject
    KubernetesClient client;

    @Getter
    Map<String, ReplicationControllerStatus> componentStatusMap;

    void onStart(@Observes StartupEvent event) {

        // initialize if namespaces not defined
        if(componentNamespaces.size() == 1 && componentNamespaces.get(0).equals(NOT_SET)) {
            componentNamespaces = new ArrayList<>();
        }

        LOGGER.debug("configured component namespaces {}", componentNamespaces);

        // iniitalize if component names not set
        if(componentNames.size() == 1 && componentNames.get(0).equals(NOT_SET)) {
            componentNames = new ArrayList<>();
        }

        LOGGER.debug("Configured component names {}", componentNames);

    }

    /**
     * Periodically updates the {@link Map} containing the {@link ReplicationController}
     * name - {@link ReplicationControllerStatus} pairs.
     */
    @Scheduled(every = "10s")
    void refreshComponentStatusMap() {
            loadComponentStatus();
    }

    /**
     * Retrieves all {@link ReplicationController} in the configured namespaces
     * and creates a {@link Map} using the {@link ReplicationController} name as 
     * the key and the {@link ReplicationControllerStatus} as the value.
     */
    void loadComponentStatus() {

        List<ReplicationController> rcList = getAllReplicationControllers();
        LOGGER.debug("replication controller list is {}", rcList);

        componentStatusMap =
                rcList
                    .stream()
                    .map(rc -> {
                        final String name = rc.getMetadata().getName().substring(0, rc.getMetadata().getName().lastIndexOf("-"));
                        rc.getMetadata().setName(name);
                        return rc;
                    })
                    .filter(rc -> (componentNames.size() == 0 || componentNames.contains(rc.getMetadata().getName())))
                    .filter(rc -> rc.getStatus().getReplicas() != null && rc.getStatus().getReadyReplicas() != null)
                    .collect(
                            Collectors.toMap(
                                    rc -> rc.getMetadata().getName(),
                                    rc -> rc.getStatus()
                                    ));

    }

    /**
     * Returns a {@link List} of all {@link ReplicationController} found for 
     * all namespaces in the configured {@link List}.  An empty {@link List}
     * is returned if no namespaces are configured.
     * 
     * @return
     */
    List<ReplicationController> getAllReplicationControllers() {

        return
                componentNamespaces
                    .parallelStream()
                    .map(namespace -> {
                        return getReplicaControllerByNamespace(namespace);
                    })
                    .flatMap(list -> list.stream())
                    .collect(Collectors.toList());

    }

    /**
     * Returns a {@link List} of all {@link ReplicationController} found for the 
     * given namespace.
     * 
     * @param namespace
     * @return
     */
    List<ReplicationController> getReplicaControllerByNamespace(String namespace) {
        
        try {
            LOGGER.debug("using k8s client to retrieve replication controllers in namespace {}", namespace);
            return client.replicationControllers().inNamespace(namespace).list().getItems();
        } catch(Exception e) {
            LOGGER.error("failed to get successful response from k8s api for namespace {}, exception message: {}", namespace, e.getMessage());
            return Arrays.asList();
        }

    }

}
