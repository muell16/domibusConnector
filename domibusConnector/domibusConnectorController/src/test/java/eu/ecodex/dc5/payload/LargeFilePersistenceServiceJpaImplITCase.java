package eu.ecodex.dc5.payload;

import eu.domibus.connector.persistence.dao.CommonPersistenceTest;
import eu.ecodex.dc5.payload.provider.LargeFilePersistenceServiceJpaImpl;
import org.junit.jupiter.api.Disabled;

import static org.assertj.core.api.Assertions.assertThat;

@CommonPersistenceTest
@Disabled
public class LargeFilePersistenceServiceJpaImplITCase extends CommonLargeFilePersistenceProviderITCase {


    @Override
    protected String getProviderName() {
        return LargeFilePersistenceServiceJpaImpl.PROVIDER_NAME;
    }
}