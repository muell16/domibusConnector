package eu.domibus.connector.tools.logging;



import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Class to manage logging Markers
 */
public class LoggingMarker {

    public static final String CONFIG_MARKER_TEXT = "CONFIG";

    public static final Marker CONFIG = MarkerFactory.getMarker(CONFIG_MARKER_TEXT);

    public static final String BUSINESS_LOG_MARKER_TEXT = "BUSINESS";
    public static final String BUSINESS_CERT_LOG_MARKER_TEXT = "BUSINESS_CERT";

    public static final Marker BUSINESS_LOG = MarkerFactory.getMarker(BUSINESS_LOG_MARKER_TEXT);
    public static final Marker BUSINESS_CERT_LOG = MarkerFactory.getMarker(BUSINESS_CERT_LOG_MARKER_TEXT);

    static {
        BUSINESS_CERT_LOG.add(BUSINESS_LOG);
    }



}
