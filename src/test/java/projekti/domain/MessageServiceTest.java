package projekti.domain;

import javax.transaction.Transactional;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import projekti.repository.MessageRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MessageServiceTest {
    
    @Autowired
    private MessageRepository msgRepo;
    
    @Autowired
    private ProfileService profServ;

    @Autowired
    private MessageService msgServ;
    
    @After
    public void after() {
        msgRepo.deleteAll();
    }
    
    @Test
    public void serviceSavesMessage() {
        profServ.addProfile("user", "User", "pass");
        Account acc = profServ.getAccountByUsername("user");
        msgServ.addMessage("Viesti", acc);
        boolean found = msgRepo.findAll().stream().anyMatch(m -> m.getMessage().equals("Viesti"));
        assertTrue(found);
        assertTrue(msgRepo.count() > 0);
    }
    
    @Transactional
    @Test
    public void getsMessagesOfUserAndFollowers() {
        profServ.addProfile("user", "User", "pass");
        profServ.addProfile("follower", "Foll", "pass");
        profServ.addProfile("nobody", "Nob", "pass");
        Account acc = profServ.getAccountByUsername("user");
        Account follower = profServ.getAccountByUsername("follower");
        Account nobody = profServ.getAccountByUsername("nobody");
        profServ.addFollowerTo(acc, follower);
        msgServ.addMessage("Viesti2", acc);
        msgServ.addMessage("Hello", follower);
        msgServ.addMessage("Hello2", follower);
        msgServ.addMessage("Welcome", nobody);

        assertTrue(msgServ.getOwnAndFollowingMessagesByAccount(follower).stream().anyMatch(m -> m.getMessage().equals("Viesti2")));
        assertTrue(msgServ.getOwnAndFollowingMessagesByAccount(follower).size() == 3); 
    }
    
    @Test
    public void getsOwnMessages() {
        profServ.addProfile("user", "User", "pass");
        profServ.addProfile("nobody", "Nob", "pass");
        Account nobody = profServ.getAccountByUsername("nobody");
        Account acc = profServ.getAccountByUsername("user");
        msgServ.addMessage("Viesti2", acc);
        msgServ.addMessage("Viesti3", acc);
        msgServ.addMessage("Hello", nobody);
        
        assertTrue(msgServ.getOwnMessagesByAccount(acc).stream().anyMatch(m -> m.getMessage().equals("Viesti2")));
        assertTrue(msgServ.getOwnMessagesByAccount(acc).size() == 2); 
    }
    
    
}
