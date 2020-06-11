package eu.domibus.connector.security.aes;

import java.io.ByteArrayOutputStream;
import java.util.List;

import eu.domibus.connector.security.proxy.DomibusConnectorProxyConfig;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.domibus.connector.domain.model.DomibusConnectorMessage;
import eu.ecodex.dss.model.EnvironmentConfiguration;
import eu.ecodex.dss.model.ProxyData;
import eu.ecodex.dss.model.token.OriginalValidationReportContainer;
import eu.ecodex.dss.model.token.Token;
import eu.ecodex.dss.model.token.TokenValidation;
import eu.ecodex.dss.model.token.ValidationVerification;
import eu.ecodex.dss.service.ECodexException;
import eu.ecodex.dss.service.ECodexTechnicalValidationService;
import eu.ecodex.dss.util.PdfValidationReportService;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.InMemoryDocument;
import eu.europa.esig.dss.MimeType;
import eu.europa.esig.dss.client.http.proxy.ProxyProperties;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.dss.validation.reports.SimpleReport;
import eu.europa.esig.dss.validation.reports.wrapper.DiagnosticData;

public class DomibusConnectorAESTechnicalValidationService implements ECodexTechnicalValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomibusConnectorAESTechnicalValidationService.class);
    
    private DomibusConnectorAESTokenValidationCreator delegate;

    private DomibusConnectorProxyConfig preferenceManager;
    
    private final DomibusConnectorMessage message;

    private EnvironmentConfiguration environmentConfiguration;

    public DomibusConnectorAESTechnicalValidationService(final DomibusConnectorMessage message, final DomibusConnectorAESTokenValidationCreator delegate, final DomibusConnectorProxyConfig preferenceManager) {
        super();
        this.message = message;
        this.delegate = delegate;
        this.preferenceManager = preferenceManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TokenValidation create(final DSSDocument businessDocument, final DSSDocument detachedSignature)
            throws ECodexException {
        LOGGER.debug("create businessDocument: [{}] detachedSignature: [{}]", businessDocument, detachedSignature);
        try {
            return delegate.createTokenValidation(message);
        } catch (Exception e) {
            throw ECodexException.wrap(e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * the report must contain exactly one object of type
     *
     */
    @Override
    public DSSDocument createReportPDF(final Token token) throws ECodexException {
        // LOGGER.mEnter("createReportPDF", token);
        try {
            return createReportPDFImpl(token);
        } catch (Exception e) {
            // LOGGER.mCause("createReportPDF", e, token);
            throw ECodexException.wrap(e);
            // } finally {
            // LOGGER.mExit("createReportPDF", token);
        }
    }

    private DSSDocument createReportPDFImpl(final Token token) throws ECodexException {
        if (token == null) {
            throw new ECodexException("the token (in parameter) must not be null");
        }
        final TokenValidation tokenValidation = token.getValidation();
        if (tokenValidation == null) {
            throw new ECodexException("the token (in parameter) must have a validation object");
        }
        final ValidationVerification tokenVerificationData = tokenValidation.getVerificationData();
        if (tokenVerificationData == null) {
            throw new ECodexException(
                    "the token (in parameter) must have a validation object with an existing verification data");
        }

        final OriginalValidationReportContainer report = tokenValidation.getOriginalValidationReport();

        if (report == null) {
            // return an empty document
            return new InMemoryDocument(new byte[0]);
        }

        final List<Object> reportDatas = report.getAny();
        if (reportDatas == null || reportDatas.isEmpty()) {
            // return an empty document
            return new InMemoryDocument(new byte[0]);
        }

        final ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

        try {
            if (report.getReports() != null) {
                Reports reports = report.getReports();
                final DiagnosticData diagnosticData = reports.getDiagnosticData();
                final SimpleReport simpleReport = reports.getSimpleReport();
                // create and write the pdf version to the stream
                final PdfValidationReportService pdfService = new PdfValidationReportService();
                pdfService.createReport(diagnosticData, simpleReport, pdfStream);
                return new InMemoryDocument(pdfStream.toByteArray(), "dss-report.pdf", MimeType.PDF);
            } else {
                return new InMemoryDocument(new byte[0]);
            }
        } catch (Exception e) {
            throw ECodexException.wrap(e);
        } finally {
            IOUtils.closeQuietly(pdfStream);
        }
    }

    /**
     * {@inheritDoc}
     * 
     *
     *
     *
     *
     *
     */
    @Override
    public void setEnvironmentConfiguration(final EnvironmentConfiguration conf) {
        this.environmentConfiguration = conf;

        LOGGER.debug("set environment configuration: " + environmentConfiguration);

        if (preferenceManager == null) {
            LOGGER.warn("NO preference manager set - unable to forward environment configuration",
                    environmentConfiguration);
            return;
        }

        final ProxyData proxyHttp = (environmentConfiguration == null) ? null : environmentConfiguration.getProxyHTTP();
        final ProxyData proxyHttps = (environmentConfiguration == null) ? null : environmentConfiguration.getProxyHTTPS();

        if (proxyHttp != null) {
            LOGGER.info(
                    "HTTP Configuration detected in EnvironmentConfiguration: Taking configuration from EnvironmentConfiguration.");
            ProxyProperties httpProperties = new ProxyProperties();
            httpProperties.setHost(proxyHttp.getHost());
            httpProperties.setPort(proxyHttp.getPort());
            httpProperties.setUser(proxyHttp.getAuthName());
            httpProperties.setPassword(proxyHttp.getAuthPass());
            preferenceManager.setHttpProperties(httpProperties);
        } else {
            LOGGER.info(
                    "No HTTP Configuration detected in EnvironmentConfiguration: Taking configuration from ProxyPreferenceManager.");
        }

        if (proxyHttps != null) {
            LOGGER.info(
                    "HTTPS Configuration detected in EnvironmentConfiguration: Taking configuration from EnvironmentConfiguration.");
            ProxyProperties httpsProperties = new ProxyProperties();
            httpsProperties.setHost(proxyHttps.getHost());
            httpsProperties.setPort(proxyHttps.getPort());
            httpsProperties.setUser(proxyHttps.getAuthName());
            httpsProperties.setPassword(proxyHttps.getAuthPass());
            preferenceManager.setHttpProperties(httpsProperties);
        } else {
            LOGGER.info(
                    "No HTTPS Configuration detected in EnvironmentConfiguration: Taking configuration from ProxyPreferenceManager.");
        }

        String logMessage = "Proxy Configuration for security library: \n" + "HTTP Proxy Enabled: "
                + (preferenceManager.getHttpProperties() == null);

        if (preferenceManager.getHttpProperties() != null) {
            logMessage = logMessage + "\nHTTP Proxy Host: " + preferenceManager.getHttpProperties().getHost();
        }

        logMessage = logMessage + "\nHTTPS Proxy Enabled: " + (preferenceManager.getHttpsProperties() == null);

        if (preferenceManager.getHttpsProperties() != null) {
            logMessage = logMessage + "\nHTTPS Proxy Host: " + preferenceManager.getHttpsProperties().getHost();
        }

        LOGGER.info(logMessage);
    }

}
