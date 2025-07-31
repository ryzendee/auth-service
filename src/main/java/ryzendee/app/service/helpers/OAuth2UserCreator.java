package ryzendee.app.service.helpers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import ryzendee.app.enums.OAuth2Provider;
import ryzendee.app.model.*;

import static java.util.UUID.randomUUID;


@Component
@RequiredArgsConstructor
public class OAuth2UserCreator {

    private static final String EMAIL = "email";
    private final PasswordEncoder passwordEncoder;

    public User create(OAuth2User oAuth2User, OAuth2UserRequest request) {
        User user = User.builder()
                .login(oAuth2User.getAttribute(EMAIL))
                .email(oAuth2User.getAttribute(EMAIL))
                .passwordHash(randomHashPassword())
                .build();

        String clientId = request.getClientRegistration()
                .getClientId();
        OAuth2Provider provider = OAuth2Provider.valueOf(clientId.toUpperCase());
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.builder()
                .user(user)
                .provider(provider)
                .build();

        user.setOAuth2UserInfo(oAuth2UserInfo);

        return user;
    }

    private String randomHashPassword() {
        return passwordEncoder.encode(randomUUID().toString());
    }
}
