alter table ECODEX_ACTION add PDF_REQUIRED number (1,0) default 1 not null;

update ECODEX_ACTION set pdf_required=0 where ecdx_action in ('SubmissionAcceptanceRejection','RelayREMMDAcceptanceRejection','RelayREMMDFailure','DeliveryNonDeliveryToRecipient','RetrievalNonRetrievalToRecipient');

commit;