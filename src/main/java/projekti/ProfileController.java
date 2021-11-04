
package projekti;

/**
 *
 * @author sakorpi
 */
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

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
        model.addAttribute("account", acc);
        model.addAttribute("messages", msgServ.getOwnAndFollowingMessagesByAccount(acc));
        model.addAttribute("icon", imgServ.getIconByUsername(auth.getName()));
        model.addAttribute("imageCount", imgServ.getImageCount(acc));
        return "profile";
    }
    
    @GetMapping("/profiles/{username}")
    public String profiles(@PathVariable String username, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account acc = profServ.getAccountByUsername(username);
        model.addAttribute("account", acc);
        model.addAttribute("isFollower", profServ.isFollowerTo(username, auth.getName()));
        model.addAttribute("isOwner", auth.getName().equals(username));
        model.addAttribute("isBlocked", profServ.isInBlacklist(acc,profServ.getAccountByUsername(auth.getName())));
        model.addAttribute("messages", msgServ.getOwnMessagesByAccount(acc));
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
    public String profilesAddMsg(@RequestParam String username, 
            @RequestParam String message) {
        if (message.length() < 1) {
            return "redirect:/profile";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account accTo = profServ.getAccountByUsername(username);
        msgServ.addMessage(message, accTo, auth.getName());
        return "redirect:/profile";
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
}
