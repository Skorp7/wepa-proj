package projekti;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByAccounttoAndUsernamefrom(Account accountto, String usernamefrom, Pageable pageable);

    @Query(value = "SELECT * FROM MESSAGE WHERE (ID IN (SELECT ID FROM MESSAGE LEFT JOIN FOLLOWING ON (USERNAME = ACCOUNT_ID) "
            + "WHERE FOLLOWING_ID = 20 ORDER BY DATETIME DESC LIMIT 25))\n"
            + "UNION\n"
            + "SELECT * FROM MESSAGE WHERE (USERNAME = 20)\n"
            + "ORDER BY DATETIME DESC LIMIT 25;", nativeQuery = true)
    List<Message> findAllByAccountId(Long id);

}
