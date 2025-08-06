package org.example;

import org.example.controller.ArticleController;
import org.example.controller.SystemController;
import org.example.repository.ArticleRepository;
import org.example.service.ArticleService;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AppContext {
    public static final Scanner scanner;
    public static final DateTimeFormatter forPrintDateTimeFormatter;
    public static final ArticleRepository articleRepository;
    public static final ArticleService articleService;
    public static final ArticleController articleController;
    public static final SystemController systemController;

    static {
        scanner = new Scanner(System.in);
        forPrintDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        articleRepository = new ArticleRepository();
        articleService = new ArticleService();
        articleController = new ArticleController();
        systemController = new SystemController();
    }
}
