package com.michaelfotiads.demomodules.data.callback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerError {

    @SerializedName("code")
    @Expose
    private final String code;
    @SerializedName("message")
    @Expose
    private final String message;

    public ServerError(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ServerError{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
