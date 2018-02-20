package pl.szotaa.snippr.user.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.szotaa.snippr.user.domain.ApplicationUser;
import pl.szotaa.snippr.user.domain.Role;
import pl.szotaa.snippr.user.exception.ApplicationUserAlreadyExistsException;
import pl.szotaa.snippr.user.exception.ApplicationUserNotFoundException;
import pl.szotaa.snippr.user.repository.ApplicationUserRepository;
import pl.szotaa.snippr.user.repository.RoleRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Collections;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
        if(applicationUser == null){
            throw new UsernameNotFoundException(username);
        }
        return new User(applicationUser.getUsername(), applicationUser.getPassword(), applicationUser.getRoles());
    }

    public void save(@Valid ApplicationUser applicationUser) throws ApplicationUserAlreadyExistsException {
        if(applicationUserRepository.existsByUsername(applicationUser.getUsername())){
            throw new ApplicationUserAlreadyExistsException(applicationUser.getUsername());
        }
        Role role = roleRepository.findOne(2L);
        applicationUser.setRoles(Collections.singleton(role));
        applicationUser.setPassword(bCryptPasswordEncoder.encode(applicationUser.getPassword()));
        applicationUserRepository.save(applicationUser);
    }

    public ApplicationUser getByUsername(String username) throws ApplicationUserNotFoundException {
        if (!applicationUserRepository.existsByUsername(username)) {
            throw new ApplicationUserNotFoundException(username);
        }
        return applicationUserRepository.findByUsername(username);
    }

    public ApplicationUser getById(Long id) throws ApplicationUserNotFoundException {
        if (!applicationUserRepository.exists(id)) {
            throw new ApplicationUserNotFoundException(id);
        }
        return applicationUserRepository.findOne(id);
    }

    public void update(@Valid ApplicationUser applicationUser) throws ApplicationUserNotFoundException {
        if(!applicationUserRepository.exists(applicationUser.getId())){
            throw new ApplicationUserNotFoundException(applicationUser.getId());
        }
        applicationUserRepository.save(applicationUser);
    }

    public void delete(Long id) throws ApplicationUserNotFoundException {
        if (!applicationUserRepository.exists(id)) {
            throw new ApplicationUserNotFoundException(id);
        }
        applicationUserRepository.delete(id);
    }
}
