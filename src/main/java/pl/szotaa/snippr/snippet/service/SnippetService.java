package pl.szotaa.snippr.snippet.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.szotaa.snippr.snippet.domain.Snippet;
import pl.szotaa.snippr.snippet.exception.SnippetNotFoundException;
import pl.szotaa.snippr.snippet.repostiory.SnippetRepository;
import pl.szotaa.snippr.user.domain.ApplicationUser;
import pl.szotaa.snippr.user.service.ApplicationUserService;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class SnippetService {

    private final SnippetRepository snippetRepository;
    private final ApplicationUserService applicationUserService;

    public void save(@Valid Snippet snippet){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username != null){
            ApplicationUser currentlyLoggedInUser = applicationUserService.getByUsername(username);
            snippet.setOwner(currentlyLoggedInUser);
        }

        snippetRepository.save(snippet);
    }

    public Snippet getById(Long id) throws Exception {
        Snippet found = snippetRepository.findOne(id);
        if(found == null){
            throw new SnippetNotFoundException(id);
        }
        return found;
    }

    public List<Snippet> getAll(){
        return null;
    }

    public void update(@Valid Snippet snippet) throws Exception {
        if(!snippetRepository.exists(snippet.getId())){
            throw new SnippetNotFoundException(snippet.getId());
        }
        snippetRepository.save(snippet);
    }

    public void delete(Long id) throws Exception {
        if(!snippetRepository.exists(id)){
            throw new SnippetNotFoundException(id);
        }
        snippetRepository.delete(id);
    }

    public boolean exists(Long id){
        return snippetRepository.exists(id);
    }
}
