package com.redhat.labs.lodestar.k8s.client;

import java.util.List;

import javax.inject.Inject;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.ReplicationControllerBuilder;
import io.fabric8.kubernetes.api.model.ReplicationControllerListBuilder;
import io.fabric8.kubernetes.api.model.ReplicationControllerStatus;
import io.fabric8.kubernetes.api.model.ReplicationControllerStatusBuilder;
import io.fabric8.kubernetes.client.server.mock.KubernetesMockServer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.kubernetes.client.KubernetesMockServerTestResource;
import io.quarkus.test.kubernetes.client.MockServer;

@QuarkusTestResource(KubernetesMockServerTestResource.class)
public class LodeStarK8sTest {

    private static final String RC_API = "/api/v1/namespaces/$NAMESPACE/replicationcontrollers";

    @MockServer
    KubernetesMockServer mockServer;

    @Inject
    KubernetesApiClient client;

    public KubernetesApiClient getClient() {
        return client;
    }

    public void loadComponentStatus() {
        client.loadComponentStatus();
    }

    public void setupMockServerWithReplicationControllerApi(String namespace, List<ReplicationController> rcList) {

        String namespacePath = RC_API.replace("$NAMESPACE", namespace);

        mockServer.expect().get().withPath(namespacePath).andReturn(200, new ReplicationControllerListBuilder()
                .withNewMetadata().withResourceVersion("1").endMetadata().withItems(rcList).build()).once();

    }

    public void setComponentNamesAndNamespaces(List<String> namesList, List<String> namespaceList) {
        client.setComponentNames(namesList);
        client.setComponentNamespaces(namespaceList);
    }

    public ReplicationController mockReplicationController(String name, Integer readyReplicas,
            Integer desiredReplicas) {
        return new ReplicationControllerBuilder().withMetadata(mockObjectMets(name))
                .withStatus(mockReplicationControllerStatus(readyReplicas, desiredReplicas)).build();
    }

    public ObjectMeta mockObjectMets(String name) {
        return new ObjectMetaBuilder().withName(name).build();
    }

    public ReplicationControllerStatus mockReplicationControllerStatus(Integer readyReplicas,
            Integer desiredReplicas) {
        return new ReplicationControllerStatusBuilder().withReadyReplicas(readyReplicas).withReplicas(desiredReplicas)
                .build();
    }

}
