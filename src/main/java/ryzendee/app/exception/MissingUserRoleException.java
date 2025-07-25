package ryzendee.app.exception;

import ryzendee.app.jwt.UserRole;

import java.util.List;

/**
 * Исключение, выбрасываемое при отсутствии ролеи.
 *
 * @author Dmitry Ryazantsev
 */

public class MissingUserRoleException extends RuntimeException {

    private final List<UserRole> missingRoles;

    public MissingUserRoleException(String message, List<UserRole> missingRoles) {
        super(message);
        this.missingRoles = missingRoles;
    }
}
