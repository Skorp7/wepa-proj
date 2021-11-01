
package projekti;

/**
 *
 * @author sakorpi
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {
    @Autowired
    ProfileService profServ;

    
    @GetMapping("/profile")
    public String profile() {
        String name = "Sinä";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        name = auth.getName();

        return "index";
    }
}
