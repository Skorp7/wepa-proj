
package projekti;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 *
 * @author sakorpi
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Profile extends AbstractPersistable<Long> {
    private String name;
    private String pass;
    private String profilename;

    @ManyToMany
    @JoinTable(name = "tbl_followers",
            joinColumns = @JoinColumn(name = "profileId"),
            inverseJoinColumns = @JoinColumn(name = "followerId")
    )
    private Set<Profile> following;
    
    @ManyToMany
    @JoinTable(name = "tbl_following",
            joinColumns = @JoinColumn(name = "profileId"),
            inverseJoinColumns = @JoinColumn(name = "followingId")
    )
    private Set<Profile> followers;
    
    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Long objId = ((Profile) obj).getId();
        return objId == null ? false : Objects.equals(getId(), objId);
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    

}