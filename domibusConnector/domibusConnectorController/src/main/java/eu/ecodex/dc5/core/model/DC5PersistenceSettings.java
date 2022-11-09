package eu.ecodex.dc5.core.model;

public interface DC5PersistenceSettings {

    String SEQ_STORE_TABLE_NAME = "DC5_SEQ_STORE";
    String SEQ_NAME_COLUMN_NAME = "SEQ_NAME";
    String SEQ_VALUE_COLUMN_NAME = "SEQ_VALUE";
    String PROPERTY_SUFFIX = "_PROPS";
    int INITIAL_VALUE = 1000;
    int ALLOCATION_SIZE = 1;
    int ALLOCATION_SIZE_BULK = 50;
}
