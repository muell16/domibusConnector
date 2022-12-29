package eu.ecodex.dc5.message.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


/**
 * @author riederb
 * @version 1.0
 */
@Entity
@Table(name = DC5DetachedSignature.TABLE_NAME)

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DC5DetachedSignature implements Serializable {

	public static final String TABLE_NAME = "DC5_DETACHED_SIGNATURE";

	@GeneratedValue
	@Id
	private long id;

	@Lob
	private byte detachedSignature[];

	private String detachedSignatureName;
	private DetachedSignatureMimeType mimeType;


}