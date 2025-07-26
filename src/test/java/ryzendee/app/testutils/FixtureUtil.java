package ryzendee.app.testutils;

import ryzendee.app.dto.RoleSaveRequest;
import ryzendee.app.dto.SignInRequest;
import ryzendee.app.dto.SignUpRequest;
import ryzendee.starter.jwt.decoder.AuthRole;
import ryzendee.app.model.Role;
import ryzendee.app.model.User;

import java.util.List;

public class FixtureUtil {

    public static RoleSaveRequest.RoleSaveRequestBuilder roleSaveRequestBuilderFixture() {
        return RoleSaveRequest.builder()
                .login("role")
                .roles( List.of(AuthRole.ADMIN));
    }


    public static SignUpRequest.SignUpRequestBuilder signUpRequestFixtureBuilder() {
        return SignUpRequest.builder()
                .login("user")
                .email("user@gmail.com")
                .password("password");
    }

    public static SignInRequest.SignInRequestBuilder signInRequestBuilderFixture() {
        return SignInRequest.builder()
                .login("user")
                .password("password");
    }

    public static Role roleFixture() {
        return Role.builder()
                .id(AuthRole.USER)
                .name("Пользователь")
                .build();
    }

    public static User userFixture() {
        return User.builder()
                .login("user")
                .email("user@gmail.com")
                .passwordHash("asdmvxcE3w903esjoplsd")
                .build();
    }
}
