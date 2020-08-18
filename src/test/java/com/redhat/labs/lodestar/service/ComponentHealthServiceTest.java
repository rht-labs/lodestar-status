package com.redhat.labs.lodestar.service;

import java.util.Arrays;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.redhat.labs.lodestar.model.Health;
import com.redhat.labs.lodestar.ocp.client.LodeStarOpenShiftTest;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.openshift.api.model.DeploymentConfig;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class ComponentHealthServiceTest extends LodeStarOpenShiftTest {

    @Inject
    ComponentHealthService service;

    @Test
    void testAllComponentsUp() {

        // given
        final Deployment d1 = mockDeployment("component1", 1, 1, "True");
        final Deployment d2 = mockDeployment("component2", 1, 1, "True");

        final DeploymentConfig dc1 = mockDeploymentConfig("component3", 1, 1, "True");
        final DeploymentConfig dc2 = mockDeploymentConfig("component4", 2, 2, "True");

        setupMockServer("n1", Arrays.asList(d1,d2), Arrays.asList());
        setupMockServer("n2", Arrays.asList(), Arrays.asList(dc1, dc2));

        service.setComponentNames(Arrays.asList());
        service.setComponentNamespaces(Arrays.asList("n1", "n2"));

        service.getComponentHealth();

        // when
        Health health = service.getHealth();

        // then
        Assert.assertNotNull(health);
        Assert.assertEquals("UP", health.getStatus());

        Assert.assertNotNull(health.getChecks());
        Assert.assertEquals(4, health.getChecks().size());

        health.getChecks().stream().forEach(check -> {
            if(check.getStatus().equals("DOWN")) {
                Assert.fail("expected check value to be UP for check" + check.getName());
            }
        });

    }

    @Test
    void testAllComponentsUpSubsetOfComponents() {

        // given
        final Deployment d1 = mockDeployment("component1", 1, 1, "True");
        final Deployment d2 = mockDeployment("component2", 1, 1, "True");

        final DeploymentConfig dc1 = mockDeploymentConfig("component3", 1, 1, "True");
        final DeploymentConfig dc2 = mockDeploymentConfig("component4", 2, 2, "True");

        setupMockServer("n1", Arrays.asList(d1,d2), Arrays.asList());
        setupMockServer("n2", Arrays.asList(), Arrays.asList(dc1, dc2));

        service.setComponentNames(Arrays.asList("component1", "component4"));
        service.setComponentNamespaces(Arrays.asList("n1", "n2"));

        service.getComponentHealth();

        // when
        Health health = service.getHealth();

        // then
        Assert.assertNotNull(health);
        Assert.assertEquals("UP", health.getStatus());

        Assert.assertNotNull(health.getChecks());
        Assert.assertEquals(2, health.getChecks().size());

        health.getChecks().stream().forEach(check -> {
            if(check.getStatus().equals("DOWN")) {
                Assert.fail("expected check value to be UP for check" + check.getName());
            }
            if("component2".equals(check.getName()) || "component3".equals(check.getName())) {
                Assert.fail(check.getName() + " should have been filtered out.");
            }
        });

    }

    @Test
    void testOneComponentDown() {

        // given
        final Deployment d1 = mockDeployment("component1", 1, 1, "True");
        final Deployment d2 = mockDeployment("component2", 1, 1, "True");

        final DeploymentConfig dc1 = mockDeploymentConfig("component3", 1, 1, "False");
        final DeploymentConfig dc2 = mockDeploymentConfig("component4", 2, 2, "True");

        setupMockServer("n1", Arrays.asList(d1,d2), Arrays.asList());
        setupMockServer("n2", Arrays.asList(), Arrays.asList(dc1, dc2));

        service.setComponentNames(Arrays.asList());
        service.setComponentNamespaces(Arrays.asList("n1", "n2"));

        service.getComponentHealth();

        // when
        Health health = service.getHealth();

        // then
        Assert. assertNotNull(health);
        Assert. assertEquals("DOWN", health.getStatus());

        Assert.assertNotNull(health.getChecks());
        Assert.assertEquals(4, health.getChecks().size());

        health.getChecks().stream().forEach(check -> {
            if(check.getName().equals("component3") && check.getStatus().equals("UP")) {
                Assert.fail("expected check value to be DOWN for check " + check);
            } else if(!check.getName().equals("component3") && check.getStatus().equals("DOWN")) {
                Assert.fail("expected check value to be UP for check" + check);
            }
        });

    }

    @Test
    void testGetHealthNoNamespacesConfigured() {

        // given
        service.setComponentNames(Arrays.asList());
        service.setComponentNamespaces(Arrays.asList());

        service.getComponentHealth();

        // when
        Health health = service.getHealth();

        // then
        Assert.assertNotNull(health);
        Assert.assertEquals(0, health.getChecks().size());

    }

}
