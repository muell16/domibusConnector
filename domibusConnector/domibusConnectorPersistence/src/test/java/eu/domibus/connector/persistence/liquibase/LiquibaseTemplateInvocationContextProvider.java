package eu.domibus.connector.persistence.liquibase;


import org.junit.jupiter.api.extension.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LiquibaseTemplateInvocationContextProvider implements TestTemplateInvocationContextProvider {

    public static final String H2_CONFIG = "H2";
    public static final String MYSQL_CONFIG = "mysql";
    public static final String ORACLE_CONFIG = "oracle";
    public static final String H2_PROFILE = "db_h2";
    public static final String MYSQL_PROFILE = "db_mysql";
    public static final String ORACLE_PROFILE = "db_oracle";
    public static String TEST_FILE_RESULTS_DIR_PROPERTY_NAME = "test.file.results";
    public static final String TEST_MYSQL_PROPERTY_PREFIX = "mysql";
    public static final String TEST_ORACLE_PROPERTY_PREFIX = "oracle";

    @Override
    public boolean supportsTestTemplate(ExtensionContext extensionContext) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext) {
        try {
            return Stream.of(H2_CONFIG, MYSQL_CONFIG, ORACLE_CONFIG)
                    .map(this::loadProperties)
                    .filter(p -> p != null)
                    .map(c -> invocationContext(c));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /*
     * TEST HELPER
     *
     *  Only return configuration if, the maven environment settings are set accordingly!
     *
     */
    protected DbConfig loadProperties(String prefix) {
        //embedded database works always
        if (H2_CONFIG.equals(prefix))
            return createDbConfig(prefix, new Properties());

        //TODO: use docker container if available for mysql, oracle, db tests

        if("true".equalsIgnoreCase(System.getenv("test.db." + prefix + ".enabled"))) {
            return initDbConfigByEnvironment(prefix);
        }
        return null;
    }

    protected Properties loadH2TestProperties() {
        Properties p = new Properties();
        p.setProperty("spring.datasource.url", "jdbc:h2:mem:testdb");
        p.setProperty("spring.datasource.username", "sa");
        p.setProperty("spring.datasource.password", "");
        p.setProperty("spring.datasource.driver-class-name", "org.h2.Driver");

        return p;
    }

    private DbConfig initDbConfigByEnvironment(String prefix) {
        final String p = "test." + prefix;
        Properties props = new Properties();
        Map<String, String> env = System.getenv();
        Map<Object, Object> collect = env.entrySet().stream().filter((Map.Entry<String, String> entry) -> {
            return entry.getKey() != null && entry.getKey().toUpperCase().startsWith(p.toUpperCase());
        }).map((Map.Entry<String, String> entry) -> {
            return new AbstractMap.SimpleEntry(entry.getKey().substring(p.length() + 1).toLowerCase(), entry.getValue().toLowerCase());
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        //.collect(Collectors.toMap(Entry::getKey(), Entry::getValue()));
        props.putAll(collect);

        return createDbConfig(prefix, props);
    }


    private TestTemplateInvocationContext invocationContext(DbConfig configuration) {
        return new TestTemplateInvocationContext() {

            @Override
            public String getDisplayName(int invocationIndex) {
                return configuration.configName + "_empty_DB";
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
                        return extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).get(id + "props");
                    }
                }, (BeforeEachCallback) extensionContext -> {


                    String id = extensionContext.getUniqueId();
                    Properties newProps = new Properties(configuration.props);
                    if (H2_CONFIG.equals(configuration.configName)) {
                        //ensure unique database name per test!
                        newProps.setProperty("spring.datasource.url", "jdbc:h2:mem:db_" + UUID.randomUUID().toString().substring(0,8));
                    }
                    extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).put(id + "props", newProps);
                }, (AfterEachCallback) extensionContext -> {
                    String id = extensionContext.getUniqueId();

                });
            }
        };
    }


    org.springframework.core.env.PropertiesPropertySource p;

//    private DataSource createDataSource(Properties p) {
////        DataSourceProperties dsp = new DataSourceProperties();
////
////        Iterable<ConfigurationPropertySource> from = ConfigurationPropertySources.from(new PropertiesPropertySource("t", p));
////
////        Binder binder = new Binder(from);
////        BindResult<DataSourceProperties> bindResult = binder.bind("spring.datasource", Bindable.of(DataSourceProperties.class));
////
//////        DataSourceProperties dsp = bindResult.get();
////
////        return dsp.initializeDataSourceBuilder().build();
//    }

    private static class DbConfig {
        String configName;
        Properties props;
    }

    private DbConfig createDbConfig(String configName, Properties p) {
        DbConfig dbConfig = new DbConfig();
        dbConfig.configName = configName;
        dbConfig.props = p;
        return dbConfig;
    }


}
