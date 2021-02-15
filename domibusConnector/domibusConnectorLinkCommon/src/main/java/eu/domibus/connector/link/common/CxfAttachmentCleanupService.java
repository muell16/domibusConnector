package eu.domibus.connector.link.common;

import org.apache.cxf.message.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 *  Addresses TooManyOpenFiles issues, also see:
 *
 *  <a href="https://davekieras.wordpress.com/2013/05/30/apache-cxf-attachment-temp-file-leak/">apache-cxf-attachment-temp-file-leak</a>
 *  <a href="https://ext2xhb.wordpress.com/2011/06/08/using-cxf-attachment-safely/">Using CXF Attachment safely</a>
 *
 *
 */
@Service
public class CxfAttachmentCleanupService {

    private static final Logger LOGGER = LogManager.getLogger(CxfAttachmentCleanupService.class);

    public void cleanCxfAttachments(Collection<Attachment> attachments) {
        attachments.stream().forEach(this::cleanAttachment);
        //TODO: trigger LargeFile deletion!
    }

    private void cleanAttachment(Attachment attachment) {
        if (attachment == null || attachment.getDataHandler() == null || attachment.getDataHandler().getDataSource() == null) {
            return;
        }
        try {
            InputStream is = attachment.getDataHandler().getDataSource().getInputStream();
            is.close();
            LOGGER.debug("CLosed InputStream for attachment [{}]", attachment.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
