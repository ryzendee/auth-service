package ryzendee.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

/**
 * DTO для запроса сохранения пользователя.
 *
 * @author Dmitry Ryazantsev
 */
@Builder
@Schema(description = "Запрос на сохранение пользователя")
public record SignUpRequest(
        @Schema(description = "Логин пользователя", example = "johndoe")
        @NotBlank(message = "login не может быть пустым")
        String login,

        @Schema(description = "Email пользователя", example = "john.doe@example.com")
        @NotBlank(message = "email не может быть пустым")
        @Email(message = "email имеет неверный формат")
        String email,

        @Schema(description = "Пароль пользователя", example = "P@ssw0rd")
        @NotBlank(message = "password не может быть пустым")
        @Size(min = 5, max = 32, message = "password должен иметь длину от {min} до {max} символов")
        String password
) {
}
