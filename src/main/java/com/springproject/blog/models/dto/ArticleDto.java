package com.springproject.blog.models.dto;

import com.springproject.blog.models.Article;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDto {
    private Integer id;
    private String title;
    private String content;
    private String author;

    public ArticleDto(Article article) {
        id = article.getId();
        title = article.getTitle();
        content = article.getContent();
        author = article.getAuthor().getUsername();
    }
}
