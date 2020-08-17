package com.redhat.labs.lodestar.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import com.redhat.labs.lodestar.k8s.client.LodeStarK8sTest;
import com.redhat.labs.lodestar.model.Health;

import io.fabric8.kubernetes.api.model.ReplicationController;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ComponentHealthServiceTest extends LodeStarK8sTest {

    @Inject
    ComponentHealthService service;

    @Test
    void testAllComponentsUp() {

        // given
        final ReplicationController rc1 = mockReplicationController("component1-2", 2, 2);
        final ReplicationController rc2 = mockReplicationController("component2-4", 1, 1);
        final ReplicationController rc3 = mockReplicationController("component3-1", 1, 2);

        setupMockServerWithReplicationControllerApi("n1", Arrays.asList(rc1, rc2));
        setupMockServerWithReplicationControllerApi("n2", Arrays.asList(rc3));

        setComponentNamesAndNamespaces(Arrays.asList(), Arrays.asList("n1", "n2"));

        loadComponentStatus();

        // when
        Health health = service.getComponentHealth();

        // then
        assertNotNull(health);
        assertEquals("UP", health.getStatus());

        assertNotNull(health.getChecks());
        assertEquals(3, health.getChecks().size());

        health.getChecks().stream().forEach(check -> {
            if(check.getStatus().equals("DOWN")) {
                fail("expected check value to be UP for check" + check.getName());
            }
        });

    }

    @Test
    void testOneComponentsDown() {

        // given
        final ReplicationController rc1 = mockReplicationController("component1-2", 2, 2);
        final ReplicationController rc2 = mockReplicationController("component2-4", 1, 1);
        final ReplicationController rc3 = mockReplicationController("component3-1", 0, 2);

        setupMockServerWithReplicationControllerApi("n1", Arrays.asList(rc1, rc2));
        setupMockServerWithReplicationControllerApi("n2", Arrays.asList(rc3));

        setComponentNamesAndNamespaces(Arrays.asList(), Arrays.asList("n1", "n2"));

        loadComponentStatus();

        // when
        Health health = service.getComponentHealth();

        // then
        assertNotNull(health);
        assertEquals("DOWN", health.getStatus());

        assertNotNull(health.getChecks());
        assertEquals(3, health.getChecks().size());

        health.getChecks().stream().forEach(check -> {
            if(check.getName().equals("component3") && check.getStatus().equals("UP")) {
                fail("expected check value to be DOWN for check " + check);
            } else if(!check.getName().equals("component3") && check.getStatus().equals("DOWN")) {
                fail("expected check value to be UP for check" + check);
            }
        });

    }

    @Test
    void testOneComponentsDownThresholdNotMet() {

        // given
        final ReplicationController rc1 = mockReplicationController("component1-2", 2, 2);
        final ReplicationController rc2 = mockReplicationController("component2-4", 2, 2);
        final ReplicationController rc3 = mockReplicationController("component3-1", 1, 2);

        setupMockServerWithReplicationControllerApi("n1", Arrays.asList(rc1, rc2));
        setupMockServerWithReplicationControllerApi("n2", Arrays.asList(rc3));

        setComponentNamesAndNamespaces(Arrays.asList(), Arrays.asList("n1", "n2"));

        service.setReplicaThreshold(2);

        loadComponentStatus();

        // when
        Health health = service.getComponentHealth();

        // then
        assertNotNull(health);
        assertEquals("DOWN", health.getStatus());

        assertNotNull(health.getChecks());
        assertEquals(3, health.getChecks().size());

        health.getChecks().stream().forEach(check -> {
            if(check.getName().equals("component3") && check.getStatus().equals("UP")) {
                fail("expected check value to be DOWN for check " + check);
            } else if(!check.getName().equals("component3") && check.getStatus().equals("DOWN")) {
                fail("expected check value to be UP for check" + check);
            }
        });

    }

}
