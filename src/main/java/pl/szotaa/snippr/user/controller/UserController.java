package pl.szotaa.snippr.user.controller;

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
import pl.szotaa.snippr.user.domain.User;
import pl.szotaa.snippr.user.exception.UserAlreadyExistsException;
import pl.szotaa.snippr.user.exception.UserCreationFailedException;
import pl.szotaa.snippr.user.exception.UserNotFoundException;
import pl.szotaa.snippr.user.exception.UserUpdateFailedException;
import pl.szotaa.snippr.user.service.UserService;

import java.net.URI;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody User user) throws UserAlreadyExistsException, UserCreationFailedException {
        userService.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "hasRole('ROLE_USER') and @securityExpressions.isHimself(#id, authentication)")
    public ResponseEntity<User> getById(@PathVariable Long id) throws UserNotFoundException {
        User result = userService.getById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "hasRole('ROLE_USER') and @securityExpressions.isHimself(#id, authentication)")
    public ResponseEntity<Void> updateExisting(@PathVariable Long id, @RequestBody User user) throws UserNotFoundException, UserUpdateFailedException {
        user.setId(id);
        userService.update(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "hasRole('ROLE_USER') and @securityExpressions.isHimself(#id, authentication)")
    public ResponseEntity<Void> deleteExisting(@PathVariable Long id) throws UserNotFoundException {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }


}
