package org.example;

import org.example.controller.ArticleController;
import org.example.controller.SystemController;
import org.example.repository.ArticleRepository;
import org.example.service.ArticleService;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AppContext {
    public static Scanner scanner;
    public static DateTimeFormatter forPrintDateTimeFormatter;
    public static ArticleRepository articleRepository;
    public static ArticleService articleService;
    public static ArticleController articleController;
    public static SystemController systemController;

    public static void renew(Scanner _scanner) {
        scanner = _scanner;
        forPrintDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        articleRepository = new ArticleRepository();
        articleService = new ArticleService();
        articleController = new ArticleController();
        systemController = new SystemController();
    }

    public static void renew() {
        renew(new Scanner(System.in));
    }
}
