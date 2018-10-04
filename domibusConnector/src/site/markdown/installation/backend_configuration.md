# Backend Configuration or how to configure a connector client?

As you can see at the [architectural overview](../development/architecture_overview.html) the connector can communicate with different connector clients. The decision
which client the incoming message is routed to is made by the service ([ecodex message](../development/message_format.html).

The messages between the connectorClient and the connectorBackend are encrypted and signed. For this purpose the setup
needs cryptographic keys and certificates. The certificates are also used to authenticate the connectorClient and determine
which connectorClient is talking to the connector.


## Configure a additional backend

Configuring a additional backend contains two steps:

 * Creating the certificates and keys
 * Configuring the backend at the connector database
    
> Note: it is planned to support these steps over the planned extended admin ui    

### What do I need?

 * Access to the connector database.
 * A tool to generate key pairs and store them into a java key store (like [KeyStore Explorer](http://keystore-explorer.org/)
or the keytool which is distributed with the jdk)

### Configuring Certificates and Keys


#### Creating the key pair for the connector

###### 1) Open your connector backend key store or create a new one 
You can read [here](certificates.html) how the connector backend key
store path can be configured.

###### 2) Create a new RSA key pair which will be valid for 3 years:

    keytool -genkey -alias connector -keyalg RSA -keysize 2048 -dname CN=connector -validity 1095 \
    -keystore ${keystorePath} -storepass ${keystorePassword}

> Note: choose the keysize according to your organisational security guidelines
> also the validity time (1095 days are 3 years)

###### 3) export the public key certificate of this key pair and store it in the file named connector.cer

    keytool -export -alias connector -file connector.cer \
    -keystore ${keystorePath} -storepass ${keystorePassword}



### Adding a backend to the connector

#### Creating the keys for the backend

###### 1) Open your connectorClient key store or create a new one 

Consult the client lib documentation how the keystores are configured.

###### 2) Create a RSA key pair:


    keytool -genkey -alias ${connectorClientName} -keyalg RSA -keysize 2048 \
     -dname CN=${connectorClientName} -validity 1095 \
     -keystore ${connectorClientKeystorePath} -storepass ${keystorePassword}

>**Important!**: The provided connectorClientName at -dname CN=${connectorClientName} in the created key pair is later used by the connector to identify
>the backend.

###### 3) Export the public key as certificate, to import it later into the connector key store

    keytool -export -alias ${connectorClientName} -file ${connectorClientName}.cer \
    -keystore ${connectorClientKeystorePath} -storepass ${keystorePassword}

The exported certificate will later imported into the connector key store. So the connector will trust messages received from
this connector client.

###### 4) Establish the trust to the connector by importing the connector certificate

    keytool -importcert -alias connector -file connector.cer \
    -keystore ${connectorClientKeystorePath} -storepass ${keystorePassword}

In the following dialog answer with yes, to trust the imported certificate. So the connectorClient will trust the signed
messages received from the connector.

###### 5) Import the connectorClient certificate into the connector key store

    keytool -importcert -alias ${connectorClientName} -file ${connectorClientName}.cer \
    -keystore ${connectorKeystorePath} -storepass ${keystorePassword}


#### Resulting key stores

After you created and imported the keys into the keystores. Your keystores should look similar:

The client name is bob here:
After this your connector keystore should look like this (opened in keyStoreExplorer):

![alt text](../images/screenshot_connector_backend_keystore.png)

After this your connectorClient keystore should look like this:

![alt text](../images/screenshot_connector_client_keystore.png)


### Configuring the backend at the database

The backend information are stored in two tables:

**DOMIBUS_CONNECTOR_BACKEND_INFO**

Contains information about the connectorClient

| Name                     | Description |
|--------------------------|------------------------------------------------|
| ID                       | a unique technical id                          |
| BACKEND_NAME             | The name of the backend **this name must match the common name (CN)** field of the assigned certifcate | 
| BACKEND_KEY_ALIAS        | The key alias in the connector backend keystore for the certificate to use to encrypt messages for the connectorClient |
| BACKEND_KEY_PASS         | If the key is encrypted this column contains the password |
| BACKEND_SERVICE_TYPE     | Not used yet, will later define the type of the backend, is it push/pull, push/push over webservices, push/push over jms |
| BACKEND_ENABLED          | Is the backend enabled, must be true if the connector should send messages to this backend |
| BACKEND_DEFAULT          | The default backend will receive all messages which aren't delivered to another backend first |
| BACKEND_DESCRIPTION      | A description of the backend, can be used by the admin to store information |
| BACKEND_PUSH_ADDRESS     | If the backend is a push backend, push publishAddress must be defined here |

 
 
**DOMIBUS_CONNECTOR_BACK_2_S**

Contains the routing information, which backend will receive the message. The [routing](../development/backend_message_routing.md) 
decision is based on the service name.

| Name                     | Description |
|--------------------------|------------------------------------------------|
| DOMIBUS_CONNECTOR_SERVICE_ID | References the service                     |
| DOMIBUS_CONNECTOR_BACKEND_ID | References the backend                     |               

###### Add example connectorClient bob

The following SQL statement will add an connectorClient named bob with the key alias bob and expects that the common name of the certificate is bob.
Bob will also be the default backend!

```SQL
INSERT INTO domibus_connector_backend_info 
(ID, BACKEND_NAME, BACKEND_KEY_ALIAS, BACKEND_ENABLED, BACKEND_DEFAULT) 
VALUES ('11', 'bob', 'bob', TRUE, TRUE);
```

###### Adding an example connectorClient with specific service

The following SQL statement will add an connectorClient named alice.

```SQL
INSERT INTO domibus_connector_backend_info 
(ID, BACKEND_NAME, BACKEND_KEY_ALIAS, BACKEND_ENABLED, BACKEND_DEFAULT) 
VALUES ('12', 'alice', 'alice', TRUE, FALSE);
```

This statement will assign the epo messages to the connectorClient with the id 12 in the database. In this case this will be the connectorClient alice.

```SQL
INSERT INTO domibus_connector_back_2_s 
(DOMIBUS_CONNECTOR_SERVICE_ID, DOMIBUS_CONNECTOR_BACKEND_ID) 
VALUES ('EPO', '12');
```
