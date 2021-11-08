
package projekti.domain;

import java.util.HashSet;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @OneToMany
    List<Comment> comments;
    
    private HashSet<String> likes = new HashSet<>();;
}