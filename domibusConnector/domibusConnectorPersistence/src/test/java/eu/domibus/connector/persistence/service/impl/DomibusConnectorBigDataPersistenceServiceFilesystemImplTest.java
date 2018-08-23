package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.domain.testutil.DomainEntityCreator;
import eu.domibus.connector.persistence.spring.DomibusConnectorFilesystemPersistenceProperties;
import eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceFilesystemImpl.FileBasedDomibusConnectorBigDataReference;
import eu.domibus.connector.testutil.assertj.DomibusByteArrayAssert;
import org.apache.poi.util.IOUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.util.FileSystemUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;


public class DomibusConnectorBigDataPersistenceServiceFilesystemImplTest {

    private DomibusConnectorBigDataPersistenceServiceFilesystemImpl filesystemImpl;

    private static final byte[] input1 = "Hallo Welt, ich bin ein Testtext".getBytes();

    private static File testStorageLocation;

    @BeforeClass
    public static void initTests() throws IOException {
        testStorageLocation = new File("./target/tests/" + DomibusConnectorBigDataPersistenceServiceFilesystemImplTest.class.getSimpleName() +  "/fsstorage/");
        FileSystemUtils.deleteRecursively(testStorageLocation);
        testStorageLocation.mkdirs();

        File src = new File("./target/test-classes/testdata/fsstorage/");

        //copy testdata to testfolder
        FileSystemUtils.copyRecursively(src, testStorageLocation);

    }

    @Before
    public void setUp() {


        DomibusConnectorFilesystemPersistenceProperties fsProps = new DomibusConnectorFilesystemPersistenceProperties();
        fsProps.setStoragePath(Paths.get(testStorageLocation.getAbsolutePath()));

        filesystemImpl = new DomibusConnectorBigDataPersistenceServiceFilesystemImpl();
        filesystemImpl.setFilesystemPersistenceProperties(fsProps);
    }

    @Test
    public void getReadableDataSource() throws IOException {
        FileBasedDomibusConnectorBigDataReference fsRef = new FileBasedDomibusConnectorBigDataReference();
        fsRef.setStorageIdReference("testmsg1" + File.separator + "file1");
        fsRef.setName("file1");
        fsRef.setMimetype("text");
        //fsRef.setEncryptionKey();

        DomibusConnectorBigDataReference readableDataSource = filesystemImpl.getReadableDataSource(fsRef);

        assertThat(readableDataSource).isNotNull();
        assertThat(readableDataSource.getInputStream()).isNotNull();

        byte[] bytes = IOUtils.toByteArray(readableDataSource.getInputStream());
        DomibusByteArrayAssert.assertThat(bytes).containsUTF8String("Hallo Welt!");


    }

    @Test
    public void createDomibusConnectorBigDataReference() {
        DomibusConnectorBigDataReference domibusConnectorBigDataReference = filesystemImpl.createDomibusConnectorBigDataReference(new ByteArrayInputStream(input1), "msg1", "file1", "text/utf-8");
        assertThat(domibusConnectorBigDataReference).isNotNull();

        String storageIdReference = domibusConnectorBigDataReference.getStorageIdReference();
        assertThat(storageIdReference).isNotNull();

        //TODO: assert file exists in FS
        File f = new File(testStorageLocation + File.separator + storageIdReference);
        assertThat(f.exists()).as(String.format("A file <%s> should exist", f.getAbsolutePath())).isTrue();

    }

    @Test
    public void deleteDomibusConnectorBigDataReference() {
        String msgId = "testmsg2";
        FileBasedDomibusConnectorBigDataReference fsRef = new FileBasedDomibusConnectorBigDataReference();
        String storageRef = msgId + File.separator + "file1";
        fsRef.setStorageIdReference(storageRef);

//        DomibusConnectorMessage msg = DomainEntityCreator.createMessage();
//        msg.setConnectorMessageId(msgId);
        filesystemImpl.deleteDomibusConnectorBigDataReference(fsRef);

//        assertThat(fileExists(storageRef)).isFalse();
        String filePath = testStorageLocation.getAbsolutePath() + File.separator + msgId + File.separator + "file1";
        File f = new File(filePath);
        assertThat(f.exists()).as("file " + filePath + " should be deleted").isFalse();

    }

//    private boolean fileExists(String storageRef) {
//        File f = new File(testStorageLocation + File.separator + storageRef);
//        return f.exists();
//    }

}