package eu.ecodex.webadmin.dao;

import java.util.Date;
import java.util.List;

import eu.ecodex.webadmin.model.gateway.TbReceiptTracking;

public interface IGatewayMessageDao {

    public List<TbReceiptTracking> findMessagesByDate(Date fromDate, Date toDate);

}