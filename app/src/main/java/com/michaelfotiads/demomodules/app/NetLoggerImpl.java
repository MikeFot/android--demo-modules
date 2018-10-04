package com.michaelfotiads.demomodules.app;

import com.michaelfotiads.demomodules.data.log.NetLogger;

public class NetLoggerImpl implements NetLogger.Logger {
    private static final String PREFIX = "NET: ";

    @Override
    public void d(final String message) {
        AppLog.d(PREFIX + message);
    }

    @Override
    public void w(final String message) {
        AppLog.w(PREFIX + message);
    }

    @Override
    public void e(final String message) {
        AppLog.e(PREFIX + message);
    }

    @Override
    public void e(final String message, final Throwable e) {
        if (e instanceof Exception) {
            AppLog.e(PREFIX + message, (Exception) e);
        } else {
            AppLog.e(PREFIX + message + " " + e);
        }
    }

    @Override
    public boolean isEnabled() {
        return BuildConfig.DEBUG;
    }


}
