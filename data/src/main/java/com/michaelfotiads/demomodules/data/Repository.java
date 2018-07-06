package com.michaelfotiads.demomodules.data;

import com.michaelfotiads.demomodules.data.callback.NetworkCallback;
import com.michaelfotiads.demomodules.data.callback.Reason;
import com.michaelfotiads.demomodules.data.callback.ServerError;
import com.michaelfotiads.demomodules.data.error.Retrofit2CallbackFactory;

import io.reactivex.annotations.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

class Repository {

    protected final Retrofit retrofit;
    private final Retrofit2CallbackFactory factory = new Retrofit2CallbackFactory();

    public Repository(final String baseUrl,
                      final boolean isDebugEnabled) {

        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (isDebugEnabled) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    protected <T> void exec(@NonNull final Call<T> call,
                            @NonNull final CallBack<T> callback) {
        call.enqueue(factory.create(new NetworkCallback<T>() {
            @Override
            public void onResponse(String url, T payload, boolean is2XX, int httpStatus, ServerError errorBody) {
                if (is2XX) {
                    callback.onSuccess(payload);
                } else {
                    callback.onError(Reason.fromCode(httpStatus));
                }
            }

            @Override
            public void onFailure(String url, Reason reason) {
                callback.onError(reason);
            }
        }));
    }


}
