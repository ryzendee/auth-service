package ryzendee.app.exception;

import lombok.Getter;
import ryzendee.starter.jwt.decoder.AuthRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Исключение, выбрасываемое при отсутствии ролеи.
 *
 * @author Dmitry Ryazantsev
 */

@Getter
public class MissingUserRoleException extends RuntimeException {

    private final List<AuthRole> missingRoles;

    public MissingUserRoleException(String message, List<AuthRole> missingRoles) {
        super(message);
        this.missingRoles = missingRoles;
    }

    public MissingUserRoleException(String message) {
        super(message);
        this.missingRoles = new ArrayList<>();
    }
}
