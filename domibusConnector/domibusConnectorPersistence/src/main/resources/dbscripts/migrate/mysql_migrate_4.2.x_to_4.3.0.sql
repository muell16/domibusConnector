-- *********************************************************************
-- Update Database Script - from domibusConnector 4.2 to 4.3
-- *********************************************************************

SET FOREIGN_KEY_CHECKS=0;
-- requires MySQL >= 5.6.6, default since MySQL 8.0.2
SET EXPLICIT_DEFAULTS_FOR_TIMESTAMP = ON;
-- fixes UUID bug: https://bugs.mysql.com/bug.php?id=101820es UUID bug
-- also see: https://stackoverflow.com/questions/36296558/mysql-generating-duplicate-uuid
SET names utf8;

-- #################### 1/6 RENAME tables that need to be recreated ####################

rename table DOMIBUS_CONNECTOR_SERVICE to BKP_DC_SERVICE;

-- #################### 2/6 CREATE tables, structural changes ####################

-- delayed to 4.4
-- alter table DOMIBUS_CONNECTOR_ACTION drop column PDF_REQUIRED;

CREATE TABLE DOMIBUS_CONNECTOR_SERVICE
(
    ID           BIGINT       NOT NULL,
    FK_PMODE_SET BIGINT       NOT NULL,
    SERVICE      VARCHAR(255) NOT NULL,
    SERVICE_TYPE VARCHAR(255)
);

alter table DC_TRANSPORT_STEP change CONNECTOR_MESSAGE_ID bkp_cmid BIGINT NOT NULL;
alter table DC_TRANSPORT_STEP add           CONNECTOR_MESSAGE_ID VARCHAR(255);
alter table DC_TRANSPORT_STEP add           TRANSPORTED_MESSAGE LONGTEXT;
update      DC_TRANSPORT_STEP set           CONNECTOR_MESSAGE_ID=CONVERT(bkp_cmid, char);

DELIMITER $$
DROP PROCEDURE IF EXISTS DropFK $$
CREATE PROCEDURE DropFK (
    IN parm_table_name VARCHAR(100),
    IN parm_key_name VARCHAR(100)
)
BEGIN
    -- Verify the foreign key exists
    IF EXISTS (SELECT NULL FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND CONSTRAINT_NAME = parm_key_name) THEN
-- Turn the parameters into local variables
        set @ParmTable = parm_table_name ;
        set @ParmKey = parm_key_name ;
-- Create the full statement to execute
        set @StatementToExecute = concat('ALTER TABLE ',@ParmTable,' DROP FOREIGN KEY ',@ParmKey);
-- Prepare and execute the statement that was built
        prepare DynamicStatement from @StatementToExecute ;
        execute DynamicStatement ;
-- Cleanup the prepared statement
        deallocate prepare DynamicStatement ;
    END IF;
END $$
DELIMITER ;
-- if the foreign key  FK_MESSAGESTEP_MESSAGE exists in table DC_TRANSPORT_STEP, it will be dropped
-- FK_MESSAGESTEP_MESSAGE is very likely already deleted, because the connector does not work, if it still exists.
-- To prevent errors I had to wrap the deletion in a procedure
call DropFK('DC_TRANSPORT_STEP', 'FK_MESSAGESTEP_MESSAGE');

alter table DC_TRANSPORT_STEP drop column   bkp_cmid;

alter table DC_TRANSPORT_STEP modify       CONNECTOR_MESSAGE_ID VARCHAR(255) not null;


-- #################### 3/6 TRANSFER & UPDATE data ####################

update DOMIBUS_CONNECTOR_SEQ_STORE
set SEQ_NAME='DOMIBUS_CONNECTOR_MESSAGE.ID'
where SEQ_NAME = 'DOMIBUS_CONNECTOR_MESSAGES.ID';

update DOMIBUS_CONNECTOR_SEQ_STORE
set SEQ_NAME='DOMIBUS_CONNECTOR_EVIDENCE.ID'
where SEQ_NAME = 'DOMIBUS_CONNECTOR_EVIDENCES.ID';

insert into DOMIBUS_CONNECTOR_SERVICE select * from BKP_DC_SERVICE;

-- update timestamps

UPDATE DOMIBUS_CONNECTOR_EVIDENCE e
INNER JOIN DOMIBUS_CONNECTOR_MESSAGE m ON m.id = e.MESSAGE_ID
SET e.DELIVERED_GW=e.updated
WHERE e.DELIVERED_GW is null
  and e.TYPE in ('DELIVERY', 'RETRIVAL', 'RELAY_REMMD_ACCEPTANCE')
  and e.updated > str_to_date('01.06.2020', '%d.%m.%Y')
  and m.DIRECTION_SOURCE = 'GATEWAY'
  and m.DIRECTION_TARGET = 'BACKEND'
  and m.confirmed is not null;

UPDATE DOMIBUS_CONNECTOR_EVIDENCE e
INNER JOIN DOMIBUS_CONNECTOR_MESSAGE m ON m.id = e.MESSAGE_ID
SET e.DELIVERED_NAT=e.updated
WHERE e.DELIVERED_NAT is null
  and e.TYPE in ('DELIVERY', 'RETRIVAL', 'RELAY_REMMD_ACCEPTANCE')
  and e.updated > str_to_date('01.06.2020', '%d.%m.%Y')
  and m.DIRECTION_SOURCE = 'BACKEND'
  and m.DIRECTION_TARGET = 'GATEWAY'
  and m.confirmed is not null;

-- #################### 4/6 DELETE temporary tables, frees fk names ####################

drop table BKP_DC_SERVICE;

-- #################### 5/6 ADD the constraints ####################

ALTER TABLE DOMIBUS_CONNECTOR_SERVICE ADD CONSTRAINT PK_DC_SERVICE PRIMARY KEY (ID);
ALTER TABLE DOMIBUS_CONNECTOR_SERVICE ADD CONSTRAINT FK_SERVICE_PMODE_SET_ID FOREIGN KEY (FK_PMODE_SET) REFERENCES DC_PMODE_SET(ID);

-- #################### 6/6 UPDATE Version ####################

SET FOREIGN_KEY_CHECKS = 1;