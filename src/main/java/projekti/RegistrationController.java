package projekti;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    @Autowired
    AccountRepository accRepo;
    
    @Autowired
    SecurityConfiguration securityConf;
    
    @ModelAttribute
    private Account getRegistration() {
        return new Account();
    }
    
    @GetMapping("/registration")
    public String view() {
        return "regform";
    }

    @PostMapping("/registration")
    public String register(@Valid
            @ModelAttribute Account registration,
            BindingResult bindingResult) {
        //check password length
        if (registration.getPassword().length() < 4) {
            bindingResult.addError(new FieldError("account", "password","Password should contain more than 5 characters."));
        }
        //check username uniqueness
        if (accRepo.existsByUsername(registration.getUsername())) {
            bindingResult.addError(new FieldError("account", "username","Username already in use"));
        }
        if (bindingResult.hasErrors()) {
            return "regform";
        }
        
        // Encode the password before saving Object
        PasswordEncoder pe = securityConf.passwordEncoder();
        String encodedPass = pe.encode(registration.getPassword());
        registration.setPassword(encodedPass);
        accRepo.save(registration);
        return "redirect:/login";
    }
}
