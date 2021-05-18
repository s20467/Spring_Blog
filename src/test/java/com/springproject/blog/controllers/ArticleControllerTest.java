package com.springproject.blog.controllers;

import com.springproject.blog.models.dto.ArticleDto;
import com.springproject.blog.models.security.User;
import com.springproject.blog.services.ArticleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ArticleControllerTest {

    @Mock
    ArticleService articleService;

    ArticleController articleController;
    AutoCloseable autoCloseable;
    SecurityContextImpl securityContext;
    MockMvc mockMvc;

    User user;
    ArticleDto article1;
    ArticleDto article2;
    Set<ArticleDto> articles;

    public ArticleControllerTest() {
        user = User.builder().id(1).username("user").build();
        article1 = ArticleDto.builder().id(1).title("title1").content("content1").author("author1").build();
        article2 = ArticleDto.builder().id(2).title("title2").content("content2").author("author2").build();
        articles = new HashSet<>(Set.of(article1, article2));

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
    void setUp(){
        autoCloseable = MockitoAnnotations.openMocks(this);
        articleController = new ArticleController(articleService);
        mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void getHomePage() throws Exception{
        mockMvc.perform(get("/article/"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles_found_view"))
                .andExpect(model().attributeExists("articles"));
    }

    @Test
    void articleCreate() throws Exception{
        mockMvc.perform(get("/article/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("article_add"))
                .andExpect(model().attributeExists("article"));
    }

    @Test
    void processArticleCreate() throws Exception{
        when(articleService.save(any())).thenReturn(article1);

        mockMvc.perform(post("/article/createProcess"))
                .andExpect(status().isOk())
                .andExpect(view().name("article_view"))
                .andExpect(model().attribute("article", article1));
    }

    @Test
    void articleFind() throws Exception{
        mockMvc.perform(get("/article/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("article_find"));
    }

    @Test
    void processArticleFind() throws Exception{
        when(articleService.findByTitle(any())).thenReturn(articles);

        mockMvc.perform(post("/article/findProcess"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles_found_view"))
                .andExpect(model().attribute("articles", articles));
    }

    @Test
    void articleFindByAuthor() throws Exception{
        mockMvc.perform(get("/article/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("article_find"));
    }

    @Test
    void processArticleFindByAuthor() throws Exception{
        when(articleService.findByAuthor(any())).thenReturn(articles);

        mockMvc.perform(post("/article/findByAuthorProcess"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles_found_view"))
                .andExpect(model().attribute("articles", articles));
    }

    @Test
    void articleShow() throws Exception{
        int id = article1.getId();
        when(articleService.findById(article1.getId())).thenReturn(article1);
        mockMvc.perform(get("/article/show/" + id))
                .andExpect(status().isOk())
                .andExpect(view().name("article_view"))
                .andExpect(model().attribute("article", article1));
    }

    @Test
    void myArticlesShow() throws Exception{
        when(articleService.findByAuthor(any())).thenReturn(articles);

        mockMvc.perform(get("/article/my"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles_found_view"))
                .andExpect(model().attribute("articles", articles));
    }

    @Test
    void articleEdit() throws Exception{
        int id = article1.getId();
        when(articleService.findById(id)).thenReturn(article1);

        mockMvc.perform(get("/article/edit/" + id))
                .andExpect(status().isOk())
                .andExpect(view().name("article_edit"))
                .andExpect(model().attribute("article", article1));
    }

    @Test
    void processArticleEdit() throws Exception{
        doNothing().when(articleService).update(any());

        mockMvc.perform(post("/article/editProcess"))
                .andExpect(status().isOk())
                .andExpect(view().name("article_view"))
                .andExpect(model().attributeExists("article"));
    }

    @Test
    void articleDelete() throws Exception{
        int id = article1.getId();
        doNothing().when(articleService).deleteById(id);

        mockMvc.perform(get("/article/delete/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article"));
    }
}