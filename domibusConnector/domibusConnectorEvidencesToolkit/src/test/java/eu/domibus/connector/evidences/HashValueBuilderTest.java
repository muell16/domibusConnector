package eu.domibus.connector.evidences;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HashValueBuilderTest {


    @Test
    public void testWrongAlgorithm() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new HashValueBuilder("SHI-1");
        });
    }

    @Test
    public void testNoAlgorithm() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new HashValueBuilder("");
        });
    }

    @Test
    public void testAllAlgorithmsFromEnum()  {
        for (HashValueBuilder.HashAlgorithm algorithm : HashValueBuilder.HashAlgorithm.values()) {
            new HashValueBuilder(algorithm);
        }
    }

}
