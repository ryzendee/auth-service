package ryzendee.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import ryzendee.app.dto.SignInRequest;
import ryzendee.app.dto.SignInResponse;
import ryzendee.app.dto.SignUpRequest;
import ryzendee.app.dto.SignUpResponse;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.exception.UserExistsException;
import ryzendee.starter.jwt.decoder.AuthRole;
import ryzendee.app.model.Role;
import ryzendee.app.model.User;
import ryzendee.app.model.UserToRole;
import ryzendee.app.model.UserToRoleId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ryzendee.app.testutils.FixtureUtil.*;

public class AuthServiceIT extends AbstractServiceIT {

    private static final AuthRole DEFAULT_ROLE_USER = AuthRole.USER;

    @Autowired
    private AuthService authService;

    private Role defaultRole;

    @BeforeEach
    void setUp() {
        databaseUtil.cleanDatabase();

        Role defaultRole = roleFixture();
        defaultRole.setId(DEFAULT_ROLE_USER);
        databaseUtil.save(defaultRole);
    }

    @Test
    void signUp_shouldCreateUserWithDefaultRole() {
        SignUpRequest request = signUpRequestFixtureBuilder().build();

        SignUpResponse response = authService.signUp(request);

        User saved = databaseUtil.find(response.id(), User.class);
        assertThat(saved).isNotNull();

        UserToRoleId userToRoleId = databaseUtil.findAll(UserToRole.class)
                .getFirst()
                .getId();
        assertThat(userToRoleId.getUserId()).isEqualTo(saved.getId());
        assertThat(userToRoleId.getRoleId()).isEqualTo(DEFAULT_ROLE_USER);
    }

    @Test
    void signUp_existingLogin_shouldThrowUserExistsException() {
        User existing = userFixture();
        databaseUtil.save(existing);
        SignUpRequest request = signUpRequestFixtureBuilder()
                .login(existing.getLogin())
                .email(existing.getEmail())
                .build();

        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(UserExistsException.class);
    }

    @Test
    void signIn_validCredentials_shouldReturnToken() {
        SignUpRequest request = signUpRequestFixtureBuilder().build();
        authService.signUp(request);
        SignInRequest signIn = SignInRequest.builder()
                .login(request.login())
                .password(request.password())
                .build();

        SignInResponse response = authService.signIn(signIn);

        assertThat(response.token()).isNotBlank();
    }

    @Test
    void signIn_invalidPassword_shouldThrowBadCredentials() {
        SignUpRequest request = signUpRequestFixtureBuilder().build();
        authService.signUp(request);
        SignInRequest wrongRequest = SignInRequest.builder()
                .login(request.login())
                .password("qwerty123")
                .build();

        assertThatThrownBy(() -> authService.signIn(wrongRequest))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void signIn_unknownUser_shouldThrowNotFound() {
        SignInRequest request = signInRequestBuilderFixture().build();

        assertThatThrownBy(() -> authService.signIn(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
