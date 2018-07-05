package com.michaelfotiads.demomodules.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.michaelfotiads.demomodules.data.Post;
import com.michaelfotiads.demomodules.data.PostsRepository;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String PATH = "https://jsonplaceholder.typicode.com/";

    // used to cancel operations
    private final CompositeDisposable subscriptions = new CompositeDisposable();
    private PostsRepository repository;
    private ArrayAdapter<String> adapter;

    @BindView(R.id.content_view)
    protected TextView contentView;
    @BindView(R.id.list_view)
    protected ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        repository = new PostsRepository(PATH, BuildConfig.DEBUG);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);


    }

    @OnClick(R.id.button_sync)
    protected void onSyncClicked(View view) {
        setLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final PostsRepository.Result result = repository.getPosts();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.hasError()) {
                            setError(result.errorMessage);
                        } else if (result.hasPosts()) {
                            setItems(result.posts);
                        }
                    }
                });

            }
        }).start();


    }

    @OnClick(R.id.button_async)
    protected void onAsyncClicked(View view) {

        setLoading();
        repository.enqueuePosts(new PostsRepository.CallBack() {
            @Override
            public void onSuccess(List<Post> posts) {
                setItems(posts);
            }

            @Override
            public void onError(String message) {
                setError(message);
            }
        });

    }

    @OnClick(R.id.button_rx)
    protected void onRxClicked(View view) {
        setLoading();
        subscriptions.add(repository.observePosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PostsRepository.Result>() {
                    @Override
                    public void accept(PostsRepository.Result result) {
                        if (result.hasError()) {
                            setError(result.errorMessage);
                        } else if (result.hasPosts()) {
                            setItems(result.posts);
                        }
                    }
                })
        );

    }

    private void setLoading() {
        contentView.setText("Loading");
        adapter.clear();
        adapter.notifyDataSetChanged();
    }

    private void setError(String errorMessage) {
        contentView.setText("Error");
        AppToast.show(MainActivity.this, errorMessage);
    }

    private void setItems(List<Post> posts) {
        contentView.setText("Loaded");
        adapter.clear();
        for (Post post : posts) {
            adapter.add(post.title);
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onStop() {
        super.onStop();
        // to avoid leaks!
        subscriptions.clear();
    }
}
