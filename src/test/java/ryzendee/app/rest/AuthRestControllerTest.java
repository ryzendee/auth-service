package ryzendee.app.rest;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ryzendee.app.config.SecurityConfiguration;
import ryzendee.app.dto.SignInRequest;
import ryzendee.app.dto.SignUpRequest;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.exception.UserExistsException;
import ryzendee.app.service.AuthService;
import ryzendee.starter.jwt.config.JwtSecurityAutoConfiguration;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static ryzendee.app.testutils.FixtureUtil.signInRequestBuilderFixture;
import static ryzendee.app.testutils.FixtureUtil.signUpRequestFixtureBuilder;

@WebMvcTest(AuthRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthRestControllerTest {

    private static final String BASE_URI = "/auth";

    @MockitoBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    private MockMvcRequestSpecification request;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.basePath = BASE_URI;
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        request = RestAssuredMockMvc.given()
                .contentType(ContentType.JSON);
    }

    @Test
    void signUp_validRequest_shouldReturnSignUpResponse() {
        SignUpRequest signUpRequest = signUpRequestFixtureBuilder().build();

        request.body(signUpRequest)
                .put("/signup")
                .then()
                .status(HttpStatus.OK);

        verify(authService).signUp(signUpRequest);
    }

    @Test
    void signUp_userAlreadyExists_shouldReturnConflict() {
        SignUpRequest signUpRequest = signUpRequestFixtureBuilder().build();

        doThrow(new UserExistsException("User with this credentials already exists"))
                .when(authService).signUp(signUpRequest);

        request.body(signUpRequest)
                .put("/signup")
                .then()
                .status(HttpStatus.CONFLICT);

        verify(authService).signUp(signUpRequest);
    }

    @Test
    void signIn_validCredentials_shouldReturnJwtToken() {
        SignInRequest signInRequest = signInRequestBuilderFixture().build();

        request.body(signInRequest)
                .post("/signin")
                .then()
                .status(HttpStatus.OK);

        verify(authService).signIn(signInRequest);
    }

    @Test
    void signIn_wrongLogin_shouldReturnNotFound() {
        SignInRequest signInRequest = signInRequestBuilderFixture().build();

        doThrow(new ResourceNotFoundException("User with this login does not exists"))
                .when(authService).signIn(signInRequest);

        request.body(signInRequest)
                .post("/signin")
                .then()
                .status(HttpStatus.NOT_FOUND);

        verify(authService).signIn(signInRequest);
    }

    @Test
    void signIn_wrongPassword_shouldReturnUnauthorized() {
        SignInRequest signInRequest = signInRequestBuilderFixture().build();

        doThrow(new BadCredentialsException("Invalid password"))
                .when(authService).signIn(signInRequest);

        request.body(signInRequest)
                .post("/signin")
                .then()
                .status(HttpStatus.UNAUTHORIZED);

        verify(authService).signIn(signInRequest);
    }
}
