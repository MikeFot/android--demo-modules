package com.michaelfotiads.demomodules.data.error;

import android.util.Log;

import com.google.gson.Gson;
import com.michaelfotiads.demomodules.data.callback.NetworkCallback;
import com.michaelfotiads.demomodules.data.callback.Reason;
import com.michaelfotiads.demomodules.data.callback.ServerError;
import com.michaelfotiads.demomodules.data.log.NetLogger;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Retrofit2CallbackFactory {

    public <T> Callback<T> create(@NonNull final NetworkCallback<T> callback) {
        return new Callback<T>() {
            @Override
            public void onResponse(final Call<T> call, final retrofit2.Response<T> response) {
                final String url = call.request().url().toString();

                final T payload = response.body();

                final boolean is2XX = response.isSuccessful();
                final int code = response.code();

                NetLogger.logOnResponseASync(url, code, payload);

                final ServerError serverError = parseErrorBody(response.errorBody());

                if (serverError != null) {
                    Log.w(Retrofit2CallbackFactory.this.getClass().getSimpleName(), "Server Error: " + serverError);
                }

                callback.onResponse(url, payload, is2XX, code, serverError);
            }

            @Override
            public void onFailure(final Call<T> call, final Throwable t) {
                final String url = call.request().url().toString();
                final Reason reason = ReasonResolver.getReason(t);

                NetLogger.logOnFailureASync(url, reason, t);
                callback.onFailure(url, reason);
            }
        };
    }

    private ServerError parseErrorBody(@Nullable final ResponseBody errorBody) {

        final ServerError serverError;
        if (errorBody != null) {

            try {
                serverError = new Gson().fromJson(errorBody.string(), ServerError.class);
            } catch (final Exception e) {
                e.printStackTrace();
                return null;
            }

        } else {
            serverError = null;
        }
        return serverError;
    }

}
