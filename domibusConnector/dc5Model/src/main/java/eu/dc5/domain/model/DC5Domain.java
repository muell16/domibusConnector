package eu.dc5.domain.model;

import javax.persistence.*;
import java.util.Map;


@Entity(name = DC5Domain.TABLE_NAME)
public class DC5Domain {

    public static final String TABLE_NAME = "DC5_DOMAIN";

    @Id
    @Column(name = "ID", nullable = false)
    @TableGenerator(name = "seq" + TABLE_NAME,
            table = DC5PersistenceSettings.SEQ_STORE_TABLE_NAME,
            pkColumnName = DC5PersistenceSettings.SEQ_NAME_COLUMN_NAME,
            pkColumnValue = TABLE_NAME + ".ID",
            valueColumnName = DC5PersistenceSettings.SEQ_VALUE_COLUMN_NAME,
            initialValue = DC5PersistenceSettings.INITIAL_VALUE,
            allocationSize = DC5PersistenceSettings.ALLOCATION_SIZE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq" + TABLE_NAME)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = TABLE_NAME + DC5PersistenceSettings.PROPERTY_SUFFIX,
            joinColumns=@JoinColumn(name= TABLE_NAME,
                    referencedColumnName = "ID"))
    @MapKeyColumn (name="PROPERTY_NAME", nullable = false)
    @Column(name="PROPERTY_VALUE", length = 2048)
    private Map<String, String> properties;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DC5Domain )) return false;
        return id != null && id.equals(((DC5Domain) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
