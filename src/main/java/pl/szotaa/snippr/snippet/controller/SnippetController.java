package pl.szotaa.snippr.snippet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.szotaa.snippr.snippet.domain.Snippet;
import pl.szotaa.snippr.snippet.exception.SnippetCreationFailedException;
import pl.szotaa.snippr.snippet.exception.SnippetExpiredException;
import pl.szotaa.snippr.snippet.exception.SnippetNotFoundException;
import pl.szotaa.snippr.snippet.exception.SnippetUpdateFailedException;
import pl.szotaa.snippr.snippet.service.SnippetService;
import pl.szotaa.snippr.user.exception.UserNotFoundException;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/snippet")
public class SnippetController {

    private final SnippetService snippetService;

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> create(@RequestBody Snippet snippet) throws UserNotFoundException, SnippetCreationFailedException {
        snippetService.save(snippet);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(snippet.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Snippet> getById(@PathVariable Long id) throws SnippetNotFoundException, SnippetExpiredException {
        Snippet result = snippetService.getById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "hasRole('ROLE_USER') and @securityExpressions.isSnippetOwner(#id, authentication)")
    public ResponseEntity<Void> updateExisting(@PathVariable Long id, @RequestBody Snippet snippet) throws SnippetNotFoundException, SnippetUpdateFailedException {
        snippet.setId(id);
        snippetService.update(snippet);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "hasRole('ROLE_USER') and @securityExpressions.isSnippetOwner(#id, authentication)")
    public ResponseEntity<Void> deleteExisting(@PathVariable Long id) throws SnippetNotFoundException {
        snippetService.delete(id);
        return ResponseEntity.ok().build();
    }
}
