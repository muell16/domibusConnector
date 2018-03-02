-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

--liquibase formatted sql

--changeset StephanSpindler:initialBackendTestdata_1
----------------------- Values for DOMIBUS_CONNECTOR_SEQ_STORE ------------------------------


INSERT INTO DOMIBUS_CONNECTOR_BACKEND_INFO (ID, BACKEND_NAME, BACKEND_KEY_ALIAS, BACKEND_KEY_PASS, BACKEND_SERVICE_TYPE, BACKEND_DESCRIPTION, BACKEND_PUSH_ADDRESS)
    VALUES (1, 'bob', 'bob', null, 'pull', 'a description', null);

INSERT INTO DOMIBUS_CONNECTOR_BACKEND_INFO (ID, BACKEND_NAME, BACKEND_KEY_ALIAS, BACKEND_KEY_PASS, BACKEND_SERVICE_TYPE, BACKEND_DESCRIPTION, BACKEND_PUSH_ADDRESS)
    VALUES (2, 'alice', 'alice', null, 'push', 'a description', null);

INSERT INTO DOMIBUS_CONNECTOR_SERVICE VALUES ('ALICE', 'urn:e-codex:services:');

INSERT INTO DOMIBUS_CONNECTOR_SERVICE VALUES ('BOB', 'urn:e-codex:services:');

INSERT INTO DOMIBUS_CONNECTOR_BACK_2_S (DOMIBUS_CONNECTOR_BACKEND_ID, DOMIBUS_CONNECTOR_SERVICE_ID )
    VALUES (1, 'EPO');

INSERT INTO DOMIBUS_CONNECTOR_BACK_2_S (DOMIBUS_CONNECTOR_BACKEND_ID, DOMIBUS_CONNECTOR_SERVICE_ID )
    VALUES (1, 'BOB');

INSERT INTO DOMIBUS_CONNECTOR_BACK_2_S (DOMIBUS_CONNECTOR_BACKEND_ID, DOMIBUS_CONNECTOR_SERVICE_ID )
    VALUES (2, 'ALICE');
