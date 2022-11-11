package eu.ecodex.dc5.events;

import eu.ecodex.dc5.process.MessageProcessId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter(AccessLevel.MODULE)
//@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DC5Event {

    private boolean processed = false;
    private MessageProcessId messageProcessId;

}
