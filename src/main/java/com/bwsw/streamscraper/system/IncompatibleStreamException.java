package com.bwsw.streamscraper.system;

public class IncompatibleStreamException extends Exception {
	
	public IncompatibleStreamException() {
	}

	public IncompatibleStreamException(String message) {
		super(message);
	}

	public IncompatibleStreamException(Throwable cause) {
		super(cause);
	}

	public IncompatibleStreamException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncompatibleStreamException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
