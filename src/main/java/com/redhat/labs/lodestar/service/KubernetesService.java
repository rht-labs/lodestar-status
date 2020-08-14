package com.redhat.labs.lodestar.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import lombok.Getter;

@ApplicationScoped
public class KubernetesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KubernetesService.class);

    @ConfigProperty(name = "lodestar.component.namespaces")
    Optional<List<String>> configuredComponentNamespaces;

    @ConfigProperty(name = "lodestar.component.names")
    Optional<List<String>> configuredComponentNames;

    @Inject
    KubernetesClient client;

    @Getter
    private Map<String, Map<String, String>> componentStatusMap;
    private List<String> componentNamespaces;
    private List<String> componentNames;

    void onStart(@Observes StartupEvent event) {

        componentNamespaces = configuredComponentNamespaces.isPresent() ? configuredComponentNamespaces.get() : new ArrayList<String>();
        componentNames = configuredComponentNames.isPresent() ? configuredComponentNames.get() : new ArrayList<String>();
        LOGGER.debug("configured component namespaces {} and component names {}", componentNamespaces.size(), componentNames.size());

        loadContainerStatusSummaryMap();
        LOGGER.debug("initial loaded component statuses: {}", componentStatusMap);

    }

    @Scheduled(every = "10s", delay = 20000)
    void updateContainerStatuses() {

        if(componentNamespaces.size() > 0 && componentNames.size() > 0) {
            LOGGER.debug("reloading component status map");
            loadContainerStatusSummaryMap();
        }

    }

    public Optional<Map<String, String>> getStatusByComponentName(String name) {
        return Optional.ofNullable(componentStatusMap.get(name));
    }

    void loadContainerStatusSummaryMap() {

        Map<String, ContainerStatus> csMap = getContainerStatusMap();

        componentStatusMap =
            csMap.keySet().stream()
            .collect(Collectors.toMap(
                key -> key, 
                key -> Map.of(
                        "started",
                        csMap.get(key).getStarted().toString(),
                        "ready",
                         csMap.get(key).getReady().toString()
                )));

    }

    Map<String, ContainerStatus> getContainerStatusMap() {

        List<Pod> podList = getRunningPods();

        return
            podList.stream()
                .flatMap(pod -> pod.getStatus().getContainerStatuses().stream())
                .filter(cStatus -> componentNames.contains(cStatus.getName()))
                .collect(Collectors.toMap(
                        cs -> cs.getName(),
                        cs -> cs,
                        (cs1, cs2) -> cs1,
                        ConcurrentHashMap::new
                        ));

    }

    List<Pod> getRunningPods() {

        return 
            componentNamespaces.stream()
                .map(namespace -> {
                    return getRunningPodsByNamespace(namespace);
                })
                .flatMap(list -> list.stream())
                .collect(Collectors.toList());

    }

    List<Pod> getRunningPodsByNamespace(String namespace) {
        return client.pods().inNamespace(namespace).list().getItems();
    }

}
