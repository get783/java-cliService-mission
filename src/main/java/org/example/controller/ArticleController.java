package org.example.controller;

import org.example.AppContext;
import org.example.entity.Article;
import org.example.service.ArticleService;

import java.util.List;
import java.util.Optional;

public class ArticleController {
    private final ArticleService service;

    public ArticleController() {
        this.service = AppContext.articleService;
    }

    public Optional<Article> getArticle(int id) throws Exception {
        return service.getArticle(id);
    }

    public List<Article> getArticleList(String order, String keyword, int page, int pageSize) throws Exception {
        List<Article> list = service.getArticleList(order, keyword, page, pageSize);
        return list;
    }

    public int createArticle(String title, String content) throws Exception {
        return service.createArticle(title, content);
    }

    public int updateArticle(int id, String title, String content) throws Exception {
        return service.updateArticle(id, title, content);
    }

    public void updateArticleCount(Article article) throws Exception {
        service.updateArticleCount(article);
    }

    public int deleteArticle(int id) throws Exception {
        return service.deleteArticle(id);
    }
}
