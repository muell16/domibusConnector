package eu.ecodex.dc5.message.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "DC5_PARTNER")
@NoArgsConstructor
@Getter
@Setter
public class DC5Partner {

    @Builder(toBuilder = true)
    public DC5Partner(DC5EcxAddress partnerAddress, DC5Role partnerRole) {
        this.partnerAddress = partnerAddress;
        this.partnerRole = partnerRole;
    }

    @Id
    @GeneratedValue
    @Column(name = "PARTNER_ID")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARTNER_ADDRESS")
    private DC5EcxAddress partnerAddress;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARTNER_ROLE")
    private DC5Role partnerRole;


}
