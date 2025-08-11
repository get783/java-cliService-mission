package org.example.service;

import org.example.AppContext;
import org.example.entity.Article;
import org.example.repository.ArticleRepository;

import java.util.List;
import java.util.Optional;

public class ArticleService {
    private final ArticleRepository repository;

    public ArticleService() {
        repository = AppContext.articleRepository;
    }

    public Optional<Article> getArticle(int id) throws Exception {
        return repository.findById(id);
    }

    public List<Article> getArticleList(String order, String keyword, int page, int pageSize) throws Exception {
        List<Article> articleList = repository.findAll(order, keyword, page, pageSize);

        return articleList;
    }

    public int createArticle(String title, String content) throws Exception {
        int lastId = repository.findLastId();
        int id = repository.save(new Article(lastId + 1, title, content));
        repository.saveLastId();

        return id;
    }

    public int updateArticle(int id, String title, String content) throws Exception {
        if (id < 0 || repository.findById(id) == null) return -1;
        return repository.update(new Article(id, title, content));
    }

    public void updateArticleCount(Article article) throws Exception {
        repository.updateCount(article);
    }

    public int deleteArticle(int id) throws Exception {
        if (id < 0 || repository.findById(id) == null) return -1;
        return repository.deleteById(id);
    }
}
