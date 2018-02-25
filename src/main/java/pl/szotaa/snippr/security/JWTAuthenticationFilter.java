package pl.szotaa.snippr.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.szotaa.snippr.user.domain.User;
import pl.szotaa.snippr.user.domain.Role;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import static pl.szotaa.snippr.security.SecurityConstants.EXPIRATION_TIME;
import static pl.szotaa.snippr.security.SecurityConstants.HEADER_STRING;
import static pl.szotaa.snippr.security.SecurityConstants.TOKEN_PREFIX;

@AllArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try{
            User credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            new HashSet<>()
                    )
            );
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {


        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();

        String token = Jwts.builder()
                .setClaims(buildClaims(user))
                /*.signWith(SignatureAlgorithm.HS512, SECRET.getBytes())*/ //TODO: find out why signed claims jws throw exception
                .compact();

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + token);
    }

    private Claims buildClaims(org.springframework.security.core.userdetails.User user){
        Claims claims = Jwts.claims();
        claims.setSubject(user.getUsername());
        claims.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
        claims.put("roles", authoritiesToString(user.getAuthorities()));
        return claims;
    }

    private String authoritiesToString(Collection<GrantedAuthority> authorities){
        StringBuilder roles = new StringBuilder();
        for(GrantedAuthority authority : authorities){
            Role role = (Role) authority;
            roles.append(role.getRoleName());
            roles.append(" ");
        }
        return roles.toString();
    }
}
