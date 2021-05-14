package com.springproject.blog.bootstrap;

import com.springproject.blog.models.Article;
import com.springproject.blog.models.security.Authority;
import com.springproject.blog.models.security.Role;
import com.springproject.blog.models.security.User;
import com.springproject.blog.repositories.ArticleRepository;
import com.springproject.blog.repositories.security.AuthorityRepository;
import com.springproject.blog.repositories.security.RoleRepository;
import com.springproject.blog.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Component
public class bootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final ArticleRepository articleRepository;

    @Override
    public void run(String... args) throws Exception {
        loadSecurityData();
    }


    private void loadSecurityData() {
        log.debug("bootstrap data");

        Authority getSecuredPageAuthority = authorityRepository.save(Authority.builder().permission("secured").build());

        Role userRole = roleRepository.save(Role.builder().authority(getSecuredPageAuthority).name("ROLE_USER").build());

        User user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("pass"))
                .role(userRole)
                .build();

        userRepository.save(user);

        Article article1 = Article.builder()
                .author(user)
                .title("TiTlE1")
                .content("1Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .lastModified(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        Article article2 = Article.builder()
                .author(user)
                .title("tItLe2")
                .content("2Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .lastModified(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        Article article3 = Article.builder()
                .author(user)
                .title("tYtUl3")
                .content("3Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .lastModified(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        Article article4 = Article.builder()
                .author(user)
                .title("TyTuL4")
                .content("4Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .lastModified(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        articleRepository.saveAll(Set.of(article1, article2, article3, article4));
    }
}
