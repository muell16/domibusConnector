package eu.domibus.connector.security.container.service;

import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;



@TestPropertySource({"classpath:test.properties", "classpath:test-sig.properties"})
public class ECodexContainerFactoryService_SignatureBased_ITCase extends ECodexContainerFactoryServiceITCaseTemplate {


}