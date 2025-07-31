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

/**
 * Маппер для преобразования {@link OAuth2User}.
 *
 * @author Dmitry Ryazantsev
 */
@Component
public class OAuth2UserAppMapper {

    private static final String ROLE_PREFIX = "ROLE_";

    /**
     * Преобразует пользователя доменной модели в {@link OAuth2User}.
     * Используется в Spring Security для идентификации и авторизации пользователя после входа.
     *
     * @param user объект {@link User}, представляющий пользователя из базы данных
     * @return {@link OAuth2User}, реализующий интерфейс Spring Security с нужными авторитетами
     */
    public OAuth2User toOauth2User(User user) {
        return OAuth2UserImpl.builder()
                .login(user.getLogin())
                .authorities(mapRoles(user.getRoles()))
                .build();
    }

    /**
     * Преобразует {@link OAuth2User} в {@link JwtPayload} — данные для создания JWT-токена.
     *
     * @param oAuth2User объект, содержащий информацию об аутентифицированном пользователе
     * @return {@link JwtPayload}, содержащий имя пользователя и список его ролей
     */
    public JwtPayload toJwtPayload(OAuth2User oAuth2User) {
        return new JwtPayload(oAuth2User.getName(), mapToAuthRoleList(oAuth2User.getAuthorities()));
    }

    /**
     * Преобразует список {@link UserToRole} в коллекцию {@link SimpleGrantedAuthority}, добавляя префикс <code>ROLE_</code>.
     *
     * @param roles список связей "пользователь-роль"
     * @return коллекция авторитетов с префиксами, подходящими для Spring Security
     */
    private Collection<SimpleGrantedAuthority> mapRoles(List<UserToRole> roles) {
        return roles.stream()
                .map(UserToRole::getRole)
                .map(Role::getId)
                .map(roleName -> ROLE_PREFIX + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * Преобразует коллекцию {@link GrantedAuthority} в список {@link AuthRole},
     * удаляя префикс <code>ROLE_</code>, чтобы соответствовать enum значению.
     *
     * @param authorities коллекция ролей Spring Security
     * @return список {@link AuthRole}, используемых в JWT-пэйлоаде
     */
    private List<AuthRole> mapToAuthRoleList(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(this::removePrefix)
                .map(AuthRole::valueOf)
                .toList();
    }

    /**
     * Удаляет префикс <code>ROLE_</code> из строки роли, если он присутствует.
     *
     * @param authority строка роли, возможно с префиксом
     * @return строка без префикса
     */
    private String removePrefix(String authority) {
        return authority.startsWith(ROLE_PREFIX)
                ? authority.substring(ROLE_PREFIX.length())
                : authority;
    }
}
