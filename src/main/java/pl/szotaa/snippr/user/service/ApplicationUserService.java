package pl.szotaa.snippr.user.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.szotaa.snippr.common.FieldError;
import pl.szotaa.snippr.user.domain.ApplicationUser;
import pl.szotaa.snippr.user.domain.Role;
import pl.szotaa.snippr.user.exception.ApplicationUserAlreadyExistsException;
import pl.szotaa.snippr.user.exception.ApplicationUserCreationFailedException;
import pl.szotaa.snippr.user.exception.ApplicationUserNotFoundException;
import pl.szotaa.snippr.user.exception.ApplicationUserUpdateFailedException;
import pl.szotaa.snippr.user.repository.ApplicationUserRepository;
import pl.szotaa.snippr.user.repository.RoleRepository;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
        if(applicationUser == null){
            throw new UsernameNotFoundException(username);
        }
        return new User(applicationUser.getUsername(), applicationUser.getPassword(), applicationUser.getRoles());
    }

    public void save(ApplicationUser applicationUser) throws ApplicationUserAlreadyExistsException, ApplicationUserCreationFailedException {
        Set<ConstraintViolation<ApplicationUser>> violations = validator.validate(applicationUser);
        if(!violations.isEmpty()){
            Set<FieldError> fieldErrors = FieldError.toFieldErrorSet(violations);
            throw new ApplicationUserCreationFailedException(fieldErrors);
        }
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

    public void update(ApplicationUser updateData) throws ApplicationUserNotFoundException, ApplicationUserUpdateFailedException {
        ApplicationUser toBeUpdated = applicationUserRepository.findOne(updateData.getId());

        if(toBeUpdated == null){
            throw new ApplicationUserNotFoundException(updateData.getId());
        }

        toBeUpdated.update(updateData);
        toBeUpdated.setPassword(bCryptPasswordEncoder.encode(toBeUpdated.getPassword()));

        Set<ConstraintViolation<ApplicationUser>> violations = validator.validate(toBeUpdated);
        if(!violations.isEmpty()){
            Set<FieldError> fieldErrors = FieldError.toFieldErrorSet(violations);
            throw new ApplicationUserUpdateFailedException(toBeUpdated.getId(), fieldErrors);
        }

        applicationUserRepository.save(toBeUpdated);
    }

    public void delete(Long id) throws ApplicationUserNotFoundException {
        if (!applicationUserRepository.exists(id)) {
            throw new ApplicationUserNotFoundException(id);
        }
        applicationUserRepository.delete(id);
    }
}
