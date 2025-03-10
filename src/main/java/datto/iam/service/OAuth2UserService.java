package datto.iam.service;

import datto.iam.dto.auth.UserPrincipal;
import datto.iam.repos.UserRepository;
import datto.iam.dto.auth.OAuth2UserInfoDto;
import datto.iam.entities.user.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    @Override
    @SneakyThrows
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        log.trace("Load user {}", oAuth2UserRequest);
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfoDto userInfoDto = OAuth2UserInfoDto
                .builder()
                .name(oAuth2User.getAttributes().get("name").toString())
                .id(oAuth2User.getAttributes().get("sub").toString())
                .email(oAuth2User.getAttributes().get("email").toString())
                .picture(oAuth2User.getAttributes().get("picture").toString())
                .build();

        Optional<User> userOptional = userRepository.findByUsername(userInfoDto.getEmail());
        User user = userOptional
                .map(existingUser -> updateExistingUser(existingUser, userInfoDto))
                .orElseGet(() -> registerNewUser(oAuth2UserRequest, userInfoDto));
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    /**
     * Register new user from OAuth2 user data
     * @param oAuth2UserRequest oauth2 request
     * @param userInfoDto user info extracted from authenticated oauth2 user
     * @return saved user in database
     */
    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfoDto userInfoDto) {
        log.trace("Register new OAuth2 user, email: {}", userInfoDto.getEmail());
        User user = User.builder()
                .provider(oAuth2UserRequest.getClientRegistration().getRegistrationId())
                .providerId(userInfoDto.getId())
                .name(userInfoDto.getName())
                .username(userInfoDto.getEmail())
                .email(userInfoDto.getEmail())
                .picture(userInfoDto.getPicture())
                .build();

//        user.setId(UUID.randomUUID());
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfoDto userInfoDto) {
        log.trace("User with email {} existed. Update with new name and picture", existingUser.getEmail());
        existingUser.setName(userInfoDto.getName());
        existingUser.setPicture(userInfoDto.getPicture());
        return userRepository.save(existingUser);
    }
}