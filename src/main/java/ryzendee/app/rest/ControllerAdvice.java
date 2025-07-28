package ryzendee.app.rest;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ryzendee.app.dto.ErrorDetails;
import ryzendee.app.exception.MissingUserRoleException;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.exception.UserExistsException;
import ryzendee.starter.jwt.decoder.AuthRole;

import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(UserExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDetails handleUserExistsException(UserExistsException ex) {
        return createErrorDetails(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDetails handleResourceNotFoundException(ResourceNotFoundException ex) {
        return createErrorDetails(ex.getMessage());
    }

    @ExceptionHandler(MissingUserRoleException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDetails handleMissingUserRoleException(MissingUserRoleException ex) {
        if (!ex.getMissingRoles().isEmpty()) {
            String msg = formatMissingUserRoleMessage(ex);
        }
        return createErrorDetails(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDetails handleBadCredentialsException(BadCredentialsException ex) {
        return createErrorDetails(ex.getMessage());
    }

    public ErrorDetails createErrorDetails(String message) {
        return new ErrorDetails(List.of(message));
    }

    private String formatMissingUserRoleMessage(MissingUserRoleException ex) {
        List<String> stringRoles = ex.getMissingRoles().stream()
                .map(AuthRole::name)
                .toList();
        return ex.getMessage() + ": " + String.join(", ", stringRoles);
    }

}
