package com.michaelfotiads.demomodules.data.callback;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public interface NetworkCallback<T> {
    void onResponse(@NonNull final String url,
                    @Nullable final T payload,
                    final boolean is2XX,
                    final int httpStatus,
                    final ServerError errorBody);

    void onFailure(@NonNull final String url, @NonNull Reason reason);

}
