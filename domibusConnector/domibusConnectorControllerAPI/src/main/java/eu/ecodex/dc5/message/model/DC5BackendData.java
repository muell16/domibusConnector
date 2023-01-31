package eu.ecodex.dc5.message.model;

import lombok.*;
import org.springframework.core.style.ToStringCreator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DC5_BACKEND_DATA")
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

    public String toString() {
        return new ToStringCreator(this)
                .append("backendMessageId", backendMessageId)
                .append("refToBackendMessageId", refToBackendMessageId)
//                .append("backendConversationId", backendConversationId)
                .toString();
    }

}
