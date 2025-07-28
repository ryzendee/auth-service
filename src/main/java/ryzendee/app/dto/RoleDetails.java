package ryzendee.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ryzendee.starter.jwt.decoder.AuthRole;

/**
 * DTO для передачи информации о роли пользователя.
 *
 * @author Dmitry Ryazantsev
 */
@Schema(description = "DTO для передачи информации о роли пользователя")
public record RoleDetails(

        @Schema(description = "Уникальный идентификатор роли", example = "ADMIN")
        AuthRole id,

        @Schema(description = "Человекочитаемое название роли", example = "Администратор")
        String name
) {
}
