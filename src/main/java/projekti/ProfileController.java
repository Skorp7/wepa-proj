
package projekti;

/**
 *
 * @author sakorpi
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {
    @Autowired
    ProfileService profServ;
    
    @GetMapping("/profile")
    public String profile(Model model) {
        String name = "Sinä";
        model.addAttribute("message", name);
        
        return "index";
    }
}
