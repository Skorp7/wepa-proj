package projekti.controller;

import projekti.domain.Account;
import projekti.domain.ProfileService;
import projekti.domain.ImageService;
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
import projekti.domain.MessageService;

@Controller
public class ImageController {
    
    @Autowired
    private MessageService msgServ;

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
        model.addAttribute("isFollower", profServ.isFollowerTo(acc, auth.getName()));
        return "pics";
    }

    @PostMapping("/profiles/{username}/pics")
    public String imageIn(@RequestParam("file") MultipartFile file,
            @RequestParam boolean icon,
            @RequestParam String text,
            @PathVariable String username) throws IOException {
        if (file.getSize() > 1048575) {
            return "redirect:/profile";
        }
            
        if (imgServ.addImage(file, username, icon, text)) {
            return "redirect:/profiles/" + username + "/pics";
        } else {
            return "redirect:/profile";
        }

    }
    
    @PostMapping("/profiles/{username}/pics/{id}")
    public String commentImg(@RequestParam String comment,
            @PathVariable String username,
            @PathVariable Long id) {
        if (comment.length() < 2 || comment.length() > 200) {
            return "redirect:/profiles/" + username + "/pics";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account commenter = profServ.getAccountByUsername(auth.getName());
        msgServ.addComment(comment, commenter, id, true);
        return "redirect:/profiles/" + username + "/pics";
    }

    @PostMapping("/profiles/{username}/delpic")
    public String imageDel(@RequestParam Long id, @PathVariable String username) {
        imgServ.delImage(id);
        return "redirect:/profiles/" + username + "/pics";
    }

    @GetMapping(path = "/profiles/{username}/pics/{id}/content", produces = "image/jpg")
    @ResponseBody
    public byte[] get(@PathVariable String username, @PathVariable Long id) {
//        Use this codeblock if you want to check if image request is coming from an owner or follower
//        It slows down the program by doing many database querys:

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        boolean isOwner = auth.getName().equals(username);
//        boolean isFollower = profServ.isFollowerTo(username, auth.getName());
//        Image img = imgServ.getImageById(id);
//        if ((isOwner || isFollower) || img.isIcon()) {
//            return img.getContent();
//        }
//        byte[] errorBytes = {1,2};
//        return errorBytes;
        return imgServ.getImageById(id).getContent();
    }

}
