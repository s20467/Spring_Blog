package com.springproject.blog.controllers;

import com.springproject.blog.models.Article;
import com.springproject.blog.models.security.User;
import com.springproject.blog.repositories.ArticleRepository;
import com.springproject.blog.repositories.security.RoleRepository;
import com.springproject.blog.repositories.security.UserRepository;
import com.springproject.blog.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor
class ArticleControllerIT {


    private final WebApplicationContext webApplicationContext;

    MockMvc mockMvc;


    String user1Password;
    String user1Username;
    User user1Test;
    String user2Password;
    String user2Username;
    User user2Test;
    String adminPassword;
    String adminUsername;
    User adminTest;
    Article user1Article;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        ArticleRepository articleRepository = webApplicationContext.getBean(ArticleRepository.class);
        RoleRepository roleRepository = webApplicationContext.getBean(RoleRepository.class);
        PasswordEncoder passwordEncoder = webApplicationContext.getBean(PasswordEncoder.class);
        UserRepository userRepository = webApplicationContext.getBean(UserRepository.class);
        user1Password = "user1Password";
        user1Username = "user1Username";
        user1Test = User.builder()
                .username(user1Username)
                .password(passwordEncoder.encode(user1Password))
                .role(roleRepository.findByName("ROLE_USER").get())
                .build();
        userRepository.save(user1Test);
        user2Password = "user2Password";
        user2Username = "user2Username";
        user2Test = User.builder()
                .username(user2Username)
                .password(passwordEncoder.encode(user2Password))
                .role(roleRepository.findByName("ROLE_USER").get())
                .build();
        userRepository.save(user2Test);
        adminPassword = "adminPassword";
        adminUsername = "adminUsername";
        adminTest = User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .role(roleRepository.findByName("ROLE_ADMIN").get())
                .build();
        userRepository.save(adminTest);
        user1Article = Article.builder().title("title").content("content1").author(user1Test).build();
        articleRepository.save(user1Article);

    }

    @AfterEach
    void tearDown() {
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
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/article/create").with(httpBasic(user1Username, user1Password)))
                .andExpect(status().isOk())
                .andExpect(view().name("article_add"))
                .andExpect(model().attributeExists("article"));

        mockMvc.perform(get("/article/create").with(httpBasic(adminUsername, adminPassword)))
                .andExpect(status().isOk())
                .andExpect(view().name("article_add"))
                .andExpect(model().attributeExists("article"));
    }



    @Test
    void articleFind() throws Exception{
        mockMvc.perform(get("/article/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("article_find"));
    }

    @Test
    void processArticleFind() throws Exception{
        mockMvc.perform(post("/article/findProcess")
                    .with(csrf())
                    .param("articleTitle", user1Article.getTitle()))
                .andExpect(status().isOk())
                .andExpect(view().name("articles_found_view"))
                .andExpect(model().attributeExists("articles"));
    }

    @Test
    void articleFindByAuthor() throws Exception{
        mockMvc.perform(get("/article/findByAuthor"))
                .andExpect(status().isOk())
                .andExpect(view().name("article_find_by_author"));
    }

    @Test
    void processArticleFindByAuthor() throws Exception{
        mockMvc.perform(post("/article/findByAuthorProcess")
                    .with(csrf())
                    .param("articleAuthor", user1Username))
                .andExpect(status().isOk())
                .andExpect(view().name("articles_found_view"))
                .andExpect(model().attributeExists("articles"));
    }

    @Test
    void articleShow() throws Exception{
        ArticleRepository articleRepository = webApplicationContext.getBean(ArticleRepository.class);
        Article article1 = StreamSupport.stream(articleRepository.findAll().spliterator(), false).findFirst().get();

        mockMvc.perform(get("/article/show/" + article1.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("article_view"))
                .andExpect(model().attributeExists("article"));
    }


    @Test
    void myArticlesShow() throws Exception{
        mockMvc.perform(get("/article/my"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/article/my").with(httpBasic(user1Username, user1Password)))
                .andExpect(status().isOk())
                .andExpect(view().name("articles_found_view"))
                .andExpect(model().attributeExists("articles"));

        mockMvc.perform(get("/article/my").with(httpBasic(adminUsername, adminPassword)))
                .andExpect(status().isOk())
                .andExpect(view().name("articles_found_view"))
                .andExpect(model().attributeExists("articles"));
    }

    @Test
    void articleEdit() throws Exception{
        mockMvc.perform(get("/article/edit/" + user1Article.getId()))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/article/edit/" + user1Article.getId()).with(httpBasic(user1Username, user1Password)))
                .andExpect(status().isOk())
                .andExpect(view().name("article_edit"))
                .andExpect(model().attributeExists("article"));

        mockMvc.perform(get("/article/edit/" + user1Article.getId()).with(httpBasic(user2Username, user2Password)))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/article/edit/" + user1Article.getId()).with(httpBasic(adminUsername, adminPassword)))
                .andExpect(status().isOk())
                .andExpect(view().name("article_edit"))
                .andExpect(model().attributeExists("article"));
    }

    @Test
    void processArticleEdit() {

    }

    @Test
    void articleDeleteUnauthorized() throws Exception{
        mockMvc.perform(get("/article/delete/" + user1Article.getId()))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void articleDeleteOwnerUser() throws Exception{
        mockMvc.perform(get("/article/delete/" + user1Article.getId()).with(httpBasic(user1Username, user1Password)))
            .andExpect(status().is3xxRedirection());
    }
    @Test
    void articleDeleteNotOwnerUser() throws Exception{
        mockMvc.perform(get("/article/delete/" + user1Article.getId()).with(httpBasic(user2Username, user2Password)))
                .andExpect(status().isForbidden());
    }
    @Test
    void articleDeleteAdmin() throws Exception{
        mockMvc.perform(get("/article/delete/" + user1Article.getId()).with(httpBasic(adminUsername, adminPassword)))
                .andExpect(status().is3xxRedirection());
    }
}