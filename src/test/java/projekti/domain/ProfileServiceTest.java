package projekti.domain;

import java.util.List;
import javax.transaction.Transactional;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import projekti.repository.AccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProfileServiceTest {

    @Autowired
    private AccountRepository accRepo;

    @Autowired
    private ProfileService profServ;
    
    @After
    public void after() {
        accRepo.deleteAll();
    }

    @Test
    public void serviceSavesAccount() {
        profServ.addProfile("usernameLisa", "Lisa", "pass");
        boolean found = accRepo.findAll().stream().anyMatch(a -> a.getUsername().equals("usernameLisa") && a.getName().equals("Lisa"));
        assertTrue(found);
        assertTrue(accRepo.count() > 0);
    }

    @Test
    public void repoSavesAccount() {
        Account acc = new Account();
        acc.setUsername("user");
        acc.setName("Anni");
        accRepo.save(acc);
        boolean found = accRepo.findAll().stream().anyMatch(a -> a.getUsername().equals("user") && a.getName().equals("Anni"));
        assertTrue(found);
        assertTrue(accRepo.count() > 0);
    }

    @Test
    public void accountIsFoundByUsername() {
        Account acc = new Account();
        acc.setUsername("user2");
        acc.setName("Anne");
        accRepo.save(acc);
        Account accFound = profServ.getAccountByUsername("user2");
        assertEquals("user2", accFound.getUsername());
        assertEquals("Anne", accFound.getName());
    }

    @Test
    public void listListsAllRequestedNames() {
        Account accR = new Account();
        Account accV = new Account();
        Account accU = new Account();
        accR.setName("Risu");
        accR.setUsername("ri");
        accV.setName("Veltsu");
        accV.setUsername("vp");
        accU.setName("Ulla");
        accU.setUsername("ulla");
        accRepo.save(accR);
        accRepo.save(accV);
        accRepo.save(accU);
        List<Account> res = profServ.getAccountsByNameContaining("su");
        assertTrue(res.size() == 2);
        assertTrue(res.contains(accV));
        assertFalse(res.contains(accU));
    }

    @Test
    public void noSameAccountByUsernameCreated() {
        profServ.addProfile("timppa", "Timo", "pass");
        profServ.addProfile("timppa", "Teemu", "password");
        boolean found = accRepo.findAll().stream().anyMatch(a -> a.getUsername().equals("timppa") && a.getName().equals("Teemu"));
        assertFalse(accRepo.findAll().size() == 2);
        assertFalse(found);
    }

    @Test
    @Transactional
    public void followerAdded() {
        profServ.addProfile("eetu", "Eetu", "pass");
        profServ.addProfile("fan", "BigFan", "passw");
        Account eetu = profServ.getAccountByUsername("eetu");
        Account fan = profServ.getAccountByUsername("fan");
        profServ.addFollowerTo(eetu, fan);
        boolean foundFollower = profServ.getAccountByUsername("eetu").getFollowers().stream().anyMatch(a -> a.getUsername().equals("fan"));
        boolean foundFollowing = profServ.getAccountByUsername("fan").getFollowing().stream().anyMatch(a -> a.getUsername().equals("eetu"));
        assertTrue(profServ.getFollowerCount(eetu) == 1);
        assertTrue(foundFollower);
        assertTrue(foundFollowing);
    }

    @Test
    @Transactional
    public void followerRemoved() {
        profServ.addProfile("user", "Iikka", "pass");
        profServ.addProfile("foll", "BigBigFan", "passw");
        Account user = profServ.getAccountByUsername("user");
        Account foll = profServ.getAccountByUsername("foll");
        profServ.addFollowerTo(user, foll);
        profServ.removeFollowerFrom(user, foll);
        boolean foundFollower = profServ.getAccountByUsername("user").getFollowers().stream().anyMatch(a -> a.getUsername().equals("foll"));
        boolean foundFollowing = profServ.getAccountByUsername("foll").getFollowing().stream().anyMatch(a -> a.getUsername().equals("user"));
        assertFalse(profServ.getFollowerCount(user) == 1);
        assertFalse(foundFollower);
        assertFalse(foundFollowing);
    }

    @Test
    @Transactional
    public void isFollowerReturnsTrue() {
        profServ.addProfile("user2", "Iikka", "pass");
        profServ.addProfile("foll2", "BigBigFan", "passw");
        Account user2 = profServ.getAccountByUsername("user2");
        Account foll2 = profServ.getAccountByUsername("foll2");
        profServ.addFollowerTo(user2, foll2);
        assertTrue(profServ.isFollowerTo("user2", "foll2"));
    }

    @Test
    @Transactional
    public void addedToBlackList() {
        profServ.addProfile("vesa", "Vesa", "pass");
        profServ.addProfile("bullier", "Dum", "passw");
        Account vesa = profServ.getAccountByUsername("vesa");
        Account bullier = profServ.getAccountByUsername("bullier");
        profServ.addToBlacklist(bullier, vesa);
        assertTrue(profServ.getAccountByUsername("vesa").getBlacklist().stream().anyMatch(a -> a.getUsername().equals("bullier")));
    }

    @Test
    @Transactional
    public void cantAddFollowerIfInBlacklist() {
        profServ.addProfile("eetu2", "Eetu2", "pass");
        profServ.addProfile("fan2", "BigFan2", "passw");
        Account eetu2 = profServ.getAccountByUsername("eetu2");
        Account fan2 = profServ.getAccountByUsername("fan2");
        profServ.addToBlacklist(fan2, eetu2);
        profServ.addFollowerTo(eetu2, fan2);
        boolean foundFollower = profServ.getAccountByUsername("eetu2").getFollowers().stream().anyMatch(a -> a.getUsername().equals("fan2"));
        boolean foundFollowing = profServ.getAccountByUsername("fan2").getFollowing().stream().anyMatch(a -> a.getUsername().equals("eetu2"));
        assertFalse(profServ.getFollowerCount(eetu2) == 1);
        assertFalse(foundFollower);
        assertFalse(foundFollowing);
    }

}
