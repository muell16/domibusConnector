package eu.ecodex.evidences;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import eu.domibus.connector.common.annotations.BusinessDomainScoped;
import eu.domibus.connector.tools.logging.LoggingUtils;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.etsi.uri._02640.soapbinding.v1_.DeliveryConstraints;
import org.etsi.uri._02640.soapbinding.v1_.Destinations;
import org.etsi.uri._02640.soapbinding.v1_.MsgIdentification;
import org.etsi.uri._02640.soapbinding.v1_.MsgMetaData;
import org.etsi.uri._02640.soapbinding.v1_.Originators;
import org.etsi.uri._02640.soapbinding.v1_.REMDispatchType;
import org.etsi.uri._02640.v2.EntityDetailsType;
import org.etsi.uri._02640.v2.EventReasonType;
import org.etsi.uri._02640.v2.REMEvidenceType;

import eu.ecodex.evidences.exception.ECodexEvidenceBuilderException;
import eu.ecodex.evidences.types.ECodexMessageDetails;
import eu.ecodex.signature.EvidenceUtils;
import eu.ecodex.signature.EvidenceUtilsXades;
import eu.spocseu.edeliverygw.REMErrorEvent;
import eu.spocseu.edeliverygw.configuration.EDeliveryDetails;
import eu.spocseu.edeliverygw.evidences.DeliveryNonDeliveryToRecipient;
import eu.spocseu.edeliverygw.evidences.Evidence;
import eu.spocseu.edeliverygw.evidences.RelayREMMDAcceptanceRejection;
import eu.spocseu.edeliverygw.evidences.RelayREMMDFailure;
import eu.spocseu.edeliverygw.evidences.RetrievalNonRetrievalByRecipient;
import eu.spocseu.edeliverygw.evidences.SubmissionAcceptanceRejection;
import eu.spocseu.edeliverygw.messageparts.SpocsFragments;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@BusinessDomainScoped
@Log4j2
public class ECodexEvidenceBuilder implements EvidenceBuilder {


    private static EvidenceUtils signer = null;

    public ECodexEvidenceBuilder(Resource javaKeyStorePath, String javaKeyStoreType, String javaKeyStorePassword, String alias, String keyPassword) {
        //TODO: use keystore provider, improve keystore loading, HSM support
        signer = new EvidenceUtilsXades(createKeyPairProvider(javaKeyStorePath, javaKeyStoreType, javaKeyStorePassword, alias, keyPassword));
    }

    private static EvidenceUtils.KeyPairProvider createKeyPairProvider(Resource store, String storeType, String storePass, String alias, String keyPass) {
        return new EvidenceUtils.KeyPairProvider() {

            @Override
            public KeyStore getKeyStore() {
                log.debug("Loading KeyStore[{}]", getPrettyPrintKeystore());

                try (InputStream kfis = store.getInputStream()){
                    KeyStore ks = KeyStore.getInstance(storeType);
                    ks.load(kfis, (storePass == null) ? null : storePass.toCharArray());
                    return ks;
                } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException e) {
                    throw new RuntimeException(String.format("Cannot load keystore [%s]", getPrettyPrintKeystore()), e);
                }
            }

            @Override
            public KeyPair getKeyPair() {
                log.debug("Loading KeyPair from Java KeyStore[{}]", getPrettyPrintKeystore());

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
                    Objects.requireNonNull(publicKey, "public key must not be null!");
                    Objects.requireNonNull(privateKey, "private key must not be null!");
                    return new KeyPair(publicKey, privateKey);

                } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException | NullPointerException  e) {
                    throw new RuntimeException(String.format("Cannot get keypair from keystore [%s]", getPrettyPrintKeystore()), e);
                }

            }

            @Override
            public X509Certificate getCertificate() {
                try {
                    Certificate crt =  getKeyStore().getCertificate(alias);
                    if (!(crt instanceof X509Certificate)) {
                        throw new RuntimeException("Retrieving certificate from keystore [" + getPrettyPrintKeystore() + "] failed, because it is not of X509");
                    }
                    return (X509Certificate)crt;
                } catch (KeyStoreException e) {
                    throw new RuntimeException("Retrieving certificate from keystore [" + getPrettyPrintKeystore() + "] failed", e);
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
                    throw new RuntimeException("Retrieving certificate chain from keystore [" + getPrettyPrintKeystore() + "] failed", e);
                }

            }

            private String getPrettyPrintKeystore() {

                return String.format("Keystore [location=%s, type=%s, pw=%s, keyalias=%s, keypw=%s]", store,
                        storeType,
                        logPassword(log, storePass),
                        alias,
                        logPassword(log, keyPass));
            }

            private String logPassword(Logger logger, Object password) {
                if (password == null) {
                    return null;
                }
                if (logger.isTraceEnabled() || !logger.getLevel().isMoreSpecificThan(Level.TRACE)) {
                    return password.toString();
                } else {
                    return String.format("**increase logger [%s] log level to [%s]] to see**", logger, Level.TRACE);
                }
            }

        };
    }

    @Override
    public byte[] createSubmissionAcceptanceRejection(boolean isAcceptance, REMErrorEvent eventReason, EDeliveryDetails evidenceIssuerDetails, ECodexMessageDetails messageDetails)
            throws ECodexEvidenceBuilderException {

        EventReasonType reason = null;

        if (eventReason != null) {
            reason = new EventReasonType();
            reason.setCode(eventReason.getEventCode());
            reason.setDetails(eventReason.getEventDetails());
        }

        return createSubmissionAcceptanceRejection(isAcceptance, reason, evidenceIssuerDetails, messageDetails);
    }

    @Override
    public byte[] createSubmissionAcceptanceRejection(boolean isAcceptance, EventReasonType eventReason, EDeliveryDetails evidenceIssuerDetails, ECodexMessageDetails messageDetails)
            throws ECodexEvidenceBuilderException {
        Date start = new Date();

        // This is the message and all related information
        REMDispatchType dispatch = new REMDispatchType();

        MsgMetaData msgMetaData = new MsgMetaData();
        EntityDetailsType recipient = new EntityDetailsType();
        EntityDetailsType sender = new EntityDetailsType();
        try {
            recipient.getAttributedElectronicAddressOrElectronicAddress().add(SpocsFragments.createElectoricAddress(messageDetails.getSenderAddress(), "displayName"));
            sender.getAttributedElectronicAddressOrElectronicAddress().add(SpocsFragments.createElectoricAddress(messageDetails.getRecipientAddress(), "displayName"));
        } catch (MalformedURLException e) {
            log.warn(e);
        }

        Destinations destinations = new Destinations();
        destinations.setRecipient(sender);

        Originators originators = new Originators();
        originators.setFrom(recipient);
        originators.setReplyTo(recipient);
        originators.setSender(recipient);

        MsgIdentification msgIdentification = new MsgIdentification();
        msgIdentification.setMessageID(messageDetails.getEbmsMessageId());

        msgMetaData.setDestinations(destinations);
        msgMetaData.setOriginators(originators);
        msgMetaData.setMsgIdentification(msgIdentification);

        GregorianCalendar cal = new GregorianCalendar();
        XMLGregorianCalendar initialSend = null;
        try {
            initialSend = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException e1) {
            e1.printStackTrace();
        }
        DeliveryConstraints deliveryConstraints = new DeliveryConstraints();
        deliveryConstraints.setInitialSend(initialSend);

        msgMetaData.setDeliveryConstraints(deliveryConstraints);

        dispatch.setMsgMetaData(msgMetaData);

        SubmissionAcceptanceRejection evidence = new SubmissionAcceptanceRejection(evidenceIssuerDetails, dispatch, isAcceptance);

        if (eventReason != null)
            evidence.setEventReason(eventReason);

        evidence.setUAMessageId(messageDetails.getNationalMessageId());
        evidence.setHashInformation(messageDetails.getHashValue(), messageDetails.getHashAlgorithm());

        byte[] signedByteArray = signEvidence(evidence, false);

        log.info("Creation of SubmissionAcceptanceRejection Evidence finished in " + (System.currentTimeMillis() - start.getTime()) + " ms.");

        return signedByteArray;
    }

    @Override
    public byte[] createRelayREMMDAcceptanceRejection(boolean isAcceptance, REMErrorEvent eventReason, EDeliveryDetails evidenceIssuerDetails, byte[] previousEvidenceInByte)
            throws ECodexEvidenceBuilderException {

        EventReasonType reason = null;

        if (eventReason != null) {
            reason = new EventReasonType();
            reason.setCode(eventReason.getEventCode());
            reason.setDetails(eventReason.getEventDetails());
        }

        return createRelayREMMDAcceptanceRejection(isAcceptance, reason, evidenceIssuerDetails, previousEvidenceInByte);
    }

    @Override
    public byte[] createRelayREMMDAcceptanceRejection(boolean isAcceptance, EventReasonType eventReason, EDeliveryDetails evidenceIssuerDetails, byte[] previousEvidenceInByte)
            throws ECodexEvidenceBuilderException {

        REMEvidenceType previousEvidence = signer.convertIntoEvidenceType(previousEvidenceInByte);

        RelayREMMDAcceptanceRejection evidence = new RelayREMMDAcceptanceRejection(evidenceIssuerDetails, previousEvidence, isAcceptance);

        if (eventReason != null)
            evidence.setEventReason(eventReason);

        byte[] signedByteArray = signEvidence(evidence, true);

        return signedByteArray;
    }

    @Override
    public byte[] createRelayREMMDFailure(REMErrorEvent eventReason, EDeliveryDetails evidenceIssuerDetails, byte[] previousEvidenceInByte)
            throws ECodexEvidenceBuilderException {

        EventReasonType reason = null;

        if (eventReason != null) {
            reason = new EventReasonType();
            reason.setCode(eventReason.getEventCode());
            reason.setDetails(eventReason.getEventDetails());
        }

        return createRelayREMMDFailure(reason, evidenceIssuerDetails, previousEvidenceInByte);
    }

    @Override
    public byte[] createRelayREMMDFailure(EventReasonType eventReason, EDeliveryDetails evidenceIssuerDetails, byte[] previousEvidenceInByte)
            throws ECodexEvidenceBuilderException {

        REMEvidenceType previousEvidence = signer.convertIntoEvidenceType(previousEvidenceInByte);

        RelayREMMDFailure evidence = new RelayREMMDFailure(evidenceIssuerDetails, previousEvidence);

        if (eventReason != null)
            evidence.setEventReason(eventReason);

        byte[] signedByteArray = signEvidence(evidence, true);

        return signedByteArray;
    }

    @Override
    public byte[] createDeliveryNonDeliveryToRecipient(boolean isDelivery, REMErrorEvent eventReason, EDeliveryDetails evidenceIssuerDetails, byte[] previousEvidenceInByte)
            throws ECodexEvidenceBuilderException {
        EventReasonType reason = null;

        if (eventReason != null) {
            reason = new EventReasonType();
            reason.setCode(eventReason.getEventCode());
            reason.setDetails(eventReason.getEventDetails());
        }

        return createDeliveryNonDeliveryToRecipient(isDelivery, reason, evidenceIssuerDetails, previousEvidenceInByte);
    }

    @Override
    public byte[] createDeliveryNonDeliveryToRecipient(boolean isDelivery, EventReasonType eventReason, EDeliveryDetails evidenceIssuerDetails, byte[] previousEvidenceInByte)
            throws ECodexEvidenceBuilderException {

        REMEvidenceType previousEvidence = signer.convertIntoEvidenceType(previousEvidenceInByte);

        DeliveryNonDeliveryToRecipient evidence = new DeliveryNonDeliveryToRecipient(evidenceIssuerDetails, previousEvidence, isDelivery);

        if (eventReason != null)
            evidence.setEventReason(eventReason);

        byte[] signedByteArray = signEvidence(evidence, true);

        return signedByteArray;
    }

    @Override
    public byte[] createRetrievalNonRetrievalByRecipient(boolean isRetrieval, REMErrorEvent eventReason, EDeliveryDetails evidenceIssuerDetails, byte[] previousEvidenceInByte)
            throws ECodexEvidenceBuilderException {

        EventReasonType reason = null;

        if (eventReason != null) {
            reason = new EventReasonType();
            reason.setCode(eventReason.getEventCode());
            reason.setDetails(eventReason.getEventDetails());
        }

        return createRetrievalNonRetrievalByRecipient(isRetrieval, reason, evidenceIssuerDetails, previousEvidenceInByte);

    }

    @Override
    public byte[] createRetrievalNonRetrievalByRecipient(boolean isRetrieval, EventReasonType eventReason, EDeliveryDetails evidenceIssuerDetails, byte[] previousEvidenceInByte)
            throws ECodexEvidenceBuilderException {

        REMEvidenceType previousEvidence = signer.convertIntoEvidenceType(previousEvidenceInByte);

        RetrievalNonRetrievalByRecipient evidence = new RetrievalNonRetrievalByRecipient(evidenceIssuerDetails, previousEvidence, isRetrieval);

        if (eventReason != null)
            evidence.setEventReason(eventReason);

        byte[] signedByteArray = signEvidence(evidence, true);

        return signedByteArray;
    }

    private byte[] signEvidence(Evidence evidenceToBeSigned, boolean removeOldSignature) {

        if (removeOldSignature) {
            // delete old signature field
            evidenceToBeSigned.getXSDObject().setSignature(null);
            log.debug("Old Signature removed");
        }

        ByteArrayOutputStream fo = new ByteArrayOutputStream();

        try {

            evidenceToBeSigned.serialize(fo);
        } catch (JAXBException e) {
            log.error("Cannot serialize evidence", e);
        }

        byte[] bytes = fo.toByteArray();

        byte[] signedByteArray = signer.signByteArray(bytes);

        return signedByteArray;
    }

}
