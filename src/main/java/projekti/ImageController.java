package projekti;

/**
 *
 * @author sakorpi
 */
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ImageRepository imageRepository;

    @GetMapping("{profilename}/images")
    public String redirect(@PathVariable String profilename) {
        return "redirect:" + profilename + "/images/1";
    }

    @PostMapping("{profilename}/images")
    public String imageIn(@RequestParam("file") MultipartFile file, @PathVariable String profilename) throws IOException {
        if (file.getContentType().contains("image")) {
            Image img = new Image();
            img.setContent(file.getBytes());
            img.setOwner(profilename);
            imageRepository.save(img);
        }
        System.out.println("contentTYPE" + file.getContentType());
        return "redirect:" + profilename + "/images";
    }

    
    @GetMapping("{profilename}/images/{id}")
    public String gifsid(Model model, @PathVariable Long id, @PathVariable String profilename) {
    model.addAttribute("count", imageRepository.count());
    if (imageRepository.existsById(id)) model.addAttribute("current", id);
    if (imageRepository.existsById(id+1)) model.addAttribute("next", (id + 1));
    if (imageRepository.existsById(id-1)) model.addAttribute("previous", (id - 1));
        return "images";
    }

    @GetMapping(path = "{profilename}/images/{id}/content", produces = "image/png")
    @ResponseBody
    public byte[] get(@PathVariable Long id) {
        return imageRepository.getOne(id).getContent();        
    }
}
