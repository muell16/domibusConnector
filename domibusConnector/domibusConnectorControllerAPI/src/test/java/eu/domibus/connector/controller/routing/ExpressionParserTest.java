package eu.domibus.connector.controller.routing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpressionParserTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "|(&(equals(ServiceName, 'Test'), equals(FromPartyId, 'gw01')), equals(FromPartyId, 'gw02'))",
            "not(|(&(equals(ServiceName, 'Test'), equals(FromPartyId, 'gw01')), equals(FromPartyId, 'gw02')))"

    })
    public void testParseOfValidExpressions(String expression) {
        ExpressionParser expressionParser = new ExpressionParser(expression);
        assertThat(expressionParser.getParsedExpression()).isPresent();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "|(&(equals(SrviceName, 'Test'), equals(FromPartyId, 'gw01')), equals(FromPartyId, 'gw02'))",
            "|((equals(ServiceName, 'Test'), equals(FromPartyId, 'gw01')), equals(FromPartyId, 'gw02'))",
            "|(&(euals(ServiceName, 'Test'), equals(FromPartyId, 'gw01')), equals(FromPartyId, 'gw02'))"

    })
    public void testParseOfInvalidExpressions(String expression) {
        ExpressionParser expressionParser = new ExpressionParser(expression);
        assertThat(expressionParser.getParsedExpression()).isEmpty();
        assertThat(expressionParser.getParsingExceptions()).hasSize(1);

        System.out.println(expressionParser.getParsingExceptions()
                .stream()
                .map(Exception::getMessage)
                .collect(Collectors.joining(",")));
    }

}