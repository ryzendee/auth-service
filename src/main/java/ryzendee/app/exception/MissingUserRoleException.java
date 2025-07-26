package ryzendee.app.exception;

import ryzendee.starter.jwt.decoder.AuthRole;

import java.util.List;

/**
 * Исключение, выбрасываемое при отсутствии ролеи.
 *
 * @author Dmitry Ryazantsev
 */

public class MissingUserRoleException extends RuntimeException {

    private final List<AuthRole> missingRoles;

    public MissingUserRoleException(String message, List<AuthRole> missingRoles) {
        super(message);
        this.missingRoles = missingRoles;
    }
}
