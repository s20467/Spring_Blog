package com.springproject.blog.services;


import com.springproject.blog.models.Article;
import com.springproject.blog.models.dto.ArticleDto;
import com.springproject.blog.models.security.User;
import com.springproject.blog.repositories.ArticleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ArticleServiceImpl implements ArticleService{

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Set<ArticleDto> findAll() {
        return StreamSupport.stream(articleRepository.findAll().spliterator(), false)
                .map(ArticleDto::new)
                .collect(Collectors.toSet());
    }

    @Override
    public ArticleDto findById(int id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article id: " + id + " not found"));
        return new ArticleDto(article);
    }

    @Override
    public ArticleDto save(ArticleDto article) {
        Article persistedArticle = Article.builder()
                .author((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .title(article.getTitle())
                .content(article.getContent())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .lastModified(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        return new ArticleDto(articleRepository.save(persistedArticle));
    }

    @Override
    public void update(ArticleDto article) {
        Article persistedArticle = articleRepository.findById(article.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article id: " + article.getId() + " not found"));
        persistedArticle.setTitle(article.getTitle());
        persistedArticle.setContent(article.getContent());
        persistedArticle.setLastModified(Timestamp.valueOf(LocalDateTime.now()));
        articleRepository.save(persistedArticle);
    }

    @Override
    public void delete(ArticleDto article) {
        if(!articleRepository.existsById(article.getId()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article id: " + article.getId() + " not found");

        articleRepository.deleteById(article.getId());
    }

    @Override
    public Set<ArticleDto> findByTitle(String title) {
        String titleLike = "%" + title.toLowerCase() + "%";
        Iterable<Article> articles = articleRepository.findArticlesByTitleLikeIgnoreCase(titleLike);
        return StreamSupport.stream(articles.spliterator(), false)
                .map(ArticleDto::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ArticleDto> findByAuthor(String author) {
        String authorLike = "%" + author.toLowerCase() + "%";
        Iterable<Article> articles = articleRepository.findArticlesByAuthorLikeIgnoreCase(authorLike);
        return StreamSupport.stream(articles.spliterator(), false)
                .map(ArticleDto::new)
                .collect(Collectors.toSet());
    }
}
