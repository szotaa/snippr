package pl.szotaa.snippr.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.szotaa.snippr.snippet.service.SnippetService;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class SecurityExpressions {

    private final SnippetService snippetService;

    public boolean isEntityOwner(Long entityId, Authentication authentication){
        log.info("security expressions called");
        if(snippetService.getById(entityId).getOwner().getUsername().equals(authentication.getName())){
            return true;
        }
        return false;
    }
}
