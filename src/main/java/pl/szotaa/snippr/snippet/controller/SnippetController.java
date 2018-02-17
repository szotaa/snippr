package pl.szotaa.snippr.snippet.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.szotaa.snippr.snippet.domain.Snippet;
import pl.szotaa.snippr.snippet.service.SnippetService;

import java.net.URI;

@RestController
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@RequestMapping("/snippet")
public class SnippetController {

    private final SnippetService snippetService;

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> create(@RequestBody Snippet snippet){
        snippetService.save(snippet);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(snippet.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Snippet> getById(@PathVariable Long id) throws Exception {
        Snippet result = snippetService.getById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "hasRole('ROLE_USER') and @securityExpressions.isSnippetOwner(#id, authentication)")
    public ResponseEntity<Void> updateExisting(@PathVariable Long id, @RequestBody Snippet snippet) throws Exception {
        snippet.setId(id);
        snippetService.update(snippet);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "hasRole('ROLE_USER') and @securityExpressions.isSnippetOwner(#id, authentication)")
    public ResponseEntity<Void> deleteExisting(@PathVariable Long id) throws Exception {
        snippetService.delete(id);
        return ResponseEntity.ok().build();
    }
}
