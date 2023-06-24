package gratchuvalsky.spring_boot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;


@Component
public class AuthProvider implements AuthenticationProvider {
    private UserDetailsService userDetailsService;

    @Autowired
    public AuthProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        String password = authentication.getCredentials().toString();
        if(!password.equals(userDetails.getPassword()))
            throw new BadCredentialsException("incorrect password!");

        return new UsernamePasswordAuthenticationToken(userDetails,
                password, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true ;
    }
}
