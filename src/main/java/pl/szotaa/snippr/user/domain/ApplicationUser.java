package pl.szotaa.snippr.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import pl.szotaa.snippr.snippet.domain.Snippet;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class ApplicationUser implements Serializable {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotNull
    @Length(min = 5, max = 50)
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull
    @Length(min = 8, max = 60)
    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
                joinColumns = @JoinColumn(name = "user", referencedColumnName = "username"),
                inverseJoinColumns = @JoinColumn(name = "role", referencedColumnName = "role_name"))
    private Set<Role> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<Snippet> snippets;
}
