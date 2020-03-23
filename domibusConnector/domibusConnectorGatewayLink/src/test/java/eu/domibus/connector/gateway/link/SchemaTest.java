package eu.domibus.connector.gateway.link;

import org.apache.wss4j.stax.setup.WSSec;
import org.apache.xml.security.stax.impl.util.ConcreteLSInput;
import org.apache.xml.security.utils.ClassLoaderUtils;
import org.junit.jupiter.api.Test;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

public class SchemaTest {

    /**
     * Test if the schemas cen be loaded,
     *  this test will avoid later hard to debug runtime errors!
     * @throws SAXException
     */
    @Test
    public void testWssec() throws SAXException {
        WSSec.loadWSSecuritySchemas();
    }

//    @Test
//    public void testSchemaLoad() throws SAXException {
//        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//        schemaFactory.setResourceResolver(new LSResourceResolver() {
//            @Override
//            public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
//                if ("http://www.w3.org/2001/XMLSchema.dtd".equals(systemId)) {
//                    ConcreteLSInput concreteLSInput = new ConcreteLSInput();
//                    concreteLSInput.setByteStream(ClassLoaderUtils.getResourceAsStream("schemas/XMLSchema.dtd", WSSec.class));
//                    return concreteLSInput;
//                } else if ("XMLSchema.dtd".equals(systemId)) {
//                    ConcreteLSInput concreteLSInput = new ConcreteLSInput();
//                    concreteLSInput.setByteStream(ClassLoaderUtils.getResourceAsStream("schemas/XMLSchema.dtd", WSSec.class));
//                    return concreteLSInput;
//                } else if ("datatypes.dtd".equals(systemId)) {
//                    ConcreteLSInput concreteLSInput = new ConcreteLSInput();
//                    concreteLSInput.setByteStream(ClassLoaderUtils.getResourceAsStream("schemas/datatypes.dtd", WSSec.class));
//                    return concreteLSInput;
//                } else if ("http://www.w3.org/TR/2002/REC-xmldsig-core-20020212/xmldsig-core-schema.xsd".equals(systemId)) {
//                    ConcreteLSInput concreteLSInput = new ConcreteLSInput();
//                    concreteLSInput.setByteStream(ClassLoaderUtils.getResourceAsStream("schemas/xmldsig-core-schema.xsd", WSSec.class));
//                    return concreteLSInput;
//                } else if ("http://www.w3.org/2001/xml.xsd".equals(systemId)) {
//                    ConcreteLSInput concreteLSInput = new ConcreteLSInput();
//                    concreteLSInput.setByteStream(ClassLoaderUtils.getResourceAsStream("schemas/xml.xsd", WSSec.class));
//                    return concreteLSInput;
//                }
//                return null;
//            }
//        });
//
//        Schema schema = schemaFactory.newSchema(
//                new Source[] {
//                        new StreamSource(ClassLoaderUtils.getResourceAsStream("schemas/xml.xsd", WSSec.class)),
//                        new StreamSource(ClassLoaderUtils.getResourceAsStream("schemas/soap-1.1.xsd", WSSec.class)),
//                        new StreamSource(ClassLoaderUtils.getResourceAsStream("schemas/soap-1.2.xsd", WSSec.class)),
//                        new StreamSource(ClassLoaderUtils.getResourceAsStream("schemas/exc-c14n.xsd", WSSec.class)),
//                        new StreamSource(ClassLoaderUtils.getResourceAsStream("schemas/xmldsig-core-schema.xsd", WSSec.class)),
//                        new StreamSource(ClassLoaderUtils.getResourceAsStream("schemas/xop-include.xsd", WSSec.class)),
//                        new StreamSource(ClassLoaderUtils.getResourceAsStream("schemas/xenc-schema.xsd", WSSec.class)),
//                        new StreamSource(ClassLoaderUtils.getResourceAsStream("schemas/xenc-schema-11.xsd", WSSec.class)),
//                        new StreamSource(ClassLoaderUtils.getResourceAsStream("schemas/xmldsig11-schema.xsd", WSSec.class)),
//                        new StreamSource(ClassLoaderUtils.getResourceAsStream("schemas/oasis-200401-wss-wssecurity-utility-1.0.xsd",
//                                WSSec.class)),
//                        new StreamSource(ClassLoaderUtils.getResourceAsStream("schemas/oasis-200401-wss-wssecurity-secext-1.0.xsd",
//                                WSSec.class)),
//                        new StreamSource(ClassLoaderUtils.getResourceAsStream("schemas/oasis-wss-wssecurity-secext-1.1.xsd",
//                                WSSec.class)),
//                        new StreamSource(ClassLoaderUtils.getResourceAsStream("schemas/ws-secureconversation-200502.xsd",
//                                WSSec.class)),
//                        new StreamSource(ClassLoaderUtils.getResourceAsStream("schemas/ws-secureconversation-1.3.xsd",
//                                WSSec.class)),
//                }
//        );
//
//    }
}
