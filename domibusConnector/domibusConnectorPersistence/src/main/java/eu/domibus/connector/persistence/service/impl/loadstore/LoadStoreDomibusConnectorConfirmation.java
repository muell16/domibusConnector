/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.persistence.service.impl.loadstore;

import eu.domibus.connector.domain.model.DomibusConnectorMessageConfirmation;
import eu.domibus.connector.persistence.model.PDomibusConnectorMsgCont;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
@Component
public class LoadStoreDomibusConnectorConfirmation implements LoadContentHandler, StoreContentHandler {

    @Override
    public Object convertFrom(PDomibusConnectorMsgCont content) {
        ObjectInputStream inputStream;
        try {
            byte[] byteContent = content.getContent();
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteContent);
            inputStream = new ObjectInputStream(byteInputStream);
            DomibusConnectorMessageConfirmation confirmation = (DomibusConnectorMessageConfirmation) inputStream.readObject();                                    
            return confirmation;            
        } catch (IOException ex) {
            //TODO:!
            throw new RuntimeException("IOException during read!", ex);
        } catch (ClassNotFoundException ex) {
            //TODO:!
            throw new RuntimeException("persisted object class not found!", ex);
        }
    }
    
    @Override
    public boolean canConvertFrom(PDomibusConnectorMsgCont content) {
        return StoreType.MESSAGE_CONFIRMATION.getDbString().equals(content.getContentType());
    }

    @Override
    public PDomibusConnectorMsgCont convertToConnectorMsgCont(Object obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}
