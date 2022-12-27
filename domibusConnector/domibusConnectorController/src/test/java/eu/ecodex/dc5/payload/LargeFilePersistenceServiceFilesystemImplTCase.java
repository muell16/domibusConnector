package eu.ecodex.dc5.payload;

import eu.domibus.connector.persistence.dao.CommonPersistenceTest;
import eu.ecodex.dc5.payload.provider.LargeFilePersistenceServiceFilesystemImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Paths;

import static eu.domibus.connector.persistence.spring.PersistenceProfiles.STORAGE_FS_PROFILE_NAME;

@CommonPersistenceTest
@ActiveProfiles({"test", "db_h2", "connector", STORAGE_FS_PROFILE_NAME})
@Disabled
class LargeFilePersistenceServiceFilesystemImplTCase extends CommonLargeFilePersistenceProviderITCase {

    @BeforeAll
    public static void deleteFS() {
        FileSystemUtils.deleteRecursively(Paths.get("./target/ittest").toFile());
    }

    @Override
    protected String getProviderName() {
        return LargeFilePersistenceServiceFilesystemImpl.PROVIDER_NAME;
    }
}