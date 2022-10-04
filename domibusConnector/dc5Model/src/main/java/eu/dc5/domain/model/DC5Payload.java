package eu.dc5.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class DC5Payload {
    private Long id;
    private DC5PayloadType payloadType = DC5PayloadType.EVIDENCE;
    private String hash;
    private String name;
    private String storageRef;
    private BigInteger size;
}
