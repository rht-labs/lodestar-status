package com.redhat.labs.lodestar.k8s.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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

@ApplicationScoped
public class KubernetesApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(KubernetesApiClient.class);
    private static final String NOT_SET = "not.set";

    @ConfigProperty(name = "component.namespaces")
    List<String> componentNamespaces;

    @Inject
    KubernetesClient client;

    Map<String, ReplicationControllerStatus> componentStatusMap;

    void onStart(@Observes StartupEvent event) {

        if(componentNamespaces.size() == 1 && componentNamespaces.get(0).equals(NOT_SET)) {
            componentNamespaces = new ArrayList<>();
        }

        LOGGER.debug("configured component namespaces {}", componentNamespaces);

        loadComponentStatus();
        LOGGER.debug("initial loaded component statuses: {}", componentStatusMap);

    }

    public Optional<ReplicationControllerStatus> getReplicationControllerStatusByName(String name) {
        return Optional.ofNullable(componentStatusMap.get(name));
    }

    @Scheduled(every = "15s", delay = 30, delayUnit = TimeUnit.SECONDS)
    void refreshComponentStatusMap() {

        if(!componentNamespaces.isEmpty()) {
            LOGGER.debug("refreshing component status data");
            loadComponentStatus();
        }

    }

    void loadComponentStatus() {

        List<ReplicationController> rcList = getAllReplicationControllers();

        componentStatusMap =
                rcList
                    .stream()
                    .filter(rc -> rc.getStatus().getReplicas() != null && rc.getStatus().getReadyReplicas() != null)
                    .collect(
                            Collectors.toMap(
                                    rc -> rc.getMetadata().getName().substring(0, rc.getMetadata().getName().lastIndexOf("-")),
                                    rc -> rc.getStatus()
                                    ));

    }

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

    List<ReplicationController> getReplicaControllerByNamespace(String namespace) {
        return client.replicationControllers().inNamespace(namespace).list().getItems();
    }



}
