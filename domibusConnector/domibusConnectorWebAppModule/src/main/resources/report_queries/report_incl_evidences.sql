select 
  myear as year,
  mmonth as month,
  party,
  service, 
  received+rec_e as received, 
  sent+sent_e as sent
from 
  (select 
     i.service as service,
     count(m.delivered_nat) as received, 
     count(m.delivered_gw) as sent, 
     sum(e.received) as rec_e, 
     sum(e.sent) as sent_e, 
     extract(YEAR FROM case when m.delivered_nat is null then m.delivered_gw else delivered_nat end) as myear, 
     extract(MONTH from  case when m.delivered_nat is null then m.delivered_gw else delivered_nat end) as mmonth,
     case when m.delivered_nat is null then i.to_party_id else i.from_party_id end as party
   from 
     domibus_connector_messages m 
       join domibus_connector_message_info i 
       on i.message_id=m.id 
       join (select 
               count(delivered_nat) as RECEIVED, 
               count(delivered_gw) as SENT ,
               message_id 
             from 
               domibus_connector_evidences 
             where 
               type not in ('SUBMISSION_ACCEPTANCE', 'SUBMISSION_REJECTION', 'RELAY_REMMD_FAILURE') 
             group by 
               message_id) e 
       on e.message_id=m.id 
   where
     m.delivered_nat between ? and ?
     or
     m.delivered_gw between ? and ?
   group by 
     i.service,
     (extract(YEAR FROM case when m.delivered_nat is null then m.delivered_gw else delivered_nat end)),
     (extract(MONTH from  case when m.delivered_nat is null then m.delivered_gw else delivered_nat end)),
     (case when m.delivered_nat is null then i.to_party_id else i.from_party_id end)
   ) x
order by 
  myear,
  mmonth,
  party,
  service