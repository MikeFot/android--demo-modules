package com.michaelfotiads.demomodules.app;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public final class AppLog {
    private static final Set<String> CLASSNAME_TO_ESCAPE = getEscapedClassNames();
    private static final boolean INCLUDE_METHOD = BuildConfig.DEBUG;
    private static final String LINE_PREFIX = "APP:";
    private static final int MAX_TAG_LENGTH = 50;
    private static final String PACKAGE_PREFIX = BuildConfig.APPLICATION_ID + ".";
    private static final Pattern COMPILE = Pattern.compile(PACKAGE_PREFIX, Pattern.LITERAL);

    private AppLog() {
        // Avoid instantiation
    }

    public static void v(final String message) {
        vInternal(message);
    }

    private static void vInternal(final String message) {
        if (BuildConfig.DEBUG) {
            Log.v(calcTag(), calcMessage(message));
        }

    }

    public static void i(final String message) {
        iInternal(message);
    }

    private static void iInternal(final String message) {
        if (BuildConfig.DEBUG) {
            Log.i(calcTag(), calcMessage(message));
        }

    }

    public static void d(final String message) {
        dInternal(message);
    }

    private static void dInternal(final String message) {
        if (BuildConfig.DEBUG) {
            Log.d(calcTag(), calcMessage(message));
        }
    }

    private static String calcTag() {
        final String caller = getCallingMethod();
        if (caller == null) {
            return "";
        } else {
            final String shortTag = COMPILE.matcher(caller).replaceAll(Matcher.quoteReplacement(""));
            final boolean shouldBeShorter = shortTag.length() > MAX_TAG_LENGTH;

            if (shouldBeShorter) {
                final int length = shortTag.length();
                final int start = length - MAX_TAG_LENGTH;
                return shortTag.substring(start, length);
            } else {
                return shortTag;
            }
        }
    }

    private static String calcMessage(final String message) {
        return LINE_PREFIX + message;
    }

    private static String getCallingMethod() {
        final StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        if (stacks != null) {
            for (final StackTraceElement stack : stacks) {
                final String cn = stack.getClassName();
                if (cn != null && !CLASSNAME_TO_ESCAPE.contains(cn)) {
                    if (INCLUDE_METHOD) {
                        return cn + "#" + stack.getMethodName();
                    } else {
                        return cn;
                    }
                }
            }
        }
        return null;
    }

    public static void e(final String message) {
        eInternal(message, null);
    }

    private static void eInternal(final String message, final Exception e) {
        if (BuildConfig.DEBUG) {
            Log.e(calcTag(), calcMessage(message), e);
        }
    }

    public static void e(final String message, final Exception e) {
        eInternal(message, e);
    }

    private static Set<String> getEscapedClassNames() {
        final Set<String> set = new HashSet<>();

        set.add("java.lang.Thread");
        set.add("dalvik.system.VMStack");
        set.add(Log.class.getName());
        set.add(AppLog.class.getName());
        set.add(NetLoggerImpl.class.getName());
        return set;
    }

    public static void w(final String message) {
        wInternal(message, null);
    }

    private static void wInternal(final String message, final Exception e) {
        if (BuildConfig.DEBUG) {
            Log.w(calcTag(), calcMessage(message), e);
        }
    }

    public static void w(final String message, final Exception e) {
        wInternal(message, e);
    }


}
