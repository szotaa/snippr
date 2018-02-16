package pl.szotaa.snippr.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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

import static pl.szotaa.snippr.security.SecurityConstants.*;

//TODO: code cleanup

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        String token = request.getHeader(HEADER_STRING);
        if(token != null){


            Claims claims = Jwts.parser()
                    /*.setSigningKey(SECRET.getBytes())*/ //TODO: find out why signing claims throws exception
                    .parseClaimsJwt(token.replace(TOKEN_PREFIX, ""))
                    .getBody();

            String username = claims.getSubject();

            if(username != null){
                return new UsernamePasswordAuthenticationToken(username, null, buildRoleSet(claims));
            }
            return null;
        }
        return null;
    }

    private Set<Role> buildRoleSet(Claims claims){
        Set<Role> roles = new HashSet<>();

        if(claims.get("roles").toString().contains("ROLE_USER")){
            Role role = new Role();
            role.setRoleName("ROLE_USER");
            roles.add(role);
        }

        if(claims.get("roles").toString().contains("ROLE_ADMIN")){
            Role role = new Role();
            role.setRoleName("ROLE_ADMIN");
            roles.add(role);
        }

        return roles;
    }
}
