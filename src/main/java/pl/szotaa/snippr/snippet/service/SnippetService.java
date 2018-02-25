package pl.szotaa.snippr.snippet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.szotaa.snippr.common.FieldError;
import pl.szotaa.snippr.snippet.domain.Snippet;
import pl.szotaa.snippr.snippet.exception.SnippetCreationFailedException;
import pl.szotaa.snippr.snippet.exception.SnippetExpiredException;
import pl.szotaa.snippr.snippet.exception.SnippetNotFoundException;
import pl.szotaa.snippr.snippet.exception.SnippetUpdateFailedException;
import pl.szotaa.snippr.snippet.repostiory.SnippetRepository;
import pl.szotaa.snippr.user.domain.User;
import pl.szotaa.snippr.user.exception.UserNotFoundException;
import pl.szotaa.snippr.user.service.UserService;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class SnippetService {

    private final SnippetRepository snippetRepository;
    private final UserService userService;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public void save(Snippet snippet) throws UserNotFoundException, SnippetCreationFailedException {
        if(!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)){
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentlyLoggedInUser = userService.getByUsername(username);
            snippet.setOwner(currentlyLoggedInUser);
        }

        Set<ConstraintViolation<Snippet>> violations = validator.validate(snippet);
        if(!violations.isEmpty()) {
            Set<FieldError> fieldErrors = FieldError.toFieldErrorSet(violations);
            throw new SnippetCreationFailedException(fieldErrors);
        }

        snippetRepository.save(snippet);
    }

    public Snippet getById(Long id) throws SnippetNotFoundException, SnippetExpiredException {
        Snippet found = snippetRepository.findOne(id);
        if(found == null){
            throw new SnippetNotFoundException(id);
        }
        if(found.isExpired()){
            throw new SnippetExpiredException(id, found.getExpiryDate());
        }
        return found;
    }

    public void update(Snippet updateData) throws SnippetNotFoundException, SnippetUpdateFailedException {
        Snippet toBeUpdated = snippetRepository.findOne(updateData.getId());
        if(toBeUpdated == null){
            throw new SnippetNotFoundException(updateData.getId());
        }
        toBeUpdated.update(updateData);
        Set<ConstraintViolation<Snippet>> violations = validator.validate(updateData);
        if(!violations.isEmpty()) {
            Set<FieldError> fieldErrors = FieldError.toFieldErrorSet(violations);
            throw new SnippetUpdateFailedException(toBeUpdated.getId(), fieldErrors);
        }
        snippetRepository.save(toBeUpdated);
    }

    public void delete(Long id) throws SnippetNotFoundException {
        if(!snippetRepository.exists(id)){
            throw new SnippetNotFoundException(id);
        }
        snippetRepository.delete(id);
    }
}
