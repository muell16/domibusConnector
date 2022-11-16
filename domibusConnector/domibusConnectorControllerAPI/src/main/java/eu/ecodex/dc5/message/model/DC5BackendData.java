package eu.ecodex.dc5.message.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DC5BackendData {

    @Id
    @GeneratedValue
    public long id;

    @Column(name = "CREATED")
    private LocalDateTime created;

    private String backendMessageId;

    private String refToBackendMessageId;

    private String backendConversationId;

}
