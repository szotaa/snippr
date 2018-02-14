package pl.szotaa.snippr.user.service;

import lombok.AllArgsConstructor;
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

import javax.transaction.Transactional;
import java.util.Collections;

@Service
@Transactional
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

        return new User(applicationUser.getUsername(), applicationUser.getPassword(), applicationUser.getRoles());
    }

    public void save(ApplicationUser applicationUser){
        Role role = roleRepository.findOne(2L);
        applicationUser.setRoles(Collections.singleton(role));
        applicationUserRepository.save(applicationUser);
    }

    public ApplicationUser getByUsername(String username){
        return applicationUserRepository.findByUsername(username);
    }

    public ApplicationUser getById(Long id){
        return applicationUserRepository.findOne(id);
    }

    public void update(ApplicationUser applicationUser){
        applicationUserRepository.save(applicationUser);
    }

    public void delete(Long id){
        applicationUserRepository.delete(id);
    }

    public boolean exists(Long id){
        return applicationUserRepository.exists(id);
    }
}
