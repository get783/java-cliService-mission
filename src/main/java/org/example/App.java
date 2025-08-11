package org.example;

import org.example.controller.ArticleController;
import org.example.controller.SystemController;
import org.example.entity.Article;

import java.util.List;
import java.util.Optional;

public class App {
    void run() {
        SystemController systemController = AppContext.systemController;
        ArticleController articleController = AppContext.articleController;

        System.out.println("== 게시판 ==");
        while (true) {
            System.out.print("\n명령어: ");

            String command = systemController.scan();
            Request rq = new Request(command);

            try {
                switch (rq.getActionName()) {
                    case "detail" -> {
                        int id = rq.getParamAsInt("id", -1);
                        if (id == -1) {
                            System.out.println("조회할 게시글 번호를 입력해 주세요. (예시: detail?id=1)");
                            break;
                        }

                        Optional<Article> optionalArticle = articleController.getArticle(id);
                        if (optionalArticle.isEmpty()) {
                            System.out.println(id + "번 게시글은 존재하지 않습니다.");
                            break;
                        }
                        Article article = optionalArticle.get();
                        articleController.updateArticleCount(article);

                        System.out.println("번호: " + article.getId());
                        System.out.println("제목: " + article.getTitle());
                        System.out.println("내용: " + article.getContent());
                        System.out.println("조회수: " + article.getCount());
                        System.out.println("등록일: " + article.getRegDate());
                    }
                    case "list" -> {
                        String order = rq.getParam("order", "N"); // 최신순: N, 등록일순: R, 조회수 많은 순: C
                        String keyword = rq.getParam("keyword", "");
                        int page = rq.getParamAsInt("page", 1);
                        int pageSize = 5;

                        List<Article> articleList = articleController.getArticleList(order, keyword, page, pageSize);
                        if (articleList.size() == 0) {
                            System.out.println("검색 결과가 없습니다.");
                            break;
                        }
                        int totalCount = 0; // TODO 총 검색 건수 담아서 리턴하기
                        System.out.printf("총 %d건의 검색 결과가 있습니다.\n\n", totalCount); // TODO
                        System.out.println(" %-2s | %-18s | %-3s | %-8s ".formatted("번호", "제목", "조회수", "등록일"));
                        System.out.println("---------------------------------------------------");
                        articleList.forEach(article -> {
                            System.out.println(" %-4d | %-20s | %-6d | %-10s ".formatted(
                                    article.getId(),
                                    article.getTitle(),
                                    article.getCount(),
                                    article.getRegDate())
                            );
                        });
                        System.out.println("\n[%d / %d]".formatted(page, (totalCount - 1) / pageSize + 1)); // TODO
                    }
                    case "write" -> {
                        System.out.print("제목: ");
                        String title = systemController.scan();

                        System.out.print("내용: ");
                        String content = systemController.scan();

                        int createdId = articleController.createArticle(title, content);
                        System.out.println("⭕ %d번 게시글이 등록되었습니다.".formatted(createdId));
                    }
                    case "update" -> {
                        int id = rq.getParamAsInt("id", -1);
                        if (id == -1) {
                            System.out.println("수정할 게시글 번호를 입력해 주세요. (예시: update?id=1)");
                            break;
                        }

                        Optional<Article> optionalArticle = articleController.getArticle(id);
                        if (optionalArticle.isEmpty()) {
                            System.out.println("❌ %d번 게시글은 존재하지 않습니다.".formatted(id));
                            break;
                        }
                        Article article = optionalArticle.get();

                        System.out.print("제목 (현재: %s): ".formatted(article.getTitle()));
                        String title = systemController.scan();
                        System.out.print("내용 (현재: %s): ".formatted(article.getContent()));
                        String content = systemController.scan();

                        id = articleController.updateArticle(id, title, content);
                        System.out.println("⭕ %d번 게시글이 수정되었습니다.".formatted(id));
                    }
                    case "delete" -> {
                        int id = rq.getParamAsInt("id", -1);
                        if (id == -1) {
                            System.out.println("삭제할 게시글 번호를 입력해 주세요. (예시: delete?id=1)");
                            break;
                        }

                        int deletedId = articleController.deleteArticle(id);
                        if (deletedId == -1) System.out.println("❌ %d번 게시글은 존재하지 않습니다.".formatted(id));
                        else System.out.println("⭕ %d번 게시글이 삭제되었습니다.".formatted(deletedId));
                    }
                    case "exit" -> {
                        System.out.println("프로그램을 종료합니다.");
                        systemController.close();
                        return;
                    }
                    default -> System.out.println("알 수 없는 명령어입니다. 종료하려면 \"exit\"을 입력하세요.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
