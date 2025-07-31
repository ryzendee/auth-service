package ryzendee.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryzendee.app.dto.SignInRequest;
import ryzendee.app.dto.SignInResponse;
import ryzendee.app.dto.SignUpRequest;
import ryzendee.app.dto.SignUpResponse;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.exception.UserExistsException;
import ryzendee.app.model.UserToRole;
import ryzendee.app.repository.UserToRoleRepository;
import ryzendee.app.service.helpers.UserRoleCreator;
import ryzendee.starter.jwt.decoder.AuthRole;
import ryzendee.app.mapper.UserAppMapper;
import ryzendee.app.model.User;
import ryzendee.app.repository.UserRepository;
import ryzendee.app.service.AuthService;
import ryzendee.starter.jwt.decoder.JwtDecoder;
import ryzendee.starter.jwt.decoder.JwtPayload;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final AuthRole USER_ROLE = AuthRole.USER;

    private final UserAppMapper userAppMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;
    private final UserRoleCreator userRoleCreator;
    private final UserToRoleRepository userToRoleRepository;

    @Transactional
    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        try {
            User created = createAndSaveUser(request);
            createAndSaveDefaultRoleForUser(created);
            return userAppMapper.toSignUpResponse(created);
        } catch (DataIntegrityViolationException ex) {
            throw new UserExistsException("User with this credentials already exists");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public SignInResponse signIn(SignInRequest request) {
        User user = userRepository.findByLogin(request.login())
                .orElseThrow(() -> new ResourceNotFoundException("User with this login does not exists"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid password");
        }

        JwtPayload payload = userAppMapper.toJwtCredentials(user);
        return SignInResponse.builder()
                .token(jwtDecoder.createJwt(payload))
                .build();
    }

    private User createAndSaveUser(SignUpRequest request) {
        User user = userAppMapper.toModel(request);
        user.setPasswordHash(encode(request.password()));
        return userRepository.saveAndFlush(user);
    }

    private void createAndSaveDefaultRoleForUser(User created) {
        UserToRole userToRole = userRoleCreator.createDefaultRoleWithoutUser();
        userToRole.assignUser(created);
        userToRoleRepository.save(userToRole);
    }

    private String encode(String password) {
        return passwordEncoder.encode(password);
    }
}
