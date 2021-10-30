
package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author sakorpi
 */
public interface ProfileRepository  extends JpaRepository<Profile, Long> {
    
    List<Profile> findByProfilename(String profilename);
    
}
