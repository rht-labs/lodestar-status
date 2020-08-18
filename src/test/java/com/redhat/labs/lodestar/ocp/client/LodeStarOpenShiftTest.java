package com.redhat.labs.lodestar.ocp.client;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import com.redhat.labs.lodestar.ocp.client.OpenShiftApiClient;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentCondition;
import io.fabric8.kubernetes.api.model.apps.DeploymentConditionBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentListBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatusBuilder;
import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer;
import io.fabric8.openshift.api.model.DeploymentConfig;
import io.fabric8.openshift.api.model.DeploymentConfigBuilder;
import io.fabric8.openshift.api.model.DeploymentConfigListBuilder;
import io.fabric8.openshift.api.model.DeploymentConfigStatus;
import io.fabric8.openshift.api.model.DeploymentConfigStatusBuilder;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.kubernetes.client.KubernetesMockServerTestResource;
import io.quarkus.test.kubernetes.client.MockServer;

@QuarkusTestResource(KubernetesMockServerTestResource.class)
public class LodeStarOpenShiftTest {

    private static final String BASE_APPS_API = "/apis/apps/v1/namespaces/{NAMESPACE}/deployments";
    private static final String BASE_APPS_OS_API = "/apis/apps.openshift.io/v1/namespaces/{NAMESPACE}/deploymentconfigs";
    private static final String DEPLOYMENT_CONDITION_TYPE_PROGRESSING = "Progressing";
    private static final String DEPLOYMENT_CONDITION_TYPE_AVAILABLE = "Available";

    @MockServer
    KubernetesMockServer mockServer;

    @Inject
    OpenShiftApiClient client;

    public void setupMockServer(String namespace, List<Deployment> deployments,
            List<DeploymentConfig> deploymentConfigs) {

//        if (null != deployments && deployments.size() > 0) {
            mockServer.expect().get().withPath(BASE_APPS_API.replace("{NAMESPACE}", namespace))
                    .andReturn(200, new DeploymentListBuilder().withItems(deployments).build()).once();
//        }

//        if (null != deploymentConfigs && deploymentConfigs.size() > 0) {
            mockServer.expect().get().withPath(BASE_APPS_OS_API.replace("{NAMESPACE}", namespace))
                    .andReturn(200, new DeploymentConfigListBuilder().withItems(deploymentConfigs).build()).once();
//        }

    }

    public Deployment mockDeployment(String name, Integer readyReplicas, Integer replicas, String availableStatus) {
        return new DeploymentBuilder().withMetadata(mockObjectMets(name))
                .withStatus(mockDeploymentStatus(readyReplicas, replicas, availableStatus)).build();
    }

    public DeploymentStatus mockDeploymentStatus(Integer readyReplicas, Integer replicas, String availableStatus) {
        return new DeploymentStatusBuilder().withReadyReplicas(readyReplicas).withReplicas(replicas)
                .withConditions(mockDeploymentConditionsForDeployment(availableStatus)).build();
    }

    public List<DeploymentCondition> mockDeploymentConditionsForDeployment(String availableStatus) {

        DeploymentCondition dc1 = new DeploymentConditionBuilder().withStatus("True")
                .withType(DEPLOYMENT_CONDITION_TYPE_PROGRESSING).build();
        DeploymentCondition dc2 = new DeploymentConditionBuilder().withStatus(availableStatus)
                .withType(DEPLOYMENT_CONDITION_TYPE_AVAILABLE).build();
        return Arrays.asList(dc1, dc2);

    }

    public DeploymentConfig mockDeploymentConfig(String name, Integer readyReplicas, Integer replicas,
            String availableStatus) {
        return new DeploymentConfigBuilder().withMetadata(mockObjectMets(name))
                .withStatus(mockDeploymentConfigStatus(readyReplicas, replicas, availableStatus)).build();
    }

    public DeploymentConfigStatus mockDeploymentConfigStatus(Integer readyReplicas, Integer replicas,
            String availableStatus) {
        return new DeploymentConfigStatusBuilder().withReadyReplicas(readyReplicas).withReplicas(replicas)
                .withConditions(mockDeploymentConditionsForDeploymentConfig(availableStatus)).build();
    }

    public List<io.fabric8.openshift.api.model.DeploymentCondition> mockDeploymentConditionsForDeploymentConfig(
            String availableStatus) {

        io.fabric8.openshift.api.model.DeploymentCondition dc1 = new io.fabric8.openshift.api.model.DeploymentConditionBuilder()
                .withStatus("True").withType(DEPLOYMENT_CONDITION_TYPE_PROGRESSING).build();
        io.fabric8.openshift.api.model.DeploymentCondition dc2 = new io.fabric8.openshift.api.model.DeploymentConditionBuilder()
                .withStatus(availableStatus).withType(DEPLOYMENT_CONDITION_TYPE_AVAILABLE).build();
        return Arrays.asList(dc1, dc2);

    }

    public ObjectMeta mockObjectMets(String name) {
        return new ObjectMetaBuilder().withName(name).build();
    }

}
