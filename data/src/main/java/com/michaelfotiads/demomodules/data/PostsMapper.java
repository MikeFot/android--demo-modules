package com.michaelfotiads.demomodules.data;

import java.util.ArrayList;
import java.util.List;

public class PostsMapper {

    public static List<Post> map(final List<com.michaelfotiads.demomodules.net.Post> netPosts) {

        final List<Post> posts = new ArrayList<>();

        for (com.michaelfotiads.demomodules.net.Post netPost : netPosts) {
            posts.add(map(netPost));
        }

        return posts;

    }

    public static Post map(final com.michaelfotiads.demomodules.net.Post netPost) {
        return new Post(netPost.userId, netPost.id, netPost.title, netPost.body);
    }

}
