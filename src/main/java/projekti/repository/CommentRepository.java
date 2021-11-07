
package projekti.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
       
}
