package projekti.domain;

import javax.transaction.Transactional;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
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
    
    @Transactional
    @Test
    public void ableToLikeAndDislike() {
        profServ.addProfile("liker", "Liz", "pass");
        profServ.addProfile("writer", "Writz", "pass");
        Account acc = profServ.getAccountByUsername("writer");
        msgServ.addMessage("Kiva viesti", acc);
        Message msg = msgServ.getOwnMessagesByAccount(acc).get(0);
        assertTrue(msg.getLikes().size() == 0);
        msgServ.likeMessage(msg.getId(), "liker");
        assertTrue(msg.getLikes().size() == 1);
        msgServ.likeMessage(msg.getId(), "liker");
        assertTrue(msg.getLikes().size() == 0);
    }
    
    @Transactional
    @Test
    public void commentGoesToTheRightMessage() {
        profServ.addProfile("commenter", "Comz", "pass");
        profServ.addProfile("writer2", "Writz2", "pass");
        Account acc = profServ.getAccountByUsername("writer2");
        Account accComm = profServ.getAccountByUsername("commenter");
        msgServ.addMessage("Mielenkiintoa herättävä viesti", acc);
        Message msg = msgServ.getOwnMessagesByAccount(acc).get(0);
        msgServ.addComment("Kommentti!", accComm, msg.getId(), false);
        assertTrue(msg.getComments().size() == 1);
        assertEquals(msg.getComments().get(0).getComment(), "Kommentti!");
    }
}
