
package eu.domibus.connector.domain.model.helper;

import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.model.DomibusConnectorMessageAttachment;
import eu.domibus.connector.domain.model.DomibusConnectorParty;
import eu.domibus.connector.domain.model.DomibusConnectorService;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 *
 *
 */
public class CopyHelperTest {

    @Test
    public void testCopyParty() {
        DomibusConnectorParty partyAT = DomainEntityCreator.createPartyAT();
        DomibusConnectorParty copiedParty = CopyHelper.copyParty(partyAT);
        
        assertThat(copiedParty).isEqualToComparingFieldByFieldRecursively(partyAT);
    }
    
    @Test
    public void testCopyParty_copyNull() {
        DomibusConnectorParty copiedParty = CopyHelper.copyParty(null);
        assertThat(copiedParty).isNull();
    }
    
    @Test
    public void testCopyAction() {
        DomibusConnectorAction action = DomainEntityCreator.createActionForm_A();
        DomibusConnectorAction copiedAction = CopyHelper.copyAction(action);
        
        assertThat(copiedAction).isEqualToComparingFieldByFieldRecursively(action);
    }
      
    @Test
    public void testCopyAction_copyNull() {       
        DomibusConnectorAction copiedAction = CopyHelper.copyAction(null);
        
        assertThat(copiedAction).isNull();
    }

    @Test
    public void testCopyService() {
        DomibusConnectorService service = DomainEntityCreator.createServiceEPO();
        DomibusConnectorService copiedService = CopyHelper.copyService(service);
        
        assertThat(copiedService).isEqualToComparingFieldByFieldRecursively(service);
    }
    
    @Test
    public void testCopyService_copyNull() {        
        DomibusConnectorService copiedService = CopyHelper.copyService(null);        
        assertThat(copiedService).isNull();
    }
    
    @Test
    public void testCopyMessageAttachment() {
        DomibusConnectorMessageAttachment attachment = DomainEntityCreator.createMessageAttachment();
        DomibusConnectorMessageAttachment copiedAttachment = CopyHelper.copyAttachment(attachment);
        
        assertThat(copiedAttachment).isEqualToComparingFieldByFieldRecursively(attachment);        
    }
    
     @Test
    public void testCopyMessageAttachment_copyNull() {        
        DomibusConnectorMessageAttachment copiedAttachment = CopyHelper.copyAttachment(null);        
        assertThat(copiedAttachment).isNull();
    }
    
    
}