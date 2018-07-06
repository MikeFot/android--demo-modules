package com.michaelfotiads.demomodules.data;

import com.michaelfotiads.demomodules.data.callback.Reason;

public interface CallBack<T> {
    void onSuccess(T item);

    void onError(Reason reason);
}
