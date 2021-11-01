
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
    
    List<Account> accounts;
    
    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("account", profServ.getAccountByUsername(auth.getName()));
        return "profile";
    }
    
    @GetMapping("/profiles/{username}")
    public String profiles(@PathVariable String username, Model model) {
        model.addAttribute("account", profServ.getAccountByUsername(username));
        return "profiles";
    }
    
    @PostMapping("/profiles")
    public String profilesAdd(@RequestParam String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account follower = profServ.getAccountByUsername(auth.getName());
        Account acc = profServ.getAccountByUsername(username);
        profServ.addFollowerTo(acc, follower);
        return "redirect:/profiles/" + username;
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
