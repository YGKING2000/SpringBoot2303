package com.example.springboot2303.entity;

import java.io.Serializable;

/**
 * @Description
 * @ClassName Article
 * @Author YGKING e-mail:hrd18960706057@163.com
 * @Date 2023/04/15 12:57
 * @Version 1.0
 */
public class Article implements Serializable {
    private String title;
    private String publisher;
    private String content;

    public Article() {
    }

    public Article(String title, String publisher, String content) {
        this.title = title;
        this.publisher = publisher;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
