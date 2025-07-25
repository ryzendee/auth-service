package ryzendee.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import ryzendee.app.jwt.UserRole;

/**
 * DTO-запрос для сохранения ролей пользователя.
 * Используется для назначения одной или нескольких ролей пользователю по логину.
 *
 * @author Dmitry Ryazantsev
 */
@Builder
@Schema(description = "DTO-запрос для сохранения ролей пользователя")
public record RoleSaveRequest(

        @Schema(description = "Логин пользователя", example = "ivan.petrov")
        @NotBlank(message = "login не может быть пустым")
        String login,

        @Schema(description = "Массив назначаемых ролей", example = "[\"ADMIN\", \"SUPERUSER\"]")
        @NotEmpty(message = "roles не может быть пустым")
        UserRole[] roles

) {
}
