package com.springproject.blog.security;

import com.springproject.blog.models.security.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ArticleAuthenticationManager {

    public boolean userMatches(Authentication authentication, String username){
        User authenticatedUser = (User) authentication.getPrincipal();
        return username.equals(authenticatedUser.getUsername());
    }
}
