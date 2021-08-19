package eu.domibus.connector.dss.configuration;

import eu.domibus.connector.lib.spring.configuration.StoreConfigurationProperties;
import org.apache.logging.log4j.core.net.ssl.TrustStoreConfiguration;

import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TrustListSourceConfigurationProperties {

    private Duration tlSync = Duration.ofDays(1);

    private List<TlSource> tlSources = new ArrayList<>();

    private List<LotlSource> lotlSources = new ArrayList<>();

    public List<TlSource> getTlSources() {
        return tlSources;
    }

    public void setTlSources(List<TlSource> tlSources) {
        this.tlSources = tlSources;
    }

    public List<LotlSource> getLotlSources() {
        return lotlSources;
    }

    public void setLotlSources(List<LotlSource> lotlSources) {
        this.lotlSources = lotlSources;
    }

    public Duration getTlSync() {
        return tlSync;
    }

    public void setTlSync(Duration tlSync) {
        this.tlSync = tlSync;
    }

    public static class TlSource {
        private String tlUrl = "";
        private StoreConfigurationProperties signingCerts = new StoreConfigurationProperties();

        public String getTlUrl() {
            return tlUrl;
        }

        public void setTlUrl(String tlUrl) {
            this.tlUrl = tlUrl;
        }

        public StoreConfigurationProperties getSigningCerts() {
            return signingCerts;
        }

        public void setSigningCerts(StoreConfigurationProperties signingCerts) {
            this.signingCerts = signingCerts;
        }
    }

    public static class LotlSource {

        private String lotlUrl = "https://ec.europa.eu/tools/lotl/eu-lotl.xml";
        private String signingCertificatesAnnouncementUri = "https://eur-lex.europa.eu/legal-content/EN/TXT/?uri=uriserv:OJ.C_.2019.276.01.0001.01.ENG";
        StoreConfigurationProperties signingCerts = new StoreConfigurationProperties();
        boolean pivotSupport = true;

        public String getLotlUrl() {
            return lotlUrl;
        }

        public void setLotlUrl(String lotlUrl) {
            this.lotlUrl = lotlUrl;
        }

        public String getSigningCertificatesAnnouncementUri() {
            return signingCertificatesAnnouncementUri;
        }

        public void setSigningCertificatesAnnouncementUri(String signingCertificatesAnnouncementUri) {
            this.signingCertificatesAnnouncementUri = signingCertificatesAnnouncementUri;
        }

        public StoreConfigurationProperties getSigningCerts() {
            return signingCerts;
        }

        public void setSigningCerts(StoreConfigurationProperties signingCerts) {
            this.signingCerts = signingCerts;
        }

        public boolean isPivotSupport() {
            return pivotSupport;
        }

        public void setPivotSupport(boolean pivotSupport) {
            this.pivotSupport = pivotSupport;
        }
    }

    public static class FileLoaderProperties {
        private Path cacheDirectory;
        private Duration cacheExpirationTime;
        private boolean offline = false;

        public Path getCacheDirectory() {
            return cacheDirectory;
        }

        public void setCacheDirectory(Path cacheDirectory) {
            this.cacheDirectory = cacheDirectory;
        }

        public Duration getCacheExpirationTime() {
            return cacheExpirationTime;
        }

        public void setCacheExpirationTime(Duration cacheExpirationTime) {
            this.cacheExpirationTime = cacheExpirationTime;
        }

        public boolean isOffline() {
            return offline;
        }

        public void setOffline(boolean offline) {
            this.offline = offline;
        }
    }


}
