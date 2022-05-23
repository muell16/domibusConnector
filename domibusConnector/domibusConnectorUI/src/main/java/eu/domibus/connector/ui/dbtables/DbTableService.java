package eu.domibus.connector.ui.dbtables;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbTableService {

    private final DataSource ds;
    private final DbTableServiceConfigurationProperties config;

    public DbTableService(DataSource ds, DbTableServiceConfigurationProperties config) {
        this.config = config;
        this.ds = ds;
    }

    public List<String> getTables() {
        return config.getTables();
    }

//    public List<String> getColumns(String tableName) {
//        ds.getConnection()
//                .getMetaData()
//                .getColumns();
//    }

    public List<ColumnDefinition> getColumns(String table) {
        List<ColumnDefinition> columns = new ArrayList<>();
        try {
            ResultSet rs = ds.getConnection()
                    .getMetaData()
                    .getColumns(null, null, table, null);
            while (rs.next()) {
                ColumnDefinition cd = new ColumnDefinition();
                String columnName = rs.getString(4); //get column name
                cd.setColumnName(columnName);
                cd.setDataType(rs.getString(5)); //java.sql.Type
                columns.add(cd);
            }
            return columns;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public class ColumnDefinition {
        private String columnName;
        private String dataType;

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }
    }


}
