package ryzendee.app.exception;

/**
 * Исключение, выбрасываемое при попытке создать пользователя, который уже существует.
 *
 * @author Dmitry Ryazantsev
 */
public class UserExistsException extends RuntimeException {

    public UserExistsException(String message) {
        super(message);
    }
}
