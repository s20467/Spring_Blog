package com.springproject.blog.bootstrap;

import com.springproject.blog.models.security.Authority;
import com.springproject.blog.models.security.Role;
import com.springproject.blog.models.security.User;
import com.springproject.blog.repositories.security.AuthorityRepository;
import com.springproject.blog.repositories.security.RoleRepository;
import com.springproject.blog.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class bootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

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
    }
}
