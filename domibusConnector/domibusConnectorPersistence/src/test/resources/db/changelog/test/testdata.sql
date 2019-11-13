--liquibase formatted sql
--
--changeset connector:initialPersistenceTestdata_1
----------------------- Values for DOMIBUS_CONNECTOR_BACKEND_INFO ------------------------------

INSERT INTO DOMIBUS_CONNECTOR_BACKEND_INFO (ID, BACKEND_NAME, BACKEND_KEY_ALIAS, BACKEND_KEY_PASS, BACKEND_SERVICE_TYPE, BACKEND_DESCRIPTION, BACKEND_PUSH_ADDRESS)
VALUES (1, 'bob', 'bob', '', 'EPO', 'a epo backend', null);

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('domibus-blue', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('domibus-red', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');


--changeset connector:initialPersistenceTestdataPmodes_2
----------------------- Values for Services,Actions,Parties ------------------------------
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('action1', false);
INSERT INTO DOMIBUS_CONNECTOR_SERVICE VALUES ('service1', 'servicetype');

----------------------- Values for DOMIBUS_CONNECTOR_PARTY ------------------------------

INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('ARHS', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('AT', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('CTP', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('CZ', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('DE', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('EC', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('EE', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('ES', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('FR', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('GR', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('IT', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('ITIC', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('MT', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('NL', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');
INSERT INTO DOMIBUS_CONNECTOR_PARTY VALUES ('PL', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');

----------------------- Values for DOMIBUS_CONNECTOR_ACTION ------------------------------

INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_A', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_B', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_C', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_D', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_E', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_F', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Form_G', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('FreeFormLetter', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('FreeFormLetterIn', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('FreeFormLetterOut', 1);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('SubmissionAcceptanceRejection', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('RelayREMMDAcceptanceRejection', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('RelayREMMDFailure', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('DeliveryNonDeliveryToRecipient', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('RetrievalNonRetrievalToRecipient', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('Test_Form', 0);
INSERT INTO DOMIBUS_CONNECTOR_ACTION VALUES ('TEST-ping-connector', 0);

----------------------- Values for DOMIBUS_CONNECTOR_SERVICE ------------------------------

INSERT INTO DOMIBUS_CONNECTOR_SERVICE VALUES ('EPO', 'urn:e-codex:services:');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE VALUES ('SmallClaims', 'urn:e-codex:services:');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE VALUES ('Connector-TEST', 'urn:e-codex:services:');
INSERT INTO DOMIBUS_CONNECTOR_SERVICE VALUES ('TEST-ping-connector', 'urn:e-codex:services:');
