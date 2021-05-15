package com.springproject.blog.controllers;

import com.springproject.blog.models.security.User;
import com.springproject.blog.repositories.security.RoleRepository;
import com.springproject.blog.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "registration_form";
    }

    @PostMapping("/register")
    public String register(Model model, User user){
        if(userRepository.existsByUsername(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with username: " + user.getUsername() + " already exists");
        User newUser = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(roleRepository.findByName("ROLE_USER").get())
                .build();
        userRepository.save(newUser);
        return "redirect:/login";
    }
}
