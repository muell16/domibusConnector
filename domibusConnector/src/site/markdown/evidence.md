# Evidence

The evidence in the connector context is the proof that a message has been delivered, rejected or not delivered.
See also: [PDF ETSI REM](http://www.etsi.org/deliver/etsi_ts/102600_102699/10264002/02.02.01_60/ts_10264002v020201p.pdf)


Following evidences are used by the connector/e-codex:

 * SUBMISSION_ACCEPTANCE: The initial business message has been successfully submitted to the Gateway
 * SUBMISSION_REJECTION: The connector was not able to submit the message to the Gateway, due the message is invalid, the asic-container could not be created, ...
 * TODO: add missing evidences...and link them to the sequence diagramm