package com.redhat.labs.lodestar.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.labs.lodestar.model.Check;
import com.redhat.labs.lodestar.model.Health;
import com.redhat.labs.lodestar.ocp.client.OpenShiftApiClient;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentCondition;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import io.fabric8.openshift.api.model.DeploymentConfig;
import io.fabric8.openshift.api.model.DeploymentConfigStatus;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import lombok.Getter;
import lombok.Setter;

@ApplicationScoped
public class ComponentHealthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentHealthService.class);

    private static final String NOT_SET = "not.set";
    private final static String UP = "UP";
    private final static String DOWN = "DOWN";
    private static final String CURRENT_REPLICAS = "Current Replicas";
    private static final String DESIRED_REPLICAS = "Desired Replicas";
    private static final String CONDITION_TYPE_AVAILABLE = "Available";

    @Setter
    @ConfigProperty(name = "component.namespaces", defaultValue = NOT_SET)
    List<String> componentNamespaces;

    @Setter
    @ConfigProperty(name = "component.names", defaultValue = NOT_SET)
    List<String> componentNames;

    @Inject
    OpenShiftApiClient client;

    @Getter
    Health health;

    /**
     * Initialize component names and namespaces lists if they were not configured.
     * 
     * @param event
     */
    void onStart(@Observes StartupEvent event) {

        // initialize if namespaces not defined
        if (componentNamespaces.size() == 1 && componentNamespaces.get(0).equals(NOT_SET)) {
            componentNamespaces = new ArrayList<>();
        }

        LOGGER.debug("configured component namespaces {}", componentNamespaces);

        // iniitalize if component names not set
        if (componentNames.size() == 1 && componentNames.get(0).equals(NOT_SET)) {
            componentNames = new ArrayList<>();
        }

        LOGGER.debug("Configured component names {}", componentNames);

    }

    /**
     * Periodically reloads the {@link Health} for all components.
     * 
     */
    @Scheduled(every = "10s")
    void checkComponentHealth() {
        getComponentHealth();
    }

    /**
     * Creates a {@link Health} report using all {@link Check} found using the
     * {@link OpenShiftApiClient}.
     * 
     */
    public void getComponentHealth() {

        // get checks
        List<Check> checks = getChecks();

        LOGGER.trace("found checks {}", checks);

        // find overall status
        String status = getOverallStatus(checks);

        LOGGER.trace("determined overall status {}", status);

        // set health to current state
        health = Health.builder().status(status).checks(checks).build();

    }

    /**
     * Returns a {@link List} of {@link Check} that summarize the status of
     * {@link Deployment} and {@link DeploymentConfig} in the configured namespaces.
     * 
     * @return
     */
    private List<Check> getChecks() {

        Stream<Check> deploymentChecks = getChecksForDeployments();
        Stream<Check> deploymentConfigChecks = getChecksForDeploymentConfigs();

        return Stream.concat(deploymentChecks, deploymentConfigChecks).collect(Collectors.toList());
    }

    /**
     * Returns a {@link Stream} of {@link Check} for each {@link Deployment} in the
     * configured namespaces.
     * 
     * @return
     */
    private Stream<Check> getChecksForDeployments() {

        return componentNamespaces.stream()
                .flatMap(namespace -> client.getDeploymentsByNamespace(namespace).stream())
                .filter(deployment -> (componentNames.size() == 0
                        || componentNames.contains(deployment.getMetadata().getName())))
                .map(deployment -> convertDeploymentToCheck(deployment));

    }

    /**
     * Creates a {@link Check} from a {@link Deployment}
     * 
     * @param deployment
     * @return
     */
    private Check convertDeploymentToCheck(Deployment deployment) {

        String name = deployment.getMetadata().getName();
        DeploymentStatus status = deployment.getStatus();
        Boolean available = isDeploymentAvailable(status.getConditions());

        return createCheck(name, available, status.getReadyReplicas(), status.getReplicas());

    }

    /**
     * Returns a {@link Stream} of {@link Check} for each {@link DeploymentConfig}
     * in the configured namespaces.
     * 
     * @return
     */
    private Stream<Check> getChecksForDeploymentConfigs() {

        return componentNamespaces.stream()
                .flatMap(namespace -> client.getDeploymentConfigsByNamespace(namespace).stream())
                .filter(deploymentConfig -> (componentNames.size() == 0
                        || componentNames.contains(deploymentConfig.getMetadata().getName())))
                .map(deployment -> convertDeploymentConfigToCheck(deployment));

    }

    /**
     * Creates a {@link Check} from a {@link DeploymentConfig}
     * 
     * @param deploymentConfig
     * @return
     */
    private Check convertDeploymentConfigToCheck(DeploymentConfig deploymentConfig) {

        String name = deploymentConfig.getMetadata().getName();
        DeploymentConfigStatus status = deploymentConfig.getStatus();
        Boolean available = isDeploymentConfigAvailable(status.getConditions());

        return createCheck(name, available, status.getReadyReplicas(), status.getReplicas());
    }

    /**
     * Uses {@link DeploymentCondition} to determine if the {@link Deployment} is
     * available.
     * 
     * @param statuses
     * @return
     */
    private Boolean isDeploymentAvailable(List<io.fabric8.kubernetes.api.model.apps.DeploymentCondition> statuses) {

        Optional<String> optional = statuses.stream()
                .filter(deploymentCondition -> deploymentCondition.getType().equals(CONDITION_TYPE_AVAILABLE))
                .map(deploymentCondition -> deploymentCondition.getStatus()).findFirst();

        return optional.isPresent() ? Boolean.valueOf(optional.get()) : Boolean.FALSE;

    }

    /**
     * Uses {@link io.fabric8.openshift.api.model.DeploymentCondition} to determine
     * if the {@link DeploymentConfig} is available.
     * 
     * @param statuses
     * @return
     */
    private Boolean isDeploymentConfigAvailable(List<io.fabric8.openshift.api.model.DeploymentCondition> statuses) {

        Optional<String> optional = statuses.stream()
                .filter(deploymentCondition -> deploymentCondition.getType().equals(CONDITION_TYPE_AVAILABLE))
                .map(deploymentCondition -> deploymentCondition.getStatus()).findFirst();

        return optional.isPresent() ? Boolean.valueOf(optional.get()) : Boolean.FALSE;

    }

    /**
     * Creates a {@link Check} for the given paramaters.
     * 
     * @param name
     * @param available
     * @param currentReplicas
     * @param desiredReplicas
     * @return
     */
    private Check createCheck(String name, Boolean available, Integer currentReplicas, Integer desiredReplicas) {
        LOGGER.info("creating check for {}", name);
        return Check.builder().name(name).status(available ? UP : DOWN)
                .data(Map.of(CURRENT_REPLICAS, currentReplicas == null ? 0 : currentReplicas, DESIRED_REPLICAS,
                        desiredReplicas == null ? 0 : desiredReplicas))
                .build();
    }

    /**
     * Returns UP if the status of all {@link Check} in the {@link List} is UP.
     * Otherwise, DOWN is returned.
     * 
     * @param checks
     * @return
     */
    private String getOverallStatus(List<Check> checks) {

        for (Check check : checks) {
            if (DOWN.equals(check.getStatus())) {
                LOGGER.trace("check found DOWN status {}", check);
                return DOWN;
            }
        }

        return UP;

    }

}