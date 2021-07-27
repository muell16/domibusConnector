package eu.domibus.connector.tools.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class LoggingUtils {

    public static final String logPassword(Logger logger, Object password) {
        if (password == null) {
            return null;
        }
        if (logger.isTraceEnabled() || logger.getLevel().isLessSpecificThan(Level.TRACE)) {
            return password.toString();
        } else {
            return "**increase log level to see**";
        }
    }
}
