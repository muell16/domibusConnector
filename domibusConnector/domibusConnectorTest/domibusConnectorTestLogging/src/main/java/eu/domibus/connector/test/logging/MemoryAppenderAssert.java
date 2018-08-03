package eu.domibus.connector.test.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.assertj.core.api.AbstractObjectAssert;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class MemoryAppenderAssert extends AbstractObjectAssert<MemoryAppenderAssert, MemoryAppender> {

    private Stream<LogEvent> stream;

    public MemoryAppenderAssert filterOnMarker(String markerName) {
        this.stream = getStream().filter(
                e -> e.getMarker() != null && e.getMarker().getName() == markerName);
        return this;
    }

    public MemoryAppenderAssert filterOnLogLevel(Level logLevel) {
        this.stream = getStream().filter(e -> e.getLevel().equals(logLevel));
        return this;
    }

    public MemoryAppenderAssert filterOn(Predicate<LogEvent> predicate) {
        this.stream = getStream().filter(predicate);
        return this;
    }

    public MemoryAppenderAssert containsLogLine(String logLine) {
        final String lowerCaseLogLine = logLine.toLowerCase();
        if (!getStream()
                .map(LogEvent::getMessage)
                .anyMatch( (Message m) ->  {
//                    System.out.println("Message: " + m.getFormattedMessage());
                    return m.getFormattedMessage().toLowerCase().contains(lowerCaseLogLine);
                })) {
            failWithMessage("No log message found with logLine <%s>", lowerCaseLogLine);
        }
        return this;
    }

    public MemoryAppenderAssert hasLogLines(long number) {
        long count = getStream().count();
        if( count != number) {
            failWithMessage("The number of log lines was <%d> instead of <%d>", count, number);
        }
        return this;
    }

    private Stream<LogEvent> getStream() {
        if (this.stream != null) {
            return stream;
        }
        isNotNull();
        Stream<LogEvent> stream = actual.getLogEventList().stream();
        this.stream = stream;
        return stream;
    }



    public MemoryAppenderAssert(MemoryAppender memoryAppender) {
        super(memoryAppender, MemoryAppenderAssert.class);
    }

    public static MemoryAppenderAssert assertThat(MemoryAppender actual) {
        return new MemoryAppenderAssert(actual);
    }



}
