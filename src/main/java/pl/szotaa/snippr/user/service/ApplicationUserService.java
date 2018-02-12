package pl.szotaa.snippr.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.szotaa.snippr.user.domain.ApplicationUser;
import pl.szotaa.snippr.user.domain.Role;
import pl.szotaa.snippr.user.repository.ApplicationUserRepository;
import pl.szotaa.snippr.user.repository.RoleRepository;

import java.util.Collections;
import java.util.HashSet;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
        if(applicationUser == null){
            throw new UsernameNotFoundException(username);
        }

        log.info("loadUserByUsername(): " + applicationUser.toString());

        return new User(applicationUser.getUsername(), applicationUser.getPassword(), applicationUser.getRoles());
    }

    public void save(ApplicationUser applicationUser){
        Role role = roleRepository.findOne(2L);
        applicationUser.setRoles(Collections.singleton(role));
        applicationUserRepository.save(applicationUser);
    }
}
