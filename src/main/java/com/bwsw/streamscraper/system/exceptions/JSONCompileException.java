package com.bwsw.streamscraper.system.exceptions;

/**
 * Created by ivan on 18.12.15.
 */
public class JSONCompileException extends Exception {
    public JSONCompileException() {
    }

    public JSONCompileException(String message) {
        super(message);
    }

    public JSONCompileException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSONCompileException(Throwable cause) {
        super(cause);
    }

    public JSONCompileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
