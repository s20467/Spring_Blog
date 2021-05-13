package com.springproject.blog.controllers;

import com.springproject.blog.models.Article;
import com.springproject.blog.models.dto.ArticleDto;
import com.springproject.blog.services.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Controller
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping({"", "index"})
    public String getHomePage(){
        return "index";
    }

    @GetMapping("/create")
    public String articleCreate(Model model){
        model.addAttribute("article", new ArticleDto());
        return "article_add";
    }

    @PostMapping("/createProcess")
    public String processArticleCreate(Model model, ArticleDto article){
        articleService.save(article);
        model.addAttribute("article", article);
        return "article_view";
    }

    @GetMapping("/find")
    public String articleFind(Model model){
        model.addAttribute("article", new ArticleDto());
        return "article_find";
    }

    @PostMapping("/findProcess")
    public String processArticleFind(Model model, ArticleDto article){
        Set<ArticleDto> articles = articleService.findByTitle(article.getTitle());
        model.addAttribute("articles", articles);
        return "articles_found_view";
    }

    @GetMapping("/findByAuthor")
    public String articleFindByAuthor(Model model){
        model.addAttribute("article", new ArticleDto());
        return "article_find_by_author";
    }

    @PostMapping("/findByAuthorProcess")
    public String processArticleFindByAuthor(Model model, ArticleDto article){
        Set<ArticleDto> articles = articleService.findByAuthor(article.getAuthor());
        model.addAttribute("articles", articles);
        return "articles_found_view";
    }

    @GetMapping("/show/{id}")
    public String articleShow(@PathVariable int id, Model model){
        model.addAttribute("article", articleService.findById(id));
        return "article_view";
    }
}
