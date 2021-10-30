
package projekti;

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
    ProfileRepository profRepo;
    
    public boolean addProfile(String name, String pass, String profilename) {
        profRepo.save(new Profile(name, pass, profilename, new HashSet<>(), new HashSet<>()));
        return true;
    }
    
    @Transactional
    public boolean addFollowerTo(Profile profile, Profile follower) {
        Set<Profile> followers = profile.getFollowers();
        followers.add(follower);
        profile.setFollowers(followers);
        profRepo.save(profile);

        Set<Profile> following = follower.getFollowing();
        following.add(profile);
        follower.setFollowing(following);
        profRepo.save(follower);
        return true;
    }
    
    public Profile getProfileByProfileName(String profilename) {
        List<Profile> lista = profRepo.findByProfilename(profilename);
        return lista.get(0);
    }
}
