package projekti;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    @Autowired
    AccountRepository accRepo;

    public boolean addProfile(String username, String name, String pass) {
        accRepo.save(new Account(username, name, pass, new HashSet<>(), new HashSet<>(), new ArrayList<>(), new ArrayList<>(), new HashSet<>()));
        return true;
    }

    public boolean isInBlacklist(Account accIsInList, Account listing) {
        return listing.getBlacklist().stream().anyMatch(a -> a.equals(accIsInList));
    }

    public void addToBlacklist(Account accountToList, Account listing) {
        Set<Account> blacklist = listing.getBlacklist();
        blacklist.add(accountToList);
        listing.setBlacklist(blacklist);
        removeFollowerFrom(listing, accountToList);
        removeFollowerFrom(accountToList, listing);
        accRepo.save(listing);
    }

    public void removeFromBlacklist(Account accountToFree, Account listing) {
        Set<Account> blacklist = listing.getBlacklist();
        blacklist.remove(accountToFree);
        listing.setBlacklist(blacklist);
        accRepo.save(listing);
    }

    @Transactional
    public boolean addFollowerTo(Account acc, Account follower) {
        if (!isInBlacklist(follower, acc) && !isInBlacklist(acc, follower)) {
            Set<Account> followers = acc.getFollowers();
            followers.add(follower);
            acc.setFollowers(followers);
            accRepo.save(acc);

            Set<Account> following = follower.getFollowing();
            following.add(acc);
            follower.setFollowing(following);
            accRepo.save(follower);
        }
        return true;
    }

    @Transactional
    public boolean removeFollowerFrom(Account acc, Account follower) {
        Set<Account> followers = acc.getFollowers();
        followers.remove(follower);
        acc.setFollowers(followers);
        accRepo.save(acc);

        Set<Account> following = follower.getFollowing();
        following.remove(acc);
        follower.setFollowing(following);
        accRepo.save(follower);
        return true;
    }

    public boolean isFollowerTo(String username, String follower) {
        Account acc = accRepo.findByUsername(username);
        return acc.getFollowers().stream().anyMatch(a -> a.getUsername().equals(follower));
    }

    public Integer getFollowerCount(Account acc) {
        if (acc == null) {
            return -99;
        }
        Set<Account> followers = acc.getFollowers();
        if (followers == null || followers.isEmpty()) {
            return 0;
        }
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
