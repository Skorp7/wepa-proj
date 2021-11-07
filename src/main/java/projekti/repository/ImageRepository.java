package projekti.repository;


import projekti.domain.Image;
import projekti.domain.Account;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    
    @EntityGraph(attributePaths = {"comments"})
    List<Image> findByAccount(Account account, Sort sort);
    
    Image findByAccountAndIcon(Account account, boolean icon);

}