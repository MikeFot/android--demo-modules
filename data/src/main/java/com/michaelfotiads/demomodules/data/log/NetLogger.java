package com.michaelfotiads.demomodules.data.log;

import com.michaelfotiads.demomodules.data.callback.Reason;

import java.util.Locale;

public final class NetLogger {
    private static final String ON_RESPONSE_TEMPLATE = "onResponse(%s): %d URL:%s, hasPayload:%s";
    private static final String ON_FAILURE_TEMPLATE = "onFailure(%s): %s, URL:%s, throwable:%s";
    private static final String TYPE_SYNC = "SYNC";
    private static final String TYPE_ASYNC = "ASYNC";
    private static Logger impl;

    private NetLogger() {
        // NOOP
    }

    public static void setLogger(Logger impl) {
        NetLogger.impl = impl;
    }

    public static void d(String message) {
        if (canLog()) {
            impl.d(message);
        }
    }

    public static void w(String message) {
        if (canLog()) {
            impl.w(message);
        }
    }

    public static void e(String message) {
        if (canLog()) {
            impl.e(message);
        }
    }

    static void e(String message, final Throwable e) {
        if (canLog()) {
            impl.e(message, e);
        }
    }

    private static boolean canLog() {
        return impl != null && impl.isEnabled();
    }

    public static void logOnResponseSync(final String url,
                                         final int httpCode,
                                         final Object payload) {
        logOnResponse(TYPE_SYNC, url, httpCode, payload != null);
    }

    public static void logOnResponseASync(final String url,
                                          final int httpCode,
                                          final Object payload) {
        logOnResponse(TYPE_ASYNC, url, httpCode, payload != null);
    }

    private static void logOnResponse(final String type,
                                      final String url,
                                      final int httpCode,
                                      final Object payload) {

        final String message = String.format(Locale.US, ON_RESPONSE_TEMPLATE,
                type, httpCode, url, payload != null);
        if (httpCode / 100 != 2) {
            w(message);
        } else {
            d(message);
        }
    }

    public static void logOnFailureSync(final String url,
                                        final Reason reason,
                                        final Throwable throwable) {
        logOnFailure(TYPE_SYNC, url, reason, throwable);
    }

    public static void logOnFailureASync(final String url,
                                         final Reason reason,
                                         final Throwable throwable) {
        logOnFailure(TYPE_ASYNC, url, reason, throwable);
    }

    private static void logOnFailure(final String type,
                                     final String url,
                                     final Reason reason,
                                     final Throwable throwable) {
        e(String.format(Locale.US, ON_FAILURE_TEMPLATE,
                type, reason, url, throwable), throwable);
    }

    public interface Logger {

        void d(String message);

        void w(String message);

        void e(String message);

        void e(String message, Throwable e);

        boolean isEnabled();
    }
}
