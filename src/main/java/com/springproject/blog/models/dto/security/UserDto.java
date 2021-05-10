package com.springproject.blog.models.dto.security;

import com.springproject.blog.models.security.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    public String username;

    public UserDto(User user){
        username = user.getUsername();
    }
}
