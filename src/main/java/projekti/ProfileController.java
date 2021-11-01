
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
import org.springframework.web.bind.annotation.PostMapping;
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
