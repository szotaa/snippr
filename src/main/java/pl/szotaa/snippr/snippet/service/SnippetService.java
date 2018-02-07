package pl.szotaa.snippr.snippet.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.szotaa.snippr.snippet.domain.Snippet;
import pl.szotaa.snippr.snippet.repostiory.SnippetRepository;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class SnippetService {

    private final SnippetRepository snippetRepository;

    public void save(Snippet snippet){
        snippetRepository.save(snippet);
    }

    public Snippet getById(Long id){
        return snippetRepository.findOne(id);
    }

    public List<Snippet> getAll(){
        return null;
    }

    public void update(Snippet snippet){
        snippetRepository.save(snippet);
    }

    public void delete(Long id){
        snippetRepository.delete(id);
    }
}
