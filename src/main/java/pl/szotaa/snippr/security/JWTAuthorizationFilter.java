package pl.szotaa.snippr.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.szotaa.snippr.user.domain.Role;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static pl.szotaa.snippr.security.SecurityConstants.HEADER_STRING;
import static pl.szotaa.snippr.security.SecurityConstants.TOKEN_PREFIX;

//TODO: code cleanup

@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        String token = request.getHeader(HEADER_STRING);
        if(token != null){


            Claims claims = Jwts.parser().parseClaimsJwt(token.replace(TOKEN_PREFIX, "")).getBody();
            String username = claims.getSubject();

            log.info("claims: " + claims.toString() + "\n + roles:" + claims.get("roles"));

            Set<Role> roles = new HashSet<>();

            if(claims.get("roles").toString().contains("ROLE_USER")){
                Role role = new Role();
                role.setId(2L);
                role.setRoleName("ROLE_USER");
                roles.add(role);
                log.info("added role user");
            }

            if(claims.get("roles").toString().contains("ROLE_ADMIN")){
                Role role = new Role();
                role.setId(1L);
                role.setRoleName("ROLE_ADMIN");
                roles.add(role);
                log.info("added role admin");
            }


            if(username != null){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, roles);
                log.info(authenticationToken.toString());
                return authenticationToken;
            }
            return null;
        }
        return null;
    }
}
