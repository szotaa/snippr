package pl.szotaa.snippr.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.szotaa.snippr.user.domain.ApplicationUser;
import pl.szotaa.snippr.user.service.ApplicationUserService;

import java.net.URI;

@RestController
@RequestMapping("/user")
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;

    @PostMapping("/register")
    public ResponseEntity<Void> signUp(@RequestBody ApplicationUser applicationUser){
        if(applicationUserService.exists(applicationUser.getUsername())){
            return ResponseEntity.badRequest().build();
        }
        applicationUserService.save(applicationUser);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(applicationUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "hasRole('ROLE_USER') and @securityExpressions.isHimself(#id, authentication)")
    public ResponseEntity<ApplicationUser> getUser(@PathVariable Long id){
        ApplicationUser result = applicationUserService.getById(id);
        if(result == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "hasRole('ROLE_USER') and @securityExpressions.isHimself(#id, authentication)")
    public ResponseEntity<Void> updateExisting(@PathVariable Long id, @RequestBody ApplicationUser applicationUser){
        if(!applicationUserService.exists(id)){
            return ResponseEntity.notFound().build();
        }
        applicationUser.setId(id);
        applicationUserService.update(applicationUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "hasRole('ROLE_USER') and @securityExpressions.isHimself(#id, authentication)")
    public ResponseEntity<Void> deleteExisting(@PathVariable Long id){
        if(!applicationUserService.exists(id)){
            return ResponseEntity.notFound().build();
        }
        applicationUserService.delete(id);
        return ResponseEntity.ok().build();
    }


}
