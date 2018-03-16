package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorAction;
import eu.domibus.connector.domain.test.util.DomainEntityCreatorForPersistenceTests;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class ActionMapperTest {

    @Test
    @Ignore("not finished yet!")
    public void mapActionToDomain() throws Exception {
    }

    @Test
    public void testMapActionToPersistence() {
        DomibusConnectorAction createActionForm_A = DomainEntityCreatorForPersistenceTests.createActionForm_A();
        eu.domibus.connector.persistence.model.PDomibusConnectorAction action = ActionMapper.mapActionToPersistence(createActionForm_A);
        assertThat(action.isDocumentRequired()).as("pdf is required so must be true").isTrue();
        assertThat(action.getAction()).as("must match").isEqualTo("Form_A");
    }


}