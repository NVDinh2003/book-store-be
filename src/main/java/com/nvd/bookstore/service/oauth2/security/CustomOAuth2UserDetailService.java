package com.nvd.bookstore.service.oauth2.security;

import com.nvd.bookstore.entity.User;
import com.nvd.bookstore.exception.BaseException;
import com.nvd.bookstore.repository.RoleRepository;
import com.nvd.bookstore.repository.UserRepository;
import com.nvd.bookstore.service.oauth2.OAuth2UserDetailFactory;
import com.nvd.bookstore.service.oauth2.OAuth2UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomOAuth2UserDetailService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        try {
            return checkingOAuth2User(userRequest, oauth2User);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User checkingOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserDetails oAuth2UserDetails = OAuth2UserDetailFactory
                .getOauth2UserDetail(oAuth2UserRequest.getClientRegistration().getRegistrationId()
                        , oAuth2User.getAttributes());
        if (ObjectUtils.isEmpty(oAuth2UserDetails))
            throw new BaseException("400", "Can not found OAuth2 user from properties!");
        Optional<User> user = userRepository.findByEmailAndProviderId(
                oAuth2UserDetails.getEmail(), oAuth2UserRequest.getClientRegistration().getRegistrationId());

        User userDetail;
        if (user.isPresent()) {
            userDetail = user.get();

            if (!userDetail.getProviderId().equals(
                    oAuth2UserRequest.getClientRegistration().getRegistrationId()
            )) throw new BaseException("400", "Invalid site login with " + userDetail.getProviderId());

            userDetail = updateOAuth2UserDetail(userDetail, oAuth2UserDetails);

        } else {
            userDetail = registerNewOAuth2UserDetail(oAuth2UserRequest, oAuth2UserDetails);
        }

        return new OAuth2UserDetailCustom(
                userDetail.getId(),
                userDetail.getEmail(),
                userDetail.getPassword(),
                userDetail.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList())
        );
    }

    public User registerNewOAuth2UserDetail(OAuth2UserRequest oAuth2UserRequest, OAuth2UserDetails oAuth2UserDetails) {
        User user = new User();
        user.setEmail(oAuth2UserDetails.getEmail());
        user.setProviderId(oAuth2UserRequest.getClientRegistration().getRegistrationId());
        user.setStatus(true);
        user.setRoles(new ArrayList<>());
        user.getRoles().add(roleRepository.findByName("ROLE_USER"));
        return userRepository.save(user);

    }

    public User updateOAuth2UserDetail(User user, OAuth2UserDetails oAuth2UserDetails) {
        user.setEmail(oAuth2UserDetails.getEmail());
        return userRepository.save(user);
    }
}
