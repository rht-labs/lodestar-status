package com.redhat.labs.lodestar.ocp.client;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.openshift.api.model.DeploymentConfig;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

@ApplicationScoped
public class OpenShiftApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenShiftApiClient.class);

    OpenShiftClient client = new DefaultOpenShiftClient();

    /**
     * Fetches all {@link Deployment} for the given namespace. Returns an empty
     * {@link List} if the call fails and logs the error.
     * 
     * @param namespace
     * @return
     */
    public List<Deployment> getDeploymentsByNamespace(String namespace) {
        try {
            LOGGER.trace("fetching deployments for namespace {} using k8s api client.", namespace);
            return client.apps().deployments().inNamespace(namespace).list().getItems();
        } catch (Exception e) {
            LOGGER.error("failed to get deploymnets from k8s api for namespace {}, exception message: {}", namespace,
                    e.getMessage());
            return Arrays.asList();
        }
    }

    /**
     * Fetches all {@link DeploymentConfig} for the given namespace. Returns an
     * empty {@link List} if the call fails and logs the error.
     * 
     * @param namespace
     * @return
     */
    public List<DeploymentConfig> getDeploymentConfigsByNamespace(String namespace) {
        try {
            LOGGER.trace("fetching deployment configs for namespace {} using k8s api client.", namespace);
            return client.deploymentConfigs().inNamespace(namespace).list().getItems();
        } catch (Exception e) {
            LOGGER.error("failed to get deploymnet configs from k8s api for namespace {}, exception message: {}",
                    namespace, e.getMessage());
            return Arrays.asList();
        }
    }

}
