package ryzendee.app.service.helpers;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Component;
import ryzendee.app.model.OAuth2UserInfo;
import ryzendee.app.model.User;

@Component
public class OAuth2UserValidator {

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
