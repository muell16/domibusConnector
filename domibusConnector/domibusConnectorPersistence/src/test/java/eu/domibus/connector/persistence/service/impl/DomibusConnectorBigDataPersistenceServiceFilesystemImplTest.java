package eu.domibus.connector.persistence.service.impl;

import eu.domibus.connector.domain.model.DomibusConnectorBigDataReference;
import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.domibus.connector.persistence.service.impl.DomibusConnectorBigDataPersistenceServiceFilesystemImpl.FileBasedDomibusConnectorBigDataReference;
import eu.domibus.connector.persistence.spring.DomibusConnectorFilesystemPersistenceProperties;
import eu.domibus.connector.testutil.assertj.DomibusByteArrayAssert;
import org.apache.poi.util.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileSystemUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class DomibusConnectorBigDataPersistenceServiceFilesystemImplTest {

    private DomibusConnectorBigDataPersistenceServiceFilesystemImpl filesystemImpl;

    private static final byte[] input1 = "Hallo Welt, ich bin ein Testtext".getBytes();

//    private static File testStorageLocation;

    private File testStorageLocation;

//    @BeforeClass
//    public static void initTests() throws IOException {
//        testStorageLocation = new File("./target/tests/" + DomibusConnectorBigDataPersistenceServiceFilesystemImplTest.class.getSimpleName() +  "/fsstorage/");
//        FileSystemUtils.deleteRecursively(testStorageLocation);
//        testStorageLocation.mkdirs();
//
//        File src = new File("./target/test-classes/testdata/fsstorage/");
//
//        //copy testdata to testfolder
//        FileSystemUtils.copyRecursively(src, testStorageLocation);
//    }

    @BeforeEach
    public void setUp(TestInfo testInfo) throws IOException {

        String methodName = testInfo.getTestMethod().get().getName();

        testStorageLocation = new File("./target/tests/"
                + DomibusConnectorBigDataPersistenceServiceFilesystemImplTest.class.getSimpleName() + "/"
                + methodName
                +  "/fsstorage/");

        FileSystemUtils.deleteRecursively(testStorageLocation);
        testStorageLocation.mkdirs();

        File src = new File("./target/test-classes/testdata/fsstorage/");

        //copy testdata to testfolder
        FileSystemUtils.copyRecursively(src, testStorageLocation);


        DomibusConnectorFilesystemPersistenceProperties fsProps = new DomibusConnectorFilesystemPersistenceProperties();
        fsProps.setStoragePath(Paths.get(testStorageLocation.getAbsolutePath()));
        fsProps.setEncryptionActive(true);

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

//    @Test
//    public void getReadableDataSource() throws IOException {
//        FileBasedDomibusConnectorBigDataReference fsRef = new FileBasedDomibusConnectorBigDataReference();
//        fsRef.setStorageIdReference("testmsg1" + File.separator + "file1");
//        fsRef.setName("file1");
//        fsRef.setMimetype("text");
//        //fsRef.setEncryptionKey();
//
//        DomibusConnectorBigDataReference readableDataSource = filesystemImpl.getReadableDataSource(fsRef);
//
//        assertThat(readableDataSource).isNotNull();
//        assertThat(readableDataSource.getInputStream()).isNotNull();
//
//        byte[] bytes = IOUtils.toByteArray(readableDataSource.getInputStream());
//        DomibusByteArrayAssert.assertThat(bytes).containsUTF8String("Hallo Welt!");
//    }

    @Test
    public void createDomibusConnectorBigDataReference() {
        DomibusConnectorBigDataReference domibusConnectorBigDataReference = filesystemImpl.createDomibusConnectorBigDataReference(new ByteArrayInputStream(input1), "msg1", "file1", "text/utf-8");
        assertThat(domibusConnectorBigDataReference).isNotNull();

        String storageIdReference = domibusConnectorBigDataReference.getStorageIdReference();
        assertThat(storageIdReference).isNotNull();

        //TODO: assert file exists in FS
        File f = new File(testStorageLocation + File.separator + storageIdReference);
        assertThat(f.exists()).as(String.format("A file <%s> should exist", f.getAbsolutePath())).isTrue();

        FileBasedDomibusConnectorBigDataReference fRef = (FileBasedDomibusConnectorBigDataReference) domibusConnectorBigDataReference;
        System.out.println("key: " + fRef.getEncryptionKey() + " iv: " + fRef.getInitVector() + " cipher-suite: " + fRef.getCipherSuite());
    }


    @Test
    public void readEncryptedBigDataReference() throws IOException {
        FileBasedDomibusConnectorBigDataReference fRef = new FileBasedDomibusConnectorBigDataReference();
        fRef.setEncryptionKey("AES#@#kC6lanKld+xuiVfarsZNLQ==");
        fRef.setInitVector("cO+U0ufVjzCheGnXYkfvXg==");
        fRef.setCipherSuite("AES/CBC/PKCS5Padding");
        fRef.setStorageIdReference("testmsg2/2de3f474-3f1a-42d7-ab7c-2151590c77f1");

        DomibusConnectorBigDataReference readableDataSource = filesystemImpl.getReadableDataSource(fRef);

        InputStream is = readableDataSource.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        assertThat(bytes).isEqualTo(input1);
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

    @Test
    public void testConvertSecretKeyToString() throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(random);

        SecretKeySpec sKey = new SecretKeySpec(Base64Utils.decodeFromString("sJ0kZ3pVBcG75ar4ADWwgg=="), "AES");

        String s = filesystemImpl.convertSecretKeyToString(sKey);

        assertThat(s).isEqualTo("AES#@#sJ0kZ3pVBcG75ar4ADWwgg==");
    }

//    @Test
//    public void testLoadFromKeyString() {
//
//    }

    @Test
    public void testGetAllAvailableReferences() {

        Map<DomibusConnectorMessage.DomibusConnectorMessageId, List<DomibusConnectorBigDataReference>> allAvailableReferences =
                filesystemImpl.getAllAvailableReferences();

        assertThat(allAvailableReferences).hasSize(2);
        assertThat(allAvailableReferences.get(new DomibusConnectorMessage.DomibusConnectorMessageId("testmsg2"))).hasSize(2);
    }

}