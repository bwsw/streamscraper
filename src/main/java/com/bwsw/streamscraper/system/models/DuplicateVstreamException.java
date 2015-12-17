package com.bwsw.streamscraper.system.models;

public class DuplicateVstreamException extends Exception {

	public DuplicateVstreamException() {
	}

	public DuplicateVstreamException(String message) {
		super(message);
	}

	public DuplicateVstreamException(Throwable cause) {
		super(cause);
	}

	public DuplicateVstreamException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateVstreamException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
