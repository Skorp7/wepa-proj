
package projekti;

/**
 *
 * @author sakorpi
 */
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    
    List<Image> findByOwner(String owner, Sort sort);
    Image findByOwnerAndIcon(String owner, boolean icon);

}