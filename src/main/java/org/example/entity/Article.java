package org.example.entity;

import lombok.*;
import org.example.AppContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private int id;
    private String title;
    private String content;
    private int count;
    private String regDate;

    private static DateTimeFormatter forPrintDateTimeFormatter = AppContext.forPrintDateTimeFormatter;

    public Article(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.count = 0;
        this.regDate = LocalDateTime.now().format(forPrintDateTimeFormatter);
    }

    public void increaseCount() {
        this.count++;
    }
}
