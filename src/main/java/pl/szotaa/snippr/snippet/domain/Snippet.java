package pl.szotaa.snippr.snippet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.szotaa.snippr.snippet.validation.NotBeforeCurrentTime;
import pl.szotaa.snippr.snippet.validation.ProgrammingLanguage;
import pl.szotaa.snippr.user.domain.ApplicationUser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
    @JsonIgnore
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

    @ProgrammingLanguage
    private String syntaxHighlighting;

    @NotBeforeCurrentTime
    private Instant expiryDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "owner_user_id")
    private ApplicationUser owner;

    @JsonIgnore
    @CreationTimestamp
    @Column(name = "date_added", nullable = false)
    private Instant dateAdded;

    @JsonIgnore
    @UpdateTimestamp
    @Column(name = "last_modified", nullable = false)
    private Instant lastModified;

    public boolean isExpired(){
        if(expiryDate != null && expiryDate.isBefore(Instant.now())){
            return true;
        }
        return false;
    }
}
