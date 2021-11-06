package projekti.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;
import org.springframework.data.jpa.domain.AbstractPersistable;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractPersistable<Long>{
    
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 characters.")
    @Column(unique = true)
    private String username;
    
    @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters.")
    private String name;
    
    private String password;
    
    @ManyToMany
    @JoinTable(name = "followers",
            joinColumns = @JoinColumn(name = "accountId"),
            inverseJoinColumns = @JoinColumn(name = "followerId")
    )
    private Set<Account> following;
    
    @ManyToMany
    @JoinTable(name = "following",
            joinColumns = @JoinColumn(name = "accountId"),
            inverseJoinColumns = @JoinColumn(name = "followingId")
    )
    private Set<Account> followers;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private List<Image> images = new ArrayList<>();
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "accountto")
    private List<Message> messages = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(name = "blacklist",
            joinColumns = @JoinColumn(name = "accountId"),
            inverseJoinColumns = @JoinColumn(name = "blockedId")
    )
    private Set<Account> blacklist;
    
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
        String objUname = ((Account) obj).getUsername();
        return objUname == null ? false : Objects.equals(getUsername(), objUname);
    }

    @Override
    public String toString() {
        return this.name;
    }
    
}
