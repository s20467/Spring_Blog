package com.springproject.blog.repositories;

import com.springproject.blog.models.Article;
import com.springproject.blog.models.security.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends CrudRepository<Article, Integer> {
    Iterable<Article> getArticlesByAuthor(User user);

    Iterable<Article> getArticlesByTitle(String title);

    @Query("from Article a where lower(a.title) like :titleLike")
    Iterable<Article> findArticlesByTitleLikeIgnoreCase(@Param("titleLike") String titleLike);

    @Query("select a from Article a left join a.author author where lower(author.username) like :authorLike")
    Iterable<Article> findArticlesByAuthorLikeIgnoreCase(@Param("authorLike") String authorLike);
}
