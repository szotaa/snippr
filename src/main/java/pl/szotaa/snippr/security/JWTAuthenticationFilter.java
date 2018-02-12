package pl.szotaa.snippr.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.szotaa.snippr.user.domain.ApplicationUser;
import pl.szotaa.snippr.user.domain.Role;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;

import static pl.szotaa.snippr.security.SecurityConstants.*;

@AllArgsConstructor
@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try{
            ApplicationUser credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), ApplicationUser.class);

            log.info("attempt aith: " + credentials.toString());

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
                                            Authentication authResult) throws IOException, ServletException {


        User user = (User) authResult.getPrincipal();
        Claims claims = Jwts.claims();
        claims.setSubject(user.getUsername());
        claims.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
        log.info("succesfulAuth: " + user.getAuthorities().toString());

        StringBuilder roles = new StringBuilder();
        for(GrantedAuthority authority : user.getAuthorities()){
            Role role = (Role) authority;
            roles.append(role.getRoleName());
            roles.append(", ");//TODO: change this
        }

        log.info("roles.toString(): " + roles.toString());

        claims.put("roles", roles.toString());

        String token = Jwts.builder()
                .setClaims(claims)
                /*.signWith(SignatureAlgorithm.HS512, SECRET.getBytes())*/ //TODO: find out why exception is thrown with signed claims
                .compact();
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + token);
    }
}
