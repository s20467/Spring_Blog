package com.springproject.blog.services;

import com.springproject.blog.models.Article;
import com.springproject.blog.models.dto.ArticleDto;
import com.springproject.blog.models.security.User;
import com.springproject.blog.repositories.ArticleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class ArticleServiceImplTest {

    @Mock
    ArticleRepository articleRepository;


    ArticleServiceImpl articleService;

    AutoCloseable autoCloseable;
    SecurityContextImpl securityContext;

    Article article1;
    ArticleDto article1Dto;
    Article article2;
    ArticleDto article2Dto;
    Iterable<Article> articles;
    Set<ArticleDto> articleDtos;
    User user;

    public ArticleServiceImplTest() {
        user = User.builder().id(1).username("user").build();
        article1 = Article.builder().id(1).title("title1").content("content1").author(user).build();
        article1Dto = new ArticleDto(article1);
        article2 = Article.builder().id(2).title("title2").content("content2").author(user).build();
        article2Dto = new ArticleDto(article2);
        articles = Set.of(article1, article2);
        articleDtos = Set.of(article1Dto, article2Dto);

        securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return user;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean b) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return null;
            }
        });
    }

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        articleService = new ArticleServiceImpl(articleRepository);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void findAll() {
        when(articleRepository.findAll()).thenReturn(articles);

        Set<ArticleDto> resultSet = articleService.findAll();

        assertEquals(articleDtos.size(), resultSet.size());
        for(ArticleDto article : resultSet){
            assertTrue(articleDtos.contains(article));
        }
    }

    @Test
    void findById() {
        int id = article1.getId();

        when(articleRepository.findById(id)).thenReturn(Optional.of(article1));

        ArticleDto result = articleService.findById(id);

        assertEquals(article1Dto, result);
    }

    @Test
    void save() {
        when(articleRepository.save(any())).thenReturn(article1);


        ArticleDto result = articleService.save(article1Dto);

        assertEquals(article1Dto, result);
    }

    @Test
    void update() {
        int id1 = article1.getId();
        int id2 = article2.getId();
        when(articleRepository.findById(id1)).thenReturn(Optional.empty());
        when(articleRepository.findById(id2)).thenReturn(Optional.of(article2));
        when(articleRepository.save(any())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> articleService.update(article1Dto));
        articleService.update(article2Dto);
    }

    @Test
    void delete() {
        int id1 = article1.getId();
        int id2 = article2.getId();
        when(articleRepository.existsById(id1)).thenReturn(false);
        when(articleRepository.existsById(id2)).thenReturn(true);
        doNothing().when(articleRepository).deleteById(any());

        assertThrows(ResponseStatusException.class, () -> articleService.delete(article1Dto));
        articleService.delete(article2Dto);
    }

    @Disabled
    @Test
    void findByTitle() {

    }

    @Disabled
    @Test
    void findByAuthor() {

    }

    @Test
    void deleteById() {
        int id1 = article1.getId();
        int id2 = article2.getId();
        when(articleRepository.existsById(id1)).thenReturn(false);
        when(articleRepository.existsById(id2)).thenReturn(true);
        doNothing().when(articleRepository).deleteById(any());

        assertThrows(ResponseStatusException.class, () -> articleService.deleteById(id1));
        articleService.deleteById(id2);
    }
}