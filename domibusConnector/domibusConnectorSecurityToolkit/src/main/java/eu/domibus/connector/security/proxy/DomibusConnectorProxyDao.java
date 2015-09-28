package eu.domibus.connector.security.proxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import eu.europa.ec.markt.dss.dao.ProxyDao;
import eu.europa.ec.markt.dss.dao.ProxyKey;
import eu.europa.ec.markt.dss.dao.ProxyPreference;

public class DomibusConnectorProxyDao implements ProxyDao {

    private final Map<ProxyKey, String> values = new HashMap<ProxyKey, String>();

    public DomibusConnectorProxyDao(String proxyEnabled, String proxyHost, String proxyPort, String proxyUser,
            String proxyPassword) {
        values.put(ProxyKey.HTTP_ENABLED, proxyEnabled);
        values.put(ProxyKey.HTTP_HOST, proxyHost);
        values.put(ProxyKey.HTTP_PASSWORD, proxyPassword);
        values.put(ProxyKey.HTTP_PORT, proxyPort);
        values.put(ProxyKey.HTTP_USER, proxyUser);
        values.put(ProxyKey.HTTPS_ENABLED, proxyEnabled);
        values.put(ProxyKey.HTTPS_HOST, proxyHost);
        values.put(ProxyKey.HTTPS_PASSWORD, proxyPassword);
        values.put(ProxyKey.HTTPS_PORT, proxyPort);
        values.put(ProxyKey.HTTPS_USER, proxyUser);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.europa.ec.markt.dss.dao.ProxyDao#get(eu.europa.ec.markt.dss.dao.ProxyKey
     * )
     */
    @Override
    public ProxyPreference get(ProxyKey id) {

        if (values.containsKey(id)) {
            ProxyPreference proxyPreference = new ProxyPreference();
            proxyPreference.setProxyKey(id.toString());
            proxyPreference.setValue(values.get(id));
            return proxyPreference;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.europa.ec.markt.dss.dao.ProxyDao#getAll()
     */
    @Override
    public Collection<ProxyPreference> getAll() {

        Collection<ProxyPreference> preferences = new ArrayList<ProxyPreference>();

        for (ProxyKey proxyKey : values.keySet()) {
            ProxyPreference proxyPreference = new ProxyPreference();
            proxyPreference.setProxyKey(proxyKey.toString());
            proxyPreference.setValue(values.get(proxyKey));

            preferences.add(proxyPreference);
        }

        return preferences;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * eu.europa.ec.markt.dss.dao.ProxyDao#update(eu.europa.ec.markt.dss.dao
     * .ProxyPreference)
     */
    @Override
    public void update(ProxyPreference entity) {
        ProxyKey proxyKey = ProxyKey.fromKey(entity.getProxyKey().getKeyName());
        values.put(proxyKey, entity.getValue());
    }

}
