package projekti.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import projekti.domain.MessageService;
import projekti.domain.Account;
import projekti.domain.ImageService;
import projekti.domain.ProfileService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.domain.Comment;
import projekti.domain.Message;

@Controller
public class ProfileController {

    @Autowired
    ProfileService profServ;

    @Autowired
    MessageService msgServ;

    @Autowired
    ImageService imgServ;

    List<Account> accounts;

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account acc = profServ.getAccountByUsername(auth.getName());
//        HashMap<Message, List<Comment>> messages = msgServ.getOwnAndFollowingMessagesByAccount(acc);
//        //Make keys-list which helps to print messages in order.
//        List<Message> keys = new ArrayList<>(messages.keySet());
//        Collections.sort(keys, (Message a1, Message a2) -> a1.getDatetime().compareTo(a2.getDatetime()));
//        Collections.reverse(keys);
        model.addAttribute("account", acc);
        model.addAttribute("messages", msgServ.getOwnAndFollowingMessagesByAccount(acc));
//        model.addAttribute("keys", keys);
        model.addAttribute("icon", imgServ.getIconByUsername(auth.getName()));
        model.addAttribute("imageCount", imgServ.getImageCount(acc));
        return "profile";
    }

    @GetMapping("/profiles/{username}")
    public String profiles(@PathVariable String username, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account acc = profServ.getAccountByUsername(username);
//        //Make keys-list which helps to print messages in order.
//        List<Message> keys = new ArrayList<>(messages.keySet());
//        Collections.sort(keys, (Message a1, Message a2) -> a1.getDatetime().compareTo(a2.getDatetime()));
//        Collections.reverse(keys);
        model.addAttribute("account", acc);
        model.addAttribute("isFollower", profServ.isFollowerTo(acc, auth.getName()));
        model.addAttribute("isOwner", auth.getName().equals(username));
        model.addAttribute("isBlocked", profServ.isInBlacklist(acc, profServ.getAccountByUsername(auth.getName())));
        model.addAttribute("messages", msgServ.getOwnMessagesByAccount(acc));
//        model.addAttribute("keys", keys);
        model.addAttribute("icon", imgServ.getIconByUsername(username));
        return "profiles";
    }

    @PostMapping("/profiles")
    public String profilesFollowOrNot(@RequestParam String username, @RequestParam boolean isFollower) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account follower = profServ.getAccountByUsername(auth.getName());
        Account acc = profServ.getAccountByUsername(username);
        if (isFollower) {
            profServ.removeFollowerFrom(acc, follower);
        } else {
            profServ.addFollowerTo(acc, follower);
        }
        return "redirect:/profiles/" + username;
    }

    @PostMapping("/profiles/block")
    public String profilesBlockOrNot(@RequestParam String username, @RequestParam boolean isBlocked) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account acc = profServ.getAccountByUsername(auth.getName());
        Account accountToList = profServ.getAccountByUsername(username);
        if (isBlocked) {
            profServ.removeFromBlacklist(accountToList, acc);
        } else {
            profServ.addToBlacklist(accountToList, acc);
        }
        return "redirect:/profiles/" + username;
    }

    @PostMapping("/messages")
    public String profilesAddMsg(@RequestParam String message) {
        if (message.length() < 1) {
            return "redirect:/profile";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account accTo = profServ.getAccountByUsername(auth.getName());
        msgServ.addMessage(message, accTo);
        return "redirect:/profile";
    }
    
    @PostMapping("/comment/{id}")
    public String commentMsg(@RequestParam String comment,
            @PathVariable Long id,
            HttpServletRequest request) {
        if (comment.length() < 2 || comment.length() > 200) {
            return "redirect:" +  request.getHeader("Referer");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account commenter = profServ.getAccountByUsername(auth.getName());
        msgServ.addComment(comment, commenter, id, false);
        return "redirect:" +  request.getHeader("Referer");
    }

    @GetMapping("/seek")
    public String seek(Model model) {
        model.addAttribute("accounts", accounts);
        return "seek";
    }

    @PostMapping("/seek")
    public String seek(@RequestParam String word) {
        accounts = profServ.getAccountsByNameContaining(word);
        return "redirect:/seek";
    }
    
    @PostMapping("/like") 
    public String like(@RequestParam String what, @RequestParam Long id, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (what.equals("msg")) {
            msgServ.likeMessage(id, auth.getName());
        } else if (what.equals("comm")) {
            msgServ.likeComment(id, auth.getName());
        } else if (what.equals("img")) {
            imgServ.likeImg(id, auth.getName());
        }
        return "redirect:" +  request.getHeader("Referer");
    }
}

