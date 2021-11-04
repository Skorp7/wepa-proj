package projekti;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    @Autowired
    ImageRepository imgRepo;

    @Autowired
    AccountRepository accRepo;

    public boolean addImage(MultipartFile file, String username, boolean icon, String text) throws IOException {
        Account acc = accRepo.findByUsername(username);
        if (getImageCount(acc) > 9) {
            return false;
        }
        if (file.getContentType().contains("image")) {
            if (icon && iconExists(username)) {
                Image old_icon = getIconByUsername(username);
                old_icon.setIcon(false);
                imgRepo.save(old_icon);
            }
            Image img = new Image(file.getBytes(), acc, icon, text);
            imgRepo.save(img);
            return true;
        } else {
            return false;
        }
    }

    public void delImage(Long id) {
        imgRepo.deleteById(id);
    }

    public byte[] getContentById(Long id) {
        return imgRepo.getOne(id).getContent();
    }

    public boolean iconExists(String username) {
        return (getIconByUsername(username) != null);
    }

    public Image getIconByUsername(String username) {
        Account acc = accRepo.findByUsername(username);
        return imgRepo.findByAccountAndIcon(acc, true);
    }

    public int getImageCount(Account acc) {
        return getImagesByAccount(acc).size();
    }

    public List<Image> getImagesByAccount(Account acc) {
        return imgRepo.findByAccount(acc, Sort.by("id").descending());
    }
}
