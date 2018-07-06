package com.michaelfotiads.demomodules.data;

import com.michaelfotiads.demomodules.data.callback.Reason;
import com.michaelfotiads.demomodules.net.PostsDataSource;

import java.io.IOException;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;

public class PostsRepository extends Repository {

    private final PostsDataSource postsDataSource;
    private final PostsMapper mapper = new PostsMapper();

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
                return new Result(mapper.map(postsResponse.body()));
            } else {
                return new Result(postsResponse.message());
            }
        } catch (IOException e) {
            return new Result(e.getMessage());
        }

    }


    public void enqueuePosts(final CallBack<List<Post>> callBack) {
        exec(postsDataSource.getPosts(), new CallBack<List<com.michaelfotiads.demomodules.net.Post>>() {
            @Override
            public void onSuccess(List<com.michaelfotiads.demomodules.net.Post> item) {
                callBack.onSuccess(mapper.map(item));
            }

            @Override
            public void onError(Reason reason) {
                callBack.onError(reason);
            }
        });
    }

    public Single<Result> observePosts() {

        return postsDataSource.observePosts()
                .map(posts -> new Result(mapper.map(posts)))
                .onErrorReturn(throwable -> new Result(throwable.getMessage()));

    }


}
