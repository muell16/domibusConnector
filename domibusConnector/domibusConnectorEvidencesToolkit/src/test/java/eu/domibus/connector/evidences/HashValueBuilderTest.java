package eu.domibus.connector.evidences;

import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Test;

import eu.domibus.connector.evidences.HashValueBuilder;

import static org.assertj.core.api.Assertions.assertThat;

public class HashValueBuilderTest {


    @Test(expected = IllegalArgumentException.class)
    public void testWrongAlgorithm() {
        new HashValueBuilder("SHI-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoAlgorithm() {
        new HashValueBuilder("");
    }

    @Test
    public void testAllAlgorithmsFromEnum()  {
        for (HashValueBuilder.HashAlgorithm algorithm : HashValueBuilder.HashAlgorithm.values()) {
            new HashValueBuilder(algorithm);
        }
    }

}
