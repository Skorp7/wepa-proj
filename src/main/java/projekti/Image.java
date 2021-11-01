
package projekti;

import javax.persistence.Entity;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

// muita sopivia annotaatioita
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image extends AbstractPersistable<Long> {

    @Lob
    private byte[] content;
    
    private String owner;

}