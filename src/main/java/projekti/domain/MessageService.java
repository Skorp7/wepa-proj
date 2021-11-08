package projekti.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projekti.repository.CommentRepository;
import projekti.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    MessageRepository msgRepo;

    @Autowired
    CommentRepository commRepo;

    @Autowired
    ImageService imgServ;

    @Autowired
    ProfileService profServ;

    public boolean addMessage(String message, Account accountto) {
        Message msg = new Message();
        msg.setAccountto(accountto);
        msg.setMessage(message);
        msg.setComments(new ArrayList<>());
        msgRepo.save(msg);
        return true;
    }

    public List<Message> getOwnMessagesByAccount(Account acc) {
        Pageable pageable = PageRequest.of(0, 25, Sort.by("datetime").descending());
        return msgRepo.findOwnByAccountto(acc, pageable);
    }

//    public HashMap<Message, List<Comment>> getOwnMessagesByAccount(Account acc) {
//        return sortMessagesAndComments(getOwnMessagesByAccountUnSorted(acc));
//    }
    public List<Message> getOwnAndFollowingMessagesByAccount(Account acc) {
        List<Message> messages = new ArrayList<>();
        messages.addAll(this.getOwnMessagesByAccount(acc));
        Set<Account> following = acc.getFollowing();
        for (Account a : following) {
            List<Message> msgs = this.getOwnMessagesByAccount(a);
            messages.addAll(msgs);
        }
        Collections.sort(messages, (Message a1, Message a2) -> a1.getDatetime().compareTo(a2.getDatetime()));
        Collections.reverse(messages);
        List<Message> cutMessages = messages.subList(0, Math.min(25, messages.size()));
        //return msgRepo.findAllByAccountId(acc.getId());
        return cutMessages;
    }

//    public HashMap<Message, List<Comment>> sortMessagesAndComments(List<Message> msgs) {
//        HashMap<Message, List<Comment>> messages = new HashMap<>();
//        for (Message msg : msgs) {
//                List<Comment> comments = msg.getComments();
//                Collections.sort(comments, (Comment a1, Comment a2) -> a1.getDatetime().compareTo(a2.getDatetime()));
//                Collections.reverse(comments);
//                List<Comment> cutComments = comments.subList(0, Math.min(10,comments.size()));
//                Collections.reverse(cutComments);
//                messages.put(msg, cutComments);
//            }
//        return messages;
//    }
    @Transactional
    public boolean addComment(String comment, Account commenter, Long id, boolean isImage) {
        Comment comm = new Comment();
        comm.setAccount(commenter);
        comm.setComment(comment);
        commRepo.save(comm);
        if (isImage) {
            imgServ.addCommentToImage(id, comm);
        } else {
            Message msg = msgRepo.getOne(id);
            List<Comment> commentList = msg.getComments();
            commentList.add(comm);
            msgRepo.save(msg);
        }        
        return true;
    }

    @CacheEvict(cacheNames = "images", allEntries = true)
    public void likeComment(long id, String accId) {
        Comment comm = commRepo.getOne(id);
        HashSet<String> likes = comm.getLikes();
        if (!likes.add(accId)) {
            likes.remove(accId);
        }
        comm.setLikes(likes);
        commRepo.save(comm);
    }

    public void likeMessage(long id, String accId) {
        Message message = msgRepo.getOne(id);
        HashSet<String> likes = message.getLikes();
        if (!likes.add(accId)) {
            likes.remove(accId);
        }
        message.setLikes(likes);
        msgRepo.save(message);
    }

}
