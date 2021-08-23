
package eu.domibus.connector.security.libtests.dss;

import eu.europa.esig.dss.model.DSSException;
import eu.europa.esig.dss.model.MimeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class MimeTypeTest {

    
    @Test
    public void testFromMimeTypeString() {        
        String pdfMimeTypeString = "application/pdf";
        
        MimeType mimeType = MimeType.fromMimeTypeString(pdfMimeTypeString);
        assertThat(mimeType).isEqualTo(MimeType.PDF);
        
    }
    
//    @Test
//    public void testFromMimeTypeString_withIllegalMimeType_shouldLogWarning() {

//                String illegalMimeType = "kljdasfhihlaerö";
//            MimeType mimeType = MimeType.fromMimeTypeString(illegalMimeType);
//       TODO: check warning
//    }
    
    @Test
    public void testFromMimeTypeString_forDssLibUnknwonMimeType_libShouldNotChangeIt() {
        String unknownMimeType = "application/superduper";
        MimeType mimeType = MimeType.fromMimeTypeString(unknownMimeType);
        MimeType expectedMimeType = MimeType.BINARY;
        assertThat(mimeType.getMimeTypeString())                
                .isEqualTo(unknownMimeType);
    }
    
    @Test
    public void testMimeTypeEquals() {
        String mimeTypeString = "application/superduper";
        MimeType mimeTypeOne = MimeType.fromMimeTypeString(mimeTypeString);
        MimeType mimeTypeTwo = MimeType.fromMimeTypeString(mimeTypeString);
        
        assertThat(mimeTypeOne).as("both mimeTypes should be the same if mimeTypeString is the same!").isEqualTo(mimeTypeTwo);        
    }
    
}
