CREATE TABLE Message (
  idMessage int(32) NOT NULL,
  uid varchar(128) DEFAULT NULL,
  pmode varchar(128) DEFAULT NULL,
  date timestamp NULL DEFAULT NULL,
  directory varchar(1024) DEFAULT NULL,
  downloaded int(1) DEFAULT NULL,
  deleted int(1) DEFAULT NULL,
  PRIMARY KEY (idMessage));

CREATE TABLE Payload (
  idPayload int(32) NOT NULL,
  idMessage int(32) DEFAULT NULL,
  fileName varchar(256) DEFAULT NULL,
  PRIMARY KEY (idPayload),
  KEY FK_ID_MESSAGE_PAYLOAD (idMessage),
  CONSTRAINT FK_ID_MESSAGE_PAYLOAD FOREIGN KEY (idMessage) REFERENCES message (idMessage));