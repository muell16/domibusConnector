package eu.ecodex.dc5.message.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "DC5_PARTNER")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DC5Partner {
    @Id
    @GeneratedValue
    @Column(name = "PARTNER_ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "PARTNER_ADDRESS")
    private DC5EcxAddress partnerAddress;
    @ManyToOne
    @JoinColumn(name = "PARTNER_ROLE")
    private DC5Role partnerRole;


}
