package pl.szotaa.snippr.snippet.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.szotaa.snippr.snippet.domain.Snippet;
import pl.szotaa.snippr.snippet.service.SnippetService;

import java.net.URI;

//TODO: validation

@RestController
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@RequestMapping("/snippet")
public class SnippetController {

    private final SnippetService snippetService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Snippet snippet){
        snippetService.save(snippet);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(snippet.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Snippet> getById(@PathVariable Long id){
        Snippet result = snippetService.getById(id);
        if(result == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExisting(@PathVariable Long id, @RequestBody Snippet snippet){
        if(snippetService.getById(id) == null){
            return ResponseEntity.notFound().build();
        }
        snippet.setId(id);
        snippetService.update(snippet);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteExisting(@PathVariable Long id){
        if(snippetService.getById(id) == null){
            return ResponseEntity.notFound().build();
        }
        snippetService.delete(id);
        return ResponseEntity.ok().build();
    }
}
