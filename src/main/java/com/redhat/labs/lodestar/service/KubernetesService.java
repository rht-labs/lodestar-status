package com.redhat.labs.lodestar.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;

@ApplicationScoped
public class KubernetesService {

    @ConfigProperty(name = "lodestar.component.container.names")
    List<String> containerNameList;

    @Inject
    KubernetesClient client;

    // get container status by name

    public Map<String, ContainerStatus> getContainerStatusMap() {

        List<Pod> podList = getRunningPods();

        return
            podList.stream()
                .flatMap(pod -> pod.getStatus().getContainerStatuses().stream())
                .filter(cStatus -> containerNameList.contains(cStatus.getName()))
                .collect(Collectors.toMap(
                        cs -> cs.getName(),
                        cs -> cs,
                        (cs1, cs2) -> cs1,
                        ConcurrentHashMap::new
                        ));

    }

    public List<Pod> getRunningPods() {

        return 
                client
                    .pods()
                    .inAnyNamespace()
                    .withField("status.phase", "Running")
                    .list()
                    .getItems();

    }

}
