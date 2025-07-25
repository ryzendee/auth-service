package ryzendee.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO для запроса аутентификации и авторизации пользователя.
 *
 * @author Dmitry Ryazantsev
 */

@Schema(description = "Объект-запрос аутентификации")
public record SignInRequest(

        @Schema(description = "Логин пользователя", example = "johndoe")
        @NotBlank(message = "login не может быть пустым")
        String login,

        @Schema(description = "Пароль пользователя", example = "P@ssw0rd")
        @NotBlank(message = "password не может быть пустым")
        @Size(min = 5, max = 32, message = "password должен иметь длину от {min} до {max} символов")
        String password

) {
}
