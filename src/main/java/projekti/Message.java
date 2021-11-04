package projekti;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message extends AbstractPersistable<Long> {

    private LocalDateTime datetime = LocalDateTime.now();

    private String message;

    @ManyToOne
    @JoinColumn(name = "username")
    private Account accountto;

    private String usernamefrom;

}
