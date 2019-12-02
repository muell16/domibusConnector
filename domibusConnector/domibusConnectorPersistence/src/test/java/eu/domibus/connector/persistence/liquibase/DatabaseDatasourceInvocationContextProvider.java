package eu.domibus.connector.persistence.liquibase;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

import javax.sql.DataSource;
import java.util.stream.Stream;

public class DatabaseDatasourceInvocationContextProvider implements TestTemplateInvocationContextProvider {


    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        return Stream.of(
                new TestTemplateInvocationContext() {

                }
        );
    }


}
