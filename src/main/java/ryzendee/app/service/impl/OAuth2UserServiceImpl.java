package ryzendee.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryzendee.app.mapper.OAuth2UserAppMapper;
import ryzendee.app.model.User;
import ryzendee.app.model.UserToRole;
import ryzendee.app.repository.UserRepository;
import ryzendee.app.repository.UserToRoleRepository;
import ryzendee.app.service.helpers.OAuth2UserCreator;
import ryzendee.app.service.helpers.OAuth2UserValidator;
import ryzendee.app.service.helpers.UserRoleCreator;

/**
 * Сервис для загрузки и обработки OAuth2 пользователей.
 *
 * Реализует интерфейс {@link OAuth2UserService}, предоставляет логику загрузки
 * и создания пользователя по запросу OAuth2.
 *
 * @author Dmitry Ryazantsev
 */
@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final String EMAIL = "email";

    private final UserRepository userRepository;
    private final UserRoleCreator userToRoleCreator;
    private final UserToRoleRepository userToRoleRepository;
    private final OAuth2UserAppMapper oAuth2UserAppMapper;
    private final OAuth2UserValidator oAuth2UserValidator;
    private final OAuth2UserCreator oAuth2UserCreator;

    /**
     * Загружает пользователя по OAuth2 запросу.
     *
     * <p>Если пользователь с указанным email существует и уже зарегистрирован с OAuth2,
     * выполняется валидация соответствия провайдера OAuth2 из запроса и пользователя.
     * Если пользователь существует, но не привязан к OAuth2, генерируется исключение.
     * Если пользователь с указанным email отсутствует, создаётся новый пользователь с привязкой OAuth2.
     * </p>
     *
     * @param userRequest объект {@link OAuth2UserRequest} с данными OAuth2 запроса
     * @return аутентифицированный {@link OAuth2User} с ролями и данными пользователя
     * @throws OAuth2AuthenticationException если пользователь зарегистрирован без OAuth2 или при несоответствии провайдера
     */
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String email = oAuth2User.getAttribute(EMAIL);
        User user = userRepository.findByEmailWithOAuth2Info(email).orElse(null);

        if (user != null) {
            oAuth2UserValidator.validate(user, userRequest);
        } else {
            user = saveUser(oAuth2User, userRequest);
        }

        return oAuth2UserAppMapper.toOauth2User(user);
    }

    /**
     * Создаёт и сохраняет нового пользователя и назначает ему дефолтную роль.
     *
     * @param oAuth2User   аутентифицированный OAuth2 пользователь
     * @param userRequest  объект запроса OAuth2 с регистрационной информацией
     * @return созданный и сохранённый {@link User}
     */
    private User saveUser(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
        User created = oAuth2UserCreator.create(oAuth2User, userRequest);
        userRepository.saveAndFlush(created);

        UserToRole userToRole = userToRoleCreator.createDefaultRoleWithoutUser();
        userToRole.assignUser(created);
        userToRoleRepository.save(userToRole);

        return created;
    }
}
