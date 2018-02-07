package pl.szotaa.snippr.snippet.repostiory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szotaa.snippr.snippet.domain.Snippet;

@Repository
public interface SnippetRepository extends JpaRepository<Snippet, Long> {
}
