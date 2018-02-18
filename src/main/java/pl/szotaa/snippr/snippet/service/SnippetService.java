package pl.szotaa.snippr.snippet.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.szotaa.snippr.snippet.domain.Snippet;
import pl.szotaa.snippr.snippet.exception.SnippetExpiredException;
import pl.szotaa.snippr.snippet.exception.SnippetNotFoundException;
import pl.szotaa.snippr.snippet.repostiory.SnippetRepository;
import pl.szotaa.snippr.user.domain.ApplicationUser;
import pl.szotaa.snippr.user.exception.ApplicationUserNotFoundException;
import pl.szotaa.snippr.user.service.ApplicationUserService;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@Service
@Validated
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class SnippetService {

    private final SnippetRepository snippetRepository;
    private final ApplicationUserService applicationUserService;

    public void save(@Valid Snippet snippet) throws ApplicationUserNotFoundException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username != null){
            ApplicationUser currentlyLoggedInUser = applicationUserService.getByUsername(username);
            snippet.setOwner(currentlyLoggedInUser);
        }

        snippetRepository.save(snippet);
    }

    public Snippet getById(Long id) throws SnippetNotFoundException, SnippetExpiredException {
        Snippet found = snippetRepository.findOne(id);
        if(found == null){
            throw new SnippetNotFoundException(id);
        }
        if(found.getExpiryDate().isBefore(Instant.now())){
            throw new SnippetExpiredException(id, found.getExpiryDate());
        }
        return found;
    }

    public void update(@Valid Snippet snippet) throws SnippetNotFoundException {
        if(!snippetRepository.exists(snippet.getId())){
            throw new SnippetNotFoundException(snippet.getId());
        }
        snippetRepository.save(snippet);
    }

    public void delete(Long id) throws SnippetNotFoundException {
        if(!snippetRepository.exists(id)){
            throw new SnippetNotFoundException(id);
        }
        snippetRepository.delete(id);
    }
}
