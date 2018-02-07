package pl.szotaa.snippr.snippet.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.szotaa.snippr.snippet.repostiory.SnippetRepository;

@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class SnippetService {

    private final SnippetRepository snippetRepository;
}
