package eu.domibus.connector.test.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class MemoryAppenderTest {

    private static final Logger LOGGER = LogManager.getLogger(MemoryAppender.class);
    private static final Marker BUSINESS = MarkerManager.getMarker("BUSINESS");

    @Test
    public void testLogging() {
        LOGGER.info("Hello World!");
        LOGGER.info(BUSINESS, "Hello Business World!");
        LOGGER.info("Test");


        MemoryAppenderAssert.assertThat(MemoryAppender.getAppender()).containsLogLine("Hello World!");

        MemoryAppenderAssert.assertThat(MemoryAppender.getAppender()).filterOnMarker("BUSINESS").hasLogLines(1);

//        assertThat(MemoryAppender.getAppender().getLogEventList())
//                .filteredOn("marker", "BUSINESS")
//                .hasSize(1);

    }


}