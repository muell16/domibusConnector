-- backend

    drop table Message cascade constraints;

    drop table Payload cascade constraints;

    drop table SEQUENCE_TABLE cascade constraints;

-- ebms3

	drop table Attachments cascade constraints;

    drop table Msg_Callback cascade constraints;

    drop table Receipt_Tracking cascade constraints;

    drop table Receipts cascade constraints;

    drop table Received_UserMsg cascade constraints;

    drop table Received_UserMsg_Attachments cascade constraints;

    drop table Sync_Responses cascade constraints;

    drop table Sync_Responses_Attachments cascade constraints;

    drop table UserMsg_Pull cascade constraints;

    drop table UserMsg_Pull_Attachments cascade constraints;

    drop table UserMsg_Push cascade constraints;

    drop table UserMsg_Push_Attachments cascade constraints;

-- logging

	drop table LoggerEvent cascade constraints;

    drop table LoggerMessage cascade constraints;

-- reliability
    
    drop table Acks cascade constraints;
    
    drop table Business_Responses cascade constraints;

    drop table Business_Responses_Attachments cascade constraints;

    drop table Delivered_Ranges cascade constraints;

    drop table Ordered_Msg cascade constraints;

    drop table Ordered_Msg_Attachments cascade constraints;

    drop table Received_Ranges cascade constraints;

    drop table Receiver_Groups cascade constraints;

    drop table Reliability cascade constraints;

    drop table Retransmit_Msg cascade constraints;

    drop table Retransmit_Msg_Attachments cascade constraints;

    drop table Sender_Groups cascade constraints;