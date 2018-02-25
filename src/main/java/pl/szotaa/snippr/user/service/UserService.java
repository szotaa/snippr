package pl.szotaa.snippr.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.szotaa.snippr.common.FieldError;
import pl.szotaa.snippr.user.domain.User;
import pl.szotaa.snippr.user.domain.Role;
import pl.szotaa.snippr.user.exception.UserAlreadyExistsException;
import pl.szotaa.snippr.user.exception.UserCreationFailedException;
import pl.szotaa.snippr.user.exception.UserNotFoundException;
import pl.szotaa.snippr.user.exception.UserUpdateFailedException;
import pl.szotaa.snippr.user.repository.UserRepository;
import pl.szotaa.snippr.user.repository.RoleRepository;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getRoles());
    }

    public void save(User user) throws UserAlreadyExistsException, UserCreationFailedException {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if(!violations.isEmpty()){
            Set<FieldError> fieldErrors = FieldError.toFieldErrorSet(violations);
            throw new UserCreationFailedException(fieldErrors);
        }
        if(userRepository.existsByUsername(user.getUsername())){
            throw new UserAlreadyExistsException(user.getUsername());
        }
        Role role = roleRepository.findOne(2L);
        user.setRoles(Collections.singleton(role));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User getByUsername(String username) throws UserNotFoundException {
        if (!userRepository.existsByUsername(username)) {
            throw new UserNotFoundException(username);
        }
        return userRepository.findByUsername(username);
    }

    public User getById(Long id) throws UserNotFoundException {
        if (!userRepository.exists(id)) {
            throw new UserNotFoundException(id);
        }
        return userRepository.findOne(id);
    }

    public void update(User updateData) throws UserNotFoundException, UserUpdateFailedException {
        User toBeUpdated = userRepository.findOne(updateData.getId());

        if(toBeUpdated == null){
            throw new UserNotFoundException(updateData.getId());
        }

        toBeUpdated.update(updateData);
        toBeUpdated.setPassword(bCryptPasswordEncoder.encode(toBeUpdated.getPassword()));

        Set<ConstraintViolation<User>> violations = validator.validate(toBeUpdated);
        if(!violations.isEmpty()){
            Set<FieldError> fieldErrors = FieldError.toFieldErrorSet(violations);
            throw new UserUpdateFailedException(toBeUpdated.getId(), fieldErrors);
        }

        userRepository.save(toBeUpdated);
    }

    public void delete(Long id) throws UserNotFoundException {
        if (!userRepository.exists(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.delete(id);
    }
}
