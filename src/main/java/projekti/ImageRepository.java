package projekti;


import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    
    List<Image> findByAccount(Account account, Sort sort);
    Image findByAccountAndIcon(Account account, boolean icon);

}