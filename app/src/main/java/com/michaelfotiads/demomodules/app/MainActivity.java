package com.michaelfotiads.demomodules.app;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.michaelfotiads.demomodules.data.CallBack;
import com.michaelfotiads.demomodules.data.Post;
import com.michaelfotiads.demomodules.data.PostsRepository;
import com.michaelfotiads.demomodules.data.callback.Reason;
import com.michaelfotiads.demomodules.data.log.NetLogger;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String PATH = "https://jsonplaceholder.typicode.com/";

    // used to cancel operations
    private final CompositeDisposable subscriptions = new CompositeDisposable();
    private PostsRepository repository;
    private ArrayAdapter<String> adapter;

    protected TextView contentView;
    protected ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentView = findViewById(R.id.content_view);
        listView = findViewById(R.id.list_view);

        // do this in APPLICATION
        NetLogger.setLogger(new NetLoggerImpl());

        repository = new PostsRepository(PATH, BuildConfig.DEBUG);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);


    }

    protected void onSyncClicked(View view) {
        setLoading();
        new Thread(() -> {
            final PostsRepository.Result result = repository.getPosts();

            runOnUiThread(() -> {
                if (result.hasError()) {
                    setError(result.errorMessage);
                } else if (result.hasPosts()) {
                    setItems(result.posts);
                }
            });

        }).start();


    }

    protected void onAsyncClicked(View view) {

        setLoading();
        repository.enqueuePosts(new CallBack<List<Post>>() {
            @Override
            public void onSuccess(List<Post> item) {
                setItems(item);
            }

            @Override
            public void onError(Reason reason) {
                setError(reason.getDescription());
            }
        });
    }

    protected void onRxClicked(View view) {
        setLoading();
        subscriptions.add(repository.observePosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (result.hasError()) {
                        setError(result.errorMessage);
                    } else if (result.hasPosts()) {
                        setItems(result.posts);
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
