package eu.ecodex.dc5.message.model;

import lombok.*;

import javax.persistence.*;
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
    public Long id;

    @Column(name = "CREATED")
    private LocalDateTime created;

    @Convert(converter = BackendMessageIdConverter.class)
    private BackendMessageId backendMessageId;

    @Convert(converter = BackendMessageIdConverter.class)
    private BackendMessageId refToBackendMessageId;

    private String backendConversationId;

}
