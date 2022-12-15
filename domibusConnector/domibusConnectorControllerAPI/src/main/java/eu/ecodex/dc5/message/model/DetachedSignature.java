package eu.ecodex.dc5.message.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.Serializable;


/**
 * @author riederb
 * @version 1.0
 */
@Entity

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DetachedSignature implements Serializable {

	@GeneratedValue
	@Id
	private long id;

	@Lob
	private byte detachedSignature[];
	private String detachedSignatureName;
	private DetachedSignatureMimeType mimeType;


}