package eu.ecodex.dc5.process.model;

import eu.ecodex.dc5.process.MessageProcessId;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Entity(name = DC5MsgProcess.TABLE_NAME)

@Getter
@Setter
@NoArgsConstructor
public class DC5MsgProcess {

    public static final String TABLE_NAME = "DC5_MSG_PROCESS";

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue
    private Long id;

    @Column(name = "PROCESS_ID")
    @Convert(converter = MessageProcessIdConverter.class)
    private MessageProcessId processId;

    @Column(name = "CREATED")
    private LocalDateTime created;

    @Column(name = "FINISHED")
    private LocalDateTime finished;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DC5ProcessStep> procStepList = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable
    @MapKeyColumn(name = "PROPERTY_NAME", nullable = false)
    @Column(name = "PROPERTY_VALUE", length = 2048)
    private Map<String, String> properties;

    public void addProcessStep(DC5ProcessStep processStep) {
        this.procStepList.add(processStep);
    }

    public static class MessageProcessIdConverter implements AttributeConverter<MessageProcessId, String> {

        @Override
        public String convertToDatabaseColumn(MessageProcessId attribute) {
            if (attribute == null) {
                return null;
            }
            return attribute.getProcessId();
        }

        @Override
        public MessageProcessId convertToEntityAttribute(String dbData) {
            if (dbData == null) {
                return null;
            }
            return MessageProcessId.ofString(dbData);
        }
    }

}
