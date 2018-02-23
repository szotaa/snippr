package pl.szotaa.snippr.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.szotaa.snippr.snippet.exception.SnippetExpiredException;
import pl.szotaa.snippr.snippet.exception.SnippetNotFoundException;
import pl.szotaa.snippr.snippet.service.SnippetService;
import pl.szotaa.snippr.user.exception.ApplicationUserNotFoundException;
import pl.szotaa.snippr.user.service.ApplicationUserService;

@Component
@RequiredArgsConstructor
public class SecurityExpressions {

    private final SnippetService snippetService;
    private final ApplicationUserService applicationUserService;

    public boolean isSnippetOwner(Long snippetId, Authentication authentication) throws SnippetNotFoundException, SnippetExpiredException {
        return snippetService.getById(snippetId).getOwner().getUsername().equals(authentication.getName());
    }

    public boolean isHimself(Long applicationUserId, Authentication authentication) throws ApplicationUserNotFoundException {
        return applicationUserService.getById(applicationUserId).getUsername().equals(authentication.getName());
    }
}
