package pl.szotaa.snippr.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.szotaa.snippr.user.domain.ApplicationUser;
import pl.szotaa.snippr.user.domain.Role;
import pl.szotaa.snippr.user.service.ApplicationUserService;

import java.util.Collections;
import java.util.HashSet;

@RestController
@RequestMapping("/user")
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody ApplicationUser applicationUser){
        applicationUser.setPassword(bCryptPasswordEncoder.encode(applicationUser.getPassword()));
        applicationUserService.save(applicationUser);
        return ResponseEntity.ok().build();
    }

    /*POC methods*/

    @GetMapping("/whoami")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> whoAmI(){

        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        /*String password = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();*/

        String authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        String result = "username: " + username + " password: null"  + " autohrity: " + authority;

        return ResponseEntity.ok(result);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> admin(){
        return ResponseEntity.ok("hi admin");
    }
}
