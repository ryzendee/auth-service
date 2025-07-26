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
import ryzendee.starter.jwt.decoder.AuthRole;
import ryzendee.app.mapper.UserAppMapper;
import ryzendee.app.model.Role;
import ryzendee.app.model.User;
import ryzendee.app.model.UserToRole;
import ryzendee.app.model.UserToRoleId;
import ryzendee.app.repository.UserRepository;
import ryzendee.app.repository.UserRoleRepository;
import ryzendee.app.repository.UserToRoleRepository;
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
    private final UserRoleRepository userRoleRepository;
    private final UserToRoleRepository userToRoleRepository;

    @Transactional
    @Override
    public SignUpResponse signUp(SignUpRequest request) {
        try {
            User created = saveUser(request);
            saveDefaultRoleForUser(created);
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

    private User saveUser(SignUpRequest request) {
        User created = userAppMapper.toModel(request);
        created.setPasswordHash(passwordEncoder.encode(request.password()));
        return userRepository.saveAndFlush(created);
    }

    private void saveDefaultRoleForUser(User user) {
        Role defaultRole = userRoleRepository.findById(USER_ROLE)
                .orElseThrow(() -> new ResourceNotFoundException(""));
        UserToRole userToRole = buildUserToRole(user, defaultRole);
        userToRoleRepository.save(userToRole);
    }

    private UserToRole buildUserToRole(User user, Role role) {
        UserToRoleId id = UserToRoleId.builder()
                .userId(user.getId())
                .roleId(role.getId())
                .build();

        return UserToRole.builder()
                .id(id)
                .user(user)
                .role(role)
                .build();
    }
}
