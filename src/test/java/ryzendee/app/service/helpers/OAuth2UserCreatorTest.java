package ryzendee.app.service.helpers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import ryzendee.app.enums.OAuth2Provider;
import ryzendee.app.model.User;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OAuth2UserCreatorTest {

    @InjectMocks
    private OAuth2UserCreator userCreator;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void create_shouldReturnUserWithEmailAndEncodedPasswordAndOAuth2Info() {
        // arrange
        String email = "test@example.com";
        String encodedPassword = "encoded-" + UUID.randomUUID();
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);

        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(oAuth2User.getAttribute("email")).thenReturn(email);

        ClientRegistration registration = ClientRegistration
                .withRegistrationId("google")
                .clientId("google")
                .authorizationGrantType(org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                .tokenUri("https://oauth2.googleapis.com/token")
                .userInfoUri("https://openidconnect.googleapis.com/v1/userinfo")
                .userNameAttributeName("email")
                .clientName("Google")
                .build();

        OAuth2UserRequest userRequest = new OAuth2UserRequest(registration, mock(OAuth2AccessToken.class));

        // act
        User user = userCreator.create(oAuth2User, userRequest);

        // assert
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getLogin()).isEqualTo(email);
        assertThat(user.getPasswordHash()).isEqualTo(encodedPassword);
        assertThat(user.getOAuth2UserInfo()).isNotNull();
        assertThat(user.getOAuth2UserInfo().getProvider()).isEqualTo(OAuth2Provider.GOOGLE);
        assertThat(user.getOAuth2UserInfo().getUser()).isEqualTo(user);
    }

}
