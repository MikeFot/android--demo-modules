package com.michaelfotiads.demomodules.data;

import com.michaelfotiads.demomodules.net.PostsDataSource;

import java.io.IOException;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsRepository extends Repository {

    private final PostsDataSource postsDataSource;

    public class Result {

        public List<Post> posts;

        public String errorMessage;

        public Result(List<Post> posts) {
            this.posts = posts;
        }

        public Result(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public boolean hasPosts() {
            return posts != null;
        }

        public boolean hasError() {
            return errorMessage != null;
        }

    }

    public interface CallBack {
        void onSuccess(List<Post> posts);

        void onError(String message);
    }

    public PostsRepository(final String baseUrl,
                           final boolean isDebugEnabled) {
        super(baseUrl, isDebugEnabled);
        // get retrofit from the parent class!
        postsDataSource = new PostsDataSource(retrofit);
    }

    public Result getPosts() {

        try {
            final Response<List<com.michaelfotiads.demomodules.net.Post>> postsResponse = postsDataSource.getPosts().execute();
            if (postsResponse.isSuccessful() && postsResponse.body() != null) {
                return new Result(PostsMapper.map(postsResponse.body()));
            } else {
                return new Result(postsResponse.message());
            }
        } catch (IOException e) {
            return new Result(e.getMessage());
        }

    }


    public void enqueuePosts(final CallBack callBack) {

        postsDataSource.getPosts().enqueue(new Callback<List<com.michaelfotiads.demomodules.net.Post>>() {
            @Override
            public void onResponse(Call<List<com.michaelfotiads.demomodules.net.Post>> call,
                                   Response<List<com.michaelfotiads.demomodules.net.Post>> response) {

                if (response.isSuccessful()) {
                    final List<com.michaelfotiads.demomodules.net.Post> netPosts = response.body();
                    if (netPosts != null) {
                        callBack.onSuccess(PostsMapper.map(netPosts));
                    } else {
                        callBack.onError("Unexpected error");
                    }
                } else {
                    callBack.onError(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<com.michaelfotiads.demomodules.net.Post>> call, Throwable t) {
                callBack.onError(t.getMessage());
            }
        });

    }

    public Single<Result> observePosts() {

        return postsDataSource.observePosts()
                .map(new Function<List<com.michaelfotiads.demomodules.net.Post>, Result>() {
                    @Override
                    public Result apply(List<com.michaelfotiads.demomodules.net.Post> posts) throws Exception {
                        return new Result(PostsMapper.map(posts));
                    }
                }).onErrorReturn(new Function<Throwable, Result>() {
                    @Override
                    public Result apply(Throwable throwable) throws Exception {
                        return new Result(throwable.getMessage());
                    }
                });

    }


}
