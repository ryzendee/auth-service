package ryzendee.app.exception;

/**
 * Исключение, выбрасываемое при отсутствии ресурса по запросу.
 * Используется для сигнализации, что искомый объект не найден.
 *
 * @author Dmitry Ryazantsev
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
