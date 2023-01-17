package eu.ecodex.dc5.events.model;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = JPAEventStorageItem.TABLE_NAME)
@Getter
@Setter
@NoArgsConstructor
public class JPAEventStorageItem {

    public final static String TABLE_NAME = "DC5_EVTITM";

    @Id
    @GeneratedValue
    private long id;

    private String event;

    private LocalDateTime created;

    private LocalDateTime consumed;

}
