package eu.domibus.connector.lib.spring;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Duration {

    private static final Map<String, TimeUnit> UNITS = new HashMap<String, TimeUnit>() {
        {
            put("ms", TimeUnit.MILLISECONDS);
            put("s", TimeUnit.SECONDS);
            put("m", TimeUnit.MINUTES);
            put("h", TimeUnit.HOURS);
            put("d", TimeUnit.DAYS);
        }
    };

    private static final Pattern PARSE_PATTERN = Pattern.compile("([0-9]+)(ms|s|m|h|d)");

    private final long duration;
    private final TimeUnit unit;

    private Duration(long value, TimeUnit unit) {
        this.duration = value;
        this.unit = unit;
    }


    public Duration(long milliseconds) {
        this(milliseconds, TimeUnit.MILLISECONDS);
    }

    public long getMilliseconds() {
        return unit.toMillis(duration);
    }

    /**
     * Factory method constructing an instance from a string.
     *
     * @param s the input string representing the duration
     * @return a {@link Duration} instance.
     * @throws NumberFormatException in case the string does not represent a valid duration
     */
    public static Duration valueOf(String s) throws NumberFormatException {
        if (!StringUtils.hasText(s)) {
            throw new NumberFormatException("Null or empty string cannot be converted to duration.");
        }

        Matcher m = PARSE_PATTERN.matcher(s.trim());
        if (!m.matches()) {
            throw new NumberFormatException("Invalid duration format: " + s);
        }

        Long value = Long.valueOf(m.group(1));
        String unit = m.group(2);

        return new Duration(value, UNITS.get(unit));
    }


}
