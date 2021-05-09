package com.springproject.blog.repositories.security;

import com.springproject.blog.models.security.Authority;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<Authority, Integer> {
}
