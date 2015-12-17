package com.bwsw.streamscraper.system.exceptions;

/**
 * Created by ivan on 17.12.15.
 */
public class BadPlatformStreamException extends Exception {

    public BadPlatformStreamException() {
    }

    public BadPlatformStreamException(String message) {
        super(message);
    }

    public BadPlatformStreamException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadPlatformStreamException(Throwable cause) {
        super(cause);
    }

    public BadPlatformStreamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
