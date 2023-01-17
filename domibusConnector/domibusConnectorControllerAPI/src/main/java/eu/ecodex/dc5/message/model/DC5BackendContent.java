package eu.ecodex.dc5.message.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "DC5_BACKEND_CONTENT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class DC5BackendContent {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(message = "BusinessXML is not allowed to be null!")
    private DC5MessageAttachment businessXml;

    /**
     * The business document here can be of any type,
     * but it has to be supported by ecodex container
     *
     */
    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(message = "BusinessDocument is not allowed to be null!") //ensure at mapping, that
    private DC5MessageAttachment businessDocument;

    @OneToMany(cascade = CascadeType.ALL)
    @Singular
    @JoinTable(name = "DC5_BACKCONT_2_ATT")
    private List<DC5MessageAttachment> attachments;

    @OneToOne(cascade = CascadeType.ALL)
    private DC5MessageAttachment trustTokenXml;

    @OneToOne(cascade = CascadeType.ALL)
    private DC5MessageAttachment trustTokenPDF;


}
