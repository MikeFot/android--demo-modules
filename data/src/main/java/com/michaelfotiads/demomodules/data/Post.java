package com.michaelfotiads.demomodules.data;

public class Post {

    public Long userId;
    public Long id;
    public String title;
    public String body;

    public Post(Long userId, Long id, String title, String body) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }
}