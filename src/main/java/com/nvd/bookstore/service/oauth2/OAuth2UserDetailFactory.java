package com.nvd.bookstore.service.oauth2;

import com.nvd.bookstore.entity.Provider;
import com.nvd.bookstore.exception.BaseException;

import java.util.Map;

public class OAuth2UserDetailFactory {

    public static OAuth2UserDetails getOauth2UserDetail(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equals(Provider.google.name()))
            return new OAuth2GoogleUser(attributes);
        else if (registrationId.equals(Provider.facebook.name()))
            return new OAuth2FacebookUser(attributes);
        else if (registrationId.equals(Provider.github.name()))
            return new OAuth2GithubUser(attributes);
        else throw new BaseException("400", "Sorry! Login with " + registrationId + " is not supported!");
    }
}
