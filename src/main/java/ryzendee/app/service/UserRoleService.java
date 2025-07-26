package ryzendee.app.service;

import ryzendee.app.dto.RoleDetails;
import ryzendee.app.dto.RoleSaveRequest;

import java.util.List;

public interface UserRoleService {

    void saveRole(RoleSaveRequest request);
    List<RoleDetails> getUserRolesByLogin(String login);

}
