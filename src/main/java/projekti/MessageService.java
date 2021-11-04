package projekti;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    MessageRepository msgRepo;

    public boolean addMessage(String message, Account accountto, String usernameFrom) {
        Message msg = new Message();
        msg.setAccountto(accountto);
        msg.setMessage(message);
        msg.setUsernamefrom(usernameFrom);
        msgRepo.save(msg);
        return true;
    }

    public List<Message> getOwnMessagesByAccount(Account acc) {
        Pageable pageable = PageRequest.of(0, 25, Sort.by("datetime").descending());
        return msgRepo.findByAccounttoAndUsernamefrom(acc, acc.getUsername(), pageable);
    }

    public List<Message> getOwnAndFollowingMessagesByAccount(Account acc) {
        Pageable pageable = PageRequest.of(0, 25, Sort.by("datetime").descending());
        return msgRepo.findAllByAccountId(acc.getId());
    }
}
