package pl.szotaa.snippr.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import pl.szotaa.snippr.user.domain.ApplicationUser;
import pl.szotaa.snippr.user.exception.ApplicationUserAlreadyExistsException;
import pl.szotaa.snippr.user.exception.ApplicationUserNotFoundException;
import pl.szotaa.snippr.user.service.ApplicationUserService;

import java.net.URI;

@RestController
@RequestMapping("/user")
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;

    @PostMapping("/register")
    public ResponseEntity<Void> signUp(@RequestBody ApplicationUser applicationUser) throws ApplicationUserAlreadyExistsException {
        applicationUserService.save(applicationUser);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(applicationUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "hasRole('ROLE_USER') and @securityExpressions.isHimself(#id, authentication)")
    public ResponseEntity<ApplicationUser> getUser(@PathVariable Long id) throws ApplicationUserNotFoundException {
        ApplicationUser result = applicationUserService.getById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "hasRole('ROLE_USER') and @securityExpressions.isHimself(#id, authentication)")
    public ResponseEntity<Void> updateExisting(@PathVariable Long id, @RequestBody ApplicationUser applicationUser) throws ApplicationUserNotFoundException {
        applicationUser.setId(id);
        applicationUserService.update(applicationUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "hasRole('ROLE_USER') and @securityExpressions.isHimself(#id, authentication)")
    public ResponseEntity<Void> deleteExisting(@PathVariable Long id) throws ApplicationUserNotFoundException {
        applicationUserService.delete(id);
        return ResponseEntity.ok().build();
    }


}
