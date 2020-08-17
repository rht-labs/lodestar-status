package com.redhat.labs.lodestar.k8s.client;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.ReplicationControllerStatus;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class KubernetesApiClientTest extends LodeStarK8sTest {

    @Test
    void testGetComponentStatusMapNoNamespacesConfigured() {

        // given
        setComponentNamesAndNamespaces(Arrays.asList(), Arrays.asList());

        // when
        loadComponentStatus();
        Map<String, ReplicationControllerStatus> rcStatusMap = getClient().getComponentStatusMap();

        // then
        assertNotNull(rcStatusMap);
        assertEquals(0, rcStatusMap.keySet().size());

    }

    @Test
    void testGetComponentStatusMapMultipleNamespacesAllComponents() {

        // given
        final ReplicationController rc1 = mockReplicationController("component1-2", 2, 2);
        final ReplicationController rc2 = mockReplicationController("component2-4", 1, 1);
        final ReplicationController rc3 = mockReplicationController("component3-1", 1, 2);

        setupMockServerWithReplicationControllerApi("n1", Arrays.asList(rc1, rc2));
        setupMockServerWithReplicationControllerApi("n2", Arrays.asList(rc3));

        setComponentNamesAndNamespaces(Arrays.asList(), Arrays.asList("n1", "n2"));

        loadComponentStatus();

        // when
        Map<String, ReplicationControllerStatus> rcStatusMap = getClient().getComponentStatusMap();

        // then
        assertNotNull(rcStatusMap);
        assertEquals(3, rcStatusMap.keySet().size());

        ReplicationControllerStatus s1 = rcStatusMap.get("component1");
        assertNotNull(s1);
        assertEquals(2, s1.getReadyReplicas());
        assertEquals(2, s1.getReplicas());

        ReplicationControllerStatus s2 = rcStatusMap.get("component2");
        assertNotNull(s2);
        assertEquals(1, s2.getReadyReplicas());
        assertEquals(1, s2.getReplicas());

        ReplicationControllerStatus s3 = rcStatusMap.get("component3");
        assertNotNull(s3);
        assertEquals(1, s3.getReadyReplicas());
        assertEquals(2, s3.getReplicas());

    }


    @Test
    void testGetComponentStatusMapMultipleNamespacesSubsetOfComponents() {

        // given
        final ReplicationController rc1 = mockReplicationController("component1-2", 2, 2);
        final ReplicationController rc2 = mockReplicationController("component2-4", 1, 1);
        final ReplicationController rc3 = mockReplicationController("component3-1", 1, 2);

        setupMockServerWithReplicationControllerApi("n1", Arrays.asList(rc1, rc2));
        setupMockServerWithReplicationControllerApi("n2", Arrays.asList(rc3));

        setComponentNamesAndNamespaces(Arrays.asList("component2", "component3"), Arrays.asList("n1", "n2"));

        loadComponentStatus();

        // when
        Map<String, ReplicationControllerStatus> rcStatusMap = getClient().getComponentStatusMap();

        // then
        assertNotNull(rcStatusMap);
        assertEquals(2, rcStatusMap.keySet().size());

        ReplicationControllerStatus s1 = rcStatusMap.get("component1");
        assertNull(s1);

        ReplicationControllerStatus s2 = rcStatusMap.get("component2");
        assertNotNull(s2);
        assertEquals(1, s2.getReadyReplicas());
        assertEquals(1, s2.getReplicas());

        ReplicationControllerStatus s3 = rcStatusMap.get("component3");
        assertNotNull(s3);
        assertEquals(1, s3.getReadyReplicas());
        assertEquals(2, s3.getReplicas());

    }

}
