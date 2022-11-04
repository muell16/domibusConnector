--liquibase formatted sql

--changeset StephanSpindler:initialBackendData_4
----------------------- Values for DOMIBUS_CONNECTOR_BACKEND_INFO ------------------------------

DELETE FROM DOMIBUS_CONNECTOR_BACKEND_INFO;

INSERT INTO DOMIBUS_CONNECTOR_BACKEND_INFO (ID, BACKEND_NAME, BACKEND_KEY_ALIAS, BACKEND_KEY_PASS, BACKEND_SERVICE_TYPE, BACKEND_DESCRIPTION, BACKEND_PUSH_ADDRESS, BACKEND_ENABLED, BACKEND_DEFAULT)
VALUES (1, 'cn=bob', 'bob', '', '', 'a bob backend - no push', null, true, true);

INSERT INTO DOMIBUS_CONNECTOR_BACKEND_INFO (ID, BACKEND_NAME, BACKEND_KEY_ALIAS, BACKEND_KEY_PASS, BACKEND_SERVICE_TYPE, BACKEND_DESCRIPTION, BACKEND_PUSH_ADDRESS, BACKEND_ENABLED, BACKEND_DEFAULT)
VALUES (2, 'cn=alice', 'alice', '', '', 'a alice backend with push', 'http://localhost:8012/services/domibusConnectorDeliveryWebservice', true, false);

INSERT INTO DOMIBUS_CONNECTOR_BACKEND_INFO (ID, BACKEND_NAME, BACKEND_KEY_ALIAS, BACKEND_KEY_PASS, BACKEND_SERVICE_TYPE, BACKEND_DESCRIPTION, BACKEND_PUSH_ADDRESS, BACKEND_ENABLED, BACKEND_DEFAULT)
VALUES (3, 'cn=exec_ri', 'exec_ri', '', '', 'a alice backend with push', 'http://localhost:8013/case-service/services/connectorClientBackendDelivery', true, false);



DELETE FROM DOMIBUS_CONNECTOR_PARTY;

INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1001, 1, 'gw01', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'initiator');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1002, 1, 'gw02', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'initiator');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1003, 1, 'gw03', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'initiator');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1004, 1, 'gw04', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'initiator');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1005, 1, 'gw05', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'initiator');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1006, 1, 'gw06', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'initiator');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1007, 1, 'gw07', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'initiator');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1008, 1, 'gw08', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'initiator');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1009, 1, 'gw09', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'initiator');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1010, 1, 'gw10', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'initiator');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1011, 1, 'gw01', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'responder');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1012, 1, 'gw02', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'responder');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1013, 1, 'gw03', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'responder');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1014, 1, 'gw04', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'responder');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1015, 1, 'gw05', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'responder');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1016, 1, 'gw06', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'responder');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1017, 1, 'gw07', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'responder');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1018, 1, 'gw08', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'responder');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1019, 1, 'gw09', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'responder');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES (1020, 1, 'gw10', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:unregistered', 'responder');

-- UPDATE DOMIBUS_CONNECTOR_SEQ_STORE SET SEQ_VALUE=1021 WHERE SEQ_NAME='';



DELETE FROM DOMIBUS_CONNECTOR_SERVICE;

INSERT INTO DOMIBUS_CONNECTOR_SERVICE (ID, FK_PMODE_SET, SERVICE, SERVICE_TYPE) VALUES (1001, 1, 'Connector-TEST', 'urn:e-codex:services:');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE (ID, FK_PMODE_SET, SERVICE, SERVICE_TYPE) VALUES (1002, 1, 'System-TEST', 'urn:e-codex:services:');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE (ID, FK_PMODE_SET, SERVICE, SERVICE_TYPE) VALUES (1003, 1, 'Service1', 'urn:e-codex:services:');

DELETE FROM DOMIBUS_CONNECTOR_ACTION;


INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, "ACTION") VALUES (1001, 1, 'SubmissionAcceptanceRejection');
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, "ACTION") VALUES (1002, 1, 'RelayREMMDAcceptanceRejection');
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, "ACTION") VALUES (1003, 1, 'DeliveryNonDeliveryToRecipient');
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, "ACTION") VALUES (1004, 1, 'RetrievalNonRetrievalToRecipient');
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, "ACTION") VALUES (1005, 1, 'System-TEST');
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, "ACTION") VALUES (1006, 1, 'Connector-TEST');
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, "ACTION") VALUES (1007, 1, 'Action1');
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, "ACTION") VALUES (1008, 1, 'Action2');

UPDATE DOMIBUS_CONNECTOR_USER_PWD SET INITIAL_PWD=0 WHERE ID=1;
UPDATE DOMIBUS_CONNECTOR_USER_PWD SET INITIAL_PWD=0 WHERE ID=2;