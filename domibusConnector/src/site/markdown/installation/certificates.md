# Certificate Configurations

TODO: insert link to architecture overview

TODO: insert certificate graphic

The connector uses multiple certificates for different purposes.

 * Signing and Encrypting SOAP messages between BackendClient and Connector
 * Establishing Transport Security (TLS) between Connector and BackendClient
 * Singing and Encrypting SOAP messages between Connector and Gateway
 * Establishing Transport Security (TLS) between Connector and Gateway
 * Validating the signature of the asic-s container
 * Signing the asic-s container
 * Signing the evidences
 * (Validating the evidences) which is not done yet
 
 
For each of this use cases you have to use at least one private-public key pair. As an example at 
the backend connection you have to generate for each client a private-public key pair.

TODO: asic-s container signing certificate source?





