package ryzendee.app.service;

import ryzendee.app.dto.SignInRequest;
import ryzendee.app.dto.SignInResponse;
import ryzendee.app.dto.SignUpRequest;
import ryzendee.app.dto.SignUpResponse;

import ryzendee.app.dto.SignInRequest;
import ryzendee.app.dto.SignInResponse;
import ryzendee.app.dto.SignUpRequest;
import ryzendee.app.dto.SignUpResponse;

/**
 * Сервис для аутентификации и регистрации пользователей.
 *
 * @author Dmitry Ryazantsev
 */
public interface AuthService {

    /**
     * Регистрирует нового пользователя.
     *
     * @param request DTO с данными для регистрации
     * @return SignUpResponse с результатом регистрации
     */
    SignUpResponse signUp(SignUpRequest request);

    /**
     * Аутентифицирует пользователя по логину и паролю.
     *
     * @param request DTO с учетными данными
     * @return SignInResponse с JWT и информацией о пользователе
     */
    SignInResponse signIn(SignInRequest request);
}
