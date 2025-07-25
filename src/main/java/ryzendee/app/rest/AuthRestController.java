package ryzendee.app.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ryzendee.app.dto.SignInRequest;
import ryzendee.app.dto.SignInResponse;
import ryzendee.app.dto.SignUpRequest;
import ryzendee.app.dto.SignUpResponse;
import ryzendee.app.rest.api.AuthApi;
import ryzendee.app.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthRestController implements AuthApi {

    private final AuthService authService;

    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        return authService.signUp(request);
    }

    @Override
    public SignInResponse signIn(SignInRequest request) {
        return authService.signIn(request);
    }
}
