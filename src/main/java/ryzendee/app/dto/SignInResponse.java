package ryzendee.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * DTO для возврата ответа на авторизацию и аутентификацию.
 *
 * @author Dmitry Ryazantsev
 */
@Builder
@Schema(description = "Объект ответа на пользовательский запрос аутентификации")
public record SignInResponse(

        @Schema(description = "Jwt токен", example = "d290f1eesadZX6c544b01ABvvADwev90e6d701748f0851")
        String token
) {
}
