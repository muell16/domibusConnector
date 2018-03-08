package eu.domibus.connector.controller.test.util;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

@Ignore
public class ZipTests {

    @Test
    public void testZip() throws IOException {




        Resource asicResource = new ClassPathResource("/testmessages/msg2/container-signed-xades-baseline-b.asics");
//        Resource asicResource = new ClassPathResource("/testmessages/msg2/asic-s.zip");
        InputStream inputStream = asicResource.getInputStream();
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry nextEntry = zipInputStream.getNextEntry();
        while (nextEntry != null)
        {
            System.out.println("nextEntryName: " + nextEntry.getName());
            nextEntry = zipInputStream.getNextEntry();
        }


    }


    @Test
    public void testZipFile() throws IOException {

        //Resource r = new ClassPathResource("testmessages/msg2/container-signed-xades-baseline-b.asics");
        Resource r = new ClassPathResource("testmessages/msg2/asic-s.zip");
        File file = r.getFile();

        System.out.println(file.getAbsolutePath());

        ZipFile zipFile = new ZipFile(file.getAbsolutePath());
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        ZipEntry ze = entries.nextElement();
        while (ze != null) {
            System.out.println("nextEntryName: " + ze.getName());
            ze = entries.nextElement();
        }

    }
}
