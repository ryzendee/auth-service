package ryzendee.app.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryzendee.app.mapper.OAuth2UserAppMapper;
import ryzendee.app.model.User;
import ryzendee.app.model.UserToRole;
import ryzendee.app.repository.UserRepository;
import ryzendee.app.repository.UserToRoleRepository;
import ryzendee.app.service.helpers.OAuth2UserCreator;
import ryzendee.app.service.helpers.OAuth2UserValidator;
import ryzendee.app.service.helpers.UserRoleCreator;


@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final String EMAIL = "email";

    private final UserRepository userRepository;
    private final UserRoleCreator userToRoleCreator;
    private final UserToRoleRepository userToRoleRepository;
    private final OAuth2UserAppMapper oAuth2UserAppMapper;
    private final OAuth2UserValidator oAuth2UserValidator;
    private final OAuth2UserCreator oAuth2UserCreator;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String email = oAuth2User.getAttribute(EMAIL);
        User user = userRepository.findByEmailWithOAuth2Info(email).orElse(null);

        if (user != null) {
            oAuth2UserValidator.validate(user, userRequest);
        } else {
            user = saveUser(oAuth2User, userRequest);
        }

        return oAuth2UserAppMapper.toOauth2User(user);
    }

    private User saveUser(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
        User created = oAuth2UserCreator.create(oAuth2User, userRequest);
        userRepository.saveAndFlush(created);

        UserToRole userToRole = userToRoleCreator.createDefaultRoleWithoutUser();
        userToRole.assignUser(created);
        userToRoleRepository.save(userToRole);

        return created;
    }
}
