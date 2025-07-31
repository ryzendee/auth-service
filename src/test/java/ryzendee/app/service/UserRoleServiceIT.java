package ryzendee.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import ryzendee.app.dto.RoleDetails;
import ryzendee.app.dto.RoleSaveRequest;
import ryzendee.app.exception.MissingUserRoleException;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.mapper.UserRoleAppMapper;
import ryzendee.app.repository.UserRepository;
import ryzendee.app.repository.UserRoleRepository;
import ryzendee.app.repository.UserToRoleRepository;
import ryzendee.app.service.impl.UserRoleServiceImpl;
import ryzendee.starter.jwt.decoder.AuthRole;
import ryzendee.app.model.Role;
import ryzendee.app.model.User;
import ryzendee.app.testutils.DatabaseUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ryzendee.app.testutils.FixtureUtil.*;

@DataJpaTest
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

        roleAdmin = saveWithRole(AuthRole.ADMIN);
        roleUser = saveWithRole(AuthRole.USER);

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
        assertThat(savedRoleDetails.getFirst().id()).isIn(AuthRole.ADMIN, AuthRole.USER);
    }

    @Test
    void saveRole_nonExistingUser_shouldThrowResourceNotFoundException() {
        RoleSaveRequest request = roleSaveRequestBuilderFixture()
                .build();

        assertThatThrownBy(() -> userRoleService.saveRole(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .message().isNotBlank();
    }

    @Test
    void saveRole_missingRole_shouldThrowMissingUserRoleException() {
        RoleSaveRequest request = roleSaveRequestBuilderFixture()
                .login(testUser.getLogin())
                .roles(List.of(AuthRole.CREDIT_USER))
                .build();

        assertThatThrownBy(() -> userRoleService.saveRole(request))
                .isInstanceOf(MissingUserRoleException.class)
                .message().isNotBlank();
    }

    @Test
    void getUserRolesByLogin_existingUser_shouldReturnRoles() {
        userRoleService.saveRole(roleSaveRequestBuilderFixture()
                .login(testUser.getLogin())
                .build());

        List<RoleDetails> roleDetails = userRoleService.getUserRolesByLogin(testUser.getLogin());
        assertThat(roleDetails).isNotEmpty();
        assertThat(roleDetails.getFirst().id()).isEqualTo(AuthRole.ADMIN);
    }

    @Test
    void getUserRolesByLogin_nonExistingUser_shouldThrowResourceNotFoundException() {
        assertThatThrownBy(() -> userRoleService.getUserRolesByLogin("nonexistent"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Role saveWithRole(AuthRole userRole) {
        Role role = roleFixture();
        role.setId(userRole);
        return databaseUtil.save(role);
    }

    @TestConfiguration
    static class Config {

        @Bean
        public UserRoleService userRoleService(UserRoleAppMapper mapper,
                                               UserRepository userRepository,
                                               UserRoleRepository roleRepository,
                                               UserToRoleRepository userToRoleRepository) {
            return new UserRoleServiceImpl(mapper, userRepository, roleRepository, userToRoleRepository);
        }

        @Bean
        public UserRoleAppMapper userRoleAppMapper() {
            return Mappers.getMapper(UserRoleAppMapper.class);
        }
    }
}
