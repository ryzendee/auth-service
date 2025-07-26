package ryzendee.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ryzendee.app.dto.RoleDetails;
import ryzendee.app.dto.RoleSaveRequest;
import ryzendee.app.exception.MissingUserRoleException;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.jwt.UserRole;
import ryzendee.app.model.Role;
import ryzendee.app.model.User;
import ryzendee.app.testutils.DatabaseUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ryzendee.app.testutils.FixtureUtil.*;

@WithMockUser(roles = "ADMIN")
public class UserRoleServiceIT extends AbstractServiceIT {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private DatabaseUtil databaseUtil;

    private User testUser;
    private Role roleAdmin;
    private Role roleUser;

    @BeforeEach
    void setUp() {
        databaseUtil.cleanDatabase();

        roleAdmin = saveWithRole(UserRole.ADMIN);
        roleUser = saveWithRole(UserRole.USER);

        testUser = userFixture();
        databaseUtil.save(testUser);
    }

    @Test
    void saveRole_existingUserAndValidRoles_shouldSaveRoles() {
        RoleSaveRequest request = roleSaveRequestBuilderFixture()
                .login(testUser.getLogin())
                .build();

        userRoleService.saveRole(request);

        List<RoleDetails> savedRoleDetails = userRoleService.getUserRolesByLogin(testUser.getLogin());
        assertThat(savedRoleDetails).isNotEmpty();
        assertThat(savedRoleDetails.getFirst().id()).isIn(UserRole.ADMIN, UserRole.USER);
    }

    @Test
    void saveRole_nonExistingUser_shouldThrowResourceNotFoundException() {
        RoleSaveRequest request = roleSaveRequestBuilderFixture()
                .build();

        assertThatThrownBy(() -> userRoleService.saveRole(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User with given login does not exists");
    }

    @Test
    void saveRole_missingRole_shouldThrowMissingUserRoleException() {
        RoleSaveRequest request = roleSaveRequestBuilderFixture()
                .login(testUser.getLogin())
                .roles(List.of(UserRole.CREDIT_USER))
                .build();

        assertThatThrownBy(() -> userRoleService.saveRole(request))
                .isInstanceOf(MissingUserRoleException.class)
                .hasMessageContaining("Some roles are missing");
    }

    @Test
    void getUserRolesByLogin_existingUser_shouldReturnRoles() {
        userRoleService.saveRole(roleSaveRequestBuilderFixture()
                .login(testUser.getLogin())
                .build());

        List<RoleDetails> roleDetails = userRoleService.getUserRolesByLogin(testUser.getLogin());
        assertThat(roleDetails).isNotEmpty();
        assertThat(roleDetails.getFirst().id()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    void getUserRolesByLogin_nonExistingUser_shouldThrowResourceNotFoundException() {
        assertThatThrownBy(() -> userRoleService.getUserRolesByLogin("nonexistent"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Role saveWithRole(UserRole userRole) {
        Role role = roleFixture();
        role.setId(userRole);
        return databaseUtil.save(role);
    }
}
