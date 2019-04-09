select 
  myear as year,
  mmonth as month,
  party,
  service, 
  received, 
  sent
from 
  (select 
    i.service as service, 
    count(m.delivered_nat) as received, 
    count(m.delivered_gw) as sent, 
    extract(YEAR FROM case when m.delivered_nat is null then m.delivered_gw else delivered_nat end) as myear, 
    extract(MONTH from  case when m.delivered_nat is null then m.delivered_gw else delivered_nat end) as mmonth,
    case when m.delivered_nat is null then i.to_party_id else i.from_party_id end as party
  from 
    DOMIBUS_CONNECTOR_MESSAGE m
      join DOMIBUS_CONNECTOR_MESSAGE_INFO i
      on i.message_id=m.id 
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