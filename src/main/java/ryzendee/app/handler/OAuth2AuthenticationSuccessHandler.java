package ryzendee.app.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ryzendee.app.mapper.OAuth2UserAppMapper;
import ryzendee.starter.jwt.decoder.JwtDecoder;
import ryzendee.starter.jwt.decoder.JwtPayload;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtDecoder jwtDecoder;
    private final OAuth2UserAppMapper oAuth2UserAppMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        JwtPayload jwtPayload = oAuth2UserAppMapper.toJwtPayload(oAuth2User);
        String token = jwtDecoder.createJwt(jwtPayload);

        // Для демонстрации токена (обработка в LoginController)
        HttpSession session = request.getSession();
        session.setAttribute("token", token);

        response.sendRedirect("/login");
    }
}
