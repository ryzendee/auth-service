package ryzendee.app.rest;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ryzendee.app.config.SecurityConfiguration;
import ryzendee.app.dto.RoleSaveRequest;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.security.JwtAuthenticationToken;
import ryzendee.app.service.UserRoleService;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static ryzendee.app.testutils.FixtureUtil.roleSaveRequestBuilderFixture;

@WebMvcTest(UserRoleRestController.class)
@Import(SecurityConfiguration.class)
public class UserRoleRestControllerTest {

    private static final String BASE_URI = "/user-role";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRoleService userRoleService;

    private MockMvcRequestSpecification request;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.basePath = BASE_URI;
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        request = RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .auth().authentication(createAuthentication());
    }

    @Test
    void saveRole_validRequest_shouldReturnNoContent() {
        RoleSaveRequest saveRequest = roleSaveRequestBuilderFixture().build();

        request.body(saveRequest)
                .put("/save")
                .then()
                .status(HttpStatus.NO_CONTENT);

        verify(userRoleService).saveRole(saveRequest);
    }

    @Test
    void saveRole_userNotFound_shouldReturnNotFound() {
        RoleSaveRequest saveRequest = roleSaveRequestBuilderFixture().build();

        doThrow(new ResourceNotFoundException("User with given login does not exists"))
                .when(userRoleService).saveRole(saveRequest);

        request.body(saveRequest)
                .put("/save")
                .then()
                .status(HttpStatus.NOT_FOUND);

        verify(userRoleService).saveRole(saveRequest);
    }

    @Test
    void getUserRoles_validUser_shouldReturnRoles() {
        String login = "john";

        request.get("/{login}", login)
                .then()
                .status(HttpStatus.OK);

        verify(userRoleService).getUserRolesByLogin(login);
    }

    @Test
    void getUserRoles_userNotFound_shouldReturnNotFound() {
        String login = "unknown";

        doThrow(new ResourceNotFoundException("User with given login does not exists"))
                .when(userRoleService).getUserRolesByLogin(login);

        request.get("/{login}", login)
                .then()
                .status(HttpStatus.NOT_FOUND);

        verify(userRoleService).getUserRolesByLogin(login);
    }

    private Authentication createAuthentication() {
        return new JwtAuthenticationToken("user", "jwt", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}
