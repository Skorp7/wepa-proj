

package projekti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.tools.FileObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author sakorpi
 */
@Service
public class ImageService {
    @Autowired
    ImageRepository imgRepo;
    
    @Autowired
    AccountRepository accRepo;
    
    public boolean addImage(MultipartFile file, String username, boolean icon) throws IOException {
        if (file.getContentType().contains("image")) {
            if (icon && iconExists(username)) {
                Image old_icon = getIconByUsername(username);
                old_icon.setIcon(false);
                imgRepo.save(old_icon);
            }
            Account acc = accRepo.findByUsername(username);
            Image img = new Image(file.getBytes(), acc, icon);
            imgRepo.save(img);
            return true;
        } else {
            return false;
        }
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
    
    public List<Image> getImagesByAccount(Account acc) {
        return imgRepo.findByAccount(acc);
    }
}
