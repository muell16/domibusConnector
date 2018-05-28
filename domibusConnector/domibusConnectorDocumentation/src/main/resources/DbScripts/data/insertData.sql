----------------------- Values for DOMIBUS_CONNECTOR_SEQ_STORE ------------------------------


INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_MESSAGES.ID', 0);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_EVIDENCES.ID', 0);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_MESSAGE_INFO.ID', 0);
INSERT INTO DOMIBUS_CONNECTOR_SEQ_STORE VALUES ('DOMIBUS_CONNECTOR_MSG_ERROR.ID', 0);

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


-- insert default user (admin) with default password (admin)
INSERT INTO DOMIBUS_WEBADMIN_USER (USERNAME, "PASSWORD", SALT, ROLE) VALUES ('admin', '2bf5e637d0d82a75ca43e3be85df2c23febffc0cc221f5e010937005df478a19b5eaab59fe7e4e97f6b43ba648c169effd432e19817f386987d058c239236306', '5b424031616564356639', 'admin');
