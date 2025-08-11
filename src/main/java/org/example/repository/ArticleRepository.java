package org.example.repository;

import org.example.entity.Article;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArticleRepository {
    private static final String DB_PATH = "db/article";
    private static final String LAST_ID_FILE_NAME = "/lastId.txt";

    private String getFilePath(int id) {
        return DB_PATH + "/" + id + ".json";
    }

    public int findLastId() throws IOException {
        File lastIdFile = new File(DB_PATH + LAST_ID_FILE_NAME);
        if (!lastIdFile.exists()) return -1;

        try (BufferedReader br = new BufferedReader(new FileReader(lastIdFile))) {
            return Integer.parseInt(br.readLine().trim());
        }
    }

    public void saveLastId() throws IOException {
        int lastId = findLastId();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DB_PATH + LAST_ID_FILE_NAME))) {
            bw.write(String.valueOf(++lastId));
        }
    }


    public Optional<Article> findById(int id) throws IOException {
        File f = new File(getFilePath(id));
        if (!f.exists()) return Optional.empty();

        Article article = new Article();
        article.setId(id);

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("\"title\"")) {
                    article.setTitle(line.split(":")[1].replace(",", "").replace("\"", "").trim());
                } else if (line.startsWith("\"content\"")) {
                    article.setContent(line.split(":")[1].replace(",", "").replace("\"", "").trim());
                } else if (line.startsWith("\"count\"")) {
                    article.setCount(Integer.parseInt(line.split(":")[1].replace(",", "").replace("\"", "").trim()));
                } else if (line.startsWith("\"regDate\"")) {
                    article.setRegDate(line.split(":")[1].replace(",", "").replace("\"", "").trim());
                }
            }

            return Optional.of(article);
        }
    }

    public List<Article> findAll(String order, String keyword, int page, int pageSize) throws IOException {
        int lastId = findLastId();
        int startCount = (page - 1) * pageSize + 1;

        if (startCount - 1 > lastId) return new ArrayList<>();

        List<Article> articleList = new ArrayList<>();
        // findAll
        for (int i = lastId; i >= 0; i--) {
            Optional<Article> optionalArticle = findById(i);
            if (optionalArticle.isPresent()) articleList.add(optionalArticle.get());
        }

        int totalCount = articleList.size(); // TODO
        if (startCount > totalCount) return new ArrayList<>();

        // filter
        Stream<Article> articleStream = articleList.stream()
                .filter(article -> article.getTitle().contains(keyword) || article.getContent().contains(keyword));
        // order ("N", "R", "C")
        if (order.equals("R")) {
            articleStream = articleStream.sorted(Comparator.comparing(Article::getId));
        } else if (order.equals("C")) {
            articleStream = articleStream.sorted(Comparator.comparingInt(Article::getCount).reversed());
        }
        // page
        articleList = articleStream
                .skip(startCount - 1)
                .limit(pageSize)
                .collect(Collectors.toList());

        return articleList;
    }

    public int save(Article article) throws IOException {
        File dir = new File(DB_PATH);
        if (!dir.exists()) dir.mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFilePath(article.getId())))) {
            bw.write("{\n");
            bw.write("  \"id\": " + article.getId() + ",\n");
            bw.write("  \"title\": \"" + article.getTitle().replace("\"", "\\\"") + "\"\n");
            bw.write("  \"content\": \"" + article.getContent().replace("\"", "\\\"") + "\",\n");
            bw.write("  \"count\": \"" + article.getCount() + ",\n");
            bw.write("  \"regDate\": \"" + article.getRegDate().replace("\"", "\\\"") + "\",\n");
            bw.write("}");
        }

        return article.getId();
    }

    public int updateCount(Article article) throws IOException {
        article.increaseCount();
        return save(article);
    }

    public int update(Article article) throws IOException {
        return save(article);
    }

    public int deleteById(int id) {
        new File(getFilePath(id)).delete();
        return id;
    }
}
