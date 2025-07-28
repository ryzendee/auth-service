package ryzendee.app.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ryzendee.app.dto.SignInRequest;
import ryzendee.app.dto.SignInResponse;
import ryzendee.app.dto.SignUpRequest;
import ryzendee.app.dto.SignUpResponse;

@RequestMapping("/auth")
@Tag(name = "API аутентификации", description = "Операции регистрации и входа пользователей")
public interface AuthApi {

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создаёт нового пользователя с уникальными логином и email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
                    @ApiResponse(responseCode = "409", description = "Пользователь с таким логином или email уже существует"),
                    @ApiResponse(responseCode = "400", description = "Некорректные входные данные")
            }
    )
    @PutMapping("/signup")
    SignUpResponse signUp(@Valid @RequestBody SignUpRequest request);

    @Operation(
            summary = "Вход пользователя",
            description = "Аутентификация пользователя по логину и паролю. Возвращает JWT для последующего доступа к сервисам",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Аутентификация прошла успешно"),
                    @ApiResponse(responseCode = "401", description = "Неверный логин или пароль")
            }
    )
    @PostMapping("/signin")
    SignInResponse signIn(@Valid @RequestBody SignInRequest request);
}
