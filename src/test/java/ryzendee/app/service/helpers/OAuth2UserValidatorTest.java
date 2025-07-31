package ryzendee.app.service.helpers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import ryzendee.app.enums.OAuth2Provider;
import ryzendee.app.model.OAuth2UserInfo;
import ryzendee.app.model.User;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class OAuth2UserValidatorTest {

    @InjectMocks
    private OAuth2UserValidator validator;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().build();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.builder()
                .id(randomUUID())
                .user(user)
                .provider(OAuth2Provider.GOOGLE)
                .build();
        user.setOAuth2UserInfo(oAuth2UserInfo);
    }

    @Test
    void validate_OAuth2InfoIsNull_throwEx() {
        user.setOAuth2UserInfo(null);

        assertThatThrownBy(() -> validator.validate(user, requestWithProvider("google")))
                .isInstanceOf(OAuth2AuthenticationException.class);
    }

    @Test
    void validate_userRegisteredWithDifferentProvider_throwEx() {
        assertThatThrownBy(() -> validator.validate(user, requestWithProvider("github")))
                .isInstanceOf(OAuth2AuthenticationException.class);
    }

    @Test
    void validate_validProvider_shouldPass() {
        String provider = OAuth2Provider.GOOGLE.name();

        assertThatCode(() -> validator.validate(user, requestWithProvider(provider)))
                .doesNotThrowAnyException();
    }

    private OAuth2UserRequest requestWithProvider(String provider) {
        ClientRegistration registration = ClientRegistration.withRegistrationId(provider)
                .clientId(provider)
                .clientSecret("secret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost/callback")
                .authorizationUri("http://auth")
                .tokenUri("http://token")
                .userInfoUri("http://userinfo")
                .userNameAttributeName("email")
                .clientName(provider)
                .build();

        return new OAuth2UserRequest(registration, mock(OAuth2AccessToken.class));
    }
}
