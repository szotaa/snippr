package pl.szotaa.snippr.snippet.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.szotaa.snippr.snippet.domain.Snippet;
import pl.szotaa.snippr.snippet.service.SnippetService;

import java.util.List;

//TODO: validation

@RestController
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@RequestMapping("/snippet")
public class SnippetController {

    private final SnippetService snippetService;

    @PostMapping
    public ResponseEntity add(@RequestBody Snippet snippet){
        //TODO: add logic
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Snippet> getById(@PathVariable Long id){
        //TODO: getById logic
        return null;
    }

    @GetMapping
    public ResponseEntity<List<Snippet>> getAll(){
        //TODO: getAll logic
        return null;
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateExisting(@PathVariable Long id){
        //TODO: updateExisting logic
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteExisting(@PathVariable Long id){
        //TODO: deleteExisting logic
        return null;
    }
}
