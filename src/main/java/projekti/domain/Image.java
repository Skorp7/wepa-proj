
package projekti.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image extends AbstractPersistable<Long> {

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] content;
    
    @ManyToOne
    @JoinColumn(name = "username")
    private Account account;
    
    private boolean icon;
    
    private String text;

}