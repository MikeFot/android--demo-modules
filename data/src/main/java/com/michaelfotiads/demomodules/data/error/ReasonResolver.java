package com.michaelfotiads.demomodules.data.error;


import com.michaelfotiads.demomodules.data.callback.Reason;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketTimeoutException;

import io.reactivex.annotations.NonNull;
import retrofit2.HttpException;

public class ReasonResolver {

    @NonNull
    public static Reason getReason(final Throwable t) {
        final Reason retVal;

        if (t instanceof SocketTimeoutException) {
            retVal = Reason.TIMEOUT;
        } else if (t instanceof EOFException) {
            retVal = Reason.DESERIALIZATION;
        } else if (t instanceof IOException) {
            retVal = Reason.NETWORK_ISSUE;
        } else if (t instanceof HttpException) {
            final HttpException httpException = (HttpException) t;
            int code = httpException.code();
            retVal = Reason.fromCode(code);
        } else {
            retVal = Reason.UNKNOWN;
        }

        return retVal;
    }
}
