package com.michaelfotiads.demomodules.net;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class PostsDataSource {

    private final PostsApi api;

    public PostsDataSource(final Retrofit retrofit) {
        this.api = retrofit.create(PostsApi.class);
    }


    public Call<List<Post>> getPosts() {
        return api.getPosts();
    }

    public Single<List<Post>> observePosts() {
        return api.observePosts();
    }

    private interface PostsApi {

        @GET("posts/")
        Call<List<Post>> getPosts();

        @GET("posts/")
        Single<List<Post>> observePosts();


    }

}
