package eu.ecodex.connector.evidences;

import java.security.NoSuchAlgorithmException;

import junit.framework.Assert;

import org.junit.Test;

public class HashValueBuilderTest {

    @Test
    public void testBuildHashValueMD5() {
        try {
            HashValueBuilder builder = new HashValueBuilder("MD5");
            Assert.assertEquals("MD5", builder.getAlgorithm().toString());

            String initialValue = "This is a JUnit Test to test the HashValueBuilder";

            String hash = builder.buildHashValueAsString(initialValue.getBytes());

            String expectedHash = "71bb69d05ca4f082e6d7ab99212f0713";

            Assert.assertEquals(expectedHash, hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testBuildHashValueSHA1() {
        try {
            HashValueBuilder builder = new HashValueBuilder("SHA-1");
            Assert.assertEquals("SHA-1", builder.getAlgorithm().toString());

            String initialValue = "This is a JUnit Test to test the HashValueBuilder";

            String hash = builder.buildHashValueAsString(initialValue.getBytes());

            String expectedHash = "2ce13638c8e2b792a9f381f8d5bd409dd158057f";

            Assert.assertEquals(expectedHash, hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testWrongAlgorithm() {
        try {
            new HashValueBuilder("SHI-1");
            Assert.fail();
        } catch (NoSuchAlgorithmException e) {
        }
    }

    @Test
    public void testNoAlgorithm() {
        try {
            new HashValueBuilder("");
            Assert.fail();
        } catch (NoSuchAlgorithmException e) {
        }
    }
}
