package ryzendee.app.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ryzendee.app.dto.RoleDetails;
import ryzendee.app.dto.RoleSaveRequest;

import java.util.List;

@RequestMapping("/user-role")
@Tag(name = "API ролей пользователей", description = "Управление ролями пользователей с контролем доступа")
public interface UserRoleApi {

    @Operation(
            summary = "Сохранение перечня ролей пользователя",
            description = "Сохраняет роли пользователя по логину. Доступно только авторизованным ADMIN пользователям.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Роли успешно сохранены"),
                    @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен из-за недостаточных прав"),
                    @ApiResponse(responseCode = "404", description = "Пользователь с указанным логином не найден"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервиса: роль отсутствует в базе данных")
            }
    )
    @PutMapping("/save")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void saveRole(@Valid @RequestBody RoleSaveRequest request);

    @Operation(
            summary = "Получение списка ролей пользователя",
            description = "Возвращает список ролей для пользователя по логину. " +
                    "Для ADMIN возвращает роли любого пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список ролей успешно получен"),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав для просмотра ролей другого пользователя"),
                    @ApiResponse(responseCode = "404", description = "Пользователь с указанным логином не найден")
            }
    )
    @GetMapping("/{login}")
    List<RoleDetails> gerUserRolesByLogin(@PathVariable("login") String login);
}
