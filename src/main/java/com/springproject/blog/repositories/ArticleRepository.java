package com.springproject.blog.repositories;

import com.springproject.blog.models.Article;
import com.springproject.blog.models.security.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ArticleRepository extends CrudRepository<Article, Integer> {
    Set<Article> getArticlesByAuthor(User user);
    Set<Article> getArticlesByTitle(String title);
}
