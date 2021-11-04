package projekti;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {

    @Autowired
    private ImageService imgServ;

    @Autowired
    private ProfileService profServ;

    @GetMapping("/profiles/{username}/pics")
    public String profilesImgs(@PathVariable String username, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account acc = profServ.getAccountByUsername(username);
        model.addAttribute("images", imgServ.getImagesByAccount(acc));
        model.addAttribute("account", acc);
        model.addAttribute("isOwner", auth.getName().equals(username));
        model.addAttribute("isFollower", profServ.isFollowerTo(username, auth.getName()));
        return "pics";
    }

    @PostMapping("/profiles/{username}/pics")
    public String imageIn(@RequestParam("file") MultipartFile file,
            @RequestParam boolean icon,
            @RequestParam String text,
            @PathVariable String username) throws IOException {
        imgServ.addImage(file, username, icon, text);
        return "redirect:/profiles/" + username + "/pics";
    }

    @PostMapping("/profiles/{username}/delpic")
    public String imageDel(@RequestParam Long id, @PathVariable String username) {
        imgServ.delImage(id);
        return "redirect:/profiles/" + username + "/pics";
    }

    @GetMapping(path = "/profiles/{username}/pics/{id}/content", produces = "image/jpg")
    @ResponseBody
    public byte[] get(@PathVariable Long id) {
        return imgServ.getContentById(id);
    }

}
