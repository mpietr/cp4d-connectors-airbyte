# cp4d-connectors-airbyte
With IBM® Cloud Pak for Data, you can connect to different data sources. There are several data source types supported out of the box. In addition for data source types not supported out-of-the-box there is an option to extend them with custom connectors. This project contains proof of concept of integrating CPD connectivity with Airbyte connectors.

A project is a part of the master thesis at Poznan University of Technology, done in collaboration with IBM Polska Sp. z o. o.

# Airbyte Connector for IBM Cloud Pak for Data

# airbyte-folder-structure

## src/main/java/pl/poznan/put/connect/airbyte

| Class | Description |
| --- | --- |
| `AirbyteAssetParser` | Parses assets using their path and namespace. Methods available to parse assets, retrieve namespaced assets, and retrieve assets without a namespace. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/AirbyteAssetParser.java)|
| `AirbyteConnector` | Represents a connector for Airbyte, an open-source data integration platform. Used to interact with Airbyte sources and targets, discover assets, perform actions, and manage interactions with the source and target. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/AirbyteConnector.java)|
| `AirbyteConnectorFactory` | Implements the ConnectorFactory interface. Creates instances of AirbyteConnector and maps data source type names to Docker image names for the Airbyte connectors. Follows the singleton pattern. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/AirbyteConnectorFactory.java)|
| `AirbyteCustomAction` | Handles custom actions in Airbyte. Provides methods to execute custom actions and retrieve the results.  [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/AirbyteCustomAction.java)|
| `AirbyteDatasourceType` | Implements the CustomFlightDatasourceType. Represents a data source type for Airbyte and sets the necessary attributes and properties for the data source types. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/AirbyteDatasourceType.java)|
| `AirbyteFlightProducer` | Implements the ConnectorFlightProducer. It represents a flight producer for Airbyte connectors. Uses the AirbyteConnectorFactory to create connectors. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/AirbyteFlightProducer.java)|
| `AirbyteImages` | Provides a way to get the image name for a given image type. Currently, only supports postgres as a valid image type. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/AirbyteImages.java)|
| `AirbyteProcessRunner` | Runs processes in Airbyte. Uses the PodProcess class to handle the actual running of the processes. Methods available to run "spec", "check", "discover", and "read" actions on given images. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/AirbyteProcessRunner.java)|
| `AirbyteSourceInteraction` | Implements the RowBasedSourceInteraction. Represents an interaction with a source in the Airbyte connector. Provides methods to get records from the source, get tickets, get fields, and close the interaction. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/AirbyteSourceInteraction.java)|
| `AirbyteStreamProcessor` | Processes custom, discover, and read actions in Airbyte. Reads lines from a BufferedReader, parses them into AirbyteMessage objects, and handles them based on their type. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/AirbyteStreamProcessor.java)|
| `AirbyteTargetInteraction` | Implements the RowBasedTargetInteraction. Represents an interaction with a target in the Airbyte connector. Provides methods to put records into the target and perform setup and wrap-up actions. However, these methods are currently empty and do not perform any action. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/AirbyteTargetInteraction.java)|

## src/main/java/pl/poznan/put/connect/airbyte/containers

| Class | Description |
| --- | --- |
| `InitialContainerFactory` | Creates an initial container setup for a given image. Configures the container with necessary settings and resources. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/containers/InitialContainerFactory.java) |
| `MainContainerFactory` | Creates the main container for a given image, volume mounts, arguments, and ports. Reads a command template, replaces placeholders, and configures the container. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/containers/MainContainerFactory.java) |
| `SocatContainerFactory` | Creates a container using the `alpine/socat` image and generates commands for redirecting data streams with `socat`. The `create` method configures the container, and `getOutStreamCommand` generates commands to transmit data to a specified host and port. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/containers/SocatContainerFactory.java) |

## src/main/java/pl/poznan/put/connect/airbyte/models
### src/main/java/pl/poznan/put/connect/airbyte/models/messages

| Message Type | Description |
| --- | --- |
| `AirbyteMessageType` | The type of the Airbyte message. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/models/messages)|
| `AirbyteLogMessage` | The log message, if the type is `LOG`. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/models/messages)|
| `AirbyteSpecMessage` | The spec message, if the type is `SPEC`.  [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/models/messages)|
| `AirbyteConnectionStatusMessage` | The connection status message, if the type is `CONNECTION_STATUS`.  [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/models/messages)|
| `AirbyteCatalogMessage` | The catalog message, if the type is `CATALOG`.  [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/models/messages)|
| `AirbyteRecordMessage` | The record message, if the type is `RECORD`.  [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/models/messages)|
| `AirbyteStateMessage` | The state message, if the type is `STATE`.  [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/models/messages)|
| `AirbyteTraceMessage` | The trace message, if the type is `TRACE`. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/models/messages)|

More details about each message type are provided in https://docs.airbyte.com/understanding-airbyte/airbyte-protocol/#airbytemessage

### src/main/java/pl/poznan/put/connect/airbyte/models/schema
| Class | Description |
| --- | --- |
| `JsonSchema` | This class represents a JSON schema, which is a tool for validating the structure of JSON data. It has several fields including `$schema`, `title`, `description`, `type`, `properties`, and `required`. The `@Data` annotation from Lombok library is used to automatically generate boilerplate code like getters, setters, equals, hashCode, and toString methods. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/models/schema)|
| `JsonSchemaProperty` | This class represents a property within a JSON schema. It has fields like `description`, `type`, `format`, and `airbyteType`. The `type` field can be any object, representing the flexibility of types a JSON property can have. The `airbyteType` field is annotated with `@SerializedName("airbyte_type")`, indicating that in JSON, this field will be named as "airbyte_type". [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/models/schema)|
| `JsonSchemaTypes` | This is an enumeration that defines the possible types a JSON schema property can have. It includes `INTEGER`, `STRING`, `NUMBER`, `BOOLEAN`, `ARRAY`, and `OBJECT`. Each enum value is annotated with `@SerializedName`, which indicates the actual JSON property name when the enum is serialized or deserialized with Gson. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/models/schema)|

 ## src/main/java/pl/poznan/put/connect/airbyte/pods

| Class | Description |
| --- | --- |
| `PodFactory` | Factory class responsible for creating Kubernetes pods for Airbyte connectors. It provides methods to create pods with different configurations and settings. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/pods/PodFactory.java) |
| `PodProcess` | Represents a process running within a Kubernetes pod. It manages the execution of commands and processes within the pod. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/pods/PodProcess.java) |
| `PodParameters` | Defines parameters and constants used for configuring Kubernetes pods in Airbyte. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/pods/PodParameters.java) |
| `PodUtils` | Utility class providing helper methods for working with Kubernetes pods in Airbyte. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/pods/PodUtils.java) |
| `PodListener` | Listener interface for monitoring events related to Kubernetes pods in Airbyte. It allows implementing custom actions based on pod lifecycle events. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/pods/PodListener.java) |

## src/main/java/pl/poznan/put/connect/airbyte/volumes
| Class | Description |
| --- | --- |
| `VolumeFactory` | Designed to create Kubernetes volumes and volume mounts, using the Fabric8 Kubernetes client library. [Source](https://github.com/IBM/cp4d-connectors-airbyte/blob/main/subprojects/airbyte/src/main/java/pl/poznan/put/connect/airbyte/volumes)|


# Main classes
## AirbyteAssetParser

***AirbyteAssetParser*** is a Java class in the `pl.poznan.put.connect.airbyte` package. 

It parses assets using their path and namespace.

### Methods

#### `parseAssets(...)`
A static method, wrapper method for retriving assets. 
Calls either getNamespacedAsset or getAssetWithoutNamespace to retrieve the appropriate assets<br>
- parameters:
    - `String path`
    - `List<CustomFlightAssetDescriptor> assets`
- returns:  
    - `List<CustomFlightAssetDescriptor>`
    
Method is used to parse a list of assets and return a subset of those assets based on the provided path and whether the assets belong to a namespace or not.
    
---

#### `getNamespacedAsset(...)` 
A static method, retrieves namespaced assets.

This method is used to retrieve a specific set of assets based on a provided path and matchers in a context where assets are organized by namespaces and possibly further categorized into streams.<br>
- parameters:
    - `Matcher namespaceMatcher` 
    - `Matcher streamMatcher` 
    - `List<CustomFlightAssetDescriptor> assets` 
    - `String path`
    - `List<String> namespaceAssetList`
- returns: 
    - `List<CustomFlightAssetDescriptor>` objects based on the provided parameters
- exeptions:
    - `IllegalArgumentException`


It returns the original list of assets if the path is empty, transforms the list into CustomFlightAssetDescriptor objects if the path is a single forward slash, filters and transforms the list based on the matchers if they match, and throws an *IllegalArgumentException* if none of these conditions are met.

---

#### `getAssetWithoutNamespace(...)`
A static method, retrieves assets without a namespace.<br><br>
- parameters:
    - `Matcher namespaceMatcher`
    - `List<CustomFlightAssetDescriptor> assets`
    - `String path`
- returns: 
    - `List<CustomFlightAssetDescriptor>`
- exeptions:
    - `IllegalArgumentException`

    This method returns either the original list of assets, transforms the list into CustomFlightAssetDescriptor objects, filters and transforms the list based on the namespaceMatcher, or throws an *IllegalArgumentException*.


## AirbyteConnector

Class `AirbyteConnector` implements `RowBasedConnector` and is a part of the `pl.poznan.put.connect.airbyte` package. 

It represents a connector for Airbyte, an open-source data integration platform.

This class is used to interact with Airbyte sources and targets. It can discover assets, perform actions, and manage interactions with the source and target. The actual interaction with the Airbyte processes is handled by the [AirbyteProcessRunner](#airbyteprocessrunner) class.

### Constructors

- `AirbyteConnector(ConnectionProperties properties)`

Initializes the connector with the given connection properties.
- `AirbyteConnector(ConnectionProperties properties, String imageName)`

Initializes the connector with the given connection properties and Docker image name.

### Methods

#### `discoverAssets(...)`
Discovers assets based on the provided criteria and returns a list of  objects representing the discovered assets.
- parameters: 
    - `CustomFlightAssetsCriteria criteria`
- returns:
    - `List<CustomFlightAssetDescriptor>`

---

#### `getSourceInteraction(...)`
Returns an instance of AirbyteSourceInteraction which represents an interaction with a source in the AirbyteConnector.
- parameters: 
    - `CustomFlightAssetDescriptor asset`
    - `Ticket ticket` 
- returns:
    - `AirbyteSourceInteraction` representing the source interaction

Function is used for obtaining source interaction with the asset. A new AirbyteSourceInteraction is created for *null* value of the ticket.

---

#### `getTargetInteraction(...)`
Creates an object, which represents an interaction with the target in the Airbyte connector.
- parameters: 
    - `CustomFlightAssetDescriptor asset`
- returns:
    - `AirbyteTargetInteraction`

---

#### `performAction(...)`
Performs the specified action on the connector with specified configuration and returns the response from the action.
- parameters: 
    - `String action`
    - `ConnectionActionConfiguration config`
- returns:
    - `ConnectionActionResponse`
- exceptions:
    - `Exception` 

## AirbyteConnectorFactory

***AirbyteConnectorFactory*** implements the `ConnectorFactory` interface and is part of the `pl.poznan.put.connect.airbyte` package. 

It represents a factory and is used to create [AirbyteConnector](#airbyteconnector) instances.

### Class variables
 `static final Map<String, String> typesImageMap` maps data source type names to Docker image names for the Airbyte connectors.

### Methods

#### `getInstance()`

A static method that ensures only one instance of ***AirbyteConnectorFactory*** is used, following the singleton pattern.

---

#### `createConnector(...)`

An overridden method from the `ConnectorFactory` interface. If the data source type name is not supported, it throws an *UnsupportedOperationException*.
- parameters:
    - `String datasourceTypeName`
    - `ConnectionProperties properties`
- returns:
    - `AirbyteConnector`
- exceptions:
    - UnsupportedOperationException

---

#### `getDatasourceTypes()`

An overridden method from the `ConnectorFactory` interface. 
- returns:
    - `CustomFlightDatasourceTypes`


## AirbyteDatasourceType

Implements `CustomFlightDatasourceType` class and is part of the `pl.poznan.put.connect.airbyte` package. 

It represents a data source type for Airbyte.

Used to define a data source type for Airbyte. It sets the necessary attributes and properties for the data source types. The actual interaction with the Airbyte processes is handled by the [AirbyteProcessRunner](#airbyteprocessrunner) class.


### Constructors

- `AirbyteDatasourceType(String datasourceName, String imageName)`

Sets default values for:
- name, label, and description based on the provided data source name
- *allowedAsSource* to true
- *allowedAsTarget* to false
- *status* to "PENDING"
- initializes *properties* with `CustomFlightDatasourceTypeProperties`

It then calls `AirbyteProcessRunner.spec(imageName)` to get the connection specification and sets the connection properties based on the specification. It also defines the source interaction properties.

## AirbyteFlightProducer

Implements `ConnectorFlightProducer` class and is part of the `pl.poznan.put.connect.airbyte` package. 

It represents a flight producer for Airbyte connectors.

### Methods

#### `getConnectorFactory()`
The actual creation of the connectors is handled by the [AirbyteConnectorFactory](#airbyteconnectorfactory), following the singleton pattern.
- returns:
    - `AirbyteConnectorFactory`.


## AirbyteImages

***AirbyteImages***, is part of the `pl.poznan.put.connect.airbyte` package. 

It provides a way to get the image name for a given image type.

### Methods

#### `getImageName(...)`
A static method takes an image type as a parameter and returns the corresponding image name. 

Currently, it only supports `postgres` as a valid image type (visible in constants). For an unsupported image type provided, throws a *RuntimeException*.

- parameters: 
    - `String image`
- returns:
    - `String`
- exeptions:
    - `RuntimeException`


## AirbyteProcessRunner
Used to call to run processes in Airbyte. The actual running of the processes is handled by the [PodProcess](#srcmainjavaplpoznanputconnectairbytepods) class (pods subdirectory).

### Methods

#### `spec(...)`
Runs the **"spec"** action on the given image. It creates a new [PodProcess](#srcmainjavaplpoznanputconnectairbytepods) with the "spec" command and the given image name, processes the output, and returns a *ConnectionActionResponse* with the processed output.

- parameters: 
    - `String imageName`
- returns
    - `ConnectionActionResponse` with the processed output from the "spec" action.
- exceptions
    - `IOException` (catches and logs)

---

#### `check(...)`
Runs the **"check"** action on the given image with the given configuration. It creates a new [PodProcess](#srcmainjavaplpoznanputconnectairbytepods) with the "check" command, the given image name, and the given configuration and returns a *ConnectionActionResponse* with the processed output.

- parameters: 
    - `String imageName`
    - `String config`
- returns
    - `ConnectionActionResponse`
- exceptions
    - `IOException` (catches and logs)

---

#### `discover(...)`
Runs the **"discover"** action on the given image with the given configuration. It creates a new [PodProcess](#srcmainjavaplpoznanputconnectairbytepods) with the "discover" command, the given image name, and the given configuration and returns a list of descriptors.

- parameters: 
    - `String imageName`
    - `String config`
- returns:
    - `List<CustomFlightAssetDescriptor>`
- exceptions
    - `IOException` (catches and logs)

---

#### `read(...)`
Runs the **"read"** action on the given image with the given configuration. It creates a new [PodProcess](#srcmainjavaplpoznanputconnectairbytepods) with the "read" command, the given image name, given configuration and catalog, and returns the pod process used.

- parameters: 
    - `String imageName`
    - `String config`
    - `String catalog`
- returns:
    - `Process`
- exceptions
    - `IOException` (catches and logs)

## AirbyteSourceInteraction

Implements `RowBasedSourceInteraction` class and is part of the `pl.poznan.put.connect.airbyte` package. 

This class is used to represent an interaction with a source in the Airbyte connector and provides methods to get records from the source.

### Constructors

- `AirbyteSourceInteraction( AirbyteConnector connector, CustomFlightAssetDescriptor asset, List<CustomFlightAssetDescriptor> descriptors)`

This constructor initializes the `AirbyteSourceInteraction` with the given connector, asset, and descriptors.

- `AirbyteSourceInteraction( AirbyteConnector connector, CustomFlightAssetDescriptor asset, Process podProcess)`

This constructor initializes the `AirbyteSourceInteraction` with the given connector, asset, and process. Additionaly, it sets up the `reader` to read the output from the process.

### Methods

#### `getRecord()`
Processes the read action using the [AirbyteStreamProcessor](#airbytestreamprocessor).[processReadAction](#processreadaction) method and returns the resulting *Record*.
- returns:
    - `Record`
- exeptions:
    - `IOException`
    - `RuntimeException`
---

#### `getTickets()`
Creates a new `Ticket` and returns it wrapped in list.
- returns:
    - `List<Ticket>`
- exeptions:

---

#### `getFields()`
The method first checks if the fields of the current asset are `null`. If they are, it retrieves the *stream_name* and *namespace* from the asset's interaction properties. Than, it searches through a list of `CustomFlightAssetDescriptor`, looking for a first matchibg descriptor and creates a new `DiscoveredAssetDetails` object, sets it as the details of the asset, and adds the `configuredCatalog` from the descriptor to the asset's details.
- returns:
    - `List<CustomFlightAssetField>`
- exception:
    - `IllegalArgumentException`

---

#### `close()`
Closes *AirbyteSourceInteraction*.
- exeptions:
    - `Exception`


## AirbyteStreamProcessor

Used to process custom, discover and read actions in Airbyte. 

Processing is done by reading lines from a `BufferedReader`, parsing them into [AirbyteMessage](#srcmainjavaplpoznanputconnectairbytemodelsmessages) (subdirectory models) objects, and handling them based on their type.

### Methods

#### `processCustomAction(...)`
This method processes a custom action by processing data from the *BufferedReader* and handling them based on their type. It returns a *HashMap* with the processed response.
- parameters: 
    - `BufferedReader bufferedReader`
- returns:
    - `HashMap<String,Object>`
- exceptions: 
    - `IOException`

---

#### `processDiscoverAction(...)`
This method processes a discover action by processing data from the *BufferedReader* and handling them based on their type. It returns a list of `CustomFlightAssetDescriptor` objects representing the discovered assets.
- parameters: 
    - `BufferedReader bufferedReader`
- returns:
    - `List<CustomFlightAssetDescriptor>`
- exceptions: 
    - `IOException`

---

#### `processReadAction()`
The method starts by reading `BufferedReader`. For each line read, the method logs a debug message indicating the content of the line and, using the `gson` library, parses the line into an `AirbyteMessage` object.
Based on message's type it creates a new `Record` object and populates it with values from the message and returning the object.

If the method finishes reading all lines in the buffer without returning a `Record`, it returns *null*. This could indicate that no record was found in the stream, or that all records encountered were of type **TRACE** with trace type **ERROR**, or of type **CONNECTION_STATUS** with status **FAILED**.

- parameters: 
    - `BufferedReader bufferedReader`
    - `List<CustomFlightAssetField> fields`
- returns:
    - `Record`
- exeptions:
    - `IOException`

## AirbyteTargetInteraction

Implements `RowBasedTargetInteraction` class and is part of the `pl.poznan.put.connect.airbyte` package. 

Used to represent an interaction with a target in the Airbyte connector. It provides methods to put records into the target and perform setup and wrap-up actions. However, these methods are currently empty and do not perform any action.

### Constructors

- `AirbyteTargetInteraction(AirbyteConnector connector, CustomFlightAssetDescriptor asset)`

This constructor initializes the `AirbyteTargetInteraction` with the given connector and asset.

### Methods

#### `putRecord(...)`
- parameters: 
    - `Record record`

---

#### `close()`
This method is intended to close the interaction. 

---

#### `putSetup()`
Currently, it returns `null`.
- returns:
    - `CustomFlightAssetDescriptor`

---

#### `putWrapup()`
 Currently, it returns `null`.
- returns:
    - `CustomFlightAssetDescriptor`
