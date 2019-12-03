package eu.domibus.connector.persistence.testutil;


import eu.domibus.connector.persistence.testutil.H2TestDatabaseFactory;
import eu.domibus.connector.persistence.testutil.TestDatabase;
import eu.domibus.connector.persistence.testutil.TestDatabaseFactory;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LiquibaseTemplateInvocationContextProvider implements TestTemplateInvocationContextProvider {


    @Override
    public boolean supportsTestTemplate(ExtensionContext extensionContext) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext) {
        //TODO: load Test method annotations...

        Method testMethod = extensionContext.getTestMethod().get();
        FromVersion[] annotationsByType = testMethod.getAnnotationsByType(FromVersion.class);

        return Stream.of(H2TestDatabaseFactory.h2Mysql(), H2TestDatabaseFactory.h2Oracle())
                .map(tdbfactory -> Stream.of(annotationsByType)
                                .map(fromVersion -> fromVersion.value())
                                .map(s -> s.isEmpty() ? null : s)
                                .map(v -> invocationContext(tdbfactory.createNewDatabase(v))
                )).flatMap(i -> i)
                ;
    }


    private TestTemplateInvocationContext invocationContext(TestDatabase testDatabase) {
        return new TestTemplateInvocationContext() {

            @Override
            public String getDisplayName(int invocationIndex) {
                return testDatabase.getName();
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return Arrays.asList(new ParameterResolver() {
                    @Override
                    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
                        return parameterContext.getParameter().getType().equals(Properties.class);
                    }

                    @Override
                    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
                        String id = extensionContext.getUniqueId();
                        TestDatabase testDatabase = (TestDatabase) extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).get(id + "testdb");
                        return testDatabase.getProperties();
                    }
                }, (BeforeEachCallback) extensionContext -> {

                    String id = extensionContext.getUniqueId();
//                    TestDatabase testDatabase = configuration.createNewDatabase(null);
                    extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).put(id + "testdb", testDatabase);

                }, (AfterEachCallback) extensionContext -> {
                    String id = extensionContext.getUniqueId();
                    TestDatabase testDatabase = (TestDatabase) extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).get(id + "testdb");
                    testDatabase.close(); //close test database after test
                });
            }
        };
    }

}
