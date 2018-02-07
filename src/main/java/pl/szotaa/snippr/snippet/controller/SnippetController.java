package pl.szotaa.snippr.snippet.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pl.szotaa.snippr.snippet.service.SnippetService;

@RestController
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class SnippetController {

    private final SnippetService snippetService;
}
