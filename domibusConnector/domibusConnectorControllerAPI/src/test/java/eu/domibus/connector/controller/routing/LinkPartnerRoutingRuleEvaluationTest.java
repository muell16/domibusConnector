package eu.domibus.connector.controller.routing;

import eu.ecodex.dc5.message.model.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class LinkPartnerRoutingRuleEvaluationTest {


    @ParameterizedTest
    @MethodSource("provideParameters")
    public void testMatch_shouldEvaluateToTrue(String expression, DC5Message message) {
        //ExpressionParser expressionParser = new ExpressionParser(expression);

        RoutingRulePattern rulePattern = new RoutingRulePattern(expression);
        boolean result = rulePattern.matches(message);
        assertThat(result).isTrue();

    }

    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.of("&(equals(ServiceName, 'EPO_SERVICE'), |(equals(FromPartyId, 'gw01'), equals(FromPartyId, 'gw02')))", getMessage1()),
                Arguments.of("&(&(equals(Action, 'ConTest_Form'), equals(ServiceName, 'Connector-TEST')), equals(ServiceType, 'urn:e-codex:services:'))", getMessage2())
        );
    }

    @ParameterizedTest
    @MethodSource("provideNotMatchingParameters")
    public void testMatch_shouldNotMatch(String expression, DC5Message message) {
        //ExpressionParser expressionParser = new ExpressionParser(expression);

        RoutingRulePattern rulePattern = new RoutingRulePattern(expression);
        boolean result = rulePattern.matches(message);
        assertThat(result).isFalse();

    }

    private static Stream<Arguments> provideNotMatchingParameters() {
        return Stream.of(
                Arguments.of("not(&(equals(ServiceName, 'EPO_SERVICE'), |(equals(FromPartyId, 'gw01'), equals(FromPartyId, 'gw02'))))", getMessage1()),
                Arguments.of("&(&(equals(Action, 'ConTest_Form'), equals(ServiceName, 'Connector-TEST')), equals(ServiceType, 'urn:e-codex:services:'))", getMessage1())

        );
    }


    private static DC5Message getMessage1() {
        return DC5Message.builder()
                .ebmsData(DC5Ebms.builder()
                        .action(new DC5Action("OtherAction"))
                        .service(DC5Service.builder()
                                .service("EPO_SERVICE")
                                .serviceType("urn:e-codex:services:")
                                .build()
                        )
                        .sender(DC5EcxAddress.builder()
                                .party(DC5Party.builder().partyId("gw01").build())
                                .build())
                        .build()
                )
                .build();
    }

    private static DC5Message getMessage2() {
        return DC5Message.builder()
                .ebmsData(DC5Ebms.builder()
                        .action(new DC5Action("ConTest_Form"))
                        .service(DC5Service.builder()
                                .service("Connector-TEST")
                                .serviceType("urn:e-codex:services:")
                                .build()
                        )
                        .sender(DC5EcxAddress.builder()
                                .party(DC5Party.builder().partyId("gw01").build())
                                .build())
                        .build()
                )
                .build();
    }


}
