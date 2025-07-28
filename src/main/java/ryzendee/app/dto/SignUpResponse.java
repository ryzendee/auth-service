package ryzendee.app.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

/**
 * DTO для возврата информации о зарегистрированном пользователе.
 *
 * @author Dmitry Ryazantsev
 */
@Builder
@Schema(description = "Информация о зарегистрированном пользователе")
public record SignUpResponse(

        @Schema(description = "Уникальный идентификатор пользователя", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
        UUID id,
        @Schema(description = "Логин пользователя", example = "johndoe")
        String login,

        @Schema(description = "Email пользователя", example = "john.doe@example.com")
        String email

) {
}
