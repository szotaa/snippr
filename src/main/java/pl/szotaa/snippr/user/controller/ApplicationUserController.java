package pl.szotaa.snippr.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szotaa.snippr.user.domain.ApplicationUser;
import pl.szotaa.snippr.user.service.ApplicationUserService;

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
}
