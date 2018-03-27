--liquibase formatted sql
--
--changeset initialPersistenceTestdata_1
----------------------- Values for DOMIBUS_CONNECTOR_BACKEND_INFO ------------------------------

INSERT INTO DOMIBUS_CONNECTOR_BACKEND_INFO (ID, BACKEND_NAME, BACKEND_KEY_ALIAS, BACKEND_KEY_PASS, BACKEND_SERVICE_TYPE, BACKEND_DESCRIPTION, BACKEND_PUSH_ADDRESS)
VALUES (1, 'bob', 'bob', '', 'EPO', 'a epo backend', null);

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('domibus-blue', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');

INSERT INTO DOMIBUS_CONNECTOR_PARTY (PARTY_ID, ROLE, PARTY_ID_TYPE) VALUES
  ('domibus-red', 'GW', 'urn:oasis:names:tc:ebcore:partyid-type:iso3166-1');


