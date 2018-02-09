package pl.szotaa.snippr.user.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.szotaa.snippr.user.domain.ApplicationUser;
import pl.szotaa.snippr.user.repository.ApplicationUserRepository;

import java.util.Collections;

@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
        if(applicationUser == null){
            throw new UsernameNotFoundException(username);
        }

        return new User(applicationUser.getUsername(), applicationUser.getPassword(), Collections.emptyList());
    }

    public void save(ApplicationUser applicationUser){
        applicationUserRepository.save(applicationUser);
    }
}
