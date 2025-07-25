package ryzendee.app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ryzendee.app.jwt.JwtDecoder;
import ryzendee.app.jwt.JwtPayload;
import ryzendee.app.jwt.UserRole;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtDecoder jwtDecoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String jwt = (String) authentication.getCredentials();

        JwtPayload payload;
        try {
            payload = jwtDecoder.parseToken(jwt);
        } catch (Exception ex) {
            throw new BadCredentialsException("Invalid JWT token", ex);
        }

        List<GrantedAuthority> authorities = roleToGrantedAuthorities(payload.roles());
        return new JwtAuthenticationToken(payload.subject(), jwt, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private List<GrantedAuthority> roleToGrantedAuthorities(List<UserRole> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }
}
