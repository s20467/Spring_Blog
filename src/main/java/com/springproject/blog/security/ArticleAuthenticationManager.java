package com.springproject.blog.security;

import com.springproject.blog.models.security.User;
import com.springproject.blog.repositories.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleAuthenticationManager {

    private final ArticleRepository articleRepository;

    public boolean userMatches(Authentication authentication, String username) {
        User authenticatedUser = (User) authentication.getPrincipal();
        return username.equals(authenticatedUser.getUsername());
    }

    public boolean userMatchesBasedOnArticleId(Authentication authentication, int id) {
        User authenticatedUser = (User) authentication.getPrincipal();
        String username = articleRepository.findById(id).get().getAuthor().getUsername();
        return username.equals(authenticatedUser.getUsername());
    }
}
