
package projekti;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sakorpi
 */
@Service
public class ProfileService {
    @Autowired
    AccountRepository accRepo;
    
    public boolean addProfile(String username, String name, String pass) {
        accRepo.save(new Account(username, name, pass, new HashSet<>(), new HashSet<>(), new ArrayList<>()));
        return true;
    }
    
    @Transactional
    public boolean addFollowerTo(Account acc, Account follower) {
        Set<Account> followers = acc.getFollowers();
        followers.add(follower);
        acc.setFollowers(followers);
        accRepo.save(acc);

        Set<Account> following = follower.getFollowing();
        following.add(acc);
        follower.setFollowing(following);
        accRepo.save(follower);
        return true;
    }
    
    public Integer getFollowerCount(Account acc) {
        if (acc == null) return -99;
        Set<Account> followers = acc.getFollowers();
        if (followers == null || followers.isEmpty()) return 0;
        return followers.size();
    }
    
    public Account getAccountByUsername(String username) {
        Account acc = accRepo.findByUsername(username);
        return acc;
    }
    
    
    public List<Account> getAccountsByNameContaining(String word) {
        return accRepo.findByNameIgnoreCaseContaining(word);
    }
}
