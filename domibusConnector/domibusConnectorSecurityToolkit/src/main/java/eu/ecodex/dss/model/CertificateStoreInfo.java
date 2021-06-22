/*
 * Project: e-CODEX Connector - Container Services/DSS
 * Contractor: ARHS-Developments
 *
 * $HeadURL: http://forge.aris-lux.lan/svn/dgmarktdss/ecodex/src/main/java/eu/ecodex/dss/model/CertificateStoreInfo.java $
 * $Revision: 1879 $
 * $Date: 2013-04-18 09:39:53 +0200 (jeu., 18 avr. 2013) $
 * $Author: meyerfr $
 */
package eu.ecodex.dss.model;

import org.apache.commons.lang.StringUtils;

/**
 * holds information how to access the store holding the e-CODEX connector certificates used for ASiC-S validation
 * <p/>
 * <p>
 * DISCLAIMER: Project owner e-CODEX
 * </p>
 *
 * @author <a href="mailto:eCodex.Project-DSS@arhs-developments.com">ARHS Developments</a>
 * @version $Revision: 1879 $ - $Date: 2013-04-18 09:39:53 +0200 (jeu., 18 avr. 2013) $
 */
public class CertificateStoreInfo {
    private String location;
    private String password;

    /**
     * the info is valid, if a non-empty location is provided
     * @return the result
     */
    public boolean isValid() {
        return !StringUtils.isEmpty(location);
    }

    /**
     * the location (in url format) for loading the keystore
     * @return the value
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * the location (in url format) for loading the keystore
     * @param v the value
     * @return this class' instance for chaining
     */
    public CertificateStoreInfo setLocation(final String v) {
        this.location = v;
        return this;
    }

    /**
     * gives the password for loading the keystore
     * @return the value
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * sets the password for loading the keystore
     * @param v the value
     * @return this class' instance for chaining
     */
    public CertificateStoreInfo setPassword(final String v) {
        this.password = v;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "CertificateStoreInfo{" +
            "location=" + (location == null ? null : ('\'' + location + '\'')) +
            ", password=" + (password == null ? null : "[set]") +
            '}';
    }

}
