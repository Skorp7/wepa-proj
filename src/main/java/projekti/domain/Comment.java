package projekti.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends AbstractPersistable<Long> {
    
    private LocalDateTime datetime = LocalDateTime.now();

    private String comment;

    @ManyToOne
    @JoinColumn(name = "username")
    private Account account;
    
    private HashSet<String> likes = new HashSet<>();
    
}
