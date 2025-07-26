package ryzendee.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryzendee.app.dto.RoleDetails;
import ryzendee.app.dto.RoleSaveRequest;
import ryzendee.app.exception.MissingUserRoleException;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.jwt.UserRole;
import ryzendee.app.mapper.UserRoleAppMapper;
import ryzendee.app.model.User;
import ryzendee.app.model.UserToRole;
import ryzendee.app.model.UserToRoleId;
import ryzendee.app.repository.UserRepository;
import ryzendee.app.repository.UserRoleRepository;
import ryzendee.app.repository.UserToRoleRepository;
import ryzendee.app.service.UserRoleService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleAppMapper userRoleAppMapper;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserToRoleRepository userToRoleRepository;

    @Transactional
    @Override
    public void saveRole(RoleSaveRequest request) {
        User user = userRepository.findByLogin(request.login())
                .orElseThrow(() -> new ResourceNotFoundException("User with given login does not exists"));

        List<ryzendee.app.model.Role> roles = userRoleRepository.findByIdIn(request.roles());
        if (roles.size() != request.roles().size()) {
            List<UserRole> missingRoles = findMissingRoles(roles, request.roles());
            throw new MissingUserRoleException("Some roles are missing, try again later", missingRoles);
        }

        List<UserToRole> userToRoles = roles.stream()
                .map(role -> buildUserToRole(user, role))
                .toList();
        userToRoleRepository.saveAll(userToRoles);
    }

    @PreAuthorize("#login == authentication.name or hasRole('ADMIN')")
    @Transactional(readOnly = true)
    @Override
    public List<RoleDetails> getUserRolesByLogin(String login) {
        if (!userRepository.existsByLogin(login)) {
            throw new ResourceNotFoundException("User with given login does not exists");
        }

        return userRoleRepository.findRolesByUserLogin(login).stream()
                .map(userRoleAppMapper::toDetails)
                .toList();
    }

    private List<UserRole> findMissingRoles(List<ryzendee.app.model.Role> roles, List<UserRole> requestedRoles) {
        Set<UserRole> foundRolesSet = roles.stream()
                .map(ryzendee.app.model.Role::getId)
                .collect(Collectors.toSet());

        return requestedRoles.stream()
                .filter(role -> !foundRolesSet.contains(role))
                .toList();
    }

    private UserToRole buildUserToRole(User user, ryzendee.app.model.Role role) {
        UserToRoleId id = UserToRoleId.builder()
                .roleId(role.getId())
                .userId(user.getId())
                .build();

        return UserToRole.builder()
                .id(id)
                .user(user)
                .role(role)
                .build();
    }
}
