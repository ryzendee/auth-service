package ryzendee.app.dto;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;


/**
 * Реализация интерфейса {@link OAuth2User}, представляющая аутентифицированного пользователя,
 * авторизованного через OAuth2-провайдера.
 * <p>
 * Используется для передачи информации о пользователе внутри Spring Security контекста.
 * </p>
 *
 * @author Dmitry Ryazantsev
 */
@Builder
public record OAuth2UserImpl (

        String login,
        Collection<? extends GrantedAuthority> authorities,
        Map<String, Object> attributes

) implements OAuth2User {

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return login;
    }
}
