package eu.domibus.connector.evidences;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class HashValueBuilderAlgorithmTest {

    static List<Object[]> testdata = new ArrayList<>();

    static final String INITIAL_VALUE = "This is a JUnit Test to test the HashValueBuilder";

    @Parameterized.Parameters
    public static Collection<Object[]> testdata() {
        addTestdata(HashValueBuilder.HashAlgorithm.MD5, "71bb69d05ca4f082e6d7ab99212f0713");
        addTestdata(HashValueBuilder.HashAlgorithm.SHA1, "2ce13638c8e2b792a9f381f8d5bd409dd158057f");
        addTestdata(HashValueBuilder.HashAlgorithm.SHA256, "cbb0dd0acbcd7a98a6e1b5ff9685d10874de9d9bdbcc4a5fa12a258c285908db");
        addTestdata(HashValueBuilder.HashAlgorithm.SHA512, "19f4cf71e261a4e6ecb52a93fb2c4bd993397399219362c0cbe7670af9556d32e3c7331f17381321604e9f01af7ccbff0cba9f1e176c44aae0e38250e95d55c1");
        //not implemented yet!
        //addTestdata(HashValueBuilder.HashAlgorithm.SHA3_256, "a6a635e647751f21ae714579f1e5550ceee00bbec0e58c2ed47990c506ac8c5c");
        //addTestdata(HashValueBuilder.HashAlgorithm.SHA3_384, "4eb5088e9bb57984900d9b422ee5cc02be8f06b3d85c8923ec8889bf7e9523ad37248c5f32ea1384df1021e49861ee76");
        //addTestdata(HashValueBuilder.HashAlgorithm.SHA3_512, "9d1c6e91fb5dbd48b94811278180c751dc95ba86755ce43003f064a669df82978256b44ae2c0547dbcf2d798dafa98f81cf31d38fb52f269cbd4617ba51d3b7c");
        return testdata;
    }

    private static void addTestdata(HashValueBuilder.HashAlgorithm algorithm, String expectedHashValue) {
        testdata.add(new Object[]{algorithm, expectedHashValue});
    }

    @Parameterized.Parameter(0)
    public HashValueBuilder.HashAlgorithm hashAlgorithm;

    @Parameterized.Parameter(1)
    public String expectedHashValue;


    @Test
    public void testHashAlgorithm() throws NoSuchAlgorithmException {
        HashValueBuilder builder = new HashValueBuilder(hashAlgorithm.toString());
        String hash = builder.buildHashValueAsString(INITIAL_VALUE.getBytes());
        assertThat(hash).isEqualTo(expectedHashValue);
    }



}
