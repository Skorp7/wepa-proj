package projekti;

/**
 *
 * @author sakorpi
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
    
    @Autowired
    private ImageRepository imgRepo;
    
//    @GetMapping("/profiles/{username}/pics")
//    public String profilesImg(@PathVariable String username, Model model) {
//        model.addAttribute("images", imgServ.getImagesByUsername(username));
//        model.addAttribute("account", profServ.getAccountByUsername(username));
//        return "pics";
//    }

    @GetMapping("/profiles/{username}/pics")
    public String profilesImg(@PathVariable String username, Model model) {
        Account acc = profServ.getAccountByUsername(username);
        List<Image> lista = imgRepo.findAll();
        ArrayList<Image> filter = new ArrayList<>();
        for (Image img : lista) {
            if (img.getAccount().getUsername().equals(acc.getUsername())) {
                filter.add(img);
            }
        }
        model.addAttribute("account", acc);
        model.addAttribute("images", filter);
        return "pics";
    }
    
    @PostMapping("/profiles/{username}/pics")
    public String imageIn(@RequestParam("file") MultipartFile file, 
            @RequestParam boolean icon,
            @PathVariable String username) throws IOException {
        imgServ.addImage(file, username, icon);
        return "redirect:/profiles/" + username + "/pics";
    }
    
    @GetMapping(path = "/profiles/{username}/pics/{id}/content", produces = "image/png")
    @ResponseBody
    public byte[] get(@PathVariable Long id) {
        return imgServ.getContentById(id);       
    }
    
    @GetMapping(path = "/conts" )
    @ResponseBody
    public String getConts() {
        Account acc = profServ.getAccountByUsername("pil");
        List<Image> lista = imgRepo.findAll();
        ArrayList<Image> filter = new ArrayList<>();
        for (Image img : lista) {
            if (img.getAccount().getUsername().equals(acc.getUsername())) {
                filter.add(img);
            }
        }
        return filter.toString();
    }

//        @GetMapping("{profilename}/images")
//    public String redirect(@PathVariable String profilename) {
//        return "redirect:" + profilename + "/images/1";
//    }
//
//    @GetMapping("{profilename}/images/{id}")
//    public String gifsid(Model model, @PathVariable Long id, @PathVariable String profilename) {
//    model.addAttribute("count", imageRepository.count());
//    if (imageRepository.existsById(id)) model.addAttribute("current", id);
//    if (imageRepository.existsById(id+1)) model.addAttribute("next", (id + 1));
//    if (imageRepository.existsById(id-1)) model.addAttribute("previous", (id - 1));
//        return "images";
//    }
//

}
