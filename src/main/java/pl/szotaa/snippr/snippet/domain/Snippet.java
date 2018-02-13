package pl.szotaa.snippr.snippet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.szotaa.snippr.user.domain.ApplicationUser;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "snippet")
public class Snippet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "owner_user_id")
    private ApplicationUser owner;

    @CreationTimestamp
    @Column(name = "date_added", nullable = false)
    private Instant dateAdded;

    @UpdateTimestamp
    @Column(name = "last_modified", nullable = false)
    private Instant lastModified;
}
