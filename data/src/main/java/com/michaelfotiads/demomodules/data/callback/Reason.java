package com.michaelfotiads.demomodules.data.callback;

/**
 * Only using HTTP 1.1
 */
public enum Reason {
    DESERIALIZATION(1, "Deserialization"),
    NETWORK_ISSUE(2, "Network Issue"),
    TIMEOUT(3, "Timeout"),
    ERROR_300(300, "Multiple Choices"),
    ERROR_301(301, "Moved Permanently"),
    ERROR_302(302, "Moved Temporarily"),
    ERROR_303(303, "See Other"),
    ERROR_304(304, "Not Modified"),
    ERROR_305(305, "Use Proxy"),
    ERROR_400(400, "Bad Request"),
    ERROR_401(401, "Unauthorized"),
    ERROR_402(402, "Payment Required"),
    ERROR_403(403, "Forbidden"),
    ERROR_404(404, "Not Found"),
    ERROR_405(405, "Method Not Allowed"),
    ERROR_406(406, "Not Acceptable"),
    ERROR_407(407, "Proxy Authentication Required"),
    ERROR_408(408, "Request Time-out"),
    ERROR_409(409, "Conflict"),
    ERROR_410(410, "Gone"),
    ERROR_411(411, "Length Required"),
    ERROR_412(412, "Precondition Failed"),
    ERROR_413(413, "Request Entity Too Large"),
    ERROR_414(414, "Request-URI Too Large"),
    ERROR_415(415, "Unsupported Media Type"),
    ERROR_500(500, "Internal Server Error"),
    ERROR_501(501, "Not Implemented"),
    ERROR_502(502, "Bad Gateway"),
    ERROR_503(503, "Service Unavailable"),
    ERROR_504(504, "Gateway Time-out"),
    ERROR_505(505, "HTTP Version not supported"),
    UNKNOWN(0, "");

    private final int code;
    private final String description;

    Reason(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Reason fromCode(int code) {
        for (Reason reason : values()) {
            if (code == reason.code) {
                return reason;
            }
        }
        return UNKNOWN;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

