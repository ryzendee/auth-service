package ryzendee.app.mapper;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import ryzendee.app.dto.OAuth2UserImpl;
import ryzendee.app.model.Role;
import ryzendee.app.model.User;
import ryzendee.app.model.UserToRole;
import ryzendee.starter.jwt.decoder.AuthRole;
import ryzendee.starter.jwt.decoder.JwtPayload;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OAuth2UserAppMapper {

    private static final String ROLE_PREFIX = "ROLE_";

    public OAuth2User toOauth2User(User user) {
        return OAuth2UserImpl.builder()
                .login(user.getLogin())
                .authorities(mapRoles(user.getRoles()))
                .build();
    }

    public JwtPayload toJwtPayload(OAuth2User oAuth2User) {
        return new JwtPayload(oAuth2User.getName(), mapToAuthRoleList(oAuth2User.getAuthorities()));
    }

    private Collection<SimpleGrantedAuthority> mapRoles(List<UserToRole> roles) {
        return roles.stream()
                .map(UserToRole::getRole)
                .map(Role::getId)
                .map(roleName -> ROLE_PREFIX + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private List<AuthRole> mapToAuthRoleList(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(this::removePrefix)
                .map(AuthRole::valueOf)
                .toList();
    }

    private String removePrefix(String authority) {
        return authority.startsWith(ROLE_PREFIX)
                ? authority.substring(ROLE_PREFIX.length())
                : authority;
    }
}
