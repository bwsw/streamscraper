package com.bwsw.streamscraper.system.exceptions;

public class ImpossibleStreamException extends Exception {

	public ImpossibleStreamException() {
	}

	public ImpossibleStreamException(String message) {
		super(message);
	}

	public ImpossibleStreamException(Throwable cause) {
		super(cause);
	}

	public ImpossibleStreamException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImpossibleStreamException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
