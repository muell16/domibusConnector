package eu.ecodex.dc5.events.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class JPAEventStorageItem {

    @Id
    @GeneratedValue
    private long id;

    private String event;

    private LocalDateTime created;

    private LocalDateTime consumed;

}
