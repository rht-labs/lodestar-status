# LodeStar - Status

The Status API for LodeStar.

This API is responsible for making the status and version information of each of the LodeStar components available to consumers.

## JSON REST APIs

The JSON REST APIs consist of the following resources:

* VersionManifest

### VersionManifest Resource

The version manifest resource exposes an API that allows clients to retrieve the current version of each LodeStar component as well as the main LodeStar version.

```
GET /api/v1/version/manifest
```

### Status Resoure

The status resource exposes an API that allows clients to retrieve the current status of each LodeStar component.  This process uses the OpenShift/Kubernetes APIs to determine the availability of the components using the associated status of their `deployments` or `deploymentconfigs`.

```
GET /api/v1/status
```

## Configuration

The preferred place to store non-sensitive data is in the application.properties.

Sensitive fields like the gitlab token and cluster credentials should be stored in a OpenShift secret at a minimum. Other environment specific information should be stored in environmental variables such as location of the version manifest file.

### LOGGING

| Name | Example Value | Required |
|------|---------------|----------|
| LODESTAR_STATUS_LOGGING | DEBUG | False |

### VERSION

| Name | Example Value | Required |
|------|---------------|----------|
| LODESTAR_STATUS_VERSIONS_PATH | /config/version-manifest.yml | False |
| VERSION_APP_KEY | lodestar | False |
| STATUS_GIT_COMMIT | 8509c02 | False |
| STATUS_GIT_TAG | latest | False |

Descriptions:

- `LODESTAR_STATUS_VERSIONS_PATH` - the path to the file containing the version manifest data
- `VERSION_APP_KEY` - the key/name of the main application found in the version manifest data
- `STATUS_GIT_COMMIT` - the Git commit of the status-service that is deployed
- `STATUS_GIT_TAG` - the Git tag of the status-service that is deployed

### VERSION MANIFEST SCHEMA

The version manifest will be stored in a ConfigMap.  The format is in Yaml and will contain a list of applications, each containing the name of the application and its associated version.  Below is an example:

```
applications:
- application: componentOne
  version: v34.4
- application: componentThree
  version: v1.2
- application: componentTwo
  version: v23.9
- application: lodestar
  version: v5.1
```

### STATUS

| Name | Example Value | Required |
|------|---------------|----------|
| LODESTAR_COMPONENT_NAMESPACES | my-namespace1,my-namespace-2 | False |
| LODESTAR_COMPONENT_NAMES | my-component | False |

Descriptions:

- `LODESTAR_COMPONENT_NAMESPACES` - comma separated list of namespaces containing components to report.  If not specified, no component status will be reported.
- `LODESTAR_COMPONENT_NAMES` - comma separated list of components to include in the reporting.  If not specified, all components in configured namespaces will be  included in the status report.

## Development

See [the deployment README](deployment/README.md) for details on how to spin up a deployment for developing on OpenShift.

### Running the Application 

You can run your application using Quarkus using:

```

# logging
export LODESTAR_STATUS_LOGGING=DEBUG

# version
export LODESTAR_STATUS_VERSIONS_PATH=<path to your versions manifest file>
export VERSION_APP_KEY=<key of the main version in the manifest file>

# package the application
./mvnw clean package

# run the application
java -jar target/lodestar-status-*-runner.jar
```


