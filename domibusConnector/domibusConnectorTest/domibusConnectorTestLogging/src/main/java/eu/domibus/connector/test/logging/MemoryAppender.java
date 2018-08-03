package eu.domibus.connector.test.logging;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.OutputStreamAppender;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Plugin(name="MemoryAppender", category="Core", elementType="appender", printObject=true)
public class MemoryAppender extends AbstractAppender {

    private List<LogEvent> logEventList = Collections.synchronizedList(new ArrayList<>());

    protected MemoryAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
        super(name, filter, layout);
    }

    @Override
    public void append(LogEvent logEvent) {
        logEventList.add(logEvent.toImmutable());
    }

    public List<LogEvent> getLogEventList() {
        return this.logEventList;
    }

    public void reset() {
        logEventList = Collections.synchronizedList(new ArrayList<>());
    }

    public static MemoryAppender getAppender() {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();

        MemoryAppender memoryAppender = config.getAppender("Memory");
        return memoryAppender;
    }


    @PluginFactory
    public static MemoryAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter)
             {
        if (name == null) {
            LOGGER.error("No name provided for MyCustomAppenderImpl");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new MemoryAppender(name, filter, layout);
    }
}
