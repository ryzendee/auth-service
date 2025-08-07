package ryzendee.app.service.helpers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ryzendee.app.exception.MissingUserRoleException;
import ryzendee.app.model.Role;
import ryzendee.app.model.UserToRole;
import ryzendee.app.model.UserToRoleId;
import ryzendee.app.repository.UserRoleRepository;
import ryzendee.starter.jwt.decoder.AuthRole;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserRoleCreator {

    private static final AuthRole DEFAULT_ROLE = AuthRole.USER;

    private final UserRoleRepository userRoleRepository;

    public UserToRole createDefaultRoleWithoutUser() {
        Role role = userRoleRepository.findById(DEFAULT_ROLE)
                .orElseThrow(() -> new MissingUserRoleException("Failed to assign default role", List.of(DEFAULT_ROLE)));

        UserToRoleId id = UserToRoleId.builder()
                .roleId(role.getId())
                .build();

        return UserToRole.builder()
                .id(id)
                .role(role)
                .build();
    }
}
