package eu.dc5.domain.model;

import java.time.LocalDateTime;
import java.util.Map;

public class DC5MsgProcess {
    private Long id;
    private String processId;
    private LocalDateTime created;
    private LocalDateTime finished;
    private DC5Message message; // TODO: reference or only id?
    private Map<String, String> properties; // TODO: does DC5MsgProcessProperty really need to be a class?
    private DC5ProcessStep currProcStep;
    private DC5Domain optionalDomain;
}
