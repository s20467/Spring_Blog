package com.springproject.blog.services;

import com.springproject.blog.models.dto.ArticleDto;

import java.util.Set;

public interface ArticleService {
    Set<ArticleDto> findAll();

    ArticleDto findById(int id);

    ArticleDto save(ArticleDto article);

    void update(ArticleDto article);

    void delete(ArticleDto Article);

    Set<ArticleDto> findByTitle(String title);

    Set<ArticleDto> findByAuthor(String author);

    void deleteById(int id);
}
