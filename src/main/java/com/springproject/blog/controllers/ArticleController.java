package com.springproject.blog.controllers;

import com.springproject.blog.models.dto.ArticleDto;
import com.springproject.blog.models.security.User;
import com.springproject.blog.services.ArticleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("")
    public String getHomePage(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "articles_found_view";
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("/create")
    public String articleCreate(Model model) {
        model.addAttribute("article", new ArticleDto());
        return "article_add";
    }

    @PreAuthorize("isFullyAuthenticated()")
    @PostMapping("/createProcess")
    public String processArticleCreate(Model model, ArticleDto article) {
        article = articleService.save(article);
        model.addAttribute("article", article);
        return "article_view";
    }

    @GetMapping("/find")
    public String articleFind(Model model) {
        model.addAttribute("article", new ArticleDto());
        return "article_find";
    }

    @PostMapping("/findProcess")
    public String processArticleFind(Model model, ArticleDto article) {
        Set<ArticleDto> articles = articleService.findByTitle(article.getTitle());
        model.addAttribute("articles", articles);
        return "articles_found_view";
    }

    @GetMapping("/findByAuthor")
    public String articleFindByAuthor(Model model) {
        model.addAttribute("article", new ArticleDto());
        return "article_find_by_author";
    }

    @PostMapping("/findByAuthorProcess")
    public String processArticleFindByAuthor(Model model, ArticleDto article) {
        Set<ArticleDto> articles = articleService.findByAuthor(article.getAuthor());
        model.addAttribute("articles", articles);
        return "articles_found_view";
    }

    @GetMapping("/show/{id}")
    public String articleShow(@PathVariable int id, Model model) {
        model.addAttribute("article", articleService.findById(id));
        return "article_view";
    }

    @PreAuthorize("isFullyAuthenticated()")
    @GetMapping("/my")
    public String myArticlesShow(Model model) {
        String currentUsername = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Set<ArticleDto> articles = articleService.findByAuthor(currentUsername);
        model.addAttribute("articles", articles);
        return "articles_found_view";
    }

    @PreAuthorize("hasAuthority('article.update') or " +
            "hasAuthority('user.article.update') and @articleAuthenticationManager.userMatchesBasedOnArticleId(authentication, #id)")
    @GetMapping("/edit/{id}")
    public String articleEdit(@PathVariable int id, Model model) {
        model.addAttribute("article", articleService.findById(id));
        return "article_edit";
    }

    @PreAuthorize("hasAuthority('article.update') or " +
            "hasAuthority('user.article.update') and @articleAuthenticationManager.userMatches(authentication, #article.author)")
    @PostMapping("/editProcess")
    public String processArticleEdit(Model model, ArticleDto article) {
        articleService.update(article);
        model.addAttribute("article", article);
        return "article_view";
    }

    @PreAuthorize("hasAuthority('article.delete') or " +
            "hasAuthority('user.article.delete') and @articleAuthenticationManager.userMatchesBasedOnArticleId(authentication, #id)")
    @GetMapping("/delete/{id}")
    public String articleDelete(@PathVariable int id) {
        articleService.deleteById(id);
        return "redirect:/article";
    }
}
