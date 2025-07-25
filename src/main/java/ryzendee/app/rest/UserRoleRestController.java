package ryzendee.app.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ryzendee.app.dto.RoleDetails;
import ryzendee.app.dto.RoleSaveRequest;
import ryzendee.app.rest.api.UserRoleApi;
import ryzendee.app.service.UserRoleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserRoleRestController implements UserRoleApi {

    private final UserRoleService userRoleService;
    @Override
    public void saveRole(RoleSaveRequest request) {
        userRoleService.saveRole(request);
    }

    @Override
    public List<RoleDetails> gerUserRolesByLogin(String login) {
        return userRoleService.gerUserRolesByLogin(login);
    }
}
