package com.springproject.blog.repositories.security;

import com.springproject.blog.models.security.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
}
