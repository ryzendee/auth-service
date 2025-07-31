package ryzendee.app.service.helpers;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Component;
import ryzendee.app.model.OAuth2UserInfo;
import ryzendee.app.model.User;

/**
 * Компонент для валидации соответствия OAuth2-провайдера у зарегистрированного пользователя.
 * <p>
 * Проверяет, что пользователь зарегистрирован через OAuth2 и что провайдер, указанный в запросе,
 * соответствует провайдеру, сохранённому в базе данных.
 *
 * @author Dmitry Ryazantsev
 */
@Component
public class OAuth2UserValidator {

    /**
     * Проверяет, что пользователь имеет OAuth2-привязку и провайдер запроса совпадает
     * с провайдером, с которым зарегистрирован пользователь.
     *
     * @param user пользователь, найденный по email
     * @param request объект {@link OAuth2UserRequest}, содержащий информацию о провайдере (например, "google", "facebook")
     *
     * @throws OAuth2AuthenticationException если:
     * <ul>
     *     <li>у пользователя отсутствует {@link OAuth2UserInfo}</li>
     *     <li>или провайдер из запроса не совпадает с зарегистрированным провайдером</li>
     * </ul>
     */
    public void validate(User user, OAuth2UserRequest request) {
        OAuth2UserInfo info = user.getOAuth2UserInfo();

        if (info == null) {
            throw new OAuth2AuthenticationException("User is registered without OAuth2");
        }

        String registered = info.getProvider().name();
        String incoming = request.getClientRegistration().getRegistrationId();

        if (!registered.equalsIgnoreCase(incoming)) {
            throw new OAuth2AuthenticationException("Wrong provider: registered as " + registered);
        }
    }

}
