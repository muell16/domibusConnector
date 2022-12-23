package eu.ecodex;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.etsi.uri._01903.v1_3.AnyType;
import org.etsi.uri._02640.soapbinding.v1_.DeliveryConstraints;
import org.etsi.uri._02640.soapbinding.v1_.Destinations;
import org.etsi.uri._02640.soapbinding.v1_.MsgIdentification;
import org.etsi.uri._02640.soapbinding.v1_.MsgMetaData;
import org.etsi.uri._02640.soapbinding.v1_.OriginalMsgType;
import org.etsi.uri._02640.soapbinding.v1_.Originators;
import org.etsi.uri._02640.soapbinding.v1_.REMDispatchType;
import org.etsi.uri._02640.v2.EntityDetailsType;
import org.etsi.uri._02640.v2.EntityNameType;
import org.etsi.uri._02640.v2.EventReasonType;
import org.etsi.uri._02640.v2.NamePostalAddressType;
import org.etsi.uri._02640.v2.NamesPostalAddressListType;
import org.etsi.uri._02640.v2.PostalAddressType;
import org.junit.jupiter.api.*;

import eu.ecodex.evidences.ECodexEvidenceBuilder;
import eu.ecodex.evidences.EvidenceBuilder;
import eu.ecodex.evidences.exception.ECodexEvidenceBuilderException;
import eu.ecodex.evidences.types.ECodexMessageDetails;
import eu.ecodex.signature.EvidenceUtils;
import eu.ecodex.signature.EvidenceUtilsXades;
import eu.spocseu.edeliverygw.configuration.EDeliveryDetails;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail.PostalAdress;
import eu.spocseu.edeliverygw.configuration.xsd.EDeliveryDetail.Server;
import eu.spocseu.edeliverygw.messageparts.SpocsFragments;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
public class SubmissionAcceptanceRejectionTest {

    private static EvidenceBuilder builder = new ECodexEvidenceBuilder(new ClassPathResource("keystore.jks"), "JKS", "test123", "new_Testcert", "test123");
    private static EvidenceUtils utils = new EvidenceUtilsXades(createKeyPairProvider(new ClassPathResource("keystore.jks"), "JKS", "test123", "new_Testcert", "test123"));

    public static EvidenceUtils.KeyPairProvider createKeyPairProvider(Resource store, String storeType, String storePass, String alias, String keyPass) {
        return new EvidenceUtils.KeyPairProvider() {

			@Override
			public KeyStore getKeyStore() {
				log.debug("Loading KeyPair from Java KeyStore(" + store + ")");

				try (InputStream kfis = store.getInputStream()){
					KeyStore ks = KeyStore.getInstance(storeType);
					ks.load(kfis, (storePass == null) ? null : storePass.toCharArray());
					return ks;
				} catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException e) {
					throw new RuntimeException("Opening keystore [" + store + "] failed", e);
				}

			}

			@Override
            public KeyPair getKeyPair() {
                log.debug("Loading KeyPair from Java KeyStore(" + store + ")");

                try {
					KeyStore ks = getKeyStore();
                    if (!ks.containsAlias(alias)) {
						throw new IllegalArgumentException("Cannot get keypair from keystore: alias does not exist");
					}
					Key key = ks.getKey(alias, keyPass.toCharArray());
					if (!(key instanceof PrivateKey)) {
						throw new IllegalArgumentException("Cannot get keypair from keystore: key is not a private key");
					}
					X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
					PublicKey publicKey = cert.getPublicKey();
					PrivateKey privateKey = (PrivateKey) key;
					return new KeyPair(publicKey, privateKey);

                } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException  e) {
//                    log.error("Cannot get keypair from keystore", e);
					throw new RuntimeException("Cannot get keypair from keystore [" + getKeyStore() + "] ", e);
                }

            }

            @Override
            public X509Certificate getCertificate() {
                try {
                    Certificate crt =  getKeyStore().getCertificate(alias);
                    if (!(crt instanceof X509Certificate)) {
                        throw new RuntimeException("Retrieving certificate from keystore [" + getKeyStore() + "] failed, because it is not of X509");
                    }
                    return (X509Certificate)crt;
                } catch (KeyStoreException e) {
                    throw new RuntimeException("Retrieving certificate from keystore [" + getKeyStore() + "] failed", e);
                }
            }

            @Override
            public List<X509Certificate> getCertificateChain() {
                try {
                    Certificate[] certificateChain = getKeyStore().getCertificateChain(alias);
                    return Arrays.stream(certificateChain)
                            .map(X509Certificate.class::cast)
                            .collect(Collectors.toList());
                } catch (KeyStoreException e) {
                    throw new RuntimeException("Retrieving certificate chain from keystore [" + getKeyStore() + "] failed", e);
                }

            }

        };
    }

    private static final String PATH_OUTPUT_FILES = "target/test/SubmissionAcceptanceRejectionTest/";
    private static final String SUBMISSION_ACCEPTANCE_FILE = "submissionAcceptance.xml";
    private static final String RELAYREMMD_ACCEPTANCE_FILE = "relayremmdAcceptance.xml";
    private static final String DELIVERY_ACCEPTANCE_FILE = "deliveryAcceptance.xml";
    private static final String RETRIEVAL_ACCEPTANCE_FILE = "retrievalAcceptance.xml";
    private static final String FAILURE_FILE = "failure.xml";

    @BeforeAll
    public static void setUpTestEnv() throws IOException {
        File testDir = Paths.get(PATH_OUTPUT_FILES).toFile();
        try {
            FileUtils.forceDelete(testDir);
        } catch (IOException e) {
        }
        FileUtils.forceMkdir(testDir);
    }

    private EDeliveryDetails createEntityDetailsObject() {

        PostalAdress address = new PostalAdress();
        address.setCountry("country");
        address.setLocality("locality");
        address.setPostalCode("postalcode");
        address.setStreetAddress("streetaddress");

        Server server = new Server();
        server.setDefaultCitizenQAAlevel(1);
        server.setGatewayAddress("gatewayaddress");
        server.setGatewayDomain("gatewaydomain");
        server.setGatewayName("gatewayname");

        EDeliveryDetail detail = new EDeliveryDetail();

        detail.setPostalAdress(address);
        detail.setServer(server);


        return new EDeliveryDetails(detail);
    }


    @SuppressWarnings("unused")
    private REMDispatchType createRemDispatchTypeObject() throws MalformedURLException, DatatypeConfigurationException {
        GregorianCalendar cal = new GregorianCalendar();
        XMLGregorianCalendar testDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);


        DeliveryConstraints deliveryConstraints = new DeliveryConstraints();
        deliveryConstraints.setAny(new AnyType());
        deliveryConstraints.setInitialSend(testDate);
        deliveryConstraints.setObsoleteAfter(testDate);
        deliveryConstraints.setOrigin(testDate);


        PostalAddressType postAdressType = new PostalAddressType();
        postAdressType.setCountryName("countryName");
        postAdressType.setLang("lang");
        postAdressType.setLocality("locality");
        postAdressType.setPostalCode("postalcode");
        postAdressType.setStateOrProvince("stateOrProvince");

        EntityNameType entityNameType = new EntityNameType();
        entityNameType.setLang("lang");

        NamePostalAddressType namesPostal = new NamePostalAddressType();
        namesPostal.setPostalAddress(postAdressType);
        namesPostal.setEntityName(entityNameType);

        NamesPostalAddressListType namesPostalList = new NamesPostalAddressListType();
        namesPostalList.getNamePostalAddress().add(namesPostal);


        EntityDetailsType recipient = new EntityDetailsType();
        recipient.setNamesPostalAddresses(namesPostalList);
        recipient.getAttributedElectronicAddressOrElectronicAddress().add(SpocsFragments.createElectoricAddress("stefan.mueller@it.nrw.de", "displayName"));

        Destinations destinations = new Destinations();
        destinations.setRecipient(recipient);

        MsgIdentification msgIdentification = new MsgIdentification();
        msgIdentification.setMessageID("messageID");

        Originators originators = new Originators();
        originators.setFrom(recipient);
        originators.setReplyTo(recipient);
        originators.setSender(recipient);


        MsgMetaData msgMetaData = new MsgMetaData();
        msgMetaData.setDeliveryConstraints(deliveryConstraints);
        msgMetaData.setDestinations(destinations);
        msgMetaData.setMsgIdentification(msgIdentification);
        msgMetaData.setOriginators(originators);


        byte[] contentValue = {0x000A, 0x000A};

        OriginalMsgType orgMsgType = new OriginalMsgType();
        orgMsgType.setContentType("contentType");
        orgMsgType.setSize(BigInteger.valueOf(1000));
        orgMsgType.setValue(contentValue);


        REMDispatchType dispatchMessage = new REMDispatchType();
        dispatchMessage.setId("id");
        dispatchMessage.setMsgMetaData(msgMetaData);
        dispatchMessage.setOriginalMsg(orgMsgType);

        return dispatchMessage;
    }


    @Test
    public void evidenceChain() throws DatatypeConfigurationException, ECodexEvidenceBuilderException, IOException {


        EDeliveryDetails details = createEntityDetailsObject();

        ECodexMessageDetails msgDetails = new ECodexMessageDetails();
        msgDetails.setEbmsMessageId("ebmsMessageId");
        msgDetails.setHashAlgorithm("hashAlgorithm");
        msgDetails.setHashValue("abc".getBytes());
        msgDetails.setNationalMessageId("nationalMessageId");
        msgDetails.setRecipientAddress("recipientAddress");
        msgDetails.setSenderAddress("senderAddress");

        byte[] subm = builder.createSubmissionAcceptanceRejection(true, (EventReasonType) null, details, msgDetails);

        writeFile(subm, PATH_OUTPUT_FILES + SUBMISSION_ACCEPTANCE_FILE);
        assertTrue(utils.verifySignature(subm));

        byte[] relayrem = builder.createRelayREMMDAcceptanceRejection(false, (EventReasonType) null, details, subm);
        writeFile(relayrem, PATH_OUTPUT_FILES + RELAYREMMD_ACCEPTANCE_FILE);
        assertTrue(utils.verifySignature(relayrem));

        byte[] delivery = builder.createDeliveryNonDeliveryToRecipient(true, (EventReasonType) null, details, relayrem);
        writeFile(delivery, PATH_OUTPUT_FILES + DELIVERY_ACCEPTANCE_FILE);
        assertTrue(utils.verifySignature(delivery));

        byte[] retrieval = builder.createRetrievalNonRetrievalByRecipient(true, (EventReasonType) null, details, delivery);
        writeFile(retrieval, PATH_OUTPUT_FILES + RETRIEVAL_ACCEPTANCE_FILE);
        assertTrue(utils.verifySignature(retrieval));

        byte[] failure = builder.createRelayREMMDFailure((EventReasonType) null, details, delivery);
        writeFile(failure, PATH_OUTPUT_FILES + FAILURE_FILE);
        assertTrue(utils.verifySignature(retrieval));
    }

    private void writeFile(byte[] data, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(fileName));
        fos.write(data);
        fos.flush();
        fos.close();
    }


}
