--liquibase formatted sql
--
--changeset connector:initialPersistenceTestdata_1
----------------------- Values for DOMIBUS_CONNECTOR_BACKEND_INFO ------------------------------


INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES
  (101, 1, 'domibus-blue', 'defaultResponder', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1', 'responder');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET,  PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES
  (102, 1, 'domibus-red', 'defaultResponder', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1', 'responder');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET,  PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES
(103, 1, 'domibus-blue', 'defaultInitiator', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1', 'initiator');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET,  PARTY_ID, ROLE, PARTY_ID_TYPE, IDENTIFIER) VALUES
(104, 1, 'domibus-red', 'defaultInitiator', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1', 'initiator');


--changeset connector:initialPersistenceTestdataPmodes_2
----------------------- Values for Services,Actions,Parties ------------------------------

----------------------- Values for DOMIBUS_CONNECTOR_SERVICE ------------------------------
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (1, 1, 'action1', false);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (2, 1, 'action2', false);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (3, 1, 'action3', false);
INSERT INTO DOMIBUS_CONNECTOR_SERVICE (ID, FK_PMODE_SET, SERVICE, SERVICE_TYPE) VALUES (2, 1, 'service1', 'servicetype');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE (ID, FK_PMODE_SET, SERVICE, SERVICE_TYPE) VALUES (3, 1, 'service2', 'servicetype');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE (ID, FK_PMODE_SET, SERVICE, SERVICE_TYPE) VALUES (4, 1, 'service3', 'servicetype');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE (ID, FK_PMODE_SET, SERVICE, SERVICE_TYPE) VALUES (11, 1, 'EPO', 'urn:e-codex:services:');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE (ID, FK_PMODE_SET, SERVICE, SERVICE_TYPE) VALUES (12, 1, 'SmallClaims', 'urn:e-codex:services:');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE (ID, FK_PMODE_SET, SERVICE, SERVICE_TYPE) VALUES (13, 1, 'Connector-TEST', 'urn:e-codex:services:');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE (ID, FK_PMODE_SET, SERVICE, SERVICE_TYPE) VALUES (14, 1, 'TEST-ping-connector', 'urn:e-codex:services:');

----------------------- Values for DOMIBUS_CONNECTOR_PARTY ------------------------------

INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES (11, 1, 'ARHS', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES (12, 1, 'AT', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES (13, 1, 'CTP', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES (14, 1, 'CZ', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES (15, 1, 'DE', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES (16, 1, 'EC', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES (17, 1, 'EE', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES (18, 1, 'ES', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES (19, 1, 'FR', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES (20, 1, 'GR', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES (21, 1, 'IT', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES (22, 1, 'ITIC', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES (23, 1, 'MT', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES (24, 1, 'NL', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY (ID, FK_PMODE_SET, PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES (25, 1, 'PL', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
----------------------- Values for DOMIBUS_CONNECTOR_ACTION ------------------------------1,

INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (11, 1, 'Form_A', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (12, 1, 'Form_B', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (13, 1, 'Form_C', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (14, 1, 'Form_D', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (15, 1, 'Form_E', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (16, 1, 'Form_F', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (17, 1, 'Form_G', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (18, 1, 'FreeFormLetter', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (19, 1, 'FreeFormLetterIn', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (20, 1, 'FreeFormLetterOut', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (21, 1, 'SubmissionAcceptanceRejection', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (22, 1, 'RelayREMMDAcceptanceRejection', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (23, 1, 'RelayREMMDFailure', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (24, 1, 'DeliveryNonDeliveryToRecipient', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (25, 1, 'RetrievalNonRetrievalToRecipient', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (26, 1, 'Test_Form', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION (ID, FK_PMODE_SET, ACTION, PDF_REQUIRED) VALUES (27, 1, 'TEST-ping-connector', 0);



INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE (SEQ_NAME, SEQ_VALUE) VALUES ('DOMIBUS_CONNECTOR_MESSAGE.ID', 1000);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE (SEQ_NAME, SEQ_VALUE) VALUES ('DOMIBUS_CONNECTOR_MESSAGE_INFO.ID', 1000);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE (SEQ_NAME, SEQ_VALUE) VALUES ('DOMIBUS_CONNECTOR_MSG_CONT.ID', 1000);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE (SEQ_NAME, SEQ_VALUE) VALUES ('DOMIBUS_CONNECTOR_EVIDENCE.ID', 1000);

