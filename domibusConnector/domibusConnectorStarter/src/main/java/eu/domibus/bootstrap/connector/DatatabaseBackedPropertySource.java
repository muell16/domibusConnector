package eu.domibus.bootstrap.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DatatabaseBackedPropertySource extends EnumerablePropertySource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatatabaseBackedPropertySource.class);

    private static final String PROPERTY_TABLE_NAME = "DOMIBUS_CONNECTOR_PROPERTIES";
    private static final String PROPERTY_NAME_COLUMN_NAME = "PROPERTY_NAME";
    private static final String PROPERTY_VALUE_COLUMN_NAME = "PROPERTY_VALUE";

    DataSource dataSource;
    private NamedParameterJdbcTemplate jdbcTemplate;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected DatatabaseBackedPropertySource(String name) {
        super(name);
    }

    @PostConstruct
    private void init() {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public String[] getPropertyNames() {
        String query = String.format("SELECT %s FROM %s", PROPERTY_NAME_COLUMN_NAME, PROPERTY_TABLE_NAME);
        List<String> propertyNames = jdbcTemplate.query(query, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(PROPERTY_NAME_COLUMN_NAME);
            }
        });
        LOGGER.trace("Returning discovered properties [{}]", propertyNames);
        return propertyNames.toArray(new String[propertyNames.size()]);
    }
    @Override
    public boolean containsProperty(String name) {
        return getProperty(name) != null;
    }

    @Override
    public Object getProperty(String name) {
        LOGGER.trace("getProperty called with property [{}]", name);
        try {
            String query = String.format("SELECT %s FROM %s WHERE %s=:PROPERTY_NAME", PROPERTY_VALUE_COLUMN_NAME, PROPERTY_TABLE_NAME, PROPERTY_NAME_COLUMN_NAME);
            Map<String, String> params = new HashMap<>();
            params.put("PROPERTY_NAME", name);
            String s = jdbcTemplate.queryForObject(query, params, String.class);
            LOGGER.trace("Resolved property: [{}]=[{}]", name, s);
            return s;
        } catch (EmptyResultDataAccessException emptyResultSet) {
            LOGGER.debug("Cannot resolve property [{}]", name);
            return null;
        }
    }
}
