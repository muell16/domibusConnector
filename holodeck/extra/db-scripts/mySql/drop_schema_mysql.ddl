
-- backend

 alter table Payload 
        drop 
        foreign key FK3454796EA6FCC7E9;

    drop table if exists Message;

    drop table if exists Payload;

    drop table if exists SEQUENCE_TABLE;
    
-- ebms3
    
    alter table Receipt_Tracking 
        drop 
        foreign key FK78EC851E85558B8E;

    alter table Received_UserMsg_Attachments 
        drop 
        foreign key FK84D1352918A1EF0F;

    alter table Received_UserMsg_Attachments 
        drop 
        foreign key FK84D135295885AD52;

    alter table Sync_Responses_Attachments 
        drop 
        foreign key FK6084EC7F10131110;

    alter table Sync_Responses_Attachments 
        drop 
        foreign key FK6084EC7F5885AD52;

    alter table UserMsg_Pull_Attachments 
        drop 
        foreign key FK4E9D823F5885AD52;

    alter table UserMsg_Pull_Attachments 
        drop 
        foreign key FK4E9D823FDAD13FDA;

    alter table UserMsg_Push_Attachments 
        drop 
        foreign key FKC1A1EB945885AD52;

    alter table UserMsg_Push_Attachments 
        drop 
        foreign key FKC1A1EB94DB3213BA;

--    drop table if exists Attachments;

    drop table if exists Msg_Callback;

    drop table if exists Receipt_Tracking;

    drop table if exists Receipts;

    drop table if exists Received_UserMsg;

    drop table if exists Received_UserMsg_Attachments;

    drop table if exists Sync_Responses;

    drop table if exists Sync_Responses_Attachments;

    drop table if exists UserMsg_Pull;

    drop table if exists UserMsg_Pull_Attachments;

    drop table if exists UserMsg_Push;

    drop table if exists UserMsg_Push_Attachments;
    
 -- logging
    
    drop table if exists LoggerEvent;

    drop table if exists LoggerMessage;
    
-- reliability
    
    alter table Business_Responses_Attachments 
        drop 
        foreign key FK8BF9E7E45BCEE911;

    alter table Business_Responses_Attachments 
        drop 
        foreign key FK8BF9E7E45885AD52;

    alter table Ordered_Msg_Attachments 
        drop 
        foreign key FKFD00A3C0112E73C;

    alter table Ordered_Msg_Attachments 
        drop 
        foreign key FKFD00A3C05885AD52;

    alter table Retransmit_Msg_Attachments 
        drop 
        foreign key FK63ABC176768643B2;

    alter table Retransmit_Msg_Attachments 
        drop 
        foreign key FK63ABC1765885AD52;

    alter table Sender_Groups 
        drop 
        foreign key FKA6DEE31E894F77F3;

    drop table if exists Acks;

    drop table if exists Attachments;

    drop table if exists Business_Responses;

    drop table if exists Business_Responses_Attachments;

    drop table if exists Delivered_Ranges;

    drop table if exists Ordered_Msg;

    drop table if exists Ordered_Msg_Attachments;

    drop table if exists Received_Ranges;

    drop table if exists Receiver_Groups;

    drop table if exists Reliability;

    drop table if exists Retransmit_Msg;

    drop table if exists Retransmit_Msg_Attachments;

    drop table if exists Sender_Groups;